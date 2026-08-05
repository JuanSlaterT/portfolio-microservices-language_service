package com.juandiego.language_service.exceptions;

public class LanguageNotFoundException extends RuntimeException {
    
    public LanguageNotFoundException(String language){
        super("No se encontró el idioma "+language);
    }
    
}
