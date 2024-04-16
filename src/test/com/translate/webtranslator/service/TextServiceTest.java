package com.translate.webtranslator.service

import com.translate.webtranslator.model.Language;
import com.translate.webtranslator.model.Text;
import com.translate.webtranslator.model.Translation;



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
    	
    	translation = new translation();
    	translation.setId(translationId);
    	translation.setTranslatedText("testTranslation");
    	
    	translationWithText = new translation();
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
            text.setId((Long)i+1);
            text.setText("testText" + i);
            textList.add(car);
        }
    }
    
    @Test
    void testGetAllTexts(){
        Mockito.when(textRepository.findAll()).thenReturn(textList);
        List<Text> result = textService.getAllCars();
        assertEquals(result, textList);
    }
	
}
