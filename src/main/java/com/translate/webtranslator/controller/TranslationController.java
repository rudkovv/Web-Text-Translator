package com.translate.webtranslator.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.translate.webtranslator.model.Translation;
import com.translate.webtranslator.service.TranslationService;

@RestController
@RequestMapping("/api/translations")
public class TranslationController {

    private TranslationService translationService;

    @Autowired
    public TranslationController(TranslationService translationService) {
        this.translationService = translationService;
    }

    @GetMapping
    public List<Translation> getAllTranslations(){
        return translationService.getAllTranslations();
    }

    @GetMapping("/find/byId/{id}")
    public Translation getTextById(@PathVariable Long id){
        return translationService.getById(id);
    }
    @GetMapping("/find/byTranslation/{translation}")
    public Translation getTextByText(@PathVariable String translation){
        return translationService.getTranslationByTranslation(translation);
    }

    @PostMapping("/create")
    public Translation saveTranslation(@RequestBody Translation translation){
        return translationService.saveTranslation(translation);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteTranslation(@PathVariable Long id){
        return translationService.deleteTranslation(id);
    }
    @PutMapping("/setText")
    public Translation setNewTextId(@RequestParam Long translationId, @RequestParam Long newTextId){
        return translationService.setNewText(translationId,newTextId);
    }
}