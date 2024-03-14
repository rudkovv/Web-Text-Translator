package com.translate.webtranslator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.translate.webtranslator.model.Text;
import com.translate.webtranslator.service.TextService;

import java.util.List;

@RestController
@RequestMapping("/api/texts")
public class TextController {

    private TextService textService;

    @Autowired
    public TextController(TextService textService) {
        this.textService = textService;
    }

    @GetMapping
    public List<Text> getAllTexts(){
        return textService.getAllTexts();
    }
    @GetMapping("/find/byId/{id}")
    public Text getTextById(@PathVariable Long id){
        return textService.getTextById(id);
    }
    @GetMapping("/find/byText/{text}")
    public Text getTextByText(@PathVariable String text){
        return textService.getTextByText(text);
    }

    @PostMapping("/create")
    public String saveText(@RequestBody Text text){
        return textService.saveText(text);
    }

    @DeleteMapping("/delete/byId/{id}")
    public String deleteText(@PathVariable Long id){
        return textService.deleteText(id);
    }

    @PutMapping("/change")
    public Text update(@RequestParam Long textId,
    				   @RequestParam(required = false) String text){
        return textService.updateText(textId, text);
    }
}