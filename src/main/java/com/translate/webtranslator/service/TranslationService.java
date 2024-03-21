package com.translate.webtranslator.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.translate.webtranslator.model.Text;
import com.translate.webtranslator.model.Translation;
import com.translate.webtranslator.repository.TextRepository;
import com.translate.webtranslator.repository.TranslationRepository;

import cache.CacheKey;
import cache.InMemoryCache;


@Service
public class TranslationService {

    private TranslationRepository translationRepository;
    private TextRepository textRepository;
    private InMemoryCache translationCache;

    @Autowired
    public TranslationService(TranslationRepository translationRepository,TextRepository textRepository) {
        this.translationRepository = translationRepository;
        this.textRepository = textRepository;
        this.translationCache = new InMemoryCache();
    }

    public List<Translation> getAllTranslations(){
    	return translationRepository.findAll();
    }
    public Translation saveTranslation(Translation newTranslation) {
          if (newTranslation.getText() != null){
              textRepository.save(newTranslation.getText());
          }
          Translation savedTranslation = translationRepository.save(newTranslation);
          translationCache.put(new CacheKey(savedTranslation.getId()), savedTranslation);
          return savedTranslation;
    }
    
    public String deleteTranslation(Long translationId) {
        translationRepository.deleteById(translationId);
        translationCache.remove(new CacheKey(translationId));
        return "successfullyy delete translation";
    }
    
    public Translation setNewText(Long transaltionId,Long newTextId) {
        Translation translation = translationRepository.findById(transaltionId).
        						  orElseThrow(() -> new IllegalStateException
        						  ("translation with id: " + transaltionId + " doesn't exist"));
        Text text = textRepository.findById(newTextId).
        		    orElseThrow(() -> new IllegalStateException
        		    ("text with id: " + newTextId + " doesnt exist"));
        translation.setText(text);
        Translation updatedTranslation = translationRepository.save(translation);
        translationCache.put(new CacheKey(updatedTranslation.getId()), updatedTranslation);
        return updatedTranslation;
    }

    public Translation getById(Long translationid) {
    	Translation cachedTranslation = (Translation) translationCache.get(new CacheKey(translationid));
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

}