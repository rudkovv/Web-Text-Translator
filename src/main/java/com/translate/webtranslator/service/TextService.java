package com.translate.webtranslator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.translate.webtranslator.model.Language;
import com.translate.webtranslator.model.Text;
import com.translate.webtranslator.model.Translation;
import com.translate.webtranslator.repository.TextRepository;

import java.util.List;

@Service
public class TextService {

    private TextRepository textRepository;

    @Autowired
    public TextService(TextRepository textRepository) {
        this.textRepository = textRepository;
    }

    public List<Text> getAllTexts() {
        return textRepository.findAll();
    }

    public Text getTextById(Long textId) {
        return textRepository.findById(textId).orElse(null);
    }

    public String saveText(Text text) {
        textRepository.save(text);
        return "successfully save text";
    }

    public Text updateText(Long textId, String newText) {
        Text text = textRepository.findById(textId)
                .orElseThrow(() -> new IllegalStateException("Text with Id: " + textId + " doesn't exist!"));
        if (newText != null && !newText.isEmpty()) {
            text.setText(newText);
        }
        return textRepository.save(text);
    }

    public String deleteText(Long textId) {
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
}