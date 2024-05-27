package com.translate.webtranslator.repository;

import com.translate.webtranslator.model.Translation;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * The TranslationRepository interface provides database operations for the Translation entity
 * in the Web-Text-Translator application.
 */
@Repository
public interface TranslationRepository extends JpaRepository<Translation, Long> {

	Optional<Translation> findByTranslatedText(String translatedText);
	
    @Query("SELECT t FROM Translation t ORDER BY t.translatedText ASC")
    Page<Translation> findAllTranslatiosWithPagination(Pageable pageable);
}
