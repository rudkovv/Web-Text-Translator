package com.translate.webtranslator.repository;

import com.translate.webtranslator.model.Text;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * The TextRepository interface provides database operations for the Text entity
 * in the Web-Text-Translator application.
 */
@Repository
public interface TextRepository extends JpaRepository<Text, Long> {

	Optional<Text> findByTextToTranslate(String textToTranslate);
	
	@Query(value = "SELECT t.text_to_translate FROM Text t "
			+ "JOIN text_language tl ON tl.text_id = t.id "
			+ "JOIN language l ON l.id = tl.language_id "
			+ "WHERE l.name = :language ORDER BY t.text_to_translate ASC", 
			nativeQuery = true)
	List<String> findTextsSortedByLanguage(@Param("language") String language);
	
	@Query("SELECT t.textToTranslate FROM Text t "
	        + "JOIN t.languages tl "
	        + "WHERE tl.name = :language ")
	List<String> findTextsByLanguage(@Param("language") String language);

    @Query("SELECT t FROM Text t ORDER BY t.textToTranslate ASC")
    	Page<Text> findAllWithPagination(Pageable pageable);

}
