package com.soulvirart.controller;

import com.soulvirart.database.DatabaseDAO;
import com.soulvirart.model.ActivityLog;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class ActivityLogController {
    private final DatabaseDAO databaseDAO;

    public ActivityLogController() {
        this.databaseDAO = new DatabaseDAO();
    }

    public List<ActivityLog> getAllActivityLogs(int page, int pageSize) throws Exception {
        if (page < 1 || pageSize < 1) {
            throw new IllegalArgumentException("Page and pageSize must be positive integers");
        }
        try {
            return databaseDAO.getAllActivityLogs(page, pageSize);
        } catch (SQLException e) {
            throw new Exception("Failed to retrieve activity logs: " + e.getMessage(), e);
        }
    }

    public void createActivityLog(int userId, String action) throws Exception {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        if (action == null || action.trim().isEmpty()) {
            throw new IllegalArgumentException("Action cannot be null or empty");
        }
        LocalDateTime timestamp = null;
        if (timestamp == null) {
            throw new IllegalArgumentException("Timestamp cannot be null");
        }

        ActivityLog log = new ActivityLog();
        log.setUserId(userId);
        log.setAction(action.trim());
        log.setTimestamp(timestamp);

        try {
            databaseDAO.createActivityLog(log);
        } catch (SQLException e) {
            throw new Exception("Failed to create activity log: " + e.getMessage(), e);
        }
    }

    public void deleteActivityLog(int logId) throws Exception {
        if (logId <= 0) {
            throw new IllegalArgumentException("Invalid log ID");
        }
        try {
            databaseDAO.deleteActivityLog(logId);
        } catch (SQLException e) {
            throw new Exception("Failed to delete activity log with ID " + logId + ": " + e.getMessage(), e);
        }
    }
}