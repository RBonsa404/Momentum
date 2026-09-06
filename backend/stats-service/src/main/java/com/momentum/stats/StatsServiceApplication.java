package com.momentum.stats;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class StatsServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(StatsServiceApplication.class, args);
    }
}
