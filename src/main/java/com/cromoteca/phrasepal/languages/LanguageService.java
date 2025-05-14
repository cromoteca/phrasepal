package com.cromoteca.phrasepal.languages;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import com.vaadin.hilla.crud.CrudRepositoryService;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NonNull;

@BrowserCallable
@AnonymousAllowed
public class LanguageService extends CrudRepositoryService<Language, Long, LanguageRepository> {

    @NonNull
    public List<@NonNull Language> getAllLanguages() {
        return getRepository().findAll();
    }

    public Optional<Language> getLanguageByCode(String code) {
        return getRepository().findByCode(code);
    }

    public Optional<Language> getLanguageByName(String name) {
        return getRepository().findByName(name);
    }
}
