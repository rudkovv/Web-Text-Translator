package com.translate.webtranslator.service;

import com.translate.webtranslator.aspect.RequestCounterAnnotation;
import com.translate.webtranslator.cache.CacheKey;
import com.translate.webtranslator.cache.InMemoryCache;
import com.translate.webtranslator.model.Language;
import com.translate.webtranslator.model.Text;
import com.translate.webtranslator.repository.LanguageRepository;
import com.translate.webtranslator.repository.TextRepository;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The LanguageService class provides business logic operations for the Language entity
 * in the Web-Text-Translator application.
 */
@Service
public class LanguageService {

    private LanguageRepository languageRepository;
    private TextRepository textRepository;
    private InMemoryCache languageCache;

    /**
     * constructor with params.
     */
    @Autowired
    public LanguageService(LanguageRepository languageRepository, TextRepository textRepository) {
        this.languageRepository = languageRepository;
        this.textRepository = textRepository;
        this.languageCache = new InMemoryCache();
    }

    
    public LanguageService(LanguageRepository languageRepository, TextRepository textRepository,
			InMemoryCache languageCache) {
		this.languageRepository =  languageRepository;
		this.textRepository = textRepository;
		this.languageCache = languageCache;
	}

    @RequestCounterAnnotation
	public List<Language> getAllLanguages() {
		return languageRepository.findAll();
    }
    
    /**
     * Retrieves a language by its ID.
     * Caches the language object for future retrievals.
     *
     * @param languageId The ID of the language to retrieve.
     * @return The language with the specified ID, or null if not found.
     */
    @RequestCounterAnnotation
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
    
    /**
     * Retrieves a language by its name.
     * Caches the language object for future retrievals.
     *
     * @param language The name of the language to retrieve.
     * @return The language with the specified name, or null if not found.
     */
    @RequestCounterAnnotation
    public Language getLanguageByLanguage(String language) {
    	Language findLanguage = languageRepository.findByName(language);
        if (findLanguage != null) {
        	languageCache.put(new CacheKey(language), findLanguage);
        }
        return findLanguage;
    }

    /**
     * Saves a language.
     * Caches the saved language object.
     *
     * @param language The language to save.
     * @return The saved language.
     */
    @RequestCounterAnnotation
    public Language saveLanguage(Language language) {
        if (language.getTexts() != null) {
            for (Text text : language.getTexts()) {
                textRepository.save(text);
            }
        }
        Language savedLanguage = languageRepository.save(language);
        return savedLanguage;
    }
    

    
    /**
     * Deletes a language by its ID.
     * Removes the language from the cache.
     *
     * @param languageId The ID of the language to delete.
     * @return A string indicating the success of the deletion.
     */
    @RequestCounterAnnotation
    public String deleteLanguage(Long languageId) {
    	Language language = languageRepository.findById(languageId)
    			           .orElseThrow(() -> new IllegalStateException("Error to delete"));
    	List<Text> textList = language.getTexts();
    	for (Text text : textList) {
    		text.getLanguages().remove(language);
    		textRepository.save(text);
    	}
        languageRepository.deleteById(languageId);
        languageCache.remove(new CacheKey(language.getName()));
        return "succes";
    }
    
    /**
     * Adds a text to the language's text list.
     * Caches the updated language object.
     *
     * @param languageId The ID of the language.
     * @param textId The ID of the text to add.
     * @return The updated language.
     */
    @RequestCounterAnnotation
    public Language addTextInTextList(Long languageId, Long textId) {
    	Language language = languageRepository.findById(languageId)
    			.orElseThrow(() -> new IllegalStateException(
    					"language with id: " + languageId + "doesn't exist"));
    	Text text = textRepository.findById(textId)
    			.orElseThrow(() -> new IllegalStateException(
    					"text with id: " + textId + "doesnt exist"));
    	if (!text.getLanguages().contains(language)) {
    	    text.getLanguages().add(language);
    	    textRepository.save(text);
    	}
    	languageCache.put(new CacheKey(language.getName()), language);
        return language;
    }
    
    /**
     * Removes a text from the language's text list.
     * Caches the updated language object.
     *
     * @param languageId The ID of the language.
     * @param textId The ID of the text to remove.
     * @return The updated language.
     */
    @RequestCounterAnnotation
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
    
    @RequestCounterAnnotation
    public List<String> bulkSaveLanguage(List<Language> languages) {
    	languageRepository.saveAll(languages);
        languages.forEach(language -> languageCache
        		.put(new CacheKey(language.getId()), language));
        return languages.stream()
                .map(Language::getName)
                .map(name -> name + " - created")
                .toList();
    }
}