package com.cromoteca.phrasepal.words;

import com.cromoteca.phrasepal.languages.Language;
import com.cromoteca.phrasepal.user.User;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import com.vaadin.hilla.crud.CrudRepositoryService;
import java.util.Collection;
import java.util.List;

@BrowserCallable
@AnonymousAllowed
public class WordService extends CrudRepositoryService<Word, Long, WordRepository> {

    public void addWordsForUser(Collection<String> words, User user, Language language) {
        var existing = getRepository().findExistingWords(user, words, language);

        words.stream()
                .filter(w -> !existing.contains(w))
                .map(w -> new Word(w, user, language))
                .forEach(this::save);
    }

    public List<Word> getWordsForUser(User user, Language language) {
        return getRepository().findByUserAndLanguage(user, language);
    }
}
