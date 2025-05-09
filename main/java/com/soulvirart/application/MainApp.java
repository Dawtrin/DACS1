package com.soulvirart.application;

import com.soulvirart.view.LoginView;

import javax.swing.*;
import java.awt.*;

public class MainApp {

    public static void main(String[] args) {
        // Đảm bảo giao diện được tạo trên Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                // Áp dụng Nimbus Look and Feel
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception e) {
                System.err.println("Không thể áp dụng Nimbus Look and Feel: " + e.getMessage());
            }

            // Tạo và hiển thị giao diện đăng nhập
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
        });
    }
}