package com.soulvirart.controller;

import com.soulvirart.database.DatabaseDAO;
import com.soulvirart.model.Notification;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class NotificationController {
    private final DatabaseDAO databaseDAO;

    public NotificationController() {
        this.databaseDAO = new DatabaseDAO();
    }

    public List<Notification> getAllNotifications(int page, int pageSize) throws Exception {
        if (page < 1 || pageSize < 1) {
            throw new IllegalArgumentException("Page and pageSize must be positive integers");
        }
        try {
            return databaseDAO.getAllNotifications(page, pageSize);
        } catch (SQLException e) {
            throw new Exception("Failed to retrieve notifications: " + e.getMessage(), e);
        }
    }

    public void createNotification(int userId, String message) throws Exception {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Message cannot be null or empty");
        }
        // Kiểm tra userId tồn tại
        try {
            if (databaseDAO.findUserById(userId) == null) {
                throw new IllegalArgumentException("User ID " + userId + " does not exist");
            }
        } catch (SQLException e) {
            throw new Exception("Failed to verify user ID: " + e.getMessage(), e);
        }
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage(message.trim());
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);
        try {
            databaseDAO.createNotification(notification);
        } catch (SQLException e) {
            throw new Exception("Failed to create notification: " + e.getMessage(), e);
        }
    }

    public void createNotification(int userId, String message, LocalDateTime createdAt, boolean isRead) throws Exception {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Message cannot be null or empty");
        }
        if (createdAt == null) {
            throw new IllegalArgumentException("Created date cannot be null");
        }
        // Kiểm tra userId tồn tại
        try {
            if (databaseDAO.findUserById(userId) == null) {
                throw new IllegalArgumentException("User ID " + userId + " does not exist");
            }
        } catch (SQLException e) {
            throw new Exception("Failed to verify user ID: " + e.getMessage(), e);
        }
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage(message.trim());
        notification.setCreatedAt(createdAt);
        notification.setRead(isRead);
        try {
            databaseDAO.createNotification(notification);
        } catch (SQLException e) {
            throw new Exception("Failed to create notification: " + e.getMessage(), e);
        }
    }

    public Notification findNotificationById(int notificationId) throws Exception {
        if (notificationId <= 0) {
            throw new IllegalArgumentException("Invalid notification ID");
        }
        try {
            Notification notification = databaseDAO.findNotificationById(notificationId);
            if (notification == null) {
                throw new Exception("Notification with ID " + notificationId + " not found");
            }
            return notification;
        } catch (SQLException e) {
            throw new Exception("Failed to find notification with ID " + notificationId + ": " + e.getMessage(), e);
        }
    }

    public void updateNotification(Notification notification) throws Exception {
        if (notification == null) {
            throw new IllegalArgumentException("Notification cannot be null");
        }
        if (notification.getNotificationId() <= 0) {
            throw new IllegalArgumentException("Invalid notification ID");
        }
        if (notification.getUserId() <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        if (notification.getMessage() == null || notification.getMessage().trim().isEmpty()) {
            throw new IllegalArgumentException("Message cannot be null or empty");
        }
        if (notification.getCreatedAt() == null) {
            throw new IllegalArgumentException("Created date cannot be null");
        }
        // Kiểm tra userId tồn tại
        try {
            if (databaseDAO.findUserById(notification.getUserId()) == null) {
                throw new IllegalArgumentException("User ID " + notification.getUserId() + " does not exist");
            }
        } catch (SQLException e) {
            throw new Exception("Failed to verify user ID: " + e.getMessage(), e);
        }
        try {
            databaseDAO.updateNotification(notification);
        } catch (SQLException e) {
            throw new Exception("Failed to update notification with ID " + notification.getNotificationId() + ": " + e.getMessage(), e);
        }
    }

    public void deleteNotification(int notificationId) throws Exception {
        if (notificationId <= 0) {
            throw new IllegalArgumentException("Invalid notification ID");
        }
        try {
            databaseDAO.deleteNotification(notificationId);
        } catch (SQLException e) {
            throw new Exception("Failed to delete notification with ID " + notificationId + ": " + e.getMessage(), e);
        }
    }
}