package com.translate.webtranslator.exception;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.web.bind.annotation.RestController;

/**
 * The RestExceptionHandler annotation is used to mark a class as a REST exception handler.
 * It is a meta-annotation that combines the functionality of the Spring's @RestController
 * annotation with the ability to handle exceptions.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@RestController
public @interface RestExceptionHandler {
}