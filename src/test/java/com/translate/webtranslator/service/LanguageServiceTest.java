package com.translate.webtranslator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
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
import com.translate.webtranslator.model.Language;
import com.translate.webtranslator.model.Text;
import com.translate.webtranslator.repository.LanguageRepository;
import com.translate.webtranslator.repository.TextRepository;
import com.translate.webtranslator.repository.TranslationRepository;

@ExtendWith(MockitoExtension.class)
class LanguageServiceTest {

	@Mock
	private TextRepository textRepository;

	@Mock
	private TranslationRepository translationRepository;

	@Mock
	private LanguageRepository languageRepository;

	@Mock
	private InMemoryCache languageCache;

	@InjectMocks
	private LanguageService languageService;

	@BeforeEach
	void setUp() {
		languageService = new LanguageService(languageRepository, textRepository, languageCache);
	}

	@Test
	void testConstructorInjection() {
		languageService = new LanguageService(languageRepository, textRepository);
		assertNotNull(languageService);
	}

	@Test
	void testGetAllLanguages() {
		List<Language> expectedLanguages = new ArrayList<>();
		Language language1 = new Language();
		language1.setId(1L);
		language1.setName("English");
		Language language2 = new Language();
		language2.setId(2L);
		language2.setName("French");
		expectedLanguages.add(language1);
		expectedLanguages.add(language2);
		when(languageRepository.findAll()).thenReturn(expectedLanguages);
		List<Language> actualLanguages = languageService.getAllLanguages();
		assertEquals(expectedLanguages, actualLanguages);
		verify(languageRepository, times(1)).findAll();
	}

	@Test
	void testGetLanguageByIdCachedLanguageExists() {
		Language cachedLanguage = new Language();
		cachedLanguage.setId(1L);
		cachedLanguage.setName("English");
		languageCache.put(new CacheKey(1L), cachedLanguage);
		when(languageCache.get(new CacheKey(1L))).thenReturn(cachedLanguage);
		Language language = languageService.getLanguageById(1L);
		assertNotNull(cachedLanguage);
		assertEquals(cachedLanguage, language);
		verify(languageRepository, never()).findById(anyLong());
	}

	@Test
	void testGetLanguageByIdCachedLanguageDoesNotExist() {
		Language uncachedLanguage = new Language();
		uncachedLanguage.setId(1L);
		uncachedLanguage.setName("English");
		when(languageRepository.findById(1L)).thenReturn(Optional.of(uncachedLanguage));
		Language language = languageService.getLanguageById(1L);
		assertEquals(uncachedLanguage, language);
		verify(languageRepository, times(1)).findById(1L);
	}

	@Test
	void testGetLanguageByLanguageCachedLanguageDoesNotExist() {
		Language uncachedLanguage = new Language();
		uncachedLanguage.setId(1L);
		uncachedLanguage.setName("English");
		when(languageRepository.findByName("English")).thenReturn(uncachedLanguage);
		Language language = languageService.getLanguageByLanguage("English");
		assertEquals(uncachedLanguage, language);
		verify(languageRepository, times(1)).findByName("English");
	}

	@Test
	void testDeleteLanguageThrowsExceptionWhenLanguageNotFound() {
		Long languageId = 1L;
		when(languageRepository.findById(languageId)).thenReturn(Optional.empty());
		Exception exception = assertThrows(IllegalStateException.class,
				() -> languageService.deleteLanguage(languageId));
		verify(languageRepository).findById(languageId);
		assertEquals("Error to delete", exception.getMessage());
	}

	@Test
	void shouldThrowExceptionWhenLanguageNotFound() {
		when(languageRepository.findById(any())).thenReturn(Optional.empty());

		assertThrows(IllegalStateException.class, () -> languageService.deleteLanguage(1L));
	}
	
	@Test
    void shouldSaveLanguage() {
        Language language = new Language();
        language.setName("English");

        when(languageRepository.save(any())).thenReturn(language);

        Language savedLanguage = languageService.saveLanguage(language);

        assertEquals(language, savedLanguage);
    }

    @Test
    void shouldSaveLanguageWithTexts() {
        Language language = new Language();
        language.setName("English");

        Text text = new Text();
        text.setTextToTranslate("text");

        language.setTexts(List.of(text));

        when(languageRepository.save(any())).thenReturn(language);
        when(textRepository.save(any())).thenReturn(text);

        Language savedLanguage = languageService.saveLanguage(language);

        assertEquals(language, savedLanguage);
    }

	@Test
	void testAddTextInTextList() {
		Long languageId = 1L;
		Language language = new Language();
		language.setId(languageId);
		language.setName("English");
		Long textId = 1L;
		Text text = new Text();
		text.setId(textId);
		text.setLanguages(new ArrayList<>());
		when(languageRepository.findById(languageId)).thenReturn(Optional.of(language));
		when(textRepository.findById(textId)).thenReturn(Optional.of(text));
		when(textRepository.save(any(Text.class))).thenReturn(null);
		Language result = languageService.addTextInTextList(languageId, textId);
		verify(languageRepository).findById(languageId);
		verify(textRepository).findById(textId);
		verify(textRepository).save(any(Text.class));
		verify(languageCache).put(new CacheKey(language.getName()), language);
		assertEquals(language, result);
		assertEquals(1, text.getLanguages().size());
		assertEquals(language, text.getLanguages().get(0));
	}

	@Test
	void testDelTextInTextList() {
		Long languageId = 1L;
		Language language = new Language();
		language.setId(languageId);
		language.setName("English");
		Long textId = 1L;
		Text text = new Text();
		text.setId(textId);
		text.setLanguages(new ArrayList<>());
		text.getLanguages().add(language);
		when(textRepository.findById(textId)).thenReturn(Optional.of(text));
		when(languageRepository.findById(languageId)).thenReturn(Optional.of(language));
		when(textRepository.save(any(Text.class))).thenReturn(null);
		when(languageRepository.findById(languageId)).thenReturn(Optional.of(language));
		Language result = languageService.delTextInTextList(languageId, textId);
		verify(textRepository).findById(textId);
		verify(textRepository).save(any(Text.class));
		verify(languageCache).put(new CacheKey(language.getName()), language);
		assertEquals(language, result);
		assertEquals(0, text.getLanguages().size());
	}

	@Test
	void testBulkSaveLanguage() {
		List<Language> languages = new ArrayList<>();
		Language language1 = new Language();
		language1.setId(1L);
		language1.setName("English");
		languages.add(language1);
		Language language2 = new Language();
		language2.setId(2L);
		language2.setName("French");
		languages.add(language2);
		when(languageRepository.saveAll(languages)).thenReturn(languages);
		List<String> result = languageService.bulkSaveLanguage(languages);
		verify(languageRepository).saveAll(languages);
		verify(languageCache, times(2)).put(any(CacheKey.class), any(Language.class));
		assertEquals(2, result.size());
		assertEquals("English - created", result.get(0));
		assertEquals("French - created", result.get(1));
	}
}