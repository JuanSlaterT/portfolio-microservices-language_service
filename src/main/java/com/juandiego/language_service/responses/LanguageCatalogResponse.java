package com.juandiego.language_service.responses;

import java.util.List;

public record LanguageCatalogResponse (int count, List<String> languages) {
    
}
