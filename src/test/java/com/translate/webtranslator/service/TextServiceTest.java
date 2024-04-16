package com.translate.webtranslator.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.translate.webtranslator.cache.InMemoryCache;
import com.translate.webtranslator.model.Text;
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
	
//	private static List<Text> textList;
//
//    private Text text;
//
//    private Translation translation, translationWithText;
//
//    private Language language;
//
//    private final Long textId = 100L;
//
//    private final Long translationId = 101L;
//
//    private final Long languageId = 102L;
//
//    private static final int NUM_OF_REPEATS = 5;
	@BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        textService = new TextService(textRepository);
    }
    
    @Test
    void testGetAllTexts() {
        List<Text> expectedTexts = new ArrayList<>();
        Text text1 = new Text();
        text1.setId(1L);
        text1.setText("Text 1");
        expectedTexts.add(text1);

        Text text2 = new Text();
        text2.setId(2L);
        text2.setText("Text 2");
        expectedTexts.add(text2);

        when(textRepository.findAll()).thenReturn(expectedTexts);

        List<Text> actualTexts = textService.getAllTexts();

        assertEquals(expectedTexts, actualTexts);
        verify(textRepository, times(1)).findAll();
    }
    
    @Test
    void testGetTextById() {
    	Text expectedText = new Text();
    	expectedText.setId(1L);
    	expectedText.setText("Text 1");

        when(textRepository.findById(1L)).thenReturn(Optional.of(expectedText));

        Text actualText = textService.getTextById(1L);

        assertEquals(expectedText, actualText);
        verify(textRepository, times(1)).findById(1L);

    }
    
    @Test
    void testDeleteTextById() {
    	 Long textId = 1L;

         textService.deleteText(textId);

         verify(textRepository, times(1)).deleteById(textId);
    }

}
