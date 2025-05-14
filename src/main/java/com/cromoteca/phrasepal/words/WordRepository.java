package com.cromoteca.phrasepal.words;

import com.cromoteca.phrasepal.languages.Language;
import com.cromoteca.phrasepal.user.User;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface WordRepository extends JpaRepository<Word, Long>, JpaSpecificationExecutor<Word> {

    @Transactional
    @Modifying
    @Query(
            "UPDATE Word w SET w.failedUsages = w.failedUsages + 1 WHERE w.word = :word AND w.language.id = :languageId AND w.user.id = :userId")
    void incrementFailedUsages(String word, Long languageId, Long userId);

    @Transactional
    @Modifying
    @Query(
            "UPDATE Word w SET w.successfulUsages = w.successfulUsages + 1 WHERE w.word = :word AND w.language.id = :languageId AND w.user.id = :userId")
    void incrementSuccessfulUsages(String word, Long languageId, Long userId);

    @Query("SELECT w.word FROM Word w WHERE w.user = :user AND w.word IN :words AND w.language = :language")
    List<String> findExistingWords(User user, Collection<String> words, Language language);

    @Query(
            "SELECT w FROM Word w WHERE w.user = :user AND w.language = :language ORDER BY (w.successfulUsages - w.failedUsages) ASC")
    List<Word> findCandidateWords(User user, Language language, Pageable pageable);

    List<Word> findByUserAndLanguage(User user, Language language);
}
