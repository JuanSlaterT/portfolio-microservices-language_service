package com.juandiego.language_service.services;

import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import com.juandiego.language_service.exceptions.LanguageNotFoundException;
import com.juandiego.language_service.responses.LanguageCatalogResponse;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

@Service
public class LanguageService {
    private static final String LANGUAGES_PATTERN = "classpath*:languages/*.json";
    private final JsonMapper jsonMapper;
    private final ResourcePatternResolver resourceResolver;

    public LanguageService(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
        this.resourceResolver = new PathMatchingResourcePatternResolver();
    }

    public LanguageCatalogResponse getLanguages() {
        List<String> languages = Arrays.stream(loadLanguageResources())
                .map(Resource::getFilename)
                .filter(Objects::nonNull)
                .map(this::removeJsonExtension)
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList();
        return new LanguageCatalogResponse(languages.size(), languages);
    }

    public JsonNode getLanguage(String requestedLanguage) {
        String normalizedRequestLanguage = normalizeLanguageName(requestedLanguage);
        Resource resource = Arrays.stream(loadLanguageResources())
                .filter(item -> matchesLanguage(item, normalizedRequestLanguage))
                .findFirst()
                .orElseThrow(
                        () -> new LanguageNotFoundException(requestedLanguage));
        try (InputStream inputStream = resource.getInputStream()) {
            return jsonMapper.readTree(inputStream);
        } catch (IOException ex) {
            throw new IllegalStateException(
                "No se pudo leer el arachivo de idiomas: "+requestedLanguage, ex
            );
        }

    }

    private Resource[] loadLanguageResources() {
        try {
            return resourceResolver.getResources(LANGUAGES_PATTERN);
        } catch (IOException ex) {
            throw new IllegalStateException("No se pudieron listar los archivos de idiomas", ex);
        }
    }

    private String removeJsonExtension(String filename) {
        return filename.replaceFirst("(?i)\\.json$", "");
    }

    private String normalizeLanguageName(String value) {
        String withoutAccents = Normalizer
                .normalize(value.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");

        return withoutAccents.toLowerCase(Locale.ROOT);
    }

    private boolean matchesLanguage(
            Resource resource,
            String normalizedRequestedLanguage) {
        String filename = resource.getFilename();

        if (filename == null) {
            return false;
        }

        String languageName = removeJsonExtension(filename);

        return normalizeLanguageName(languageName)
                .equals(normalizedRequestedLanguage);
    }

}
