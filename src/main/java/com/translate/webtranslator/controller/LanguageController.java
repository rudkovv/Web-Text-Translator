package com.translate.webtranslator.controller;

import com.translate.webtranslator.aspect.AspectAnnotation;
import com.translate.webtranslator.exception.RestExceptionHandler;
import com.translate.webtranslator.model.Language;
import com.translate.webtranslator.service.LanguageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for language.
 */
@RestExceptionHandler
@RestController
@RequestMapping("/api/languages")
public class LanguageController {

    private LanguageService languageService;

    @Autowired
    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    @GetMapping
    @AspectAnnotation
    @Operation(summary = "Get all the languages",
               description = "Allows you to view all languages in the database")
    public String getAllLanguages() {
        return languageService.getAllLanguages();
    }

    @GetMapping("/find/byId/{id}")
    @AspectAnnotation
    @Operation(summary = "Get language by ID",
               description = "Allows you to find a specific language in the database by ID")
    public Language getTextById(@PathVariable Long id) {
        return languageService.getLanguageById(id);
    }
    
    @GetMapping("/find/byLanguage/{language}")
    @AspectAnnotation
    @Operation(summary = "Get information about the language from the entered language",
               description = "Allows you to view information about the language")
    public Language getTextByText(@PathVariable String language) {
        return languageService.getLanguageByLanguage(language);
    }
    
    @PostMapping("/create")
    @AspectAnnotation
    @Operation(summary = "Create language",
               description = "Allows you to add new language to the database")
    public String addLanguage(@Valid @RequestBody Language newLanguage) {
        return languageService.saveLanguage(newLanguage);
    }

    @DeleteMapping("/delete/{languageId}")
    @AspectAnnotation
    @Operation(summary = "Delete language by ID",
	           description = "Allows you to delete language by the entered ID")
    public String deleteTeam(@PathVariable Long languageId){
        return languageService.deleteLanguage(languageId);
    }

    @PutMapping("/addText")
    @AspectAnnotation
    @Operation(summary = "Add text to the language",
               description = "Allows add text from the database to the language by ID")
    public Language addInTextList(@RequestParam Long languageId, @RequestParam Long textId) {
        return languageService.addTextInTextList(languageId, textId);
    }

    @PutMapping("/delText")
    @AspectAnnotation
    @Operation(summary = "Remove text from the language",
               description = "Allows you to remove text to the language by entering the ID")
    public Language delInTextList(@RequestParam Long languageId, @RequestParam Long textId) {
        return languageService.delTextInTextList(languageId, textId);
    }
    
    @PostMapping("/create/bulk")
    @AspectAnnotation
    @Operation(summary = "Create many languages(bulk)",
               description = "Allows you to create many languages")
    public List<String> bulkSaveLanguage(@Valid @RequestBody final List<Language> languages) {
        return languageService.bulkSaveLanguage(languages);
    }
}