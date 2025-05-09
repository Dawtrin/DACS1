package com.soulvirart.model;

import java.time.LocalDate;

public class Exhibition {
    private int exhibitionId;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;

    public Exhibition() {}

    public Exhibition(int exhibitionId, String title, String description, LocalDate startDate, LocalDate endDate, String status) {
        this.exhibitionId = exhibitionId;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    // Getters and Setters
    public int getExhibitionId() { return exhibitionId; }
    public void setExhibitionId(int exhibitionId) { this.exhibitionId = exhibitionId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}