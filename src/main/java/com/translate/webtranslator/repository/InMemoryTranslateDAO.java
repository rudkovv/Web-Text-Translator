package com.translate.webtranslator.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.stereotype.Repository;

import com.translate.webtranslator.model.TranslateWord;

@Repository
public class InMemoryTranslateDAO {
	
	private final List<TranslateWord> listOfWords = new ArrayList<>();
	public List<TranslateWord> findAllWords(){
		return listOfWords;
	}
	
	public TranslateWord saveWordInDictionary(TranslateWord word) {
		listOfWords.add(word);
		return word;
	}
	
	public TranslateWord findByWordToTranslate(String wordToTranslate) {
		return listOfWords.stream()
				.filter(element->element.getWordToTranslate().equals(wordToTranslate))
				.findFirst()
				.orElse(null);
	}
	
	public TranslateWord updateDictionary(TranslateWord word) {
		var wordIndex = IntStream.range(0, listOfWords.size())
				.filter(index->listOfWords.get(index).getWordToTranslate().
						equals(word.getWordToTranslate()))
				.findFirst()
				.orElse(-1);
		if (wordIndex > -1) {
			listOfWords.set(wordIndex, word);
			return word;
		}
		return null;
	}
	
	public void deleteWord(String wordToTranslate) {
		var word = findByWordToTranslate(wordToTranslate);
		if (word != null) {
			listOfWords.remove(word);
		}
	}
}
