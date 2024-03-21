package com.translate.webtranslator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.translate.webtranslator.model.Language;
import com.translate.webtranslator.model.Text;
import com.translate.webtranslator.repository.LanguageRepository;
import com.translate.webtranslator.repository.TextRepository;

import cache.CacheKey;
import cache.InMemoryCache;

import java.util.List;		

@Service
public class LanguageService {

    private LanguageRepository languageRepository;
    private TextRepository textRepository;
    private InMemoryCache languageCache;

    @Autowired
    public LanguageService(LanguageRepository languageRepository,TextRepository textRepository) {
        this.languageRepository = languageRepository;
        this.textRepository = textRepository;
        this.languageCache = new InMemoryCache();
    }

    public List<Language> getAllLanguages() {
        return languageRepository.findAll();
    }
    
    public Language getLanguageById(Long languageId) {
        Language cachedLanguage = (Language) languageCache.get(new CacheKey(languageId));
        if (cachedLanguage != null) {
            return cachedLanguage;
        }
        Language language = languageRepository.findById(languageId).orElse(null);
        if (language != null) {
        	languageCache.put(new CacheKey(languageId), language);
        }
        return language;
    }
    
    public Language getLanguageByLanguage(String language) {
        Language cachedLanguage = (Language) languageCache.get(new CacheKey(language));
        if (cachedLanguage != null) {
            return cachedLanguage;
        }
        Language findLanguage = languageRepository.findByName(language).orElse(null);
        if (findLanguage != null) {
        	languageCache.put(new CacheKey(language), findLanguage);
        }
        return findLanguage;
    }

    public Language saveLanguage(Language language) {
    	Language savedLanguage = languageRepository.save(language);
    	languageCache.put(new CacheKey(savedLanguage.getName()), savedLanguage);
        return savedLanguage;
    }
    
    public String deleteLanguage(Long languageId) {
    	Language language = languageRepository.findById(languageId).orElseThrow(() -> new IllegalStateException("Error to delete"));
    	List<Text> textList = language.getTexts();
    	for (Text text: textList) {
    		text.getLanguages().remove(language);
    		textRepository.save(text);
    	}
        languageRepository.deleteById(languageId);
        languageCache.remove(new CacheKey(language.getName()));
        return "succes";
    }
    public Language addTextInTextList(Long languageId, Long textId) {
    	Language language = languageRepository.findById(languageId).
    						orElseThrow(() -> new IllegalStateException("language with id: " + languageId + "doesn't exist"));
    	Text text = textRepository.findById(textId).
    						orElseThrow(() -> new IllegalStateException("text with id: " + textId + "doesnt exist"));
    	if (!text.getLanguages().contains(language)){
    	    text.getLanguages().add(language);
    	    textRepository.save(text);
    	}
    	languageCache.put(new CacheKey(language.getName()), language);
        return language;
    }
    
    public Language delTextInTextList(Long languageId, Long textId) {
        Text text = textRepository.findById(textId)
        		.orElseThrow(() -> new IllegalStateException("text doesnt exist"));
        Language language = languageRepository.findById(languageId)
        		.orElseThrow(() -> new IllegalStateException("language doesnt exist"));
        if (text.getLanguages().remove(language)) {
            textRepository.save(text);
        }
        languageCache.put(new CacheKey(language.getName()), language);
        return languageRepository.findById(languageId).orElse(null);
    }  
}