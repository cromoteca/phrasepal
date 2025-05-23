package com.cromoteca.phrasepal.ai;

import com.cromoteca.phrasepal.languages.LanguageService;
import com.cromoteca.phrasepal.user.UserService;
import com.cromoteca.phrasepal.words.WordService;
import com.vaadin.hilla.BrowserCallable;
import jakarta.annotation.security.PermitAll;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@BrowserCallable
@PermitAll
public class AIService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AIService.class);

    private final ChatModel chatModel;
    private final WordService wordService;
    private final UserService userService;
    private final LanguageService languageService;

    private SystemPromptTemplate translateToTargetLanguage;
    private SystemPromptTemplate getWordsFromPhrase;
    private SystemPromptTemplate createPhraseFromWords;
    private SystemPromptTemplate correctTranslation;

    @Value("${prompts.translateToTargetLanguage}")
    public void setTranslateToTargetLanguage(String value) {
        this.translateToTargetLanguage = new SystemPromptTemplate(value);
    }

    @Value("${prompts.getWordsFromPhrase}")
    public void setGetWordsFromPhrase(String value) {
        this.getWordsFromPhrase = new SystemPromptTemplate(value);
    }

    @Value("${prompts.createPhraseFromWords}")
    public void setCreatePhraseFromWords(String value) {
        this.createPhraseFromWords = new SystemPromptTemplate(value);
    }

    @Value("${prompts.correctTranslation}")
    public void setCorrectTranslation(String value) {
        this.correctTranslation = new SystemPromptTemplate(value);
    }

    public AIService(
            ChatModel chatModel, WordService wordService, UserService userService, LanguageService languageService) {
        this.chatModel = chatModel;
        this.wordService = wordService;
        this.userService = userService;
        this.languageService = languageService;
    }
    /**
     * This method is used to "tee" a Flux, allowing us to consume the elements of the Flux at the end,
     * while still giving access to the flux.
     *
     * @param flux The original Flux to be "tee'd".
     * @param consumer The consumer that will process the elements of the Flux.
     * @param <T> The type of elements in the Flux.
     * @return The original Flux.
     */
    private static <T> Flux<T> tee(Flux<T> source, Consumer<List<T>> consumer) {
        var wrapped = source.publish().autoConnect(2);
        wrapped.collectList()
                .flatMap(list -> Mono.fromCallable(() -> {
                            consumer.accept(list);
                            return null;
                        })
                        .subscribeOn(Schedulers.boundedElastic()))
                .subscribe();
        return wrapped;
    }

    public Flux<String> translateToTargetLanguage(String text, String sourceLanguage, String targetLanguage) {
        var user = userService.getCurrentUser().orElseThrow();
        var translationMessage =
                translateToTargetLanguage.createMessage(Map.of("source", sourceLanguage, "target", targetLanguage));
        var translationPrompt = new Prompt(translationMessage, new UserMessage(text));
        return tee(
                chatModel.stream(translationPrompt)
                        .map(ChatResponse::getResult)
                        .map(Generation::getOutput)
                        .map(AssistantMessage::getText),
                chunks -> {
                    var fullText = String.join("", chunks);
                    var extractionMessage = getWordsFromPhrase.createMessage();
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
                                                .getLanguageByName(targetLanguage)
                                                .orElseThrow());
                            });
                });
    }

    public record PhraseWithWords(@NonNull String phrase, @NonNull List<String> words) {}

    @NonNull
    public PhraseWithWords generatePhrase() {
        var user = userService.getCurrentUser().orElseThrow();
        var words = wordService.findCandidateWords(user, user.getStudiedLanguage(), 5);
        var phraseGenerationMessage = createPhraseFromWords.createMessage(
                Map.of("lang", user.getStudiedLanguage().getName()));
        var phraseGenerationPrompt = new Prompt(
                phraseGenerationMessage,
                new UserMessage(words.stream().map(String::toLowerCase).collect(Collectors.joining(","))));
        var phraseGenerationResponse = chatModel.call(phraseGenerationPrompt);

        return Optional.ofNullable(phraseGenerationResponse)
                .map(ChatResponse::getResult)
                .map(Generation::getOutput)
                .map(AssistantMessage::getText)
                .map(text -> new PhraseWithWords(text, words))
                .orElseThrow();
    }

    public Flux<String> checkTranslation(
            String phrase, String translation, List<String> usedWords, String sourceLanguage, String targetLanguage) {
        var user = userService.getCurrentUser().orElseThrow();
        var correctTranslationMessage = correctTranslation.createMessage(Map.of(
                "source",
                sourceLanguage,
                "target",
                targetLanguage,
                "spoken",
                user.getSpokenLanguage().getName()));
        var correctTranslationPrompt =
                new Prompt(correctTranslationMessage, new UserMessage(phrase + " :: " + translation));
        LOGGER.debug("Calling chatModel.call with prompt:  {}", correctTranslationPrompt);
        return tee(
                chatModel.stream(correctTranslationPrompt)
                        .map(ChatResponse::getResult)
                        .map(Generation::getOutput)
                        .map(AssistantMessage::getText),
                chunks -> {
                    var correction = String.join("", chunks);
                    if (correction.contains("🟡")) {
                        wordService.updateScoresForUser(user, usedWords, false);
                    } else if (correction.contains("🟢")) {
                        wordService.updateScoresForUser(user, usedWords, true);
                    } else {
                        LOGGER.warn(
                                "It is not clear if the translation is correct or not ({} -> {})", phrase, correction);
                    }
                });
    }
}
