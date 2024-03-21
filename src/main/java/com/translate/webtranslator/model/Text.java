package com.translate.webtranslator.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;

@Entity
public class Text {
	@Id
	@Column(unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String textToTranslate;
	
	@JsonIgnoreProperties("text")
	@OneToMany(mappedBy = "text",  fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
	private List<Translation> translations;

    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.DETACH,CascadeType.REFRESH},
    	    fetch = FetchType.LAZY)
    @JsonIgnoreProperties("texts")
    @JoinTable(
        name = "textLanguage",
        joinColumns = @JoinColumn(name = "textId"),
        inverseJoinColumns = @JoinColumn(name = "languageId")
    )
    private List<Language> languages;
    @JsonIgnoreProperties("languages")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return textToTranslate;
    }

    public void setText(String text) {
        this.textToTranslate = text;
    }

    public List<Translation> getTranslations() {
        return translations;
    }

    public void setTranslations(List<Translation> translations) {
        this.translations = translations;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }

}