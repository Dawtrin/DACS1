package com.soulvirart.controller;

import com.soulvirart.database.DatabaseDAO;
import com.soulvirart.model.User;

import java.sql.SQLException;
import java.util.List;

public class UserController {
    private final DatabaseDAO databaseDAO;

    public UserController() {
        this.databaseDAO = new DatabaseDAO();
    }

    public List<User> getAllUsers(int page, int pageSize) throws Exception {
        if (page < 1 || pageSize < 1) {
            throw new IllegalArgumentException("Page and pageSize must be positive integers");
        }
        try {
            return databaseDAO.getAllUsers(page, pageSize);
        } catch (SQLException e) {
            throw new Exception("Failed to retrieve users: " + e.getMessage(), e);
        }
    }

    public void createUser(String username, String password, String email, String fullName, String role) throws Exception {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Email format is invalid");
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be null or empty");
        }
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role cannot be null or empty");
        }

        // Kiểm tra email trùng lặp
        try {
            if (databaseDAO.checkEmailExists(email.trim())) {
                throw new IllegalArgumentException("Email already exists");
            }
        } catch (SQLException e) {
            throw new Exception("Failed to check email existence: " + e.getMessage(), e);
        }

        User user = new User();
        user.setUsername(username.trim());
        user.setPassword(password.trim());
        user.setEmail(email.trim());
        user.setFullName(fullName.trim());
        user.setRole(role.trim());
        user.setActive(true); // Giá trị mặc định khi tạo mới

        try {
            databaseDAO.createUser(user);
        } catch (SQLException e) {
            throw new Exception("Failed to create user: " + e.getMessage(), e);
        }
    }

    public User findUserById(int userId) throws Exception {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        try {
            return databaseDAO.findUserById(userId);
        } catch (SQLException e) {
            throw new Exception("Failed to find user with ID " + userId + ": " + e.getMessage(), e);
        }
    }

    public void updateUser(User user) throws Exception {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (user.getUserId() <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Email format is invalid");
        }
        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be null or empty");
        }
        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            throw new IllegalArgumentException("Role cannot be null or empty");
        }

        // Kiểm tra email trùng lặp (trừ email của chính user đang cập nhật)
        try {
            User existingUser = databaseDAO.findUserById(user.getUserId());
            if (existingUser != null && !existingUser.getEmail().equals(user.getEmail())) {
                if (databaseDAO.checkEmailExists(user.getEmail())) {
                    throw new IllegalArgumentException("Email already exists");
                }
            }
        } catch (SQLException e) {
            throw new Exception("Failed to check email existence: " + e.getMessage(), e);
        }

        try {
            databaseDAO.updateUser(user);
        } catch (SQLException e) {
            throw new Exception("Failed to update user with ID " + user.getUserId() + ": " + e.getMessage(), e);
        }
    }

    public void deleteUser(int userId) throws Exception {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        try {
            databaseDAO.deleteUser(userId);
        } catch (SQLException e) {
            throw new Exception("Failed to delete user with ID " + userId + ": " + e.getMessage(), e);
        }
    }
}