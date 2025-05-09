package com.soulvirart.view;

import com.soulvirart.controller.AuthController;
import com.soulvirart.database.DatabaseDAO;
import com.soulvirart.model.User;
import com.soulvirart.util.EmailSender;
import com.soulvirart.util.PasswordUtils;

import javax.mail.MessagingException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.SQLException;

public class LoginView extends JFrame {
    private JPanel mainPanel, leftPanel, rightPanel;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton, forgotPasswordButton, registerButton;
    private JCheckBox rememberMeCheckbox;
    private final AuthController authController;
    private final DatabaseDAO databaseDAO;

    public LoginView() {
        authController = new AuthController();
        databaseDAO = new DatabaseDAO();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("SoulVirArt - Đăng nhập");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel(new GridLayout(1, 2));

        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(40, 40, 40));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(80, 50, 80, 50));

        rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(new Color(5, 15, 40));

        JLabel titleLabel = new JLabel("Đăng nhập");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        emailField = createStyledTextField("Email hoặc Tên đăng nhập");
        passwordField = createStyledPasswordField("Mật khẩu");

        rememberMeCheckbox = new JCheckBox("Ghi nhớ đăng nhập");
        rememberMeCheckbox.setForeground(Color.LIGHT_GRAY);
        rememberMeCheckbox.setBackground(new Color(40, 40, 40));
        rememberMeCheckbox.setFont(new Font("Arial", Font.PLAIN, 14));
        rememberMeCheckbox.setAlignmentX(Component.LEFT_ALIGNMENT);

        forgotPasswordButton = new JButton("Quên mật khẩu?");
        forgotPasswordButton.setBorderPainted(false);
        forgotPasswordButton.setContentAreaFilled(false);
        forgotPasswordButton.setForeground(new Color(200, 200, 200));
        forgotPasswordButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotPasswordButton.setFont(new Font("Arial", Font.PLAIN, 14));
        forgotPasswordButton.setAlignmentX(Component.RIGHT_ALIGNMENT);

        loginButton = new JButton("ĐĂNG NHẬP");
        loginButton.setBackground(new Color(255, 87, 51));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel orLabel = new JLabel("hoặc sử dụng tài khoản khác");
        orLabel.setForeground(Color.LIGHT_GRAY);
        orLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        orLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel socialPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        socialPanel.setOpaque(false);

        JButton facebookButton = createSocialButton("F", new Color(59, 89, 152));
        JButton googleButton = createSocialButton("G", new Color(219, 68, 55));
        JButton gitHubButton = createSocialButton("GH", new Color(33, 31, 31));

        socialPanel.add(facebookButton);
        socialPanel.add(googleButton);
        socialPanel.add(gitHubButton);

        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(titleLabel);
        leftPanel.add(Box.createVerticalStrut(40));
        leftPanel.add(emailField);
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(passwordField);
        leftPanel.add(Box.createVerticalStrut(20));

        JPanel optionsPanel = new JPanel(new BorderLayout());
        optionsPanel.setOpaque(false);
        optionsPanel.add(rememberMeCheckbox, BorderLayout.WEST);
        optionsPanel.add(forgotPasswordButton, BorderLayout.EAST);
        leftPanel.add(optionsPanel);

        leftPanel.add(Box.createVerticalStrut(40));
        leftPanel.add(loginButton);
        leftPanel.add(Box.createVerticalStrut(30));
        leftPanel.add(orLabel);
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(socialPanel);
        leftPanel.add(Box.createVerticalGlue());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0.3;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel rightTitleLabel = new JLabel("Bắt đầu hành trình của bạn");
        rightTitleLabel.setForeground(Color.WHITE);
        rightTitleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        rightPanel.add(rightTitleLabel, gbc);

        gbc.gridy = 1;
        gbc.weighty = 0.1;
        JLabel rightSubtitleLabel = new JLabel("Tham gia cùng chúng tôi ngay hôm nay");
        rightSubtitleLabel.setForeground(Color.LIGHT_GRAY);
        rightSubtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        rightPanel.add(rightSubtitleLabel, gbc);

        gbc.gridy = 2;
        gbc.weighty = 0.3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(20, 0, 0, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        registerButton = new JButton("ĐĂNG KÝ");
        registerButton.setPreferredSize(new Dimension(200, 50));
        registerButton.setOpaque(true);
        registerButton.setContentAreaFilled(true);
        registerButton.setBackground(Color.BLACK);
        registerButton.setForeground(Color.WHITE);
        registerButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));
        registerButton.setFont(new Font("Arial", Font.BOLD, 16));
        registerButton.setFocusPainted(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        rightPanel.add(registerButton, gbc);

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        add(mainPanel);

        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> openRegisterForm());
        forgotPasswordButton.addActionListener(e -> handleForgotPassword());
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField textField = new JTextField(20);
        textField.setBackground(new Color(60, 60, 60));
        textField.setForeground(Color.WHITE);
        textField.setCaretColor(Color.WHITE);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 70)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        textField.setText(placeholder);
        textField.setForeground(Color.GRAY);

        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.WHITE);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(Color.GRAY);
                }
            }
        });

        return textField;
    }

    private JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setBackground(new Color(60, 60, 60));
        passwordField.setForeground(Color.WHITE);
        passwordField.setCaretColor(Color.WHITE);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 70)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        passwordField.setEchoChar((char) 0);
        passwordField.setText(placeholder);
        passwordField.setForeground(Color.GRAY);

        passwordField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).equals(placeholder)) {
                    passwordField.setText("");
                    passwordField.setEchoChar('•');
                    passwordField.setForeground(Color.WHITE);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).isEmpty()) {
                    passwordField.setEchoChar((char) 0);
                    passwordField.setText(placeholder);
                    passwordField.setForeground(Color.GRAY);
                }
            }
        });

        return passwordField;
    }

    private JButton createSocialButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(40, 40));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMargin(new Insets(0, 0, 0, 0));
        return button;
    }

    private void handleLogin() {
        String emailOrUsername = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        // Kiểm tra đầu vào
        if (emailOrUsername.equals("Email hoặc Tên đăng nhập") || emailOrUsername.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập email hoặc tên đăng nhập",
                    "Lỗi đăng nhập",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (password.equals("Mật khẩu") || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập mật khẩu",
                    "Lỗi đăng nhập",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            User user = authController.authenticate(emailOrUsername, password);
            JOptionPane.showMessageDialog(this,
                    "Đăng nhập thành công! Xin chào " + user.getUsername(),
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);
            openMainView(user.getUserId(), user.getUsername());
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    "Dữ liệu nhập không hợp lệ: " + e.getMessage(),
                    "Lỗi đăng nhập",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            String message = e.getMessage();
            if (message.contains("User not found")) {
                int option = JOptionPane.showConfirmDialog(this,
                        "Không tìm thấy tài khoản. Bạn muốn đăng ký không?",
                        "Tài khoản không tồn tại",
                        JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    openRegisterForm();
                }
            } else if (message.contains("Invalid password")) {
                int option = JOptionPane.showConfirmDialog(this,
                        "Mật khẩu không đúng. Bạn muốn đặt lại mật khẩu không?",
                        "Lỗi đăng nhập",
                        JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    resetPassword(emailOrUsername);
                }
            } else if (message.contains("User account is inactive")) {
                JOptionPane.showMessageDialog(this,
                        "Tài khoản của bạn đang bị khóa. Vui lòng liên hệ quản trị viên.",
                        "Tài khoản bị khóa",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Lỗi hệ thống: " + message,
                        "Lỗi đăng nhập",
                        JOptionPane.ERROR_MESSAGE);
            }
            System.err.println("Login error for " + emailOrUsername + ": " + e.getMessage());
        }
    }

    private void handleForgotPassword() {
        String email = JOptionPane.showInputDialog(this,
                "Vui lòng nhập địa chỉ email của bạn:",
                "Đặt lại mật khẩu",
                JOptionPane.QUESTION_MESSAGE);

        if (email != null && !email.trim().isEmpty()) {
            resetPassword(email.trim());
        }
    }

    private void resetPassword(String email) {
        try {
            User user = databaseDAO.findUserByEmail(email);
            if (user == null) {
                JOptionPane.showMessageDialog(this,
                        "Không tìm thấy tài khoản với địa chỉ email này.",
                        "Lỗi đặt lại mật khẩu",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String newPassword = PasswordUtils.generateRandomPassword();
            String hashedPassword = PasswordUtils.hashPassword(newPassword);

            // Cập nhật mật khẩu qua DatabaseDAO
            user.setPassword(hashedPassword);
            databaseDAO.updateUser(user);

            EmailSender.sendPasswordResetEmail(email, newPassword);

            JOptionPane.showMessageDialog(this,
                    "Mật khẩu mới đã được gửi đến email của bạn.",
                    "Đặt lại mật khẩu",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi cơ sở dữ liệu: " + ex.getMessage(),
                    "Lỗi đặt lại mật khẩu",
                    JOptionPane.ERROR_MESSAGE);
            System.err.println("SQL error during password reset for email " + email + ": " + ex.getMessage());
        } catch (MessagingException ex) {
            JOptionPane.showMessageDialog(this,
                    "Không thể gửi email. Vui lòng kiểm tra kết nối.",
                    "Lỗi gửi email",
                    JOptionPane.ERROR_MESSAGE);
            System.err.println("Messaging error for email " + email + ": " + ex.getMessage());
        }
    }

    private void openRegisterForm() {
        this.dispose();
        SwingUtilities.invokeLater(() -> new RegisterView().setVisible(true));
    }

    private void openMainView(int userId, String username) {
        this.dispose();
        SwingUtilities.invokeLater(() -> new MainView().setVisible(true));
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Không thể thiết lập Look and Feel: " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> new LoginView().setVisible(true));
    }
}