package com.soulvirart.controller;

import com.soulvirart.database.DatabaseDAO;
import com.soulvirart.model.Artwork;

import java.sql.SQLException;
import java.util.List;

public class ArtworkTourController {
    private final DatabaseDAO databaseDAO;

    public ArtworkTourController() {
        this.databaseDAO = new DatabaseDAO();
    }

    public List<Artwork> getArtworksByTourId(int tourId) throws Exception {
        if (tourId <= 0) {
            throw new IllegalArgumentException("ID chuyến tham quan không hợp lệ");
        }
        try {
            return databaseDAO.getArtworksByTourId(tourId);
        } catch (SQLException e) {
            throw new Exception("Không thể lấy danh sách tác phẩm cho chuyến tham quan ID " + tourId + ": " + e.getMessage(), e);
        }
    }

    public void addArtworkToTour(int tourId, int artworkId) throws Exception {
        if (tourId <= 0 || artworkId <= 0) {
            throw new IllegalArgumentException("ID chuyến tham quan hoặc ID tác phẩm không hợp lệ");
        }
        try {
            databaseDAO.addArtworkToTour(tourId, artworkId);
        } catch (SQLException e) {
            throw new Exception("Không thể thêm tác phẩm ID " + artworkId + " vào chuyến tham quan ID " + tourId + ": " + e.getMessage(), e);
        }
    }

    public void removeArtworkFromTour(int tourId, int artworkId) throws Exception {
        if (tourId <= 0 || artworkId <= 0) {
            throw new IllegalArgumentException("ID chuyến tham quan hoặc ID tác phẩm không hợp lệ");
        }
        try {
            databaseDAO.removeArtworkFromTour(tourId, artworkId);
        } catch (SQLException e) {
            throw new Exception("Không thể xóa tác phẩm ID " + artworkId + " khỏi chuyến tham quan ID " + tourId + ": " + e.getMessage(), e);
        }
    }

    public void reorderArtworks(int tourId, List<Artwork> artworks) throws Exception {
        if (tourId <= 0 || artworks == null || artworks.isEmpty()) {
            throw new IllegalArgumentException("ID chuyến tham quan hoặc danh sách tác phẩm không hợp lệ");
        }
        try {
            databaseDAO.reorderArtworks(tourId, artworks);
        } catch (SQLException e) {
            throw new Exception("Không thể sắp xếp lại tác phẩm cho chuyến tham quan ID " + tourId + ": " + e.getMessage(), e);
        }
    }
}