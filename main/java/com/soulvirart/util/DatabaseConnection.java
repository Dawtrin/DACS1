package com.soulvirart.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        config.setJdbcUrl("jdbc:sqlserver://localhost:1433;databaseName=SoulVir_Art;encrypt=true;trustServerCertificate=true");
        config.setUsername("sa");
        config.setPassword("yourpassword");
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);
        config.setIdleTimeout(300000);
        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            LoggerUtil.error("Lỗi khi kết nối đến cơ sở dữ liệu: " + e.getMessage(), e);
            throw e;
        }
    }

    public static void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}