package com.translate.webtranslator.service;

import com.translate.webtranslator.cache.CacheKey;
import com.translate.webtranslator.cache.InMemoryCache;
import com.translate.webtranslator.model.Text;
import com.translate.webtranslator.model.Translation;
import com.translate.webtranslator.repository.TextRepository;
import com.translate.webtranslator.repository.TranslationRepository;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The TranslationService class provides business logic operations for the Translation entity
 * in the Web-Text-Translator application.
 */
@Service
public class TranslationService {

    private TranslationRepository translationRepository;
    private TextRepository textRepository;
    private InMemoryCache translationCache;

    /**
     * constructor with params.
     */
    @Autowired
    public TranslationService(TranslationRepository translationRepository,
    		                  TextRepository textRepository) {
        this.translationRepository = translationRepository;
        this.textRepository = textRepository;
        this.translationCache = new InMemoryCache();
    }

    public String getAllTranslations() {
    	List<Translation> translations = translationRepository.findAll();
    	return translations.stream()
                .map(Translation::toString)
                .collect(Collectors.joining("\n"));
    }
    
    /**
     * Saves a new translation.
     * If the translation has associated text, it is also saved.
     * Caches the saved translation object.
     *
     * @param newTranslation The new translation to save.
     * @return The saved translation.
     */
    public String saveTranslation(Translation newTranslation) {
          if (newTranslation.getText() != null) {
              textRepository.save(newTranslation.getText());
          }
          translationRepository.save(newTranslation);
          translationCache.put(new CacheKey(newTranslation.getId()), newTranslation);
          return newTranslation.getTranslatedText() + " successfully save";
    }
    
    /**
     * Deletes a translation by its ID.
     * Removes the translation from the cache.
     *
     * @param translationId The ID of the translation to delete.
     * @return A string indicating the success of the deletion.
     */
    public String deleteTranslation(Long translationId) {
    	translationRepository.findById(translationId)
                .orElseThrow(() -> new IllegalStateException(
                "Translation with Id: " + translationId + " doesn't exist!"));
        translationRepository.deleteById(translationId);
        translationCache.remove(new CacheKey(translationId));
        return "successfullyy delete translation";
    }
    
    /**
     * Sets a new text for a translation.
     * Caches the updated translation object.
     *
     * @param newTextId The ID of the new text.
     * @return The updated translation.
     */
    public Translation setNewText(Long transaltionId, Long newTextId) {
        Translation translation = translationRepository.findById(transaltionId)
        		.orElseThrow(() -> new IllegalStateException(
        	    "translation with id: " + transaltionId + " doesn't exist"));
        Text text = textRepository.findById(newTextId)
        		.orElseThrow(() -> new IllegalStateException(
        		"text with id: " + newTextId + " doesnt exist"));
        translation.setText(text);
        Translation updatedTranslation = translationRepository.save(translation);
        translationCache.put(new CacheKey(updatedTranslation.getId()), updatedTranslation);
        return updatedTranslation;
    }

    /**
     * Retrieves a translation by its ID.
     * Caches the translation object for future retrievals.
     *
     * @return The translation with the specified ID, or null if not found.
     */
    public Translation getById(Long translationid) {
    	Translation cachedTranslation = (Translation) translationCache.get(
    			                                   new CacheKey(translationid));
        if (cachedTranslation != null) {
            return cachedTranslation;
        } else {
            Translation translation = translationRepository.findById(translationid).orElse(null);
            if (translation != null) {
                translationCache.put(new CacheKey(translationid), translation);
            }
            return translation;
        }
    }
    
    public Translation getTranslationByTranslation(String translation) {
        return translationRepository.findByTranslatedText(translation).orElse(null);
    }
    
    public List<String> bulkSaveTranslation(final List<Translation> translations) {
    	translationRepository.saveAll(translations);
    	translations.forEach(translation -> translationCache
    			.put(new CacheKey(translation.getId()), translation));
        return translations.stream()
                .map(Translation::getTranslatedText)
                .map(translatedText -> translatedText + " - created")
                .collect(Collectors.toList());
     }

}