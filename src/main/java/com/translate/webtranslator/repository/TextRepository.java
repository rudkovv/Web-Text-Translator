package com.translate.webtranslator.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.translate.webtranslator.model.Text;

@Repository
public interface TextRepository extends JpaRepository <Text, Long> {

	Optional<Text> findByTextToTranslate(String textToTranslate);
	
	@Query(value = "SELECT t.text_to_translate FROM Text t "
			+ "JOIN text_language tl ON tl.text_id = t.id "
			+ "JOIN language l ON l.id = tl.language_id "
			+ "WHERE l.name = :language ORDER BY t.text_to_translate ASC", nativeQuery = true)
	List<String> findTextsSortedByLanguage(@Param("language") String language);
	
	@Query("SELECT t.textToTranslate FROM Text t "
	        + "JOIN t.languages tl "
	        + "WHERE tl.name = :language ")
	List<String> findTextsByLanguage(@Param("language") String language);
}
