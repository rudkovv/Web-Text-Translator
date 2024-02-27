package com.translate.webtranslator.model;

public class TranslateWord {

	private String wordToTranslate;
	private String wordAfterTranslate;
	
	public TranslateWord(){
		wordToTranslate = null;
		wordAfterTranslate = null;
	}
	
	public TranslateWord(String wordToTranslate,String wordAfterTranslate){
		this.wordToTranslate = wordToTranslate;
		this.wordAfterTranslate = wordAfterTranslate;
	}
	
	public String getWordToTranslate() {
		return wordToTranslate;
	}
	
	public String getWordAfterTranslate() {
		return wordAfterTranslate;
	}
	
	public void setWordToTranslate(String wordToTranslate) {
		this.wordToTranslate = wordToTranslate;
	}
	
	public void setWordAfterTranslate(String wordAfterTranslate) {
		this.wordAfterTranslate = wordAfterTranslate;
	}
}
