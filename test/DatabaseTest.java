package com.soulvirart.test;

import com.soulvirart.database.DatabaseConnection;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DatabaseTest {

    @Test
    public void testDatabaseConnection() {
        Connection connection = DatabaseConnection.getConnection();
        assertNotNull(connection, "Kết nối không được null");
        DatabaseConnection.closeConnection();
    }
}