package com.cromoteca.phrasepal.words;

import com.cromoteca.phrasepal.languages.Language;
import com.cromoteca.phrasepal.user.User;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import com.vaadin.hilla.crud.CrudRepositoryService;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;

@BrowserCallable
@AnonymousAllowed
public class WordService extends CrudRepositoryService<Word, Long, WordRepository> {

    public void addWordsForUser(Collection<String> words, User user, Language language) {
        var normalizedWords = words.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(Predicate.not(String::isEmpty))
                .map(String::toLowerCase)
                .toList();
        var existing = getRepository().findExistingWords(user, language, normalizedWords);
        var existingSet = existing.stream().map(Word::getWord).collect(Collectors.toSet());
        normalizedWords.stream()
                .filter(Predicate.not(existingSet::contains))
                .map(w -> new Word(w, user, language))
                .forEach(this::save);
    }

    public List<Word> getWordsForUser(User user) {
        return getRepository().findByUserAndLanguage(user, user.getStudiedLanguage());
    }

    public void updateScoresForUser(User user, List<String> words, boolean success) {
        if (!words.isEmpty()) {
            if (success) {
                getRepository().incrementSuccessfulUsages(words, user, user.getStudiedLanguage());
            } else {
                getRepository().incrementFailedUsages(words, user, user.getStudiedLanguage());
            }
        }
    }

    public List<String> findCandidateWords(User user, Language language, int limit) {
        return getRepository().findCandidateWords(user, language, PageRequest.of(0, limit)).stream()
                .map(Word::getWord)
                .toList();
    }
}
