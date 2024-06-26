package com.translate.webtranslator.service;

import com.translate.webtranslator.aspect.RequestCounterAnnotation;
import com.translate.webtranslator.cache.CacheKey;
import com.translate.webtranslator.cache.InMemoryCache;
import com.translate.webtranslator.model.Language;
import com.translate.webtranslator.model.Text;
import com.translate.webtranslator.model.Translation;
import com.translate.webtranslator.repository.TextRepository;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public TextService(TextRepository textRepository, InMemoryCache textCache) {
		this.textRepository = textRepository;
		this.textCache = textCache;
	}

    @RequestCounterAnnotation
    public Page<Text> getTextsWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("textToTranslate").ascending());
        return textRepository.findAllWithPagination(pageable);
    }

    /**
     * Retrieves a text by its ID.
     * Caches the text object for future retrievals.
     *
     * @param textId The ID of the text to retrieve.
     * @return The text with the specified ID, or null if not found.
     */
    @RequestCounterAnnotation
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
    @RequestCounterAnnotation
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
    @RequestCounterAnnotation
    public Text saveText(Text text) {
        if (text == null) {
            throw new IllegalArgumentException("Text cannot be null");
        }
        Optional<Text> existingText = textRepository.findByTextToTranslate(text.getTextToTranslate());
        if (existingText.isPresent()) {
            return text;
        }
        Text savedText = textRepository.save(text);
        textCache.put(new CacheKey(savedText.getId()), savedText);
        return textRepository.save(text);
    }

    /**
     * Updates a text with new content.
     * Caches the updated text object.
     *
     * @param textId The ID of the text to update.
     * @param newText The new content of the text.
     * @return The updated text.
     */
    @RequestCounterAnnotation
    public Text updateText(Long textId, String newText) {
        Text text = textRepository.findById(textId)
                .orElseThrow(() -> new IllegalStateException(
                "Text with Id: " + textId + " doesn't exist!"));
        if (newText != null && !newText.isEmpty()) {
            text.setTextToTranslate(newText);
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
    @RequestCounterAnnotation
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
    
    @RequestCounterAnnotation
    public List<String> findTextsSortedByLanguage(String language) {
        return textRepository.findTextsSortedByLanguage(language);
    }
    
    @RequestCounterAnnotation
    public List<String> findTextsByLanguage(String language) {
        return textRepository.findTextsByLanguage(language);
    }
    
    @RequestCounterAnnotation
    public List<String> bulkSaveText(List<Text> texts) {
       textRepository.saveAll(texts);
       texts.forEach(text -> textCache.put(new CacheKey(text.getId()), text));
       return texts.stream()
               .map(Text::getTextToTranslate)
               .map(textToTranslate -> textToTranslate + " - created")
               .toList();
    }

	public Object getTextRepository() {
		return textRepository;
	}

	public Object getTextCache() {
		return textCache;
	}
}