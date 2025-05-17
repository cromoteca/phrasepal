package com.cromoteca.phrasepal.languages;

import jakarta.annotation.PostConstruct;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LanguageInitializer {
    @Autowired
    private LanguageRepository languageRepository;

    @PostConstruct
    public void initLanguages() {
        if (languageRepository.count() == 0) {
            List<Language> languages = List.of(
                    new Language("English", "en-US", "🇺🇸"),
                    new Language("French", "fr-FR", "🇫🇷"),
                    new Language("German", "de-DE", "🇩🇪"),
                    new Language("Hindi", "hi-IN", "🇮🇳"),
                    new Language("Italian", "it-IT", "🇮🇹"),
                    new Language("Portuguese", "pt-PT", "🇵🇹"),
                    new Language("Spanish", "es-ES", "🇪🇸"),
                    new Language("Thai", "th-TH", "🇹🇭"));
            languageRepository.saveAll(languages);
        }
    }
}
