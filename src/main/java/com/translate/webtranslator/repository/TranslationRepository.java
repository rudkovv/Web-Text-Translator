package com.translate.webtranslator.repository;

import com.translate.webtranslator.model.Translation;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The TranslationRepository interface provides database operations for the Translation entity
 * in the Web-Text-Translator application.
 */
@Repository
public interface TranslationRepository extends JpaRepository<Translation, Long> {

	Optional<Translation> findByTranslatedText(String translatedText);
}
