package com.example.aijavagrammar.dto;

import io.github.cdimascio.dotenv.Dotenv;

public class AppConfigDTO {
    private final static Dotenv dotenv = Dotenv.configure().directory("/app").load();
    
    public static String get(String key) {
        return dotenv.get(key);
    }
}
