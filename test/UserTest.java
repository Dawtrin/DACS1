package com.soulvirart.test;

import com.soulvirart.database.UserDAO;
import com.soulvirart.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private UserDAO userDAO;
    private User testUser;

    @BeforeEach
    public void setUp() {
        userDAO = new UserDAO();
        testUser = new User(0, "testuser", "password123", "test@example.com", "Test User", "STAFF", LocalDateTime.now(), null, true);
    }

    @AfterEach
    public void tearDown() {
        if (testUser != null && testUser.getUserId() != 0) {
            userDAO.delete(testUser.getUserId());
        }
    }

    @Test
    public void testCreateAndFindUser() {
        userDAO.create(testUser);

        User foundUser = userDAO.findById(testUser.getUserId());
        assertNotNull(foundUser, "User should be found");
        assertEquals("testuser", foundUser.getUsername(), "Username should match");
    }

    @Test
    public void testFindByUsernameAndPassword() {
        User user = userDAO.findByUsernameAndPassword("admin", "admin123");
        assertNotNull(user, "User should be found");
        assertEquals("admin", user.getUsername(), "Username should match");
    }
}