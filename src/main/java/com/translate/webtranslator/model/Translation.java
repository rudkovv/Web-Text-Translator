package com.translate.webtranslator.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;

/**
 * The Translation class represents a translation in the Web-Text-Translator application.
 * It contains information about the translation's ID, the translated text, and the associated text.
 */
@Entity
public class Translation {
	@Id
	@Column(unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@NotBlank
    private String translatedText;

    @ManyToOne
    @JoinColumn(name = "textId")
    @JsonIgnoreProperties("translations")
    private Text text;

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTranslatedText() {
        return translatedText;
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id: ").append(id);
        sb.append("\ntranslation: ").append(translatedText).append("\n");

        if (text != null) {
            sb.append("text: ").append(text.getTextToTranslate());
        }
        
        sb.append("\n");
        
        return sb.toString();
    }
}