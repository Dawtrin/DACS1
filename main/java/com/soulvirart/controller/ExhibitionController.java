package com.soulvirart.controller;

import com.soulvirart.database.DatabaseDAO;
import com.soulvirart.model.Exhibition;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ExhibitionController {
    private final DatabaseDAO databaseDAO;

    public ExhibitionController() {
        this.databaseDAO = new DatabaseDAO();
    }

    public List<Exhibition> getAllExhibitions(int page, int pageSize) throws Exception {
        if (page < 1 || pageSize < 1) {
            throw new IllegalArgumentException("Page and pageSize must be positive integers");
        }
        try {
            return databaseDAO.getAllExhibitions(page, pageSize);
        } catch (SQLException e) {
            throw new Exception("Failed to retrieve exhibitions: " + e.getMessage(), e);
        }
    }

    // Thêm phương thức getExhibitionById
    public Exhibition getExhibitionById(int exhibitionId) throws Exception {
        if (exhibitionId <= 0) {
            throw new IllegalArgumentException("Invalid exhibition ID");
        }
        try {
            return databaseDAO.findExhibitionById(exhibitionId);
        } catch (SQLException e) {
            throw new Exception("Failed to find exhibition with ID " + exhibitionId + ": " + e.getMessage(), e);
        }
    }

    // Thêm phương thức getExhibitionsBySearch
    public List<Exhibition> getExhibitionsBySearch(String searchQuery, int page, int pageSize) throws Exception {
        if (page < 1 || pageSize < 1) {
            throw new IllegalArgumentException("Page and pageSize must be positive integers");
        }
        try {
            return databaseDAO.searchExhibitions(searchQuery, page, pageSize);
        } catch (SQLException e) {
            throw new Exception("Failed to search exhibitions: " + e.getMessage(), e);
        }
    }

    public void createExhibition(String title, String description, LocalDate startDate, LocalDate endDate, String status) throws Exception {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Exhibition title cannot be null or empty");
        }
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        Exhibition exhibition = new Exhibition();
        exhibition.setTitle(title.trim());
        exhibition.setDescription(description != null ? description.trim() : null);
        exhibition.setStartDate(startDate);
        exhibition.setEndDate(endDate);
        exhibition.setStatus(status != null ? status.trim() : "Draft");

        try {
            databaseDAO.createExhibition(exhibition);
        } catch (SQLException e) {
            throw new Exception("Failed to create exhibition: " + e.getMessage(), e);
        }
    }

    public void updateExhibition(Exhibition exhibition) throws Exception {
        if (exhibition == null) {
            throw new IllegalArgumentException("Exhibition cannot be null");
        }
        if (exhibition.getExhibitionId() <= 0) {
            throw new IllegalArgumentException("Invalid exhibition ID");
        }
        if (exhibition.getTitle() == null || exhibition.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Exhibition title cannot be null or empty");
        }
        if (exhibition.getStartDate() != null && exhibition.getEndDate() != null && exhibition.getStartDate().isAfter(exhibition.getEndDate())) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        try {
            databaseDAO.updateExhibition(exhibition);
        } catch (SQLException e) {
            throw new Exception("Failed to update exhibition with ID " + exhibition.getExhibitionId() + ": " + e.getMessage(), e);
        }
    }

    public void deleteExhibition(int exhibitionId) throws Exception {
        if (exhibitionId <= 0) {
            throw new IllegalArgumentException("Invalid exhibition ID");
        }
        try {
            databaseDAO.deleteExhibition(exhibitionId);
        } catch (SQLException e) {
            throw new Exception("Failed to delete exhibition with ID " + exhibitionId + ": " + e.getMessage(), e);
        }
    }

    public void createExhibition(Exhibition newExhibition) throws Exception {
        if (newExhibition == null) {
            throw new IllegalArgumentException("Exhibition cannot be null");
        }
        if (newExhibition.getTitle() == null || newExhibition.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Exhibition title cannot be null or empty");
        }
        try {
            databaseDAO.createExhibition(newExhibition);
        } catch (SQLException e) {
            throw new Exception("Failed to create exhibition: " + e.getMessage(), e);
        }
    }
}