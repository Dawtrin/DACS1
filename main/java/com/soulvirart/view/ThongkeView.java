package com.soulvirart.view;

import com.soulvirart.controller.TicketController;
import com.soulvirart.controller.ArtworkController;
import com.soulvirart.controller.VirtualTourController;
import com.soulvirart.model.Ticket;
import com.soulvirart.model.Artwork;
import com.soulvirart.model.VirtualTour;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Locale;
import com.opencsv.CSVWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class ThongkeView extends JPanel {
    private final TicketController ticketController;
    private final ArtworkController artworkController;
    private final VirtualTourController virtualTourController;
    private JTable ticketTable;
    private DefaultTableModel tableModel;
    private JLabel totalRevenueLabel;
    private JComboBox<String> dayComboBox, monthComboBox, yearComboBox, chartTypeComboBox, statusComboBox, ageCategoryComboBox, chartStyleComboBox, chartColorComboBox;
    private JTextField searchField;
    private JButton filterButton, resetButton, searchButton, showChartButton, mostPurchasedArtworkButton, mostPurchasedTourButton, exportCSVButton, exportPDFButton, sendEmailButton;
    private JButton prevPageButton, nextPageButton;
    private JLabel pageLabel;
    private JLabel errorLabel;
    private JList<String> searchHistoryList;
    private DefaultListModel<String> searchHistoryModel;
    private Deque<String> searchHistory;
    private List<Ticket> currentTickets;
    private List<Ticket> filteredTickets;
    private int currentPage = 1;
    private final int pageSize = 10;
    private static final int MAX_SEARCH_HISTORY = 10;

    public ThongkeView() {
        ticketController = new TicketController();
        artworkController = new ArtworkController();
        virtualTourController = new VirtualTourController();
        searchHistory = new ArrayDeque<>();
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 248, 255));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Total Revenue Label (on top of the table)
        totalRevenueLabel = new JLabel("Tổng Doanh Thu: 0 VNĐ", SwingConstants.CENTER);
        totalRevenueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        totalRevenueLabel.setForeground(new Color(25, 25, 112));
        totalRevenueLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(totalRevenueLabel, BorderLayout.NORTH);

        // Search Panel
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(new Color(245, 245, 220));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JPanel searchInputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        searchInputPanel.setBackground(new Color(245, 245, 220));
        searchInputPanel.add(createStyledLabel("Tìm kiếm (Ticket ID / User ID):"));
        searchField = new JTextField(15);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchInputPanel.add(searchField);
        searchButton = createStyledButton("Tìm kiếm", "/icons/search_icon.png");
        searchInputPanel.add(searchButton);
        searchPanel.add(searchInputPanel, BorderLayout.NORTH);

        // Search History
        searchHistoryModel = new DefaultListModel<>();
        searchHistoryList = new JList<>(searchHistoryModel);
        searchHistoryList.setFont(new Font("Arial", Font.PLAIN, 14));
        searchHistoryList.setVisibleRowCount(5);
        searchHistoryList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = searchHistoryList.getSelectedValue();
                if (selected != null) {
                    searchField.setText(selected);
                    searchTickets();
                }
            }
        });
        JScrollPane historyScrollPane = new JScrollPane(searchHistoryList);
        historyScrollPane.setBorder(BorderFactory.createTitledBorder("Lịch Sử Tìm Kiếm"));
        searchPanel.add(historyScrollPane, BorderLayout.CENTER);

        mainPanel.add(searchPanel, BorderLayout.PAGE_START);

        // Table Panel (contains table and pagination)
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(240, 248, 255));

        // Ticket Table
        String[] columnNames = {"ID", "User ID", "Tour ID", "Price", "Purchase Date", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        ticketTable = new JTable(tableModel);
        ticketTable.setRowHeight(25);
        ticketTable.setGridColor(new Color(200, 200, 200));
        ticketTable.setSelectionBackground(new Color(135, 206, 250));
        ticketTable.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(ticketTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Pagination Panel
        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        paginationPanel.setBackground(new Color(240, 248, 255));
        prevPageButton = createStyledButton("Trang trước", "/icons/prev_icon.png");
        nextPageButton = createStyledButton("Trang sau", "/icons/next_icon.png");
        pageLabel = new JLabel("Trang 1", SwingConstants.CENTER);
        pageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        paginationPanel.add(prevPageButton);
        paginationPanel.add(pageLabel);
        paginationPanel.add(nextPageButton);
        tablePanel.add(paginationPanel, BorderLayout.SOUTH);

        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        filterPanel.setBackground(new Color(245, 245, 220));
        filterPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ComboBox for Day, Month, Year
        filterPanel.add(createStyledLabel("Ngày:"));
        String[] days = new String[32];
        days[0] = "Tất cả";
        for (int i = 1; i <= 31; i++) {
            days[i] = String.valueOf(i);
        }
        dayComboBox = new JComboBox<>(days);
        dayComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        filterPanel.add(dayComboBox);

        filterPanel.add(createStyledLabel("Tháng:"));
        String[] months = new String[13];
        months[0] = "Tất cả";
        for (int i = 1; i <= 12; i++) {
            months[i] = String.valueOf(i);
        }
        monthComboBox = new JComboBox<>(months);
        monthComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        filterPanel.add(monthComboBox);

        filterPanel.add(createStyledLabel("Năm:"));
        String[] years = new String[11];
        years[0] = "Tất cả";
        int currentYear = LocalDate.now().getYear();
        for (int i = 0; i < 10; i++) {
            years[i + 1] = String.valueOf(currentYear - i);
        }
        yearComboBox = new JComboBox<>(years);
        yearComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        filterPanel.add(yearComboBox);

        // Status Filter
        filterPanel.add(createStyledLabel("Trạng Thái:"));
        String[] statuses = {"Tất cả", "PAID", "PENDING", "CANCELLED"};
        statusComboBox = new JComboBox<>(statuses);
        statusComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        filterPanel.add(statusComboBox);

        // Age Category Filter
        filterPanel.add(createStyledLabel("Loại Tuổi:"));
        String[] ageCategories = {"Tất cả", "ADULT", "CHILD", "SENIOR"};
        ageCategoryComboBox = new JComboBox<>(ageCategories);
        ageCategoryComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        filterPanel.add(ageCategoryComboBox);

        // Chart Type ComboBox
        filterPanel.add(createStyledLabel("Kiểu Biểu Đồ:"));
        String[] chartTypes = {"Theo Ngày", "Theo Tuần", "Theo Tháng"};
        chartTypeComboBox = new JComboBox<>(chartTypes);
        chartTypeComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        filterPanel.add(chartTypeComboBox);

        // Chart Style ComboBox
        filterPanel.add(createStyledLabel("Kiểu Hiển Thị:"));
        String[] chartStyles = {"Cột", "Đường", "Tròn"};
        chartStyleComboBox = new JComboBox<>(chartStyles);
        chartStyleComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        filterPanel.add(chartStyleComboBox);

        // Chart Color ComboBox
        filterPanel.add(createStyledLabel("Màu Sắc:"));
        String[] chartColors = {"Mặc Định", "Xanh", "Đỏ", "Xanh Lá"};
        chartColorComboBox = new JComboBox<>(chartColors);
        chartColorComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        filterPanel.add(chartColorComboBox);

        // Buttons
        filterButton = createStyledButton("Lọc", "/icons/filter_icon.png");
        resetButton = createStyledButton("Đặt lại", "/icons/reset_icon.png");
        showChartButton = createStyledButton("Hiển Thị Biểu Đồ", "/icons/chart_icon.png");
        mostPurchasedArtworkButton = createStyledButton("Tranh Được Mua Nhiều Nhất", "/icons/art_icon.png");
        mostPurchasedTourButton = createStyledButton("Tour Được Mua Nhiều Nhất", "/icons/tour_icon.png");
        exportCSVButton = createStyledButton("Xuất CSV", "/icons/export_icon.png");
        exportPDFButton = createStyledButton("Xuất PDF", "/icons/pdf_icon.png");
        sendEmailButton = createStyledButton("Gửi Email", "/icons/email_icon.png");

        filterPanel.add(filterButton);
        filterPanel.add(resetButton);
        filterPanel.add(showChartButton);
        filterPanel.add(mostPurchasedArtworkButton);
        filterPanel.add(mostPurchasedTourButton);
        filterPanel.add(exportCSVButton);
        filterPanel.add(exportPDFButton);
        filterPanel.add(sendEmailButton);

        mainPanel.add(filterPanel, BorderLayout.SOUTH);

        // Error Label
        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        mainPanel.add(errorLabel, BorderLayout.PAGE_END);

        add(mainPanel);

        // Action Listeners
        filterButton.addActionListener(e -> filterTicketsByDate());
        resetButton.addActionListener(e -> resetFilters());
        searchButton.addActionListener(e -> searchTickets());
        showChartButton.addActionListener(e -> showRevenueChart());
        mostPurchasedArtworkButton.addActionListener(e -> showMostPurchasedArtwork());
        mostPurchasedTourButton.addActionListener(e -> showMostPurchasedTour());
        exportCSVButton.addActionListener(e -> exportToCSV());
        exportPDFButton.addActionListener(e -> exportToPDF());
        sendEmailButton.addActionListener(e -> sendEmailWithReport());
        prevPageButton.addActionListener(e -> previousPage());
        nextPageButton.addActionListener(e -> nextPage());

        // Load initial data
        loadData();
    }

    public void loadData() {
        loadTicketsAsync(1, 1000); // Load all tickets initially
    }

    private void loadTicketsAsync(int page, int pageSize) {
        SwingWorker<List<Ticket>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Ticket> doInBackground() throws Exception {
                return ticketController.getAllTickets(page, pageSize);
            }

            @Override
            protected void done() {
                try {
                    currentTickets = get();
                    filteredTickets = new ArrayList<>(currentTickets);
                    currentPage = 1;
                    updateTableWithPagination();
                    updateTotalRevenue(filteredTickets);
                    errorLabel.setText("");
                } catch (Exception e) {
                    errorLabel.setText("Lỗi khi tải dữ liệu: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void updateTableWithPagination() {
        tableModel.setRowCount(0);
        int start = (currentPage - 1) * pageSize;
        int end = Math.min(start + pageSize, filteredTickets.size());
        for (int i = start; i < end; i++) {
            Ticket ticket = filteredTickets.get(i);
            tableModel.addRow(new Object[]{
                    ticket.getTicketId(),
                    ticket.getUserId(),
                    ticket.getTourId(),
                    ticket.getPrice(),
                    ticket.getVisitDate(),
                    ticket.getStatus()
            });
        }
        pageLabel.setText("Trang " + currentPage);
        prevPageButton.setEnabled(currentPage > 1);
        nextPageButton.setEnabled(end < filteredTickets.size());
    }

    private void updateTotalRevenue(List<Ticket> tickets) {
        double totalRevenue = tickets.stream()
                .filter(ticket -> "PAID".equalsIgnoreCase(ticket.getStatus()))
                .mapToDouble(ticket -> ticket.getPrice() * ticket.getQuantity())
                .sum();
        totalRevenueLabel.setText(String.format("Tổng Doanh Thu: %.2f VNĐ", totalRevenue));
    }

    private void filterTicketsByDate() {
        try {
            String selectedDay = (String) dayComboBox.getSelectedItem();
            String selectedMonth = (String) monthComboBox.getSelectedItem();
            String selectedYear = (String) yearComboBox.getSelectedItem();
            String selectedStatus = (String) statusComboBox.getSelectedItem();
            String selectedAgeCategory = (String) ageCategoryComboBox.getSelectedItem();

            filteredTickets = new ArrayList<>(currentTickets);

            if (!"Tất cả".equals(selectedYear)) {
                int year = Integer.parseInt(selectedYear);
                filteredTickets = filteredTickets.stream()
                        .filter(ticket -> ticket.getVisitDate().getYear() == year)
                        .collect(Collectors.toList());
            }

            if (!"Tất cả".equals(selectedMonth)) {
                int month = Integer.parseInt(selectedMonth);
                filteredTickets = filteredTickets.stream()
                        .filter(ticket -> ticket.getVisitDate().getMonthValue() == month)
                        .collect(Collectors.toList());
            }

            if (!"Tất cả".equals(selectedDay)) {
                int day = Integer.parseInt(selectedDay);
                filteredTickets = filteredTickets.stream()
                        .filter(ticket -> ticket.getVisitDate().getDayOfMonth() == day)
                        .collect(Collectors.toList());
            }

            if (!"Tất cả".equals(selectedStatus)) {
                filteredTickets = filteredTickets.stream()
                        .filter(ticket -> selectedStatus.equalsIgnoreCase(ticket.getStatus()))
                        .collect(Collectors.toList());
            }

            if (!"Tất cả".equals(selectedAgeCategory)) {
                filteredTickets = filteredTickets.stream()
                        .filter(ticket -> selectedAgeCategory.equalsIgnoreCase(ticket.getAgeCategory()))
                        .collect(Collectors.toList());
            }

            currentPage = 1;
            updateTableWithPagination();
            updateTotalRevenue(filteredTickets);
            JOptionPane.showMessageDialog(this, "Lọc thành công!", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetFilters() {
        dayComboBox.setSelectedIndex(0);
        monthComboBox.setSelectedIndex(0);
        yearComboBox.setSelectedIndex(0);
        statusComboBox.setSelectedIndex(0);
        ageCategoryComboBox.setSelectedIndex(0);
        chartTypeComboBox.setSelectedIndex(0);
        chartStyleComboBox.setSelectedIndex(0);
        chartColorComboBox.setSelectedIndex(0);
        searchField.setText("");
        filteredTickets = new ArrayList<>(currentTickets);
        currentPage = 1;
        updateTableWithPagination();
        updateTotalRevenue(filteredTickets);
        JOptionPane.showMessageDialog(this, "Đã đặt lại bộ lọc!", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void searchTickets() {
        try {
            String searchText = searchField.getText().trim();
            if (searchText.isEmpty()) {
                filteredTickets = new ArrayList<>(currentTickets);
            } else {
                int searchId = Integer.parseInt(searchText);
                filteredTickets = ticketController.searchTickets(searchId);
                addToSearchHistory(searchText);
            }
            currentPage = 1;
            updateTableWithPagination();
            updateTotalRevenue(filteredTickets);
            JOptionPane.showMessageDialog(this, "Tìm kiếm thành công!", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập ID hợp lệ (số nguyên)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addToSearchHistory(String searchText) {
        if (searchHistory.contains(searchText)) {
            searchHistory.remove(searchText);
        }
        searchHistory.addFirst(searchText);
        while (searchHistory.size() > MAX_SEARCH_HISTORY) {
            searchHistory.removeLast();
        }
        searchHistoryModel.clear();
        for (String history : searchHistory) {
            searchHistoryModel.addElement(history);
        }
    }

    private void showRevenueChart() {
        String chartType = (String) chartTypeComboBox.getSelectedItem();
        String chartStyle = (String) chartStyleComboBox.getSelectedItem();
        String chartColor = (String) chartColorComboBox.getSelectedItem();

        if ("Tròn".equals(chartStyle)) {
            DefaultPieDataset dataset = new DefaultPieDataset();
            if ("Theo Ngày".equals(chartType)) {
                Map<LocalDate, Double> revenueByDate = filteredTickets.stream()
                        .filter(ticket -> "PAID".equalsIgnoreCase(ticket.getStatus()))
                        .collect(Collectors.groupingBy(
                                ticket -> ticket.getVisitDate().toLocalDate(),
                                Collectors.summingDouble(ticket -> ticket.getPrice() * ticket.getQuantity())
                        ));
                revenueByDate.forEach((date, revenue) -> dataset.setValue(date.toString(), revenue));
            } else if ("Theo Tuần".equals(chartType)) {
                Map<String, Double> revenueByWeek = filteredTickets.stream()
                        .filter(ticket -> "PAID".equalsIgnoreCase(ticket.getStatus()))
                        .collect(Collectors.groupingBy(
                                ticket -> {
                                    LocalDate date = ticket.getVisitDate().toLocalDate();
                                    int year = date.getYear();
                                    int week = date.get(WeekFields.of(Locale.getDefault()).weekOfYear());
                                    return year + "-W" + String.format("%02d", week);
                                },
                                Collectors.summingDouble(ticket -> ticket.getPrice() * ticket.getQuantity())
                        ));
                revenueByWeek.forEach((week, revenue) -> dataset.setValue(week, revenue));
            } else if ("Theo Tháng".equals(chartType)) {
                Map<String, Double> revenueByMonth = filteredTickets.stream()
                        .filter(ticket -> "PAID".equalsIgnoreCase(ticket.getStatus()))
                        .collect(Collectors.groupingBy(
                                ticket -> {
                                    LocalDate date = ticket.getVisitDate().toLocalDate();
                                    return date.getYear() + "-" + String.format("%02d", date.getMonthValue());
                                },
                                Collectors.summingDouble(ticket -> ticket.getPrice() * ticket.getQuantity())
                        ));
                revenueByMonth.forEach((month, revenue) -> dataset.setValue(month, revenue));
            }

            JFreeChart chart = ChartFactory.createPieChart(
                    "Doanh Thu " + chartType,
                    dataset,
                    true,
                    true,
                    false
            );
            PiePlot plot = (PiePlot) chart.getPlot();
            applyChartColor(plot, chartColor, dataset);

            ChartPanel chartPanel = new ChartPanel(chart);
            JFrame frame = new JFrame("Biểu Đồ Doanh Thu");
            frame.setContentPane(chartPanel);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(this);
            frame.setVisible(true);
        } else {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            if ("Theo Ngày".equals(chartType)) {
                Map<LocalDate, Double> revenueByDate = filteredTickets.stream()
                        .filter(ticket -> "PAID".equalsIgnoreCase(ticket.getStatus()))
                        .collect(Collectors.groupingBy(
                                ticket -> ticket.getVisitDate().toLocalDate(),
                                Collectors.summingDouble(ticket -> ticket.getPrice() * ticket.getQuantity())
                        ));
                revenueByDate.forEach((date, revenue) -> dataset.addValue(revenue, "Doanh thu", date.toString()));
            } else if ("Theo Tuần".equals(chartType)) {
                Map<String, Double> revenueByWeek = filteredTickets.stream()
                        .filter(ticket -> "PAID".equalsIgnoreCase(ticket.getStatus()))
                        .collect(Collectors.groupingBy(
                                ticket -> {
                                    LocalDate date = ticket.getVisitDate().toLocalDate();
                                    int year = date.getYear();
                                    int week = date.get(WeekFields.of(Locale.getDefault()).weekOfYear());
                                    return year + "-W" + String.format("%02d", week);
                                },
                                Collectors.summingDouble(ticket -> ticket.getPrice() * ticket.getQuantity())
                        ));
                revenueByWeek.forEach((week, revenue) -> dataset.addValue(revenue, "Doanh thu", week));
            } else if ("Theo Tháng".equals(chartType)) {
                Map<String, Double> revenueByMonth = filteredTickets.stream()
                        .filter(ticket -> "PAID".equalsIgnoreCase(ticket.getStatus()))
                        .collect(Collectors.groupingBy(
                                ticket -> {
                                    LocalDate date = ticket.getVisitDate().toLocalDate();
                                    return date.getYear() + "-" + String.format("%02d", date.getMonthValue());
                                },
                                Collectors.summingDouble(ticket -> ticket.getPrice() * ticket.getQuantity())
                        ));
                revenueByMonth.forEach((month, revenue) -> dataset.addValue(revenue, "Doanh thu", month));
            }

            JFreeChart chart;
            if ("Đường".equals(chartStyle)) {
                chart = ChartFactory.createLineChart(
                        "Doanh Thu " + chartType,
                        chartType.replace("Theo ", ""),
                        "Doanh Thu (VNĐ)",
                        dataset,
                        PlotOrientation.VERTICAL,
                        true,
                        true,
                        false
                );
            } else {
                chart = ChartFactory.createBarChart(
                        "Doanh Thu " + chartType,
                        chartType.replace("Theo ", ""),
                        "Doanh Thu (VNĐ)",
                        dataset,
                        PlotOrientation.VERTICAL,
                        true,
                        true,
                        false
                );
            }
            CategoryPlot plot = chart.getCategoryPlot();
            applyChartColor(plot, chartColor, null);

            ChartPanel chartPanel = new ChartPanel(chart);
            JFrame frame = new JFrame("Biểu Đồ Doanh Thu");
            frame.setContentPane(chartPanel);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(this);
            frame.setVisible(true);
        }
    }

    private void applyChartColor(Object plot, String chartColor, DefaultPieDataset dataset) {
        Color color;
        switch (chartColor) {
            case "Xanh":
                color = Color.BLUE;
                break;
            case "Đỏ":
                color = Color.RED;
                break;
            case "Xanh Lá":
                color = Color.GREEN;
                break;
            default:
                color = Color.GRAY; // Mặc định
                break;
        }
        if (plot instanceof CategoryPlot) {
            CategoryPlot categoryPlot = (CategoryPlot) plot;
            categoryPlot.getRenderer().setSeriesPaint(0, color);
        } else if (plot instanceof PiePlot && dataset != null) {
            PiePlot piePlot = (PiePlot) plot;
            for (int i = 0; i < dataset.getItemCount(); i++) {
                piePlot.setSectionPaint(dataset.getKey(i), color);
            }
        }
    }

    private void showMostPurchasedArtwork() {
        try {
            Map<Integer, Long> artworkCount = new HashMap<>();
            for (Ticket ticket : filteredTickets) {
                List<Artwork> artworks = artworkController.getArtworksByTourId(ticket.getTourId());
                for (Artwork artwork : artworks) {
                    artworkCount.merge(artwork.getArtworkId(), 1L, Long::sum);
                }
            }
            if (artworkCount.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không có tác phẩm nào được mua!", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            Map.Entry<Integer, Long> mostPurchased = artworkCount.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .orElse(null);
            if (mostPurchased != null) {
                Artwork artwork = artworkController.findArtworkById(mostPurchased.getKey());
                if (artwork != null) {
                    JOptionPane.showMessageDialog(this,
                            String.format("Tác phẩm được mua nhiều nhất: %s (Số lần: %d)", artwork.getTitle(), mostPurchased.getValue()),
                            "Tác Phẩm Phổ Biến", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy tác phẩm với ID: " + mostPurchased.getKey(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showMostPurchasedTour() {
        try {
            Map<Integer, Long> tourCount = new HashMap<>();
            for (Ticket ticket : filteredTickets) {
                tourCount.merge(ticket.getTourId(), 1L, Long::sum);
            }
            if (tourCount.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không có tour nào được mua!", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            Map.Entry<Integer, Long> mostPurchased = tourCount.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .orElse(null);
            if (mostPurchased != null) {
                VirtualTour tour = virtualTourController.findVirtualTourById(mostPurchased.getKey());
                if (tour != null) {
                    JOptionPane.showMessageDialog(this,
                            String.format("Tour được mua nhiều nhất: %s (Số lần: %d)", tour.getTitle(), mostPurchased.getValue()),
                            "Tour Phổ Biến", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy tour với ID: " + mostPurchased.getKey(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Không xác định được tour được mua nhiều nhất!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportToCSV() {
        try {
            String filePath = "thongke_tickets_" + System.currentTimeMillis() + ".csv";
            try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
                writer.writeNext(new String[]{"ID", "User ID", "Tour ID", "Price", "Purchase Date", "Status"});
                for (Ticket ticket : filteredTickets) {
                    writer.writeNext(new String[]{
                            String.valueOf(ticket.getTicketId()),
                            String.valueOf(ticket.getUserId()),
                            String.valueOf(ticket.getTourId()),
                            String.valueOf(ticket.getPrice()),
                            ticket.getVisitDate().toString(),
                            ticket.getStatus()
                    });
                }
            }
            JOptionPane.showMessageDialog(this, "Xuất báo cáo CSV thành công: " + filePath, "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xuất báo cáo CSV: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportToPDF() {
        try {
            String filePath = "thongke_tickets_" + System.currentTimeMillis() + ".pdf";
            PdfWriter writer = new PdfWriter(new FileOutputStream(filePath));
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("BÁO CÁO THỐNG KÊ VÉ").setFontSize(16).setBold());
            document.add(new Paragraph("Tổng Doanh Thu: " + totalRevenueLabel.getText()).setFontSize(14));

            float[] columnWidths = {50, 50, 50, 50, 100, 50};
            Table table = new Table(columnWidths);
            table.addHeaderCell("ID");
            table.addHeaderCell("User ID");
            table.addHeaderCell("Tour ID");
            table.addHeaderCell("Price");
            table.addHeaderCell("Purchase Date");
            table.addHeaderCell("Status");

            for (Ticket ticket : filteredTickets) {
                table.addCell(String.valueOf(ticket.getTicketId()));
                table.addCell(String.valueOf(ticket.getUserId()));
                table.addCell(String.valueOf(ticket.getTourId()));
                table.addCell(String.valueOf(ticket.getPrice()));
                table.addCell(ticket.getVisitDate().toString());
                table.addCell(ticket.getStatus());
            }

            document.add(table);
            document.close();
            JOptionPane.showMessageDialog(this, "Xuất báo cáo PDF thành công: " + filePath, "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xuất báo cáo PDF: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sendEmailWithReport() {
        try {
            // Export PDF first
            String filePath = "thongke_tickets_" + System.currentTimeMillis() + ".pdf";
            PdfWriter writer = new PdfWriter(new FileOutputStream(filePath));
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("BÁO CÁO THỐNG KÊ VÉ").setFontSize(16).setBold());
            document.add(new Paragraph("Tổng Doanh Thu: " + totalRevenueLabel.getText()).setFontSize(14));

            float[] columnWidths = {50, 50, 50, 50, 100, 50};
            Table table = new Table(columnWidths);
            table.addHeaderCell("ID");
            table.addHeaderCell("User ID");
            table.addHeaderCell("Tour ID");
            table.addHeaderCell("Price");
            table.addHeaderCell("Purchase Date");
            table.addHeaderCell("Status");

            for (Ticket ticket : filteredTickets) {
                table.addCell(String.valueOf(ticket.getTicketId()));
                table.addCell(String.valueOf(ticket.getUserId()));
                table.addCell(String.valueOf(ticket.getTourId()));
                table.addCell(String.valueOf(ticket.getPrice()));
                table.addCell(ticket.getVisitDate().toString());
                table.addCell(ticket.getStatus());
            }

            document.add(table);
            document.close();

            // Email configuration
            String toEmail = JOptionPane.showInputDialog(this, "Nhập email người nhận:");
            if (toEmail == null || toEmail.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Email không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String fromEmail = "your-email@gmail.com"; // Thay bằng email của bạn
            String password = "your-app-password"; // Thay bằng mật khẩu ứng dụng của bạn

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, password);
                }
            });

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Báo Cáo Thống Kê Vé - " + LocalDate.now().toString());

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("Kính gửi,\n\nĐính kèm là báo cáo thống kê vé. Vui lòng kiểm tra.\n\nTrân trọng,\nHệ thống SoulVirArt");

            // Create the attachment part
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(new File(filePath));

            // Create multipart message
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);

            // Send the email
            Transport.send(message);

            JOptionPane.showMessageDialog(this, "Email đã được gửi thành công!", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);

            // Delete the temporary PDF file
            new File(filePath).delete();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi gửi email: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void previousPage() {
        if (currentPage > 1) {
            currentPage--;
            updateTableWithPagination();
        }
    }

    private void nextPage() {
        int maxPage = (int) Math.ceil((double) filteredTickets.size() / pageSize);
        if (currentPage < maxPage) {
            currentPage++;
            updateTableWithPagination();
        }
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(new Color(25, 25, 112));
        return label;
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