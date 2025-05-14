package com.cromoteca.phrasepal.ai;

import com.cromoteca.phrasepal.languages.LanguageService;
import com.cromoteca.phrasepal.user.UserService;
import com.cromoteca.phrasepal.words.WordService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import java.util.Arrays;
import java.util.Map;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;

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

    public String translateToTargetLanguage(String text, String sourceLanguage, String targetLanguage) {
        var systemMessage =
                translationPromptTemplate.createMessage(Map.of("source", sourceLanguage, "target", targetLanguage));
        var prompt = new Prompt(systemMessage, new UserMessage(text));
        var response = chatModel.call(prompt);
        var translation = response.getResult().getOutput().getText();

        systemMessage = extractionPromptTemplate.createMessage();
        prompt = new Prompt(systemMessage, new UserMessage(translation));
        response = chatModel.call(prompt);
        var words = Arrays.stream(response.getResult().getOutput().getText().split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .toList();
        wordService.addWordsForUser(
                words,
                userService.getCurrentUser().orElseThrow(),
                languageService.getLanguageByName(targetLanguage).orElseThrow());

        return translation;
    }
}
