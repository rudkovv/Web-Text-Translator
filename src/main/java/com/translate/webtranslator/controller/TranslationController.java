package com.translate.webtranslator.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.translate.webtranslator.aspect.AspectAnnotation;
import com.translate.webtranslator.exception.RestExceptionHandler;
import com.translate.webtranslator.model.Translation;
import com.translate.webtranslator.service.TranslationService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

/**
 * Controller for translation.
 */
@RestExceptionHandler
@RestController
@RequestMapping("/api/translations")
public class TranslationController {

    private TranslationService translationService;

    @Autowired
    public TranslationController(TranslationService translationService) {
        this.translationService = translationService;
    }

    @GetMapping
    @AspectAnnotation
    @Operation(summary = "Get all translations",
               description = "Allows you to view all translations from the database")
    public String getAllTranslations() {
        return translationService.getAllTranslations();
    }

    @GetMapping("/find/byId/{id}")
    @AspectAnnotation
    @Operation(summary = "Get a translation by ID",
               description = "Allows you to get a transfer using the entered ID")
    public Translation getTextById(@PathVariable Long id) {
        return translationService.getById(id);
    }
    
    @GetMapping("/find/byTranslation/{translation}")
    @AspectAnnotation
    @Operation(summary = "Get information about the translation from the entered translation",
               description = "Allows to view information about the translation by translation")
    public Translation getTextByText(@PathVariable String translation) {
        return translationService.getTranslationByTranslation(translation);
    }

    @PostMapping("/create")
    @AspectAnnotation
    @Operation(summary = "Create translation",
               description = "Allows you to add new translation to the database")
    public String saveTranslation(@Valid @RequestBody Translation translation) {
        return translationService.saveTranslation(translation);
    }

    @DeleteMapping("/delete/{id}")
    @AspectAnnotation
    @Operation(summary = "Delete translation by ID",
               description = "Allows you to delete translation by the entered ID")
    public String deleteTranslation(@PathVariable Long id) {
        return translationService.deleteTranslation(id);
    }
    
    @PutMapping("/setText")
    @AspectAnnotation
    @Operation(summary = "Set the text for translation",
               description = "Allows you to set the text for translation by ID")
    public Translation setNewTextId(
                                   @RequestParam Long translationId, @RequestParam Long newTextId) {
        return translationService.setNewText(translationId, newTextId);
    }
    
    @PostMapping("/create/bulk")
    @AspectAnnotation
    @Operation(summary = "Create many translations(bulk)",
               description = "Allows you to create many translations")
    public List<String> bulkSaveTranslation(@Valid @RequestBody final List<Translation> transaltions) {
        return translationService.bulkSaveTranslation(transaltions);
    }
}