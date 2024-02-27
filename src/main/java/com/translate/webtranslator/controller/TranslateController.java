package com.translate.webtranslator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.translate.webtranslator.model.TranslateWord;
import com.translate.webtranslator.service.TranslateService;

import java.util.List;

@RestController
@RequestMapping("/api/translator")
public class TranslateController {
	
	@Autowired
	private TranslateService service;
	
	@GetMapping("/allWords")
	public List<TranslateWord> findAllWords(){
		return service.findAllWords();
	}
	
	@PostMapping("save_word_in_dictionary")
	public String saveWordInDictionary(@RequestBody TranslateWord word) {
		service.saveWordInDictionary(word);
		return "Word save in dictionary";
	}
	
	@GetMapping("/{wordToTranslate}")
	public TranslateWord findTranslate(@PathVariable String wordToTranslate) {
		return service.findByWordToTranslate(wordToTranslate);
	}
	
	@PutMapping("update_word_in_dictionary")
	public TranslateWord updateWordInDictionary(@RequestBody TranslateWord word) {
		return service.updateDictionary(word);
	}
	
	@DeleteMapping("delete_word/{wordToTranslate}")
	public void deleteWordInDictionaty(@PathVariable String wordToTranslate) {
		service.deleteWord(wordToTranslate);
	}
}
