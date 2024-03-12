package com.translate.webtranslator.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.stereotype.Repository;

import com.translate.webtranslator.model.Text;

@Repository
public class InMemoryTranslateDAO {
	
	private final List<Text> listOfWords = new ArrayList<>();
	public List<Text> findAllWords(){
		return listOfWords;
	}
	
	public Text saveWordInDictionary(Text word) {
		listOfWords.add(word);
		return word;
	}
	
	public Text findByWordToTranslate(String wordToTranslate) {
		return listOfWords.stream()
				.filter(element->element.getText().equals(wordToTranslate))
				.findFirst()
				.orElse(null);
	}
	
	public Text updateDictionary(Text word) {
		var wordIndex = IntStream.range(0, listOfWords.size())
				.filter(index->listOfWords.get(index).getText().
						equals(word.getText()))
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
