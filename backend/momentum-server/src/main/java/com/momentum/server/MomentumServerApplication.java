package com.momentum.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MomentumServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MomentumServerApplication.class, args);
    }
}
