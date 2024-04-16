package com.translate.webtranslator.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.translate.webtranslator.cache.InMemoryCache;
import com.translate.webtranslator.model.Language;
import com.translate.webtranslator.model.Text;
import com.translate.webtranslator.model.Translation;
import com.translate.webtranslator.repository.LanguageRepository;
import com.translate.webtranslator.repository.TextRepository;
import com.translate.webtranslator.repository.TranslationRepository;

@ExtendWith(MockitoExtension.class)
public class TextServiceTest {

	@Mock
	private TextRepository textRepository;
	
	@Mock
	private TranslationRepository translationRepository;
	
	@Mock
	private LanguageRepository languageRepository;
	
	@Mock
	private InMemoryCache textCache;
	
	@InjectMocks
	private TextService textService;
	
	private static List<Text> textList;

    private Text text;

    private Translation translation, translationWithText;

    private Language language;

    private final Long textId = 100L;

    private final Long translationId = 101L;

    private final Long languageId = 102L;

    private static final int NUM_OF_REPEATS = 5;
    
    @BeforeEach
    void setUp(){
    	text = new Text();
    	text.setId(textId);
    	text.setText("testText");
    	
    	translation = new Translation();
    	translation.setId(translationId);
    	translation.setTranslatedText("testTranslation");
    	
    	translationWithText = new Translation();
    	translationWithText.setId(translationId);
    	translationWithText.setTranslatedText("testTranslation");
    	
    	language = new Language();
    	language.setId(languageId);
    	language.setName("testLanguage");    	
    }
    
    @BeforeAll
    static void setUpList(){
        textList = new ArrayList<>();
        for(int i=0; i<NUM_OF_REPEATS; i++){
            Text text = new Text();
            text.setId((long)i+1);
            text.setText("testText" + i);
            textList.add(text);
        }
    }
    
    @Test
    void testGetAllTexts(){
        Mockito.when(textRepository.findAll()).thenReturn(textList);
        String result = textService.getAllTexts();
        assertEquals(result, textList);
    }
    
    @Test
    void testGetText(){
        Mockito.when(textRepository.findById(textId)).thenReturn(Optional.of(text));
        Text result = textService.getTextById(textId);
        assertNotNull(result);
        assertEquals(result, text);
    }
    
//    @Test
//    void testGetTextException(){
//        Mockito.when(textRepository.findById(textId)).thenReturn(Optional.empty());
//        assertThrows(TextNotFoundException.class, () -> textService.getText(textId));
//    }
    
    @Test
    void testSaveText(){
        Mockito.when(textRepository.save(text)).thenReturn(text);
        String result = textService.saveText(text);
        assertNotNull(result);
        assertEquals(result, text);
        Mockito.verify(textRepository, Mockito.times(1)).save(text);
    }
	
}
