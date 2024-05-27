package com.translate.webtranslator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.translate.webtranslator.aspect.AspectAnnotation;
import com.translate.webtranslator.exception.RestExceptionHandler;
import com.translate.webtranslator.model.Language;
import com.translate.webtranslator.model.Text;
import com.translate.webtranslator.model.Translation;
import com.translate.webtranslator.service.LanguageService;
import com.translate.webtranslator.service.TextService;
import com.translate.webtranslator.service.TranslationService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Controller for text.
 */
@CrossOrigin
@RestExceptionHandler
@RestController
@RequestMapping("/api/texts")
public class TextController {

    private TextService textService;
    private TranslationService translationService;
    private LanguageService languageService;

    @Autowired
    public TextController(TextService textService,
    					  TranslationService translationService,
    					  LanguageService languageService) {
        this.textService = textService;
        this.translationService = translationService;
        this.languageService = languageService;
    }

    @GetMapping
    @AspectAnnotation
    @Operation(summary = "Get all the text",
               description = "Allows you to view all the texts in the database")
    public Page<Text> getTexts(@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
    	return textService.getTextsWithPagination(page, size);
    	}
    
    @GetMapping("/find/byId/{id}")
    @AspectAnnotation	
    @Operation(summary = "Get text by ID",
               description = "Allows you to find a specific text in the database by ID")
    public Text getTextById(@PathVariable Long id) {
        return textService.getTextById(id);
    }
    
    @GetMapping("/find/byText/{text}")
    @AspectAnnotation
    @Operation(summary = "Get information about the text from the entered text",
               description = "Allows you to view information about the text")
    public Text getTextByText(@Valid @PathVariable String text) {
        return textService.getTextByText(text);
    }

    @GetMapping("/find/byLanguage/sort/{language}")
    @AspectAnnotation
    @Operation(summary = "Get a sorted list of texts by the entered language",
               description = "Allows you to view a sorted list of texts by language")
    public ResponseEntity<List<String>> getTextsSortedByLanguage(
                                        @PathVariable("language") String language) {
        List<String> texts = textService.findTextsSortedByLanguage(language);
        return ResponseEntity.ok(texts);
    }
    
    @GetMapping("/find/byLanguage/{language}")
    @AspectAnnotation
    @Operation(summary = "Get a list of texts by the entered language",
               description = "Allows you to view a list of texts by the entered language")
    public ResponseEntity<List<String>> getTextsByLanguage(
                                        @PathVariable("language") String language) {
        List<String> texts = textService.findTextsByLanguage(language);
        return ResponseEntity.ok(texts);
    }
    
    @PostMapping("/create")
    @AspectAnnotation
    @Operation(summary = "Create text",
               description = "Allows you to add new text to the database")
    public Text saveText(@RequestBody Text text) {
        if (text == null) {
            return null;
        }
        Text existingText = textService.getTextByText(text.getTextToTranslate());
        if (existingText != null) {
            return updateExistingText(existingText, text);
        } else {
            return saveNewText(text);
        }
    }

    private Text updateExistingText(Text existingText, Text newText) {
        for (Translation newTranslation : newText.getTranslations()) {
            if (!existingText.getTranslations().contains(newTranslation)) {
                newTranslation.setText(existingText);
                translationService.saveTranslation(newTranslation);
                existingText.getTranslations().add(newTranslation);
            }
        }

        for (Language newLanguage : newText.getLanguages()) {
            Language existingLanguage = languageService.getLanguageByLanguage(newLanguage.getName());
            if (existingLanguage == null) {
                newLanguage.setTexts(Collections.singletonList(existingText));
                Language savedLanguage = languageService.saveLanguage(newLanguage);
                existingText.getLanguages().add(savedLanguage);
            } else if (!existingLanguage.getTexts().contains(existingText)) {
                existingLanguage.getTexts().add(existingText);
                languageService.saveLanguage(existingLanguage);
                existingText.getLanguages().add(existingLanguage);
            }
        }
        textService.saveText(existingText);
        return existingText;
    }

    private Text saveNewText(Text text) {
        textService.saveText(text);
        for (Translation translation : text.getTranslations()) {
            translation.setText(text);
            translationService.saveTranslation(translation);
        }
        for (Language language : text.getLanguages()) {
            if (language != null) {
                List<Text> texts = language.getTexts();
                if (texts == null) {
                    texts = new ArrayList<>();
                    language.setTexts(texts);
                }
                texts.add(text);
                languageService.saveLanguage(language);
            }
        }
        return text;
     }

    @DeleteMapping("/delete/byId/{id}")
    @AspectAnnotation
    @Operation(summary = "Delete text by ID",
               description = "Allows you to delete text by the entered ID")
    public String deleteText(@PathVariable Long id) {
        return textService.deleteText(id);
    }

    @PutMapping("/change")
    @AspectAnnotation
    @Operation(summary = "Change text by ID",
               description = "Allows you to change the text and enter a new text")
    public Text update(@RequestParam Long textId,
                       @RequestParam(required = false) String text) {
        return textService.updateText(textId, text);
    }
    
    @PostMapping("/create/bulk")
    @AspectAnnotation
    @Operation(summary = "Create many texts(bulk)",
               description = "Allows you to create many texts")
    public List<String> bulkSaveText(@RequestBody final List<Text> texts) {
        return textService.bulkSaveText(texts);
    }

}