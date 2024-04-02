package com.translate.webtranslator.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

/**
 * The SwaggerConfig class is responsible for configuring the OpenAPI
 *  documentation for the Web-Text-Translator application.
 */
@OpenAPIDefinition(
        info = @Info(
                title = "Web-Text-Translator",
                description = "text translator",
                version = "1.0.0",
                contact = @Contact(
                        name = "Rudko Vadim",
                        email = "vrudko22@@gmail.com"
                )
        )
)

public class SwaggerConfig {
}