package com.momentum.planning.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateSubTaskRequest {
    @NotBlank
    private String title;
    private Integer sortOrder;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
}