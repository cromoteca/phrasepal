package com.cromoteca.phrasepal.languages;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LanguageRepository extends JpaRepository<Language, Long>, JpaSpecificationExecutor<Language> {
    Optional<Language> findByCode(String code);

    Optional<Language> findByName(String name);
}
