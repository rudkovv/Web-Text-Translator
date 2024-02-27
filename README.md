# Web text translator
Web приложение представляет собой переводчик текста, разработанное на Java с использованием Spring Boot Framework. 
## Описание
Приложение позволяет перевести текст. В основе реализации лежит архитектурный паттерн MVC (Model-View-Controller), что обеспечивает четкое разделение бизнес-логики, представления и управления веб-запросами.
## Задание №1
1. Создайте и запустите локально простейший веб/REST сервис, используя любой открытый пример с использованием Java stack: Spring (Spring Boot)/Maven/Gradle/Jersey/Spring MVC.
2. Добавьте GET ендпоинт, принимающий входные параметры в качестве queryParams в URL согласно варианту, и возвращающий любой hard-coded результат в виде JSON согласно варианту.

## API Endpoints
- GET /api/translator/allWords: Получить все слова с переводом в словаре.
- GET /api/translator/{word}: Получить перевод слова {word}. 
- POST /api/translator/save_word_in_dictionary: Сохранить слово и его перевод в словарь. 
- PUT /api/translator/update_word_in_dictionary: Обновить перевод слова в словаре.
- DELETE /api/translator/delete_word/{word}: Удалить слово {word} и его перевод из словаря.

## Как запустить проект
1. Клонируем репозиторий: https://github.com/rudkovv/Web-Text-Translator.
2. Запустить проект через WebTranslatorApplication.java.

## Пример вывода
После успешного запуска приложения откройте веб-браузер и перейдите по адресу http://localhost:8080/api/translator/save_word_in_dictionary, чтобы добавить слова и их перевод в словарь(с помощью postman). Перейдите по адресу http://localhost:8080/api/translator/allWords, чтобы увидеть добавленные слова в формате JSON.

Вывод имеет вид: {"wordToTranslate" : "Word","wordAfterTranslate" : "Слово"}.

