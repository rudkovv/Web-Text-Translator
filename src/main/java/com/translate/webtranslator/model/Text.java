package com.translate.webtranslator.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * The Text class represents a text in the Web-Text-Translator application.
 * It contains information about the text's ID, the text to translate, a list of translations,
 * and a list of languages associated with the text.
 */
@Entity
public class Text {
	@Id
	@Column(unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String textToTranslate;
	
	@JsonIgnoreProperties("text")
	@OneToMany(mappedBy = "text",  fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL})
	private List<Translation> translations;

    @ManyToMany(cascade = {CascadeType.ALL},
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

    public String getTextToTranslate() {
        return textToTranslate;
    }

    public void setTextToTranslate(String text) {
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
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id: ").append(id);
        sb.append("\ntext: ").append(textToTranslate);

        if (translations != null && !translations.isEmpty()) {
            sb.append("\ntranslations: ");
            for (Translation translation : translations) {
                sb.append(translation.getTranslatedText()).append(" ");
            }
        }

        if (languages != null && !languages.isEmpty()) {
            sb.append("\nlanguages: ");
            for (Language language : languages) {
                sb.append(language.getName()).append(" ");
            }
        }
        sb.append("\n");

        return sb.toString();
    }

}