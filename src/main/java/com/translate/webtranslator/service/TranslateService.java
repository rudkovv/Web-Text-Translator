package com.translate.webtranslator.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.translate.webtranslator.model.TranslateWord;

@Service
public interface TranslateService {

	List<TranslateWord> findAllWords();
	TranslateWord saveWordInDictionary(TranslateWord word);
	TranslateWord findByWordToTranslate(String wordToTranslate);
	TranslateWord updateDictionary(TranslateWord word);
	void deleteWord(String wordToTranslate);
}
