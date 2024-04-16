package com.translate.webtranslator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.translate.webtranslator.aspect.AspectAnnotation;
import com.translate.webtranslator.exception.RestExceptionHandler;
import com.translate.webtranslator.model.Text;
import com.translate.webtranslator.service.TextService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import java.util.List;

/**
 * Controller for text.
 */
@RestExceptionHandler
@RestController
@RequestMapping("/api/texts")
public class TextController {

    private TextService textService;

    @Autowired
    public TextController(TextService textService) {
        this.textService = textService;
    }

    @GetMapping
    @AspectAnnotation
    @Operation(summary = "Get all the text",
               description = "Allows you to view all the texts in the database")
    public String getAllTexts() {
        return textService.getAllTexts();
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
    public String saveText(@Valid @RequestBody Text text) {
        return textService.saveText(text);
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
    public List<String> bulkSaveText(@Valid @RequestBody final List<Text> texts) {
        return textService.bulkSaveText(texts);
    }

}