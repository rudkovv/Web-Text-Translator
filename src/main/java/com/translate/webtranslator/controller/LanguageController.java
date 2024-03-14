package com.translate.webtranslator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.translate.webtranslator.model.Language;
import com.translate.webtranslator.service.LanguageService;

import java.util.List;

@RestController
@RequestMapping("/api/languages")
public class LanguageController {

    private LanguageService languageService;

    @Autowired
    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    @GetMapping
    public List<Language> getAllLanguages(){
        return languageService.getAllLanguages();
    }

    @GetMapping("/find/byId/{id}")
    public Language getTextById(@PathVariable Long id){
        return languageService.getLanguageById(id);
    }
    @GetMapping("/find/byLanguage/{language}")
    public Language getTextByText(@PathVariable String language){
        return languageService.getLanguageByLanguage(language);
    }
    
    @PostMapping("/create")
    public Language addLanguage(@RequestBody Language newLanguage){
        return languageService.saveLanguage(newLanguage);
    }

    @DeleteMapping("/delete/{languageId}")
    public String deleteTeam(@PathVariable Long languageId){
        return languageService.deleteLanguage(languageId);
    }

    @PutMapping("/addText")
    public Language addInTextList(@RequestParam Long languageId,@RequestParam Long textId){
        return languageService.addTextInTextList(languageId, textId);
    }

    @PutMapping("/delText")
    public Language delInTextList(@RequestParam Long languageId,@RequestParam Long textId){
        return languageService.delTextInTextList(languageId, textId);
    }
}