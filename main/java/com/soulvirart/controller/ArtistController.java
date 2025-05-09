package com.soulvirart.controller;

import com.soulvirart.database.DatabaseDAO;
import com.soulvirart.model.Artist;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ArtistController {
    private final DatabaseDAO databaseDAO;

    public ArtistController() {
        this.databaseDAO = new DatabaseDAO();
    }

    public List<Artist> getAllArtists(int page, int pageSize) throws Exception {
        if (page < 1 || pageSize < 1) {
            throw new IllegalArgumentException("Page and pageSize must be positive integers");
        }
        try {
            return databaseDAO.getAllArtists(page, pageSize);
        } catch (SQLException e) {
            throw new Exception("Failed to retrieve artists: " + e.getMessage(), e);
        }
    }

    public void createArtist(String name, String biography, LocalDate birthDate, String nationality) throws Exception {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Artist name cannot be null or empty");
        }
        if (nationality == null || nationality.trim().isEmpty()) {
            throw new IllegalArgumentException("Nationality cannot be null or empty");
        }

        Artist artist = new Artist();
        artist.setName(name.trim());
        artist.setBiography(biography != null ? biography.trim() : null);
        artist.setBirthDate(birthDate);
        artist.setNationality(nationality.trim());

        try {
            databaseDAO.createArtist(artist);
        } catch (SQLException e) {
            throw new Exception("Failed to create artist: " + e.getMessage(), e);
        }
    }

    public void updateArtist(Artist artist) throws Exception {
        if (artist == null) {
            throw new IllegalArgumentException("Artist cannot be null");
        }
        if (artist.getArtistId() <= 0) {
            throw new IllegalArgumentException("Invalid artist ID");
        }
        if (artist.getName() == null || artist.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Artist name cannot be null or empty");
        }
        if (artist.getNationality() == null || artist.getNationality().trim().isEmpty()) {
            throw new IllegalArgumentException("Nationality cannot be null or empty");
        }

        try {
            databaseDAO.updateArtist(artist);
        } catch (SQLException e) {
            throw new Exception("Failed to update artist with ID " + artist.getArtistId() + ": " + e.getMessage(), e);
        }
    }

    public void deleteArtist(int artistId) throws Exception {
        if (artistId <= 0) {
            throw new IllegalArgumentException("Invalid artist ID");
        }
        try {
            databaseDAO.deleteArtist(artistId);
        } catch (SQLException e) {
            throw new Exception("Failed to delete artist with ID " + artistId + ": " + e.getMessage(), e);
        }
    }
}