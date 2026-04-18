package com.perfectmarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PerfectMarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(PerfectMarketApplication.class, args);
    }
}
