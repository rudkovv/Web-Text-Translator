package com.translate.webtranslator.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.translate.webtranslator.model.Text;
import com.translate.webtranslator.model.Translation;
import com.translate.webtranslator.repository.TextRepository;
import com.translate.webtranslator.repository.TranslationRepository;

@Service
public class TranslationService {

    private TranslationRepository translationRepository;
    private TextRepository textRepository;

    @Autowired
    public TranslationService(TranslationRepository translationRepository,TextRepository textRepository) {
        this.translationRepository = translationRepository;
        this.textRepository = textRepository;
    }

    public List<Translation> getAllTranslations(){
    	return translationRepository.findAll();
    }
    public Translation saveTranslation(Translation newTranslation) {
          if (newTranslation.getText() != null){
              textRepository.save(newTranslation.getText());
          }
          return translationRepository.save(newTranslation);
    }
    
    public String deleteTranslation(Long translationId) {
        translationRepository.deleteById(translationId);
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
        return translationRepository.save(translation);
    }

    public Optional<Translation> findById(Long translationid) {
        return translationRepository.findById(translationid);
    }
}