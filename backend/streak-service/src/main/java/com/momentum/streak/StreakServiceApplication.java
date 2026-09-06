package com.momentum.streak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;

@SpringBootApplication
@EnableRabbit
public class StreakServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(StreakServiceApplication.class, args);
    }
}
