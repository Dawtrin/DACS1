package com.soulvirart.model;

public class ArtworkTour {
    private int tourId;
    private int artworkId;

    public ArtworkTour() {}

    public ArtworkTour(int tourId, int artworkId) {
        this.tourId = tourId;
        this.artworkId = artworkId;
    }

    // Getters and Setters
    public int getTourId() { return tourId; }
    public void setTourId(int tourId) { this.tourId = tourId; }

    public int getArtworkId() { return artworkId; }
    public void setArtworkId(int artworkId) { this.artworkId = artworkId; }
}