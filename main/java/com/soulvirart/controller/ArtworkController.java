package com.soulvirart.controller;

import com.soulvirart.database.DatabaseDAO;
import com.soulvirart.model.Artwork;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ArtworkController {
    private final DatabaseDAO databaseDAO;

    public ArtworkController() {
        this.databaseDAO = new DatabaseDAO();
    }

    public List<Artwork> getArtworksByExhibitionId(int exhibitionId, int page, int pageSize) throws Exception {
        if (exhibitionId <= 0) {
            throw new IllegalArgumentException("Invalid exhibition ID");
        }
        if (page < 1 || pageSize < 1) {
            throw new IllegalArgumentException("Page and pageSize must be positive integers");
        }
        try {
            return databaseDAO.getArtworksByExhibitionId(exhibitionId, page, pageSize);
        } catch (SQLException e) {
            throw new Exception("Failed to retrieve artworks for exhibition ID " + exhibitionId + ": " + e.getMessage(), e);
        }
    }

    public List<Artwork> getArtworksByTourId(int tourId) throws Exception {
        if (tourId <= 0) {
            throw new IllegalArgumentException("Invalid tour ID");
        }
        try {
            return databaseDAO.getArtworksByTourId(tourId);
        } catch (SQLException e) {
            throw new Exception("Failed to retrieve artworks for tour ID " + tourId + ": " + e.getMessage(), e);
        }
    }

    public List<Artwork> getArtworksBySearch(String searchQuery, int page, int pageSize) throws Exception {
        if (page < 1 || pageSize < 1) {
            throw new IllegalArgumentException("Page and pageSize must be positive integers");
        }
        try {
            return databaseDAO.searchArtworks(searchQuery, page, pageSize);
        } catch (SQLException e) {
            throw new Exception("Failed to search artworks: " + e.getMessage(), e);
        }
    }

    public List<Artwork> getAllArtworks(int page, int pageSize) throws Exception {
        if (page < 1 || pageSize < 1) {
            throw new IllegalArgumentException("Page and pageSize must be positive integers");
        }
        try {
            return databaseDAO.getAllArtworks(page, pageSize);
        } catch (SQLException e) {
            throw new Exception("Failed to retrieve all artworks: " + e.getMessage(), e);
        }
    }

    public Artwork findArtworkById(int artworkId) throws Exception {
        if (artworkId <= 0) {
            throw new IllegalArgumentException("Invalid artwork ID");
        }
        try {
            return databaseDAO.findArtworkById(artworkId);
        } catch (SQLException e) {
            throw new Exception("Failed to find artwork with ID " + artworkId + ": " + e.getMessage(), e);
        }
    }

    public void createArtwork(String title, String description, LocalDate creationDate, int artistId, String imagePath, Integer exhibitionId) throws Exception {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Artwork title cannot be null or empty");
        }
        if (artistId <= 0) {
            throw new IllegalArgumentException("Invalid artist ID");
        }
        if (exhibitionId != null && exhibitionId.intValue() <= 0) { // Sửa để tránh unboxing trực tiếp
            throw new IllegalArgumentException("Invalid exhibition ID");
        }

        Artwork artwork = new Artwork();
        artwork.setTitle(title.trim());
        artwork.setDescription(description != null ? description.trim() : null);
        artwork.setCreationDate(creationDate);
        artwork.setArtistId(artistId);
        artwork.setImagePath(imagePath != null ? imagePath.trim() : null);
        artwork.setExhibitionId(exhibitionId);

        try {
            databaseDAO.createArtwork(artwork);
        } catch (SQLException e) {
            throw new Exception("Failed to create artwork: " + e.getMessage(), e);
        }
    }

    public void updateArtwork(Artwork artwork) throws Exception {
        if (artwork == null) {
            throw new IllegalArgumentException("Artwork cannot be null");
        }
        if (artwork.getArtworkId() <= 0) {
            throw new IllegalArgumentException("Invalid artwork ID");
        }
        if (artwork.getTitle() == null || artwork.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Artwork title cannot be null or empty");
        }
        if (artwork.getArtistId() <= 0) {
            throw new IllegalArgumentException("Invalid artist ID");
        }
        if (artwork.getExhibitionId() != null && artwork.getExhibitionId().intValue() <= 0) { // Sửa để tránh unboxing trực tiếp
            throw new IllegalArgumentException("Invalid exhibition ID");
        }

        try {
            databaseDAO.updateArtwork(artwork);
        } catch (SQLException e) {
            throw new Exception("Failed to update artwork with ID " + artwork.getArtworkId() + ": " + e.getMessage(), e);
        }
    }

    public void deleteArtwork(int artworkId) throws Exception {
        if (artworkId <= 0) {
            throw new IllegalArgumentException("Invalid artwork ID");
        }
        try {
            databaseDAO.deleteArtwork(artworkId);
        } catch (SQLException e) {
            throw new Exception("Failed to delete artwork with ID " + artworkId + ": " + e.getMessage(), e);
        }
    }
    public List<Artwork> getArtworksByArtistId(int artistId, int page, int pageSize) throws Exception {
        if (artistId <= 0) {
            throw new IllegalArgumentException("Invalid artist ID");
        }
        if (page < 1 || pageSize < 1) {
            throw new IllegalArgumentException("Page and pageSize must be positive integers");
        }
        try {
            return databaseDAO.getArtworksByArtistId(artistId, page, pageSize);
        } catch (SQLException e) {
            throw new Exception("Failed to retrieve artworks for artist ID " + artistId + ": " + e.getMessage(), e);
        }
    }
}