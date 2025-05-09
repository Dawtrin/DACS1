package com.soulvirart.view;

import com.soulvirart.controller.UserController;
import com.soulvirart.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UserView extends JPanel {
    private final UserController userController;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JTextField usernameField, passwordField, emailField, fullNameField, roleField;
    private JButton addButton, updateButton, deleteButton, refreshButton;
    private JLabel errorLabel;
    private List<User> currentUsers;

    public UserView() {
        userController = new UserController();
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 248, 255));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title
        JLabel titleLabel = new JLabel("Quản Lý Người Dùng", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(25, 25, 112));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // User Table
        String[] columnNames = {"ID", "Username", "Email", "Full Name", "Role", "Active"};
        tableModel = new DefaultTableModel(columnNames, 0);
        userTable = new JTable(tableModel);
        userTable.setRowHeight(25);
        userTable.setGridColor(new Color(200, 200, 200));
        userTable.setSelectionBackground(new Color(135, 206, 250));
        userTable.setFont(new Font("Arial", Font.PLAIN, 14));

        // Căn chỉnh cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        userTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // ID
        userTable.getColumnModel().getColumn(5).setCellRenderer(centerRenderer); // Active
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        userTable.getColumnModel().getColumn(1).setCellRenderer(leftRenderer); // Username
        userTable.getColumnModel().getColumn(2).setCellRenderer(leftRenderer); // Email
        userTable.getColumnModel().getColumn(3).setCellRenderer(leftRenderer); // Full Name
        userTable.getColumnModel().getColumn(4).setCellRenderer(leftRenderer); // Role

        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(new Color(245, 245, 220));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(createStyledLabel("Username:"), gbc);
        gbc.gridx = 1;
        usernameField = createStyledTextField();
        gbc.weightx = 1;
        inputPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        inputPanel.add(createStyledLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = createStyledTextField();
        gbc.weightx = 1;
        inputPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        inputPanel.add(createStyledLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = createStyledTextField();
        gbc.weightx = 1;
        inputPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        inputPanel.add(createStyledLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        fullNameField = createStyledTextField();
        gbc.weightx = 1;
        inputPanel.add(fullNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;
        inputPanel.add(createStyledLabel("Role:"), gbc);
        gbc.gridx = 1;
        roleField = createStyledTextField();
        gbc.weightx = 1;
        inputPanel.add(roleField, gbc);

        mainPanel.add(inputPanel, BorderLayout.NORTH);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(240, 248, 255));
        addButton = createStyledButton("Thêm", "/icons/add_icon.png");
        updateButton = createStyledButton("Cập nhật", "/icons/update_icon.png");
        deleteButton = createStyledButton("Xóa", "/icons/delete_icon.png");
        refreshButton = createStyledButton("Làm mới", "/icons/refresh_icon.png");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Error Label
        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        mainPanel.add(errorLabel, BorderLayout.PAGE_END);

        add(mainPanel);

        // Action Listeners
        addButton.addActionListener(e -> addUser());
        updateButton.addActionListener(e -> updateUser());
        deleteButton.addActionListener(e -> deleteUser());
        refreshButton.addActionListener(e -> loadUsersAsync(1, 10));

        userTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow >= 0) {
                usernameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                emailField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                fullNameField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                roleField.setText(tableModel.getValueAt(selectedRow, 4).toString());
                passwordField.setText(""); // Không hiển thị mật khẩu
            }
        });

        // Load initial data
        loadUsersAsync(1, 10);
    }

    public void loadData() {
        loadUsersAsync(1, 10);
    }

    private void loadUsersAsync(int page, int pageSize) {
        SwingWorker<List<User>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<User> doInBackground() throws Exception {
                return userController.getAllUsers(page, pageSize);
            }

            @Override
            protected void done() {
                try {
                    currentUsers = get();
                    if (currentUsers == null) {
                        throw new IllegalStateException("Không thể tải dữ liệu người dùng!");
                    }
                    updateTable(currentUsers);
                    errorLabel.setText("");
                } catch (Exception e) {
                    errorLabel.setText("Lỗi khi tải dữ liệu: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void updateTable(List<User> users) {
        tableModel.setRowCount(0);
        for (User user : users) {
            tableModel.addRow(new Object[]{
                    user.getUserId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getFullName(),
                    user.getRole(),
                    user.isActive()
            });
        }
    }

    private void addUser() {
        try {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            String email = emailField.getText().trim();
            String fullName = fullNameField.getText().trim();
            String role = roleField.getText().trim();

            userController.createUser(username, password, email, fullName, role);
            JOptionPane.showMessageDialog(this, "Thêm người dùng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            loadUsersAsync(1, 10);
            clearFields();
        } catch (Exception e) {
            errorLabel.setText("Lỗi: " + e.getMessage());
        }
    }

    private void updateUser() {
        try {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow < 0) {
                errorLabel.setText("Vui lòng chọn người dùng để cập nhật!");
                return;
            }
            int userId = (int) tableModel.getValueAt(selectedRow, 0);
            User user = userController.findUserById(userId);
            if (user == null) {
                throw new IllegalStateException("Không tìm thấy người dùng với ID: " + userId);
            }

            user.setUsername(usernameField.getText().trim());
            user.setPassword(passwordField.getText().trim());
            user.setEmail(emailField.getText().trim());
            user.setFullName(fullNameField.getText().trim());
            user.setRole(roleField.getText().trim());

            userController.updateUser(user);
            JOptionPane.showMessageDialog(this, "Cập nhật người dùng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            loadUsersAsync(1, 10);
            clearFields();
        } catch (Exception e) {
            errorLabel.setText("Lỗi: " + e.getMessage());
        }
    }

    private void deleteUser() {
        try {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow < 0) {
                errorLabel.setText("Vui lòng chọn người dùng để xóa!");
                return;
            }
            int userId = (int) tableModel.getValueAt(selectedRow, 0);

            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa người dùng này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            userController.deleteUser(userId);
            JOptionPane.showMessageDialog(this, "Xóa người dùng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            loadUsersAsync(1, 10);
            clearFields();
        } catch (Exception e) {
            errorLabel.setText("Lỗi: " + e.getMessage());
        }
    }

    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        emailField.setText("");
        fullNameField.setText("");
        roleField.setText("");
        errorLabel.setText("");
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(new Color(25, 25, 112));
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        textField.setBackground(Color.WHITE);
        return textField;
    }

    private JButton createStyledButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBackground(new Color(135, 206, 250));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            Image scaledImage = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledImage));
            button.setHorizontalTextPosition(SwingConstants.RIGHT);
            button.setIconTextGap(5);
        } catch (Exception e) {
            System.err.println("Không thể tải biểu tượng: " + iconPath);
        }
        return button;
    }
}