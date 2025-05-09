package com.soulvirart.controller;

import com.soulvirart.database.DatabaseDAO;
import com.soulvirart.model.VirtualTour;

import java.sql.SQLException;
import java.util.List;

public class VirtualTourController {
    private final DatabaseDAO databaseDAO;

    public VirtualTourController() {
        this.databaseDAO = new DatabaseDAO();
    }

    public List<VirtualTour> getAllVirtualTours(int page, int pageSize) throws Exception {
        if (page < 1 || pageSize < 1) {
            throw new IllegalArgumentException("Page and pageSize must be positive integers");
        }
        try {
            return databaseDAO.getAllVirtualTours(page, pageSize);
        } catch (SQLException e) {
            throw new Exception("Failed to retrieve virtual tours: " + e.getMessage(), e);
        }
    }

    // Thêm phương thức getVirtualToursBySearch
    public List<VirtualTour> getVirtualToursBySearch(String searchQuery, int page, int pageSize) throws Exception {
        if (page < 1 || pageSize < 1) {
            throw new IllegalArgumentException("Page and pageSize must be positive integers");
        }
        try {
            return databaseDAO.searchVirtualTours(searchQuery, page, pageSize);
        } catch (SQLException e) {
            throw new Exception("Failed to search virtual tours: " + e.getMessage(), e);
        }
    }

    public VirtualTour findVirtualTourById(int tourId) throws Exception {
        if (tourId <= 0) {
            throw new IllegalArgumentException("Invalid tour ID");
        }
        try {
            return databaseDAO.findVirtualTourById(tourId);
        } catch (SQLException e) {
            throw new Exception("Failed to find virtual tour with ID " + tourId + ": " + e.getMessage(), e);
        }
    }

    public void createVirtualTour(VirtualTour tour) throws Exception {
        if (tour == null) {
            throw new IllegalArgumentException("Virtual tour cannot be null");
        }
        if (tour.getTitle() == null || tour.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Tour title cannot be null or empty");
        }
        if (tour.getPrice() < 0) {
            throw new IllegalArgumentException("Tour price cannot be negative");
        }

        try {
            databaseDAO.createVirtualTour(tour);
        } catch (SQLException e) {
            throw new Exception("Failed to create virtual tour: " + e.getMessage(), e);
        }
    }

    public void updateVirtualTour(VirtualTour tour) throws Exception {
        if (tour == null) {
            throw new IllegalArgumentException("Virtual tour cannot be null");
        }
        if (tour.getTourId() <= 0) {
            throw new IllegalArgumentException("Invalid tour ID");
        }
        if (tour.getTitle() == null || tour.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Tour title cannot be null or empty");
        }
        if (tour.getPrice() < 0) {
            throw new IllegalArgumentException("Tour price cannot be negative");
        }

        try {
            databaseDAO.updateVirtualTour(tour);
        } catch (SQLException e) {
            throw new Exception("Failed to update virtual tour with ID " + tour.getTourId() + ": " + e.getMessage(), e);
        }
    }

    public void deleteVirtualTour(int tourId) throws Exception {
        if (tourId <= 0) {
            throw new IllegalArgumentException("Invalid tour ID");
        }
        try {
            databaseDAO.deleteVirtualTour(tourId);
        } catch (SQLException e) {
            throw new Exception("Failed to delete virtual tour with ID " + tourId + ": " + e.getMessage(), e);
        }
    }
}