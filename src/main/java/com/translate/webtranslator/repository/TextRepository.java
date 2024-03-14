package com.translate.webtranslator.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.translate.webtranslator.model.Text;

@Repository
public interface TextRepository extends JpaRepository <Text, Long> {

	Optional<Text> findByTextToTranslate(String textToTranslate);
}
