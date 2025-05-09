package com.soulvirart.view;

import com.soulvirart.controller.ActivityLogController;
import com.soulvirart.model.ActivityLog;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

public class ActivityLogView extends JPanel {
    private final ActivityLogController activityLogController;
    private JTable logTable;
    private DefaultTableModel tableModel;
    private JTextField userIdField, actionField, startDateField, endDateField;
    private JButton addButton, deleteButton, filterButton, refreshButton;
    private JLabel errorLabel;
    private List<ActivityLog> currentLogs;

    public ActivityLogView() {
        activityLogController = new ActivityLogController();
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 248, 255));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title
        JLabel titleLabel = new JLabel("Quản Lý Nhật Ký Hoạt Động", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(25, 25, 112));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Log Table
        String[] columnNames = {"ID", "User ID", "Action", "Timestamp"};
        tableModel = new DefaultTableModel(columnNames, 0);
        logTable = new JTable(tableModel);
        logTable.setRowHeight(25);
        logTable.setGridColor(new Color(200, 200, 200));
        logTable.setSelectionBackground(new Color(135, 206, 250));
        logTable.setFont(new Font("Arial", Font.PLAIN, 14));

        // Căn chỉnh cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        logTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // ID
        logTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // User ID
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        logTable.getColumnModel().getColumn(2).setCellRenderer(leftRenderer); // Action
        logTable.getColumnModel().getColumn(3).setCellRenderer(leftRenderer); // Timestamp

        JScrollPane scrollPane = new JScrollPane(logTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Input and Filter Panel
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(new Color(245, 245, 220));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Input Section
        gbc.gridx = 0;
        gbc.gridy = 0;
        topPanel.add(createStyledLabel("User ID:"), gbc);
        gbc.gridx = 1;
        userIdField = createStyledTextField();
        gbc.weightx = 1;
        topPanel.add(userIdField, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        topPanel.add(createStyledLabel("Action:"), gbc);
        gbc.gridx = 3;
        actionField = createStyledTextField();
        gbc.weightx = 1;
        topPanel.add(actionField, gbc);

        // Filter Section
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        topPanel.add(createStyledLabel("Start Date (yyyy-MM-dd HH:mm):"), gbc);
        gbc.gridx = 1;
        startDateField = createStyledTextField();
        gbc.weightx = 1;
        topPanel.add(startDateField, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        topPanel.add(createStyledLabel("End Date (yyyy-MM-dd HH:mm):"), gbc);
        gbc.gridx = 3;
        endDateField = createStyledTextField();
        gbc.weightx = 1;
        topPanel.add(endDateField, gbc);

        gbc.gridx = 4;
        gbc.weightx = 0;
        filterButton = createStyledButton("Lọc Theo Ngày", "/icons/search_icon.png");
        topPanel.add(filterButton, gbc);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(240, 248, 255));
        addButton = createStyledButton("Thêm", "/icons/add_icon.png");
        deleteButton = createStyledButton("Xóa", "/icons/delete_icon.png");
        refreshButton = createStyledButton("Làm mới", "/icons/refresh_icon.png");

        buttonPanel.add(addButton);
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
        addButton.addActionListener(e -> addActivityLog());
        deleteButton.addActionListener(e -> deleteActivityLog());
        filterButton.addActionListener(e -> filterLogsByDateRange());
        refreshButton.addActionListener(e -> loadActivityLogsAsync(1, 10));

        logTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = logTable.getSelectedRow();
            if (selectedRow >= 0) {
                userIdField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                actionField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                startDateField.setText("");
                endDateField.setText("");
            }
        });

        // Load initial data
        loadActivityLogsAsync(1, 10);
    }

    public void loadData() {
        loadActivityLogsAsync(1, 10);
    }

    private void loadActivityLogsAsync(int page, int pageSize) {
        SwingWorker<List<ActivityLog>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<ActivityLog> doInBackground() throws Exception {
                return activityLogController.getAllActivityLogs(page, pageSize);
            }

            @Override
            protected void done() {
                try {
                    currentLogs = get();
                    if (currentLogs == null) {
                        throw new IllegalStateException("Không thể tải dữ liệu nhật ký!");
                    }
                    updateTable(currentLogs);
                    errorLabel.setText("");
                } catch (Exception e) {
                    errorLabel.setText("Lỗi khi tải dữ liệu: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void updateTable(List<ActivityLog> logs) {
        tableModel.setRowCount(0);
        for (ActivityLog log : logs) {
            tableModel.addRow(new Object[]{
                    log.getLogId(),
                    log.getUserId(),
                    log.getAction(),
                    log.getTimestamp()
            });
        }
    }

    private void addActivityLog() {
        try {
            String userIdText = userIdField.getText().trim();
            String actionText = actionField.getText().trim();

            // Validate User ID
            if (userIdText.isEmpty()) {
                throw new IllegalArgumentException("User ID không được để trống!");
            }
            int userId;
            try {
                userId = Integer.parseInt(userIdText);
                if (userId <= 0) {
                    throw new IllegalArgumentException("User ID phải là số dương!");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("User ID phải là một số hợp lệ!");
            }

            // Validate Action
            if (actionText.isEmpty()) {
                throw new IllegalArgumentException("Hành động không được để trống!");
            }

            activityLogController.createActivityLog(userId, actionText);
            JOptionPane.showMessageDialog(this, "Thêm nhật ký hoạt động thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            loadActivityLogsAsync(1, 10);
            clearFields();
        } catch (Exception e) {
            errorLabel.setText("Lỗi: " + e.getMessage());
        }
    }

    private void deleteActivityLog() {
        try {
            int selectedRow = logTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhật ký để xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Object logIdObj = tableModel.getValueAt(selectedRow, 0);
            if (logIdObj == null) {
                throw new IllegalStateException("ID nhật ký không hợp lệ!");
            }

            int logId;
            try {
                logId = (int) logIdObj;
            } catch (ClassCastException e) {
                throw new IllegalStateException("ID nhật ký không phải là số hợp lệ!");
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa nhật ký này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            activityLogController.deleteActivityLog(logId);
            JOptionPane.showMessageDialog(this, "Xóa nhật ký hoạt động thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            loadActivityLogsAsync(1, 10);
            clearFields();
        } catch (Exception e) {
            errorLabel.setText("Lỗi: " + e.getMessage());
        }
    }

    private void filterLogsByDateRange() {
        try {
            String startDateText = startDateField.getText().trim();
            String endDateText = endDateField.getText().trim();

            if (currentLogs == null || currentLogs.isEmpty()) {
                throw new IllegalStateException("Danh sách nhật ký rỗng! Vui lòng tải dữ liệu trước.");
            }

            if (startDateText.isEmpty() && endDateText.isEmpty()) {
                updateTable(currentLogs); // Reset to all logs if no filter applied
                JOptionPane.showMessageDialog(this, "Đã hiển thị tất cả nhật ký!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (startDateText.isEmpty() || endDateText.isEmpty()) {
                throw new IllegalArgumentException("Vui lòng nhập cả ngày bắt đầu và ngày kết thúc!");
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime startDate = LocalDateTime.parse(startDateText, formatter);
            LocalDateTime endDate = LocalDateTime.parse(endDateText, formatter);

            if (startDate.isAfter(endDate)) {
                throw new IllegalArgumentException("Ngày bắt đầu không được lớn hơn ngày kết thúc!");
            }

            List<ActivityLog> filteredLogs = currentLogs.stream()
                    .filter(log -> !log.getTimestamp().isBefore(startDate) && !log.getTimestamp().isAfter(endDate))
                    .collect(Collectors.toList());

            if (filteredLogs.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy nhật ký nào trong khoảng thời gian này!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Lọc nhật ký theo ngày thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            }
            updateTable(filteredLogs);
        } catch (DateTimeParseException e) {
            errorLabel.setText("Định dạng ngày không hợp lệ! Vui lòng nhập theo định dạng yyyy-MM-dd HH:mm.");
        } catch (Exception e) {
            errorLabel.setText("Lỗi: " + e.getMessage());
        }
    }

    private void clearFields() {
        userIdField.setText("");
        actionField.setText("");
        startDateField.setText("");
        endDateField.setText("");
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