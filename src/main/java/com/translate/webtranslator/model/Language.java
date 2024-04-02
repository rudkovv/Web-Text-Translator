package com.translate.webtranslator.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

/**
 * The Language class represents a language in the Web-Text-Translator application.
 * It contains information about the language's ID, name, and a list of texts
 * associated with the language.
 */
@Entity
public class Language {
	@Id
	@Column(unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@NotBlank
    private String name;

    @ManyToMany(mappedBy = "languages",
    		    cascade = {CascadeType.PERSIST, CascadeType.DETACH, 
    		    		   CascadeType.MERGE, CascadeType.REFRESH})
    
    private List<Text> texts;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Text> getTexts() {
        return texts;
    }

    public void setTexts(List<Text> texts) {
        this.texts = texts;
    }
}