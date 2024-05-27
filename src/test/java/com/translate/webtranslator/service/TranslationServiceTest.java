package com.translate.webtranslator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
import com.translate.webtranslator.model.Text;
import com.translate.webtranslator.model.Translation;
import com.translate.webtranslator.repository.LanguageRepository;
import com.translate.webtranslator.repository.TextRepository;
import com.translate.webtranslator.repository.TranslationRepository;

@ExtendWith(MockitoExtension.class)
class TranslationServiceTest {

	@Mock
	private TextRepository textRepository;
	
	@Mock
	private TranslationRepository translationRepository;
	
	@Mock
	private LanguageRepository languageRepository;
	
	@Mock
	private InMemoryCache translationCache;
	
	@InjectMocks
	private TranslationService translationService;
	
	@BeforeEach
    void setUp() {
		translationService = new TranslationService(translationRepository, textRepository, translationCache);
    }
	
	@Test
	void testConstructorInjection() {
		translationService = new TranslationService(translationRepository,textRepository);
		assertNotNull(translationService);
	}
	
	@Test
	void testDeleteTranslation() {
	   Long translationId = 1L;
	   Translation translation = new Translation();
	   translation.setId(translationId);
       when(translationRepository.findById(translationId)).thenReturn(Optional.of(translation));
       Translation result = translationService.deleteTranslation(translationId);
       verify(translationRepository).findById(translationId);
       verify(translationRepository).deleteById(translationId);
       verify(translationCache).remove(new CacheKey(translationId));
       assertEquals(translation, result);
	}

	@Test
	void testSetNewText() {
       Long translationId = 1L;
       Long newTextId = 2L;
       Translation translation = new Translation();
       translation.setId(translationId);
       Text newText = new Text();
       newText.setId(newTextId);
       when(translationRepository.findById(translationId)).thenReturn(Optional.of(translation));
       when(textRepository.findById(newTextId)).thenReturn(Optional.of(newText));
       when(translationRepository.save(translation)).thenReturn(translation);
       Translation result = translationService.setNewText(translationId, newTextId);
       verify(translationRepository).findById(translationId);
       verify(textRepository).findById(newTextId);
       verify(translationRepository).save(translation);
       verify(translationCache).put(new CacheKey(translation.getId()), translation);
       assertEquals(translation, result);
       assertEquals(newText, translation.getText());
   }
	 
	@Test
    void testGetByIdCachedTranslation() {
       Long translationId = 1L;
       Translation cachedTranslation = new Translation();
       cachedTranslation.setId(translationId);
       when(translationCache.get(new CacheKey(translationId))).thenReturn(cachedTranslation);
       Translation result = translationService.getById(translationId);
       verify(translationCache).get(new CacheKey(translationId));
       verify(translationRepository, never()).findById(anyLong());
       verify(translationCache, never()).put(any(), any());
       assertEquals(cachedTranslation, result);
    }
	 
	@Test
	void testGetByIdNotCachedTranslation() {
       Long translationId = 1L;
       Translation translation = new Translation();
       translation.setId(translationId);
       when(translationCache.get(new CacheKey(translationId))).thenReturn(null);
       when(translationRepository.findById(translationId)).thenReturn(Optional.of(translation));
       Translation result = translationService.getById(translationId);
       verify(translationCache).get(new CacheKey(translationId));
       verify(translationRepository).findById(translationId);
       verify(translationCache).put(new CacheKey(translationId), translation);
       assertEquals(translation, result);
    }
	 
	@Test
	void testGetTranslationByTranslation() {
	   String translationText = "Translated Text";
	   Translation translation = new Translation();
	   translation.setTranslatedText(translationText);
       when(translationRepository.findByTranslatedText(translationText)).thenReturn(Optional.of(translation));
       Translation result = translationService.getTranslationByTranslation(translationText);
       verify(translationRepository).findByTranslatedText(translationText);
       assertEquals(translation, result);
	}
	 
	@Test
	void testBulkSaveTranslation() {
	   List<Translation> translations = new ArrayList<>();
	   Translation translation1 = new Translation();
	   translation1.setId(1L);
	   translation1.setTranslatedText("Translation 1");
	   Translation translation2 = new Translation();
	   translation2.setId(2L);
	   translation2.setTranslatedText("Translation 2");
	   translations.add(translation1);
	   translations.add(translation2);
	   when(translationRepository.saveAll(translations)).thenReturn(translations);
	   List<String> result = translationService.bulkSaveTranslation(translations);
	   verify(translationRepository).saveAll(translations);
	   translations.forEach(translation -> verify(translationCache).put(new CacheKey(translation.getId()), translation));
	   assertEquals(2, result.size());
	   assertTrue(result.contains("Translation 1 - created"));
	   assertTrue(result.contains("Translation 2 - created"));
	}
}