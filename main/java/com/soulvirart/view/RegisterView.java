package com.soulvirart.view;

import com.soulvirart.controller.AuthController;
import com.soulvirart.util.EmailSender;

import javax.mail.MessagingException;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.regex.Pattern;

public class RegisterView extends JFrame {
    private JPanel mainPanel, leftPanel, rightPanel;
    private JTextField usernameField, emailField;
    private JPasswordField passwordField, confirmPasswordField;
    private JButton registerButton, loginButton;
    private JLabel passwordStrengthLabel, errorLabel;
    private final AuthController authController;

    // Cập nhật EMAIL_PATTERN để hỗ trợ nhiều domain email
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    private static final Pattern PASSWORD_UPPER_PATTERN = Pattern.compile("[A-Z]");
    private static final Pattern PASSWORD_SPECIAL_PATTERN = Pattern.compile("[^a-zA-Z0-9]");
    private static final Pattern PASSWORD_DIGIT_PATTERN = Pattern.compile("\\d");

    public RegisterView() {
        authController = new AuthController();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("SoulVirArt - Đăng Ký");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel(new GridLayout(1, 2));

        // Left Panel (Form)
        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(40, 40, 40));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(80, 50, 80, 50));

        JLabel titleLabel = new JLabel("Đăng Ký");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        usernameField = createStyledTextField("Tên người dùng");
        emailField = createStyledTextField("Email");
        passwordField = createStyledPasswordField("Mật khẩu");
        confirmPasswordField = createStyledPasswordField("Xác nhận mật khẩu");

        passwordStrengthLabel = new JLabel("Độ mạnh mật khẩu: Yếu");
        passwordStrengthLabel.setForeground(Color.RED);
        passwordStrengthLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordStrengthLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        registerButton = new JButton("ĐĂNG KÝ");
        registerButton.setBackground(new Color(255, 87, 51));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Arial", Font.BOLD, 16));
        registerButton.setFocusPainted(false);
        registerButton.setBorderPainted(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

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

        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(titleLabel);
        leftPanel.add(Box.createVerticalStrut(30));
        leftPanel.add(usernameField);
        leftPanel.add(Box.createVerticalStrut(15));
        leftPanel.add(emailField);
        leftPanel.add(Box.createVerticalStrut(15));
        leftPanel.add(passwordField);
        leftPanel.add(Box.createVerticalStrut(15));
        leftPanel.add(confirmPasswordField);
        leftPanel.add(Box.createVerticalStrut(15));
        leftPanel.add(passwordStrengthLabel);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(errorLabel);
        leftPanel.add(Box.createVerticalStrut(30));
        leftPanel.add(registerButton);
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(orLabel);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(socialPanel);
        leftPanel.add(Box.createVerticalGlue());

        // Right Panel (Branding)
        rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(new Color(5, 15, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0.3;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel rightTitleLabel = new JLabel("Tham gia SoulVirArt");
        rightTitleLabel.setForeground(Color.WHITE);
        rightTitleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        rightPanel.add(rightTitleLabel, gbc);

        gbc.gridy = 1;
        gbc.weighty = 0.1;
        JLabel rightSubtitleLabel = new JLabel("Khám phá nghệ thuật ngay hôm nay");
        rightSubtitleLabel.setForeground(Color.LIGHT_GRAY);
        rightSubtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        rightPanel.add(rightSubtitleLabel, gbc);

        gbc.gridy = 2;
        gbc.weighty = 0.3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(20, 0, 0, 0);

        loginButton = new JButton("ĐĂNG NHẬP");
        loginButton.setPreferredSize(new Dimension(200, 50));
        loginButton.setOpaque(true);
        loginButton.setContentAreaFilled(true);
        loginButton.setBackground(Color.BLACK);
        loginButton.setForeground(Color.WHITE);
        loginButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rightPanel.add(loginButton, gbc);

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
        add(mainPanel);

        // Password Strength Listener
        passwordField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updatePasswordStrength();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updatePasswordStrength();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updatePasswordStrength();
            }
        });

        // Clear error label on focus
        FocusListener clearErrorListener = new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                errorLabel.setText("");
            }
        };
        usernameField.addFocusListener(clearErrorListener);
        emailField.addFocusListener(clearErrorListener);
        passwordField.addFocusListener(clearErrorListener);
        confirmPasswordField.addFocusListener(clearErrorListener);

        // Button Actions
        registerButton.addActionListener(e -> registerUser());
        loginButton.addActionListener(e -> {
            dispose();
            new LoginView().setVisible(true);
        });

        setVisible(true);
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

    private void updatePasswordStrength() {
        String password = new String(passwordField.getPassword());
        int score = 0;

        if (password.length() >= 8 && !password.equals("Mật khẩu")) score++;
        if (PASSWORD_UPPER_PATTERN.matcher(password).find()) score++;
        if (PASSWORD_SPECIAL_PATTERN.matcher(password).find()) score++;
        if (PASSWORD_DIGIT_PATTERN.matcher(password).find()) score++;

        switch (score) {
            case 0:
            case 1:
                passwordStrengthLabel.setText("Độ mạnh mật khẩu: Yếu");
                passwordStrengthLabel.setForeground(Color.RED);
                break;
            case 2:
                passwordStrengthLabel.setText("Độ mạnh mật khẩu: Trung bình");
                passwordStrengthLabel.setForeground(Color.ORANGE);
                break;
            case 3:
                passwordStrengthLabel.setText("Độ mạnh mật khẩu: Khá");
                passwordStrengthLabel.setForeground(Color.BLUE);
                break;
            case 4:
                passwordStrengthLabel.setText("Độ mạnh mật khẩu: Mạnh");
                passwordStrengthLabel.setForeground(Color.GREEN);
                break;
        }
    }

    private void registerUser() {
        try {
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            // Validate inputs
            if (username.equals("Tên người dùng") || username.isEmpty()) {
                errorLabel.setText("Tên người dùng không được để trống!");
                return;
            }
            if (username.length() < 3) {
                errorLabel.setText("Tên người dùng phải có ít nhất 3 ký tự!");
                return;
            }

            if (email.equals("Email") || email.isEmpty()) {
                errorLabel.setText("Email không được để trống!");
                return;
            }
            if (!EMAIL_PATTERN.matcher(email).matches()) {
                errorLabel.setText("Email không hợp lệ!");
                return;
            }

            if (password.equals("Mật khẩu") || password.isEmpty()) {
                errorLabel.setText("Mật khẩu không được để trống!");
                return;
            }
            if (password.length() < 8) {
                errorLabel.setText("Mật khẩu phải có ít nhất 8 ký tự!");
                return;
            }
            if (!PASSWORD_UPPER_PATTERN.matcher(password).find() ||
                    !PASSWORD_SPECIAL_PATTERN.matcher(password).find() ||
                    !PASSWORD_DIGIT_PATTERN.matcher(password).find()) {
                errorLabel.setText("Mật khẩu phải chứa ít nhất một chữ cái in hoa, một ký tự đặc biệt và một chữ số!");
                return;
            }

            if (!password.equals(confirmPassword)) {
                errorLabel.setText("Mật khẩu và xác nhận mật khẩu không khớp!");
                return;
            }

            // Register user using AuthController
            authController.register(username, password, email, username, "user");

            // Send confirmation email
            try {
                EmailSender.sendVerificationEmail(email, username);
            } catch (MessagingException e) {
                System.err.println("Không thể gửi email xác nhận: " + e.getMessage());
                JOptionPane.showMessageDialog(this,
                        "Đăng ký thành công, nhưng không thể gửi email xác nhận. Vui lòng liên hệ hỗ trợ.",
                        "Cảnh báo",
                        JOptionPane.WARNING_MESSAGE);
            }

            JOptionPane.showMessageDialog(this,
                    "Đăng ký thành công! Vui lòng kiểm tra email để xác nhận.",
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new LoginView().setVisible(true);

        } catch (IllegalArgumentException e) {
            errorLabel.setText(e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi đăng ký: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Không thể thiết lập Look and Feel: " + e.getMessage());
        }
        SwingUtilities.invokeLater(() -> new RegisterView().setVisible(true));
    }
}