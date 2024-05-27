package com.translate.webtranslator.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.translate.webtranslator.cache.CacheKey;
import com.translate.webtranslator.cache.InMemoryCache;
import com.translate.webtranslator.model.Language;
import com.translate.webtranslator.model.Text;
import com.translate.webtranslator.model.Translation;
import com.translate.webtranslator.repository.LanguageRepository;
import com.translate.webtranslator.repository.TextRepository;
import com.translate.webtranslator.repository.TranslationRepository;

@ExtendWith(MockitoExtension.class)
class TextServiceTest {

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
		assertNotNull(textService);
	}

	@Test
	void shouldReturnPageOfTextsWhenTextsExist() {
		int page = 1;
		int size = 10;
		Pageable pageable = PageRequest.of(page - 1, size, Sort.by("textToTranslate").ascending());
		Page<Text> expectedPage = new PageImpl<>(List.of(new Text()));
		when(textRepository.findAllWithPagination(pageable)).thenReturn(expectedPage);
		Page<Text> actualPage = textService.getTextsWithPagination(page, size);
		assertThat(actualPage).isEqualTo(expectedPage);
		verify(textRepository).findAllWithPagination(pageable);
		verify(textRepository, times(0)).findById(1L);
		verify(textRepository, times(0)).save(new Text());
		verify(textRepository, times(0)).deleteById(1L);
	}

	@Test
	void shouldReturnEmptyPageWhenTextsDoNotExist() {
		int page = 1;
		int size = 10;
		Pageable pageable = PageRequest.of(page - 1, size, Sort.by("textToTranslate").ascending());
		when(textRepository.findAllWithPagination(pageable)).thenReturn(Page.empty());
		Page<Text> actualPage = textService.getTextsWithPagination(page, size);
		assertThat(actualPage).isEmpty();
		verify(textRepository).findAllWithPagination(pageable);
		verify(textRepository, times(0)).findById(1L);
		verify(textRepository, times(0)).save(new Text());
		verify(textRepository, times(0)).deleteById(1L);
	}

	@Test
	void saveText_existingText() {
		Text text = new Text();
		text.setTextToTranslate("Hello world");
		Optional<Text> existingText = Optional.of(text);
		when(textRepository.findByTextToTranslate(text.getTextToTranslate())).thenReturn(existingText);
		Text savedText = textService.saveText(text);
		verify(textRepository, never()).save(text);
		verifyNoInteractions(textCache);
		assertEquals(text, savedText);
	}

	@Test
	void saveText_nullText() {
		Text text = null;
		assertThrows(IllegalArgumentException.class, () -> textService.saveText(text));
		verifyNoInteractions(textRepository);
		verifyNoInteractions(textCache);
	}

	@Test
	void testGetTextByIdTextInCache() {
		Long textId = 1L;
		Text cachedText = new Text();
		cachedText.setId(textId);
		cachedText.setTextToTranslate("Cached text");
		when(textCache.get(new CacheKey(textId))).thenReturn(cachedText);
		Text result = textService.getTextById(cachedText.getId());
		assertEquals(cachedText, result);
		verify(textRepository, never()).findById(anyLong());
		verify(textCache, never()).put(any(CacheKey.class), any(Text.class));
	}

	@Test
	void testGetTextByIdTextInRepository() {
		Long textId = 1L;
		Text repositoryText = new Text();
		repositoryText.setId(textId);
		repositoryText.setTextToTranslate("Repository text");
		when(textCache.get(new CacheKey(textId))).thenReturn(null);
		when(textRepository.findById(textId)).thenReturn(Optional.of(repositoryText));
		Text result = textService.getTextById(textId);
		assertEquals(repositoryText, result);
		verify(textRepository, times(1)).findById(textId);
	}

	@Test
	void testGetTextByIdTextNotFound() {
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
		repositoryText.setTextToTranslate(text);
		CacheKey cacheKey = new CacheKey(text);
		when(textCache.get(cacheKey)).thenReturn(null);
		when(textRepository.findByTextToTranslate(text)).thenReturn(Optional.of(repositoryText));
		Text result = textService.getTextByText(text);
		assertEquals(repositoryText, result);
		verify(textRepository, times(1)).findByTextToTranslate(text);
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
		existingText.setTextToTranslate(oldText);
		when(textRepository.findById(textId)).thenReturn(Optional.of(existingText));
		when(textRepository.save(existingText)).thenReturn(existingText);
		Text result = textService.updateText(textId, newText);
		assertEquals(newText, result.getTextToTranslate());
		verify(textRepository, times(1)).findById(textId);
		verify(textRepository, times(1)).save(existingText);
	}

	@Test
	void testUpdateTextNonExistingText() {
		Long textId = 1L;
		String newText = "Updated text";
		when(textRepository.findById(textId)).thenReturn(Optional.empty());
		assertThrows(IllegalStateException.class, () -> textService.updateText(textId, newText));
		verify(textRepository, times(1)).findById(textId);
		verify(textRepository, never()).save(any(Text.class));
		verify(textCache, never()).put(any(CacheKey.class), any(Text.class));
	}

	@Test
	void testFindTextsSortedByLanguage() {
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
		existingText.setTextToTranslate("Old Text");
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
		existingText.setTextToTranslate("Text");
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
	void testFindTextsByLanguage() {
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
		text1.setTextToTranslate("Text 1");
		Text text2 = new Text();
		text2.setId(2L);
		text2.setTextToTranslate("Text 2");
		Text text3 = new Text();
		text3.setId(3L);
		text3.setTextToTranslate("Text 3");
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
