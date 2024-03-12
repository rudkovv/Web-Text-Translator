package com.translate.webtranslator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.translate.webtranslator.model.Translation;

@Repository
public interface TranslationRepository extends JpaRepository<Translation, Long>{

}
