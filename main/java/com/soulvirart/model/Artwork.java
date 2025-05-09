package com.soulvirart.model;

import java.time.LocalDate;

public class Artwork {
    private int artworkId;
    private String title;
    private String description;
    private LocalDate creationDate;
    private int artistId;
    private String imagePath;
    private Integer exhibitionId;

    public Artwork() {}

    public Artwork(int artworkId, String title, String description, LocalDate creationDate, int artistId, String imagePath, Integer exhibitionId) {
        this.artworkId = artworkId;
        this.title = title;
        this.description = description;
        this.creationDate = creationDate;
        this.artistId = artistId;
        this.imagePath = imagePath;
        this.exhibitionId = exhibitionId;
    }

    // Getters and Setters
    public int getArtworkId() {
        return artworkId;
    }
    public void setArtworkId(int artworkId) {
        this.artworkId = artworkId;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public int getArtistId() {
        return artistId;
    }
    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    public String getImagePath() {
        return imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Integer getExhibitionId() { // Thay đổi kiểu trả về từ int sang Integer
        return exhibitionId;
    }
    public void setExhibitionId(Integer exhibitionId) {
        this.exhibitionId = exhibitionId;
    }
}