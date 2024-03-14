package com.translate.webtranslator.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.translate.webtranslator.model.Language;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {
	
	Optional<Language> findByName(String name);
}
