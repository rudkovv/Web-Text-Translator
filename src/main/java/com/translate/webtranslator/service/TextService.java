package com.translate.webtranslator.service;

import com.translate.webtranslator.cache.CacheKey;
import com.translate.webtranslator.cache.InMemoryCache;
import com.translate.webtranslator.model.Language;
import com.translate.webtranslator.model.Text;
import com.translate.webtranslator.model.Translation;
import com.translate.webtranslator.repository.TextRepository;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The TextService class provides business logic operations for the Text entity
 * in the Web-Text-Translator application.
 */
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
    	List<Text> texts = textRepository.findAll();
    	return texts;
    }

    /**
     * Retrieves a text by its ID.
     * Caches the text object for future retrievals.
     *
     * @param textId The ID of the text to retrieve.
     * @return The text with the specified ID, or null if not found.
     */
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
    
    /**
     * Retrieves a text by its content.
     * Caches the text object for future retrievals.
     *
     * @param text The content of the text to retrieve.
     * @return The text with the specified content, or null if not found.
     */
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

    /**
     * Saves a text.
     * Caches the saved text object.
     *
     * @param text The text to save.
     * @return A string indicating the success of the save operation.
     */
    public String saveText(Text text) {
        textRepository.save(text);
        textCache.put(new CacheKey(text.getId()), text);
        return text.getText() + " successfully save";
    }

    /**
     * Updates a text with new content.
     * Caches the updated text object.
     *
     * @param textId The ID of the text to update.
     * @param newText The new content of the text.
     * @return The updated text.
     */
    public Text updateText(Long textId, String newText) {
        Text text = textRepository.findById(textId)
                .orElseThrow(() -> new IllegalStateException(
                "Text with Id: " + textId + " doesn't exist!"));
        if (newText != null && !newText.isEmpty()) {
            text.setText(newText);
        }
        textCache.put(new CacheKey(textId), text);
        return textRepository.save(text);
    }

    /**
     * Deletes a text by its ID.
     * Removes the text from the cache.
     * Removes associations with translations and languages.
     *
     * @param textId The ID of the text to delete.
     * @return A string indicating the success of the deletion.
     */
    public String deleteText(Long textId) {
    	textCache.remove(new CacheKey(textId));
    	Text text = textRepository.findById(textId)
                   .orElseThrow(() -> new IllegalStateException(
                   "Text with Id: " + textId + " doesn't exist!"));
    	List<Translation> translationsList = text.getTranslations();
    	for (Translation translation : translationsList) {
    		translation.setText(null);
    	}
    	List<Language> languagesList = text.getLanguages();
    	for (Language language : languagesList) {
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
    
    public List<String> bulkSaveText(final List<Text> texts) {
       textRepository.saveAll(texts);
       texts.forEach(text -> textCache.put(new CacheKey(text.getId()), text));
       return texts.stream()
               .map(Text::getText)
               .map(textToTranslate -> textToTranslate + " - created")
               .toList();
    }
}