package com.url_shortener.ShortURL;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

@Component 
public class CodeGenerator {
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int CODE_LENGTH = 8;

    public String generateCode() {
        StringBuilder str = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            str.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return str.toString();
    }
}
