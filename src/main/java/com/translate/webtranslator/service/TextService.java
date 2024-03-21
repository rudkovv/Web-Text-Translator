package com.translate.webtranslator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.translate.webtranslator.model.Language;
import com.translate.webtranslator.model.Text;
import com.translate.webtranslator.model.Translation;
import com.translate.webtranslator.repository.TextRepository;

import cache.InMemoryCache;
import cache.CacheKey;

import java.util.List;

@Service
public class TextService {

    private TextRepository textRepository;
    private InMemoryCache textCache;

    @Autowired
    public TextService(TextRepository textRepository) {
        this.textRepository = textRepository;
        this.textCache = new InMemoryCache();
    }

    public List<Text> getAllTexts() {
        return textRepository.findAll();
    }

    public Text getTextById(Long textId) {
    	Text cachedText = (Text) textCache.get(new CacheKey(textId));
        if (cachedText != null) {
            return cachedText;
        } else {
            Text text = textRepository.findById(textId).orElse(null);
            if (text != null) {
            	textCache.put(new CacheKey(textId), text);
            }
            return text;
        }
    }
    
    public Text getTextByText(String text) {
    	 Text cachedText = (Text) textCache.get(new CacheKey(text));
    	 if (cachedText != null) {
    		 return cachedText;
    	 } else {
    		 Text findText = textRepository.findByTextToTranslate(text).orElse(null);
    	    if (findText != null) {
    	        textCache.put(new CacheKey(text), findText);
    	    }
    	    return findText;
    	 }
    }

    public String saveText(Text text) {
        textRepository.save(text);
        textCache.put(new CacheKey(text.getId()), text);
        return "successfully save text";
    }

    public Text updateText(Long textId, String newText) {
        Text text = textRepository.findById(textId)
                .orElseThrow(() -> new IllegalStateException("Text with Id: " + textId + " doesn't exist!"));
        if (newText != null && !newText.isEmpty()) {
            text.setText(newText);
        }
        textCache.put(new CacheKey(textId), text);
        return textRepository.save(text);
    }

    public String deleteText(Long textId) {
    	textCache.remove(new CacheKey(textId));
    	Text text = textRepository.findById(textId)
                   .orElseThrow(() -> new IllegalStateException("Text with Id: " + textId + " doesn't exist!"));
    	List<Translation> translationsList = text.getTranslations();
    	for (Translation translation: translationsList) {
    		translation.setText(null);
    	}
    	List<Language> languagesList = text.getLanguages();
    	for (Language language: languagesList) {
    		language.setTexts(null);
    	}
        textRepository.deleteById(textId);
        return "successfully delete text";
    }
    
    public List<String> findTextsSortedByLanguage(String language) {
        return textRepository.findTextsSortedByLanguage(language);
    }
    
    public List<String> findTextsByLanguage(String language) {
        return textRepository.findTextsByLanguage(language);
    }
}