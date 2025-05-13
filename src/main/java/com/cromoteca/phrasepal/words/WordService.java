package com.cromoteca.phrasepal.words;

import java.util.Collection;

import com.cromoteca.phrasepal.user.User;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;

@BrowserCallable
@AnonymousAllowed
public class WordService {
    private final WordRepository wordRepository;

    public WordService(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

public void addWordsForUser(Collection<String> wordStrings, User user) {
        var existing = wordRepository.findExistingWords(user, wordStrings);

        wordStrings.stream()
            .filter(w -> !existing.contains(w))
            .map(w -> new Word(w, user))
            .forEach(wordRepository::save);
    }
}
