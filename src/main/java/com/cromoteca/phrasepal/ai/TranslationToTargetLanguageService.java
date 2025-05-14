package com.cromoteca.phrasepal.ai;

import com.cromoteca.phrasepal.languages.LanguageService;
import com.cromoteca.phrasepal.user.UserService;
import com.cromoteca.phrasepal.words.WordService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@BrowserCallable
@AnonymousAllowed
public class TranslationToTargetLanguageService {

    private final ChatModel chatModel;
    private final SystemPromptTemplate translationPromptTemplate;
    private final SystemPromptTemplate extractionPromptTemplate;
    private final WordService wordService;
    private final UserService userService;
    private final LanguageService languageService;

    public TranslationToTargetLanguageService(
            ChatModel chatModel,
            Prompts prompts,
            WordService wordService,
            UserService userService,
            LanguageService languageService) {
        this.chatModel = chatModel;
        this.wordService = wordService;
        this.userService = userService;
        this.languageService = languageService;
        translationPromptTemplate = new SystemPromptTemplate(prompts.getTranslateToTargetLanguage());
        extractionPromptTemplate = new SystemPromptTemplate(prompts.getGetWordsFromPhrase());
    }

    public Flux<String> translateToTargetLanguage(String text, String sourceLanguageName, String targetLanguageName) {
        var user = userService.getCurrentUser().orElseThrow();
        var translationMessage = translationPromptTemplate.createMessage(
                Map.of("source", sourceLanguageName, "target", targetLanguageName));
        var translationPrompt = new Prompt(translationMessage, new UserMessage(text));
        var translationFlux = chatModel.stream(translationPrompt)
                .map(ChatResponse::getResult)
                .map(Generation::getOutput)
                .map(AssistantMessage::getText)
                .publish()
                .autoConnect(2);

        translationFlux
                .collectList()
                .map(chunks -> String.join("", chunks))
                .flatMap(fullText -> Mono.fromCallable(() -> {
                            var extractionMessage = extractionPromptTemplate.createMessage();
                            var extractionPrompt = new Prompt(extractionMessage, new UserMessage(fullText));
                            var extractionResponse = chatModel.call(extractionPrompt);

                            Optional.ofNullable(extractionResponse)
                                    .map(ChatResponse::getResult)
                                    .map(Generation::getOutput)
                                    .map(AssistantMessage::getText)
                                    .map(t -> t.split(","))
                                    .ifPresent(split -> {
                                        var words = Arrays.stream(split)
                                                .map(String::trim)
                                                .map(String::toLowerCase)
                                                .toList();

                                        wordService.addWordsForUser(
                                                words,
                                                user,
                                                languageService
                                                        .getLanguageByName(targetLanguageName)
                                                        .orElseThrow());
                                    });

                            return null; // return needed
                        })
                        .subscribeOn(Schedulers.boundedElastic()))
                .subscribe();

        return translationFlux;
    }
}
