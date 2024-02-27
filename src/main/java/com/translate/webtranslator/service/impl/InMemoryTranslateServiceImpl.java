package com.translate.webtranslator.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.translate.webtranslator.model.TranslateWord;
import com.translate.webtranslator.repository.InMemoryTranslateDAO;
import com.translate.webtranslator.service.TranslateService;

@Service
public class InMemoryTranslateServiceImpl implements TranslateService {

	private InMemoryTranslateDAO repository;
	
	@Autowired
    public InMemoryTranslateServiceImpl(InMemoryTranslateDAO repository) {
        this.repository = repository;
    }
	
	@Override
	public List<TranslateWord> findAllWords(){
		return repository.findAllWords();
	}
	
	@Override
	public TranslateWord saveWordInDictionary(TranslateWord word) {
		return repository.saveWordInDictionary(word);
	}
	
	@Override
	public TranslateWord findByWordToTranslate(String wordToTranslate) {
		return repository.findByWordToTranslate(wordToTranslate);
	}
	
	@Override
	public TranslateWord updateDictionary(TranslateWord word) {
		return repository.updateDictionary(word);
	}
	
	@Override
	public void deleteWord(String wordToTranslate) {
		repository.deleteWord(wordToTranslate);
	}
}
