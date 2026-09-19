package com.momentum.server.dto.goal;

import jakarta.validation.constraints.NotBlank;

public class CreateHabitRequest {
    @NotBlank
    private String title;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
}
