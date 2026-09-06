package com.momentum.goal.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Map;

@FeignClient(name = "planning-service", path = "/api/planning")
public interface PlanningClient {
    @GetMapping("/days")
    Map<String, Object> day(@RequestParam("date") LocalDate date);
}
