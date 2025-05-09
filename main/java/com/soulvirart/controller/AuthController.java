package com.soulvirart.controller;

import com.soulvirart.database.DatabaseDAO;
import com.soulvirart.model.User;
import com.soulvirart.util.PasswordUtils;

import java.sql.SQLException;

public class AuthController {
    private final DatabaseDAO databaseDAO;

    public AuthController() {
        this.databaseDAO = new DatabaseDAO();
    }

    public User authenticate(String emailOrUsername, String password) throws Exception {
        if (emailOrUsername == null || emailOrUsername.trim().isEmpty()) {
            throw new IllegalArgumentException("Email or username cannot be null or empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        try {
            User user = null;
            // Kiểm tra xem đầu vào có phải là email không
            if (emailOrUsername.contains("@")) {
                user = findUserByEmail(emailOrUsername.trim());
            } else {
                user = databaseDAO.findUserByUsername(emailOrUsername.trim());
            }

            if (user == null) {
                throw new Exception("User not found with email or username: " + emailOrUsername);
            }
            if (!PasswordUtils.verifyPassword(password, user.getPassword())) {
                throw new Exception("Invalid password for user: " + emailOrUsername);
            }
            if (!user.isActive()) {
                throw new Exception("User account is inactive: " + emailOrUsername);
            }
            System.out.println("User authenticated successfully: " + user.getUsername());
            return user;
        } catch (SQLException e) {
            System.err.println("Failed to authenticate user " + emailOrUsername + ": " + e.getMessage());
            throw new Exception("Failed to authenticate user: " + e.getMessage(), e);
        }
    }

    public User login(String username, String password) throws Exception {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        try {
            User user = databaseDAO.findUserByUsername(username.trim());
            if (user == null) {
                throw new Exception("User not found with username: " + username);
            }
            if (!PasswordUtils.verifyPassword(password, user.getPassword())) {
                throw new Exception("Invalid password for username: " + username);
            }
            if (!user.isActive()) {
                throw new Exception("User account is inactive: " + username);
            }
            System.out.println("User logged in successfully: " + username);
            return user;
        } catch (SQLException e) {
            System.err.println("Failed to login for username " + username + ": " + e.getMessage());
            throw new Exception("Failed to login for username " + username + ": " + e.getMessage(), e);
        }
    }

    public void register(String username, String password, String email, String fullName, String role) throws Exception {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be null or empty");
        }
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role cannot be null or empty");
        }

        String hashedPassword = PasswordUtils.hashPassword(password);
        User user = new User();
        user.setUsername(username.trim());
        user.setPassword(hashedPassword);
        user.setEmail(email.trim());
        user.setFullName(fullName.trim());
        user.setRole(role.trim());
        user.setActive(true);

        try {
            if (databaseDAO.checkEmailExists(email.trim())) {
                throw new Exception("Email is already registered: " + email);
            }
            databaseDAO.createUser(user);
            System.out.println("User registered successfully: " + username);
        } catch (SQLException e) {
            System.err.println("Failed to register user " + username + ": " + e.getMessage());
            throw new Exception("Failed to register user " + username + ": " + e.getMessage(), e);
        }
    }

    private User findUserByEmail(String email) throws SQLException {
        return databaseDAO.findUserByEmail(email);
    }
}