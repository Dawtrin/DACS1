package com.soulvirart.model;

public class VirtualTour {
    private int tourId;
    private String title;
    private String description;
    private double price;

    public VirtualTour() {}

    public VirtualTour(int tourId, String title, String description, double price) {
        this.tourId = tourId;
        this.title = title;
        this.description = description;
        this.price = price;
    }

    // Getters and Setters
    public int getTourId() { return tourId; }
    public void setTourId(int tourId) { this.tourId = tourId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}