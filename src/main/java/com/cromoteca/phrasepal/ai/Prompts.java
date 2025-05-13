package com.cromoteca.phrasepal.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Prompts {
    @Value("${prompts.translateToTargetLanguage}")
    private String translateToTargetLanguage;

    @Value("${prompts.getWordsFromPhrase}")
    private String getWordsFromPhrase;

    @Value("${prompts.createPhraseFromWords}")
    private String createPhraseFromWords;

    @Value("${prompts.correctTranslation}")
    private String correctTranslation;

    public String getTranslateToTargetLanguage() {
        return translateToTargetLanguage;
    }

    public String getGetWordsFromPhrase() {
        return getWordsFromPhrase;
    }

    public String getCreatePhraseFromWords() {
        return createPhraseFromWords;
    }

    public String getCorrectTranslation() {
        return correctTranslation;
    }
}
