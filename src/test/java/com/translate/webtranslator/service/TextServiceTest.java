package com.translate.webtranslator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.translate.webtranslator.cache.CacheKey;
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
	
	@BeforeEach
    void setUp() {
        textService = new TextService(textRepository, textCache);
    }
	
	@Test
	void testConstructorInjection() {
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
    public void testGetTextByIdTextInCache() {
        Long textId = 1L;
        Text cachedText = new Text();
        cachedText.setId(textId);
        cachedText.setText("Cached text");
        when(textCache.get(new CacheKey(textId))).thenReturn(cachedText);
        Text result = textService.getTextById(cachedText.getId());
        assertEquals(cachedText, result);
        verify(textRepository, never()).findById(anyLong());
        verify(textCache, never()).put(any(CacheKey.class), any(Text.class));
    }
    
    @Test
    public void testGetTextByIdTextInRepository() {
        Long textId = 1L;
        Text repositoryText = new Text();
        repositoryText.setId(textId);
        repositoryText.setText("Repository text");
        when(textCache.get(new CacheKey(textId))).thenReturn(null);
        when(textRepository.findById(textId)).thenReturn(Optional.of(repositoryText));
        Text result = textService.getTextById(textId);
        assertEquals(repositoryText, result);
        verify(textRepository, times(1)).findById(textId);
    }
    
    @Test
    public void testGetTextByIdTextNotFound() {
        Long textId = 1L;
        when(textRepository.findById(textId)).thenReturn(Optional.empty());
        Text result = textService.getTextById(textId);
        assertEquals(null, result);
        verify(textRepository, times(1)).findById(textId);
        verify(textCache, never()).put(any(CacheKey.class), any(Text.class));
    }
    
    @Test
    void testGetTextByTextTextInRepository() {
        String text = "Text to translate";
        Text repositoryText = new Text();
        repositoryText.setId(1L);
        repositoryText.setText(text);
        CacheKey cacheKey = new CacheKey(text);
        when(textCache.get(cacheKey)).thenReturn(null);
        when(textRepository.findByTextToTranslate(text)).thenReturn(Optional.of(repositoryText));
        Text result = textService.getTextByText(text);
        assertEquals(repositoryText, result);
        verify(textRepository, times(1)).findByTextToTranslate(text);
    }
    
    @Test
    public void testSaveText() {
        Text text = new Text();
        text.setId(1L);
        text.setText("Sample text");
        String result = textService.saveText(text);
        textCache.put(new CacheKey(text.getId()), text);
        assertEquals("Sample text successfully save", result);
        verify(textRepository, times(1)).save(text);
    }
    
    @Test
    void testGetTextByTextTextNotFound() {
        String text = "Text to translate";
        when(textRepository.findByTextToTranslate(text)).thenReturn(Optional.empty());
        Text result = textService.getTextByText(text);
        assertNull(result);
        verify(textRepository, times(1)).findByTextToTranslate(text);
    }
    
    @Test
    void testUpdateTextTextExists() {
        Long textId = 1L;
        String newText = "Updated text";
        String oldText = "Old text";
        Text existingText = new Text();
        existingText.setId(textId);
        existingText.setText(oldText);
        when(textRepository.findById(textId)).thenReturn(Optional.of(existingText));
        when(textRepository.save(existingText)).thenReturn(existingText);
        Text result = textService.updateText(textId, newText);
        assertEquals(newText, result.getText());
        verify(textRepository, times(1)).findById(textId);
        verify(textRepository, times(1)).save(existingText);
    }
    
    @Test
    public void testUpdateTextNonExistingText() {
        Long textId = 1L;
        String newText = "Updated text";
        when(textRepository.findById(textId)).thenReturn(Optional.empty());
        assertThrows(IllegalStateException.class, () -> textService.updateText(textId, newText));
        verify(textRepository, times(1)).findById(textId);
        verify(textRepository, never()).save(any(Text.class));
        verify(textCache, never()).put(any(CacheKey.class), any(Text.class));
    }
    
    @Test
    public void testFindTextsSortedByLanguage() {
        String language = "English";
        List<String> expectedTexts = Arrays.asList("Text 1", "Text 2", "Text 3");
        when(textRepository.findTextsSortedByLanguage(language)).thenReturn(expectedTexts);
        List<String> result = textService.findTextsSortedByLanguage(language);
        assertEquals(expectedTexts, result);
        verify(textRepository, times(1)).findTextsSortedByLanguage(language);
    }
    
    @Test
    void testUpdateTextEmptyNewText() {
        Long textId = 1L;
        String newText = "";
        Text existingText = new Text();
        existingText.setId(textId);
        existingText.setText("Old Text");
        when(textRepository.findById(textId)).thenReturn(Optional.of(existingText));
        when(textRepository.save(existingText)).thenReturn(existingText);
        Text result = textService.updateText(textId, newText);
        assertEquals(existingText, result);
        verify(textRepository, times(1)).findById(textId);
        verify(textRepository, times(1)).save(existingText);
    }
    
    @Test
    void testDeleteTextTextExists() {
        Long textId = 1L;
        Text existingText = new Text();
        existingText.setId(textId);
        existingText.setText("Text");
        List<Translation> translationsList = new ArrayList<>();
        Translation translation1 = new Translation();
        translation1.setTranslatedText("Translation 1");
        Translation translation2 = new Translation();
        translation2.setTranslatedText("Translation 2");
        translationsList.add(translation1);
        translationsList.add(translation2);
        existingText.setTranslations(translationsList);
        List<Language> languagesList = new ArrayList<>();
        Language language1 = new Language();
        language1.setName("Language 1");
        Language language2 = new Language();
        language2.setName("Language 2");
        languagesList.add(language1);
        languagesList.add(language2);
        existingText.setLanguages(languagesList);
        when(textRepository.findById(textId)).thenReturn(Optional.of(existingText));
        String result = textService.deleteText(textId);
        assertEquals("successfully delete text", result);
        verify(textRepository, times(1)).findById(textId);
        verify(textRepository, times(1)).deleteById(textId);
        for (Translation translation : translationsList) {
            assertNull(translation.getText());
        }
        for (Language language : languagesList) {
            assertNull(language.getTexts());
        }
    }
    
    @Test
    void testDeleteTextTextDoesNotExist() {
        Long textId = 1L;
        when(textRepository.findById(textId)).thenReturn(Optional.empty());
        assertThrows(IllegalStateException.class, () -> textService.deleteText(textId));
        verify(textCache, times(1)).remove(any(CacheKey.class));
        verify(textRepository, times(1)).findById(textId);
        verify(textRepository, never()).deleteById(textId);
    }
    
    @Test
    public void testFindTextsByLanguage() {
        String language = "English";
        List<String> expectedTexts = Arrays.asList("Text 1", "Text 2", "Text 3");
        when(textRepository.findTextsByLanguage(language)).thenReturn(expectedTexts);
        List<String> result = textService.findTextsByLanguage(language);
        assertEquals(expectedTexts, result);
        verify(textRepository, times(1)).findTextsByLanguage(language);
    }
    
    @Test
    void testBulkSaveText() {
        List<Text> texts = new ArrayList<>();
        Text text1 = new Text();
        text1.setId(1L);
        text1.setText("Text 1");
        Text text2 = new Text();
        text2.setId(2L);
        text2.setText("Text 2");
        Text text3 = new Text();
        text3.setId(3L);
        text3.setText("Text 3");
        texts.add(text1);
        texts.add(text2);
        texts.add(text3);
        List<String> result = textService.bulkSaveText(texts);
        assertEquals(3, result.size());
        verify(textRepository, times(1)).saveAll(texts);
        assertEquals("Text 1 - created", result.get(0));
        assertEquals("Text 2 - created", result.get(1));
        assertEquals("Text 3 - created", result.get(2));
    }

    @Test
    void testTextRepositoryNotNull() {
        assertNotNull(textService.getTextRepository());
    }

    @Test
    void testTextCacheNotNull() {
        assertNotNull(textService.getTextCache());
    }
}
