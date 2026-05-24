package com.perfectmarket.configs;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EnvCheck {
    @Value("${DB_URL}")
    private String dbUrl;

    @PostConstruct
    public void printConfig() {
        System.out.println("Database URL: " + dbUrl);
    }
}