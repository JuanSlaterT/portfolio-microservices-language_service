package com.juandiego.language_service.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.juandiego.language_service.responses.LanguageCatalogResponse;
import com.juandiego.language_service.services.LanguageService;

import tools.jackson.databind.JsonNode;

@RestController
@RequestMapping("/api/languages")
public class LanguageController {

    private final LanguageService languageService;

    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    @GetMapping
    public LanguageCatalogResponse getLanguages() {
        return languageService.getLanguages();
    }

    @GetMapping("/{language}")
    public JsonNode getLanguage(
            @PathVariable String language
    ) {
        return languageService.getLanguage(language);
    }
}