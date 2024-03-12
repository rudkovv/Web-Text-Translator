package com.translate.webtranslator.controller;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/{id}")
    public Optional<Translation> findById(@PathVariable String id){
        return translationService.findById((long) Integer.parseInt(id));
    }

    @PostMapping("/create")
    public Translation saveTranslation(@RequestBody Translation Translation){
        return translationService.saveTranslation(Translation);
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