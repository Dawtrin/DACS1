package com.soulvirart.view;

import com.soulvirart.controller.TicketController;
import com.soulvirart.model.Ticket;
import com.soulvirart.util.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class TicketView extends JPanel {
    private final TicketController ticketController;
    private JTable ticketTable;
    private DefaultTableModel tableModel;
    private JTextField userIdField, tourIdField, customerNameField, quantityField, ageCategoryField, visitDateField, priceField, statusField;
    private JButton addButton, updateButton, deleteButton;
    private JLabel errorLabel;

    public TicketView() {
        ticketController = new TicketController();
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 248, 255));

        // Bảng hiển thị vé
        String[] columnNames = {"ID", "User ID", "Tour ID", "Customer Name", "Quantity", "Age Category", "Visit Date", "Price", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        ticketTable = new JTable(tableModel);
        ticketTable.setRowHeight(25);
        ticketTable.setGridColor(new Color(200, 200, 200));
        ticketTable.setSelectionBackground(new Color(135, 206, 250));
        JScrollPane scrollPane = new JScrollPane(ticketTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Bảng nhập liệu
        JPanel inputPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        inputPanel.setBackground(new Color(245, 245, 220));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(createStyledLabel("User ID:"));
        userIdField = createStyledTextField();
        inputPanel.add(userIdField);

        inputPanel.add(createStyledLabel("Tour ID:"));
        tourIdField = createStyledTextField();
        inputPanel.add(tourIdField);

        inputPanel.add(createStyledLabel("Customer Name:"));
        customerNameField = createStyledTextField();
        inputPanel.add(customerNameField);

        inputPanel.add(createStyledLabel("Quantity:"));
        quantityField = createStyledTextField();
        inputPanel.add(quantityField);

        inputPanel.add(createStyledLabel("Age Category:"));
        ageCategoryField = createStyledTextField();
        inputPanel.add(ageCategoryField);

        inputPanel.add(createStyledLabel("Visit Date (yyyy-MM-dd HH:mm):"));
        visitDateField = createStyledTextField();
        inputPanel.add(visitDateField);

        inputPanel.add(createStyledLabel("Price:"));
        priceField = createStyledTextField();
        inputPanel.add(priceField);

        inputPanel.add(createStyledLabel("Status:"));
        statusField = createStyledTextField();
        inputPanel.add(statusField);

        mainPanel.add(inputPanel, BorderLayout.NORTH);

        // Bảng nút
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 248, 255));
        addButton = createStyledButton("Thêm", "/icons/add_icon.png");
        updateButton = createStyledButton("Cập nhật", "/icons/update_icon.png");
        deleteButton = createStyledButton("Xóa", "/icons/delete_icon.png");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Nhãn lỗi
        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(errorLabel, BorderLayout.PAGE_END);

        add(mainPanel);

        // Sự kiện nút
        addButton.addActionListener(e -> addTicket());
        updateButton.addActionListener(e -> updateTicket());
        deleteButton.addActionListener(e -> deleteTicket());

        // Sự kiện chọn hàng trong bảng
        ticketTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = ticketTable.getSelectedRow();
            if (selectedRow >= 0) {
                userIdField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                tourIdField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                customerNameField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                quantityField.setText(tableModel.getValueAt(selectedRow, 4).toString());
                ageCategoryField.setText(tableModel.getValueAt(selectedRow, 5).toString());
                Object visitDate = tableModel.getValueAt(selectedRow, 6);
                visitDateField.setText(visitDate != null ? visitDate.toString() : "");
                priceField.setText(tableModel.getValueAt(selectedRow, 7).toString());
                statusField.setText(tableModel.getValueAt(selectedRow, 8).toString());
            }
        });
    }

    public void loadData() {
        loadTicketsAsync(1, 10);
    }

    private void loadTicketsAsync(int page, int pageSize) {
        SwingWorker<List<Ticket>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Ticket> doInBackground() throws Exception {
                // Kiểm tra kết nối cơ sở dữ liệu
                try (var conn = DatabaseConnection.getConnection()) {
                    if (conn == null) {
                        throw new SQLException("Không thể kết nối đến cơ sở dữ liệu.");
                    }
                }
                return ticketController.getAllTickets(page, pageSize);
            }

            @Override
            protected void done() {
                try {
                    List<Ticket> tickets = get();
                    tableModel.setRowCount(0);
                    if (tickets.isEmpty()) {
                        errorLabel.setText("Không có vé nào để hiển thị.");
                    } else {
                        for (Ticket ticket : tickets) {
                            tableModel.addRow(new Object[]{
                                    ticket.getTicketId(),
                                    ticket.getUserId(),
                                    ticket.getTourId(),
                                    ticket.getCustomerName(),
                                    ticket.getQuantity(),
                                    ticket.getAgeCategory(),
                                    ticket.getVisitDate(),
                                    ticket.getPrice(),
                                    ticket.getStatus()
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

    private void addTicket() {
        try {
            // Kiểm tra và lấy dữ liệu đầu vào
            String userIdText = userIdField.getText().trim();
            if (userIdText.isEmpty()) {
                throw new IllegalArgumentException("User ID không được để trống.");
            }
            int userId = Integer.parseInt(userIdText);

            String tourIdText = tourIdField.getText().trim();
            if (tourIdText.isEmpty()) {
                throw new IllegalArgumentException("Tour ID không được để trống.");
            }
            int tourId = Integer.parseInt(tourIdText);

            String customerName = customerNameField.getText().trim();
            if (customerName.isEmpty()) {
                throw new IllegalArgumentException("Customer Name không được để trống.");
            }

            String quantityText = quantityField.getText().trim();
            if (quantityText.isEmpty()) {
                throw new IllegalArgumentException("Quantity không được để trống.");
            }
            int quantity = Integer.parseInt(quantityText);
            if (quantity <= 0) {
                throw new IllegalArgumentException("Quantity phải lớn hơn 0.");
            }

            String ageCategory = ageCategoryField.getText().trim();
            if (ageCategory.isEmpty()) {
                throw new IllegalArgumentException("Age Category không được để trống.");
            }

            String visitDateText = visitDateField.getText().trim();
            LocalDateTime visitDate = null;
            if (!visitDateText.isEmpty()) {
                try {
                    visitDate = LocalDateTime.parse(visitDateText, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("Visit Date phải có định dạng yyyy-MM-dd HH:mm.");
                }
            }

            String priceText = priceField.getText().trim();
            if (priceText.isEmpty()) {
                throw new IllegalArgumentException("Price không được để trống.");
            }
            double price = Double.parseDouble(priceText);
            if (price < 0) {
                throw new IllegalArgumentException("Price không được âm.");
            }

            String status = statusField.getText().trim();
            if (status.isEmpty()) {
                throw new IllegalArgumentException("Status không được để trống.");
            }

            // Tạo đối tượng Ticket
            Ticket ticket = new Ticket();
            ticket.setUserId(userId);
            ticket.setTourId(tourId);
            ticket.setCustomerName(customerName);
            ticket.setQuantity(quantity);
            ticket.setAgeCategory(ageCategory);
            ticket.setVisitDate(visitDate);
            ticket.setPrice(price);
            ticket.setStatus(status);

            // Gọi controller để tạo vé
            ticketController.createTicket(ticket);
            JOptionPane.showMessageDialog(this, "Thêm vé thành công!");
            loadTicketsAsync(1, 10);
            clearFields();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "User ID, Tour ID, Quantity hoặc Price phải là số hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void updateTicket() {
        try {
            // Kiểm tra hàng được chọn
            int selectedRow = ticketTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn vé để cập nhật!");
                return;
            }
            int ticketId = (int) tableModel.getValueAt(selectedRow, 0);

            // Tìm vé
            Ticket ticket = ticketController.findTicketById(ticketId);
            if (ticket == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy vé với ID: " + ticketId, "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Kiểm tra và lấy dữ liệu đầu vào
            String userIdText = userIdField.getText().trim();
            if (userIdText.isEmpty()) {
                throw new IllegalArgumentException("User ID không được để trống.");
            }
            int userId = Integer.parseInt(userIdText);

            String tourIdText = tourIdField.getText().trim();
            if (tourIdText.isEmpty()) {
                throw new IllegalArgumentException("Tour ID không được để trống.");
            }
            int tourId = Integer.parseInt(tourIdText);

            String customerName = customerNameField.getText().trim();
            if (customerName.isEmpty()) {
                throw new IllegalArgumentException("Customer Name không được để trống.");
            }

            String quantityText = quantityField.getText().trim();
            if (quantityText.isEmpty()) {
                throw new IllegalArgumentException("Quantity không được để trống.");
            }
            int quantity = Integer.parseInt(quantityText);
            if (quantity <= 0) {
                throw new IllegalArgumentException("Quantity phải lớn hơn 0.");
            }

            String ageCategory = ageCategoryField.getText().trim();
            if (ageCategory.isEmpty()) {
                throw new IllegalArgumentException("Age Category không được để trống.");
            }

            String visitDateText = visitDateField.getText().trim();
            LocalDateTime visitDate = null;
            if (!visitDateText.isEmpty()) {
                try {
                    visitDate = LocalDateTime.parse(visitDateText, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("Visit Date phải có định dạng yyyy-MM-dd HH:mm.");
                }
            }

            String priceText = priceField.getText().trim();
            if (priceText.isEmpty()) {
                throw new IllegalArgumentException("Price không được để trống.");
            }
            double price = Double.parseDouble(priceText);
            if (price < 0) {
                throw new IllegalArgumentException("Price không được âm.");
            }

            String status = statusField.getText().trim();
            if (status.isEmpty()) {
                throw new IllegalArgumentException("Status không được để trống.");
            }

            // Cập nhật đối tượng Ticket
            ticket.setUserId(userId);
            ticket.setTourId(tourId);
            ticket.setCustomerName(customerName);
            ticket.setQuantity(quantity);
            ticket.setAgeCategory(ageCategory);
            ticket.setVisitDate(visitDate);
            ticket.setPrice(price);
            ticket.setStatus(status);

            // Gọi controller để cập nhật vé
            ticketController.updateTicket(ticket);
            JOptionPane.showMessageDialog(this, "Cập nhật vé thành công!");
            loadTicketsAsync(1, 10);
            clearFields();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "User ID, Tour ID, Quantity hoặc Price phải là số hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void deleteTicket() {
        try {
            int selectedRow = ticketTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn vé để xóa!");
                return;
            }
            int ticketId = (int) tableModel.getValueAt(selectedRow, 0);
            ticketController.deleteTicket(ticketId);
            JOptionPane.showMessageDialog(this, "Xóa vé thành công!");
            loadTicketsAsync(1, 10);
            clearFields();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void clearFields() {
        userIdField.setText("");
        tourIdField.setText("");
        customerNameField.setText("");
        quantityField.setText("");
        ageCategoryField.setText("");
        visitDateField.setText("");
        priceField.setText("");
        statusField.setText("");
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
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            button.setIcon(icon);
            button.setHorizontalTextPosition(SwingConstants.RIGHT);
            button.setIconTextGap(5);
        } catch (Exception e) {
            System.err.println("Không thể tải biểu tượng: " + iconPath);
        }
        return button;
    }
}