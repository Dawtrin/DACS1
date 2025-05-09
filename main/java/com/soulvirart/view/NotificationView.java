package com.soulvirart.view;

import com.soulvirart.controller.NotificationController;
import com.soulvirart.model.Notification;
import com.soulvirart.util.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class NotificationView extends JPanel {
    private final NotificationController notificationController;
    private JTable notificationTable;
    private DefaultTableModel tableModel;
    private JTextField userIdField, messageField, isReadField;
    private JButton addButton, updateButton, deleteButton;
    private JLabel errorLabel;

    public NotificationView() {
        notificationController = new NotificationController();
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 248, 255));

        String[] columnNames = {"ID", "User ID", "Message", "Created At", "Is Read"};
        tableModel = new DefaultTableModel(columnNames, 0);
        notificationTable = new JTable(tableModel);
        notificationTable.setRowHeight(25);
        notificationTable.setGridColor(new Color(200, 200, 200));
        notificationTable.setSelectionBackground(new Color(135, 206, 250));
        JScrollPane scrollPane = new JScrollPane(notificationTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBackground(new Color(245, 245, 220));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(createStyledLabel("User ID:"));
        userIdField = createStyledTextField();
        inputPanel.add(userIdField);

        inputPanel.add(createStyledLabel("Message:"));
        messageField = createStyledTextField();
        inputPanel.add(messageField);

        inputPanel.add(createStyledLabel("Is Read (true/false):"));
        isReadField = createStyledTextField();
        inputPanel.add(isReadField);

        mainPanel.add(inputPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 248, 255));
        addButton = createStyledButton("Thêm", "/icons/add_icon.png");
        updateButton = createStyledButton("Cập nhật", "/icons/update_icon.png");
        deleteButton = createStyledButton("Xóa", "/icons/delete_icon.png");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton); // Sửa lỗi ở đây

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(errorLabel, BorderLayout.PAGE_END);

        add(mainPanel);

        addButton.addActionListener(e -> addNotification());
        updateButton.addActionListener(e -> updateNotification());
        deleteButton.addActionListener(e -> deleteNotification());

        notificationTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = notificationTable.getSelectedRow();
            if (selectedRow >= 0) {
                userIdField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                messageField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                isReadField.setText(tableModel.getValueAt(selectedRow, 4).toString());
            }
        });
    }

    public void loadData() {
        loadNotificationsAsync(1, 10);
    }

    private void loadNotificationsAsync(int page, int pageSize) {
        SwingWorker<List<Notification>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Notification> doInBackground() throws Exception {
                try (var conn = DatabaseConnection.getConnection()) {
                    if (conn == null) {
                        throw new SQLException("Không thể kết nối đến cơ sở dữ liệu.");
                    }
                }
                return notificationController.getAllNotifications(page, pageSize);
            }

            @Override
            protected void done() {
                try {
                    List<Notification> notifications = get();
                    tableModel.setRowCount(0);
                    if (notifications.isEmpty()) {
                        errorLabel.setText("Không có thông báo nào để hiển thị.");
                    } else {
                        for (Notification notification : notifications) {
                            tableModel.addRow(new Object[]{
                                    notification.getNotificationId(),
                                    notification.getUserId(),
                                    notification.getMessage(),
                                    notification.getCreatedAt(),
                                    notification.isRead()
                            });
                        }
                        errorLabel.setText("");
                    }
                } catch (InterruptedException e) {
                    errorLabel.setText("Tác vụ bị gián đoạn.");
                    e.printStackTrace();
                } catch (Exception e) {
                    String errorMessage;
                    if (e.getCause() instanceof SQLException) {
                        errorMessage = "Lỗi cơ sở dữ liệu: " + e.getCause().getMessage();
                    } else if (e.getCause() instanceof IllegalArgumentException) {
                        errorMessage = "Lỗi tham số: " + e.getCause().getMessage();
                    } else {
                        errorMessage = "Lỗi khi tải dữ liệu: " + e.getMessage();
                    }
                    errorLabel.setText(errorMessage);
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    private void addNotification() {
        try {
            String userIdText = userIdField.getText().trim();
            if (userIdText.isEmpty()) {
                throw new IllegalArgumentException("User ID không được để trống.");
            }
            int userId = Integer.parseInt(userIdText);
            String message = messageField.getText().trim();
            if (message.isEmpty()) {
                throw new IllegalArgumentException("Message không được để trống.");
            }
            notificationController.createNotification(userId, message);
            JOptionPane.showMessageDialog(this, "Thêm thông báo thành công!");
            loadNotificationsAsync(1, 10);
            clearFields();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "User ID phải là một số nguyên hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateNotification() {
        try {
            int selectedRow = notificationTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn thông báo để cập nhật!");
                return;
            }
            int notificationId = (int) tableModel.getValueAt(selectedRow, 0);
            Notification notification = notificationController.findNotificationById(notificationId);
            if (notification == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy thông báo với ID: " + notificationId, "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String userIdText = userIdField.getText().trim();
            if (userIdText.isEmpty()) {
                throw new IllegalArgumentException("User ID không được để trống.");
            }
            int userId = Integer.parseInt(userIdText);
            String message = messageField.getText().trim();
            if (message.isEmpty()) {
                throw new IllegalArgumentException("Message không được để trống.");
            }
            String isReadText = isReadField.getText().trim().toLowerCase();
            if (!isReadText.equals("true") && !isReadText.equals("false")) {
                throw new IllegalArgumentException("Is Read phải là true hoặc false.");
            }
            notification.setUserId(userId);
            notification.setMessage(message);
            notification.setRead(Boolean.parseBoolean(isReadText));
            notificationController.updateNotification(notification);
            JOptionPane.showMessageDialog(this, "Cập nhật thông báo thành công!");
            loadNotificationsAsync(1, 10);
            clearFields();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "User ID phải là một số nguyên hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteNotification() {
        try {
            int selectedRow = notificationTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn thông báo để xóa!");
                return;
            }
            int notificationId = (int) tableModel.getValueAt(selectedRow, 0);
            notificationController.deleteNotification(notificationId);
            JOptionPane.showMessageDialog(this, "Xóa thông báo thành công!");
            loadNotificationsAsync(1, 10);
            clearFields();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        userIdField.setText("");
        messageField.setText("");
        isReadField.setText("");
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
        return textField;
    }

    private JButton createStyledButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBackground(new Color(135, 206, 250));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        return button;
    }
}