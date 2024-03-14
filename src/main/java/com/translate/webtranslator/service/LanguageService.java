package com.translate.webtranslator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.translate.webtranslator.model.Language;
import com.translate.webtranslator.model.Text;
import com.translate.webtranslator.repository.LanguageRepository;
import com.translate.webtranslator.repository.TextRepository;

import java.util.List;		

@Service
public class LanguageService {

    private LanguageRepository languageRepository;
    private TextRepository textRepository;

    @Autowired
    public LanguageService(LanguageRepository languageRepository,TextRepository textRepository) {
        this.languageRepository = languageRepository;
        this.textRepository = textRepository;
    }

    public List<Language> getAllLanguages() {
        return languageRepository.findAll();
    }
    
    public Language getLanguageById(Long languageId) {
    	return languageRepository.findById(languageId).orElse(null);
    }
    
    public Language getLanguageByLanguage(String language) {
    	return languageRepository.findByName(language).orElse(null);
    }

    public Language saveLanguage(Language language) {
        return languageRepository.save(language);
    }
    
    public String deleteLanguage(Long languageId) {
    	Language language = languageRepository.findById(languageId).orElseThrow(() -> new IllegalStateException("Error to delete"));
    	List<Text> textList = language.getTexts();
    	for (Text text: textList) {
    		text.getLanguages().remove(language);
    		textRepository.save(text);
    	}
        languageRepository.deleteById(languageId);
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
        else if (!text.getLanguages().contains(language)){
            text.getLanguages().add(language);
                textRepository.save(text);
        }
        return languageRepository.findById(languageId).orElse(null);
    }
    public Language delTextInTextList(Long languageId, Long textId) {
        Text text = textRepository.findById(textId)
        		.orElseThrow(() -> new IllegalStateException("text doesnt exist"));
        Language language = languageRepository.findById(languageId)
        		.orElseThrow(() -> new IllegalStateException("language doesnt exist"));
        if (text.getLanguages().remove(language)) {
            textRepository.save(text);
        }
        return languageRepository.findById(languageId).orElse(null);
    }  
}