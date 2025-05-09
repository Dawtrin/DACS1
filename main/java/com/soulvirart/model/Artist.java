package com.soulvirart.model;

import java.time.LocalDate;

public class Artist {
    private int artistId;
    private String name;
    private String biography;
    private LocalDate birthDate;
    private String nationality;

    public Artist() {}

    public Artist(int artistId, String name, String biography, LocalDate birthDate, String nationality) {
        this.artistId = artistId;
        this.name = name;
        this.biography = biography;
        this.birthDate = birthDate;
        this.nationality = nationality;
    }

    // Getters and Setters
    public int getArtistId() { return artistId; }
    public void setArtistId(int artistId) { this.artistId = artistId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBiography() { return biography; }
    public void setBiography(String biography) { this.biography = biography; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }
}