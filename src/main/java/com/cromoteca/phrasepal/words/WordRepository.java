package com.cromoteca.phrasepal.words;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.cromoteca.phrasepal.user.User;

public interface WordRepository extends JpaRepository<Word, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE Word w SET w.failedUsages = w.failedUsages + 1 WHERE w.word = :word AND w.user.id = :userId")
    void incrementFailedUsages(String word, Long userId);

    @Transactional
    @Modifying
    @Query("UPDATE Word w SET w.successfulUsages = w.successfulUsages + 1 WHERE w.word = :word AND w.user.id = :userId")
    void incrementSuccessfulUsages(String word, Long userId);

    @Query("SELECT w.word FROM Word w WHERE w.user = :user AND w.word IN :words")
    List<String> findExistingWords(User user, Collection<String> words);
}
