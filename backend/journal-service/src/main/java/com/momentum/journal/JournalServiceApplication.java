package com.momentum.journal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JournalServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(JournalServiceApplication.class, args);
    }
}
