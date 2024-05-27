package com.translate.webtranslator.repository;

import com.translate.webtranslator.model.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The LanguageRepository interface provides database operations for the Language entity
 * in the Web-Text-Translator application.
 */
@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {
	Language findByName(String name);
}
