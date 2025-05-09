package com.soulvirart.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainView extends JFrame {

    private CardLayout cardLayout;
    private JPanel contentPanel;
    private UserView userView;
    private ExhibitionView exhibitionView;
    private ArtistView artistView; // Thêm ArtistView
    private TicketView ticketView;
    private ThongkeView thongkeView;
    private NotificationView notificationView;
    private ActivityLogView activityLogView;

    public MainView() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("SoulVirArt - Giao diện chính");
        setSize(1200, 800); // Tăng kích thước để giao diện thoáng hơn
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // Sidebar (left panel)
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(26, 36, 47));
        sidebar.setPreferredSize(new Dimension(220, getHeight())); // Tăng chiều rộng sidebar
        sidebar.setBorder(BorderFactory.createEmptyBorder(30, 15, 30, 15)); // Tăng padding

        // Logo
        JLabel sidebarTitle = new JLabel("SoulVirArt");
        sidebarTitle.setFont(new Font("Arial", Font.BOLD, 22));
        sidebarTitle.setForeground(Color.WHITE);
        sidebarTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(sidebarTitle);
        sidebar.add(Box.createRigidArea(new Dimension(0, 40))); // Tăng khoảng cách

        // Sidebar buttons
        JButton dashboardButton = createSidebarButton("Trang chủ", "/icons/DashboardMain.png");
        JButton userButton = createSidebarButton("Người dùng", "/icons/Dashboard3.png");
        JButton artistButton = createSidebarButton("Nghệ sĩ", "/icons/artist.png");
        JButton exhibitionButton = createSidebarButton("Triển lãm", "/icons/Exhibitions.png");
        JButton ticketButton = createSidebarButton("Vé", "/icons/Ticket1.png");
        JButton thongkeButton = createSidebarButton("Thống kê", "/icons/Thongke1.png");
        JButton notificationButton = createSidebarButton("Thông báo", "/icons/Thongbao1.png");
        JButton activityLogButton = createSidebarButton("Nhật ký", "/icons/Nhatki.png");

        sidebar.add(dashboardButton);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15))); // Tăng khoảng cách giữa các nút
        sidebar.add(userButton);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        sidebar.add(artistButton); // Đặt giữa userButton và exhibitionButton
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        sidebar.add(exhibitionButton);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        sidebar.add(ticketButton);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        sidebar.add(thongkeButton);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        sidebar.add(notificationButton);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        sidebar.add(activityLogButton);

        // Main content area
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(Color.WHITE);
        mainContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header in main content
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        JLabel titleLabel = new JLabel("Chào mừng đến với SoulVirArt!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28)); // Tăng kích thước chữ
        titleLabel.setForeground(new Color(26, 36, 47));
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        JButton logoutButton = createStyledButton("Đăng xuất", "/icons/out.png");
        logoutButton.setBackground(new Color(220, 20, 60));
        logoutButton.setPreferredSize(new Dimension(120, 40));
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.setBackground(Color.WHITE);
        logoutPanel.add(logoutButton);
        headerPanel.add(logoutPanel, BorderLayout.EAST);

        mainContent.add(headerPanel, BorderLayout.NORTH);

        // Content panel with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);

        // Dashboard panel (default view)
        JPanel dashboardPanel = new JPanel(new BorderLayout());
        JLabel dashboardLabel = new JLabel("Chào mừng đến với Dashboard!", SwingConstants.CENTER);
        dashboardLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        dashboardLabel.setForeground(Color.GRAY);
        dashboardPanel.add(dashboardLabel, BorderLayout.CENTER);

        // Initialize views with try-catch
        try {
            userView = new UserView();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khởi tạo UserView: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        try {
            exhibitionView = new ExhibitionView();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khởi tạo ExhibitionView: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        try {
            artistView = new ArtistView();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khởi tạo ArtistView: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        try {
            ticketView = new TicketView();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khởi tạo TicketView: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        try {
            thongkeView = new ThongkeView();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khởi tạo ThongkeView: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        try {
            notificationView = new NotificationView();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khởi tạo NotificationView: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        try {
            activityLogView = new ActivityLogView();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khởi tạo ActivityLogView: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        // Add panels to CardLayout
        contentPanel.add(dashboardPanel, "Dashboard");
        if (userView != null) contentPanel.add(userView, "UserView");
        if (artistView != null) contentPanel.add(artistView, "ArtistView");
        if (exhibitionView != null) contentPanel.add(exhibitionView, "ExhibitionView");
        if (ticketView != null) contentPanel.add(ticketView, "TicketView");
        if (thongkeView != null) contentPanel.add(thongkeView, "ThongkeView");
        if (notificationView != null) contentPanel.add(notificationView, "NotificationView");
        if (activityLogView != null) contentPanel.add(activityLogView, "ActivityLogView");

        mainContent.add(contentPanel, BorderLayout.CENTER);

        // Add components to frame
        add(sidebar, BorderLayout.WEST);
        add(mainContent, BorderLayout.CENTER);

        // Hiển thị Dashboard mặc định
        cardLayout.show(contentPanel, "Dashboard");

        // Action listeners for switching panels and loading data
        dashboardButton.addActionListener(e -> cardLayout.show(contentPanel, "Dashboard"));
        userButton.addActionListener(e -> {
            if (userView != null) {
                cardLayout.show(contentPanel, "UserView");
                userView.loadData();
            } else {
                JOptionPane.showMessageDialog(this, "UserView không khả dụng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        artistButton.addActionListener(e -> {
            if (artistView != null) {
                cardLayout.show(contentPanel, "ArtistView");
                artistView.loadData();
            } else {
                JOptionPane.showMessageDialog(this, "ArtistView không khả dụng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        exhibitionButton.addActionListener(e -> {
            if (exhibitionView != null) {
                cardLayout.show(contentPanel, "ExhibitionView");
                exhibitionView.loadData();
            } else {
                JOptionPane.showMessageDialog(this, "ExhibitionView không khả dụng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        ticketButton.addActionListener(e -> {
            if (ticketView != null) {
                cardLayout.show(contentPanel, "TicketView");
                ticketView.loadData();
            } else {
                JOptionPane.showMessageDialog(this, "TicketView không khả dụng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        thongkeButton.addActionListener(e -> {
            if (thongkeView != null) {
                cardLayout.show(contentPanel, "ThongkeView");
                thongkeView.loadData();
            } else {
                JOptionPane.showMessageDialog(this, "ThongkeView không khả dụng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        notificationButton.addActionListener(e -> {
            if (notificationView != null) {
                cardLayout.show(contentPanel, "NotificationView");
                notificationView.loadData();
            } else {
                JOptionPane.showMessageDialog(this, "NotificationView không khả dụng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        activityLogButton.addActionListener(e -> {
            if (activityLogView != null) {
                cardLayout.show(contentPanel, "ActivityLogView");
                activityLogView.loadData();
            } else {
                JOptionPane.showMessageDialog(this, "ActivityLogView không khả dụng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginView().setVisible(true);
        });

        setVisible(true);
    }

    private JButton createSidebarButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 16)); // Tăng kích thước chữ
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(26, 36, 47));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20)); // Tăng padding
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50)); // Tăng chiều cao nút
        button.setHorizontalAlignment(SwingConstants.LEFT); // Căn trái văn bản và icon

        // Hiệu ứng hover mượt mà
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(new Color(45, 55, 72));
                button.setForeground(new Color(135, 206, 250)); // Đổi màu chữ khi hover
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                button.setBackground(new Color(26, 36, 47));
                button.setForeground(Color.WHITE);
            }
        });

        // Thêm xử lý lỗi khi tải icon
        if (iconPath != null) {
            try {
                ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
                Image scaledImage = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH); // Tăng kích thước icon
                button.setIcon(new ImageIcon(scaledImage));
                button.setHorizontalTextPosition(SwingConstants.RIGHT);
                button.setIconTextGap(10); // Tăng khoảng cách giữa icon và text
            } catch (Exception e) {
                System.err.println("Không thể tải icon: " + iconPath + " - Lỗi: " + e.getMessage());
                button.setIcon(null);
            }
        }
        return button;
    }

    private JButton createStyledButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        // Hiệu ứng hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(new Color(200, 20, 50));
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                button.setBackground(new Color(220, 20, 60));
            }
        });
        // Thêm xử lý lỗi khi tải icon
        if (iconPath != null) {
            try {
                ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
                Image scaledImage = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                button.setIcon(new ImageIcon(scaledImage));
                button.setHorizontalTextPosition(SwingConstants.RIGHT);
                button.setIconTextGap(5);
            } catch (Exception e) {
                System.err.println("Không thể tải icon: " + iconPath + " - Lỗi: " + e.getMessage());
                button.setIcon(null);
            }
        }
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainView();
        });
    }
}