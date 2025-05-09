package com.soulvirart.view;

import com.soulvirart.controller.ArtistController;
import com.soulvirart.controller.ArtworkController;
import com.soulvirart.controller.ExhibitionController;
import com.soulvirart.controller.VirtualTourController;
import com.soulvirart.controller.ArtworkTourController;
import com.soulvirart.model.Artist;
import com.soulvirart.model.Artwork;
import com.soulvirart.model.Exhibition;
import com.soulvirart.model.VirtualTour;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class ExhibitionView extends JPanel {
    private final ExhibitionController exhibitionController;
    private final ArtworkController artworkController;
    private final VirtualTourController virtualTourController;
    private final ArtworkTourController artworkTourController;
    private final ArtistController artistController;
    private Exhibition selectedExhibition;
    private Artwork selectedArtwork;
    private VirtualTour selectedTour;

    // Tab 1: Artworks
    private JPanel artworkPanel;
    private JTextField artworkTitleField, artworkDescriptionField, artworkCreationDateField, artworkImagePathField;
    private JComboBox<Artist> artworkArtistComboBox;
    private JTextField artworkSearchField, artistIdFilterField;
    private JButton prevButton, nextButton;
    private JLabel artworkPageLabel;
    private int artworkPage = 1;
    private final int ARTWORKS_PER_PAGE = 8;

    // Tab 2: Exhibitions
    private JTable exhibitionTable;
    private DefaultTableModel exhibitionTableModel;
    private JTextField exhibitionTitleField, exhibitionDescriptionField, exhibitionStartDateField, exhibitionEndDateField, exhibitionStatusField;
    private JTextField exhibitionMinDateField, exhibitionMaxDateField, exhibitionStatusFilterField;
    private JList<Artwork> artworkSelectionList;
    private DefaultListModel<Artwork> artworkSelectionListModel;
    private JTextField exhibitionSearchField;
    private JButton exhibitionPrevButton, exhibitionNextButton;
    private JLabel exhibitionPageLabel;
    private int exhibitionPage = 1;
    private final int EXHIBITIONS_PER_PAGE = 10;

    // Tab 3: Virtual Tours
    private JList<VirtualTour> tourList;
    private DefaultListModel<VirtualTour> tourListModel;
    private JTextField tourTitleField, tourDescriptionField, tourPriceField;
    private JTextField tourMinPriceField, tourMaxPriceField, tourMinArtworksField;
    private JList<Artwork> tourArtworkSelectionList;
    private DefaultListModel<Artwork> tourArtworkSelectionListModel;
    private JList<Artwork> tourDetailArtworkList;
    private DefaultListModel<Artwork> tourDetailArtworkListModel;
    private JTextField tourSearchField;
    private JButton tourPrevButton, tourNextButton, tourUpButton, tourDownButton;
    private JLabel tourPageLabel;
    private int tourPage = 1;
    private final int TOURS_PER_PAGE = 10;

    private ResourceBundle messages;

    public ExhibitionView() {
        this.exhibitionController = new ExhibitionController();
        this.artworkController = new ArtworkController();
        this.virtualTourController = new VirtualTourController();
        this.artworkTourController = new ArtworkTourController();
        this.artistController = new ArtistController();
        try {
            messages = ResourceBundle.getBundle("messages");
        } catch (Exception e) {
            System.err.println("Không thể tải ResourceBundle: " + e.getMessage());
            messages = ResourceBundle.getBundle("messages", new Locale("en"));
        }
        initializeUI();
        loadData();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 242, 245));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Roboto", Font.BOLD, 14));
        tabbedPane.setBackground(new Color(70, 130, 180));
        tabbedPane.setForeground(Color.WHITE);
        tabbedPane.setOpaque(true);

        tabbedPane.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override
            protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(x, y, new Color(135, 206, 250), x, y + h, new Color(70, 130, 180)));
                g2.fillRect(x, y, w, h);
            }
        });

        tabbedPane.addTab(messages.getString("tab.artworks"), createArtworkTab());
        tabbedPane.addTab(messages.getString("tab.exhibitions"), createExhibitionTab());
        tabbedPane.addTab(messages.getString("tab.virtualTours"), createVirtualTourTab());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createArtworkTab() {
        JPanel artworkTab = new JPanel(new BorderLayout(10, 10));
        artworkTab.setBackground(new Color(240, 242, 245));

        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createTitledBorder(messages.getString("artwork.manage")));
        leftPanel.setPreferredSize(new Dimension(300, 0));

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);

        formPanel.add(createStyledLabel(messages.getString("artwork.title")));
        artworkTitleField = createStyledTextField();
        formPanel.add(artworkTitleField);

        formPanel.add(createStyledLabel(messages.getString("artwork.description")));
        artworkDescriptionField = createStyledTextField();
        formPanel.add(artworkDescriptionField);

        formPanel.add(createStyledLabel(messages.getString("artwork.creationDate")));
        artworkCreationDateField = createStyledTextField();
        formPanel.add(artworkCreationDateField);

        formPanel.add(createStyledLabel(messages.getString("artwork.artistId")));
        artworkArtistComboBox = new JComboBox<>();
        artworkArtistComboBox.setFont(new Font("Roboto", Font.PLAIN, 14));
        artworkArtistComboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        formPanel.add(artworkArtistComboBox);

        formPanel.add(createStyledLabel(messages.getString("artwork.imagePath")));
        artworkImagePathField = createStyledTextField();
        formPanel.add(artworkImagePathField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton addButton = createStyledButton(messages.getString("button.add"), "/icons/add.png");
        addButton.addActionListener(e -> addArtwork());

        JButton updateButton = createStyledButton(messages.getString("button.update"), "/icons/edit.png");
        updateButton.addActionListener(e -> updateArtwork());

        JButton deleteButton = createStyledButton(messages.getString("button.delete"), "/icons/delete.png");
        deleteButton.addActionListener(e -> deleteArtwork());

        JButton clearButton = createStyledButton(messages.getString("button.clear"), "/icons/clear.png");
        clearButton.addActionListener(e -> clearArtworkFields());

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        leftPanel.add(formPanel, BorderLayout.CENTER);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBackground(new Color(240, 242, 245));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(new Color(240, 242, 245));
        JLabel searchLabel = createStyledLabel(messages.getString("search"));
        artworkSearchField = createStyledTextField();
        artworkSearchField.setPreferredSize(new Dimension(200, 30));
        artworkSearchField.addActionListener(e -> {
            artworkPage = 1;
            loadArtworks();
        });
        searchPanel.add(searchLabel);
        searchPanel.add(artworkSearchField);

        JButton refreshButton = createStyledButton(messages.getString("button.refresh"), "/icons/refresh.png");
        refreshButton.addActionListener(e -> {
            artworkPage = 1;
            loadArtworks();
        });
        searchPanel.add(refreshButton);

        JLabel filterLabel = createStyledLabel("Filter by Artist ID");
        artistIdFilterField = createStyledTextField();
        artistIdFilterField.setPreferredSize(new Dimension(100, 30));
        JButton filterButton = createStyledButton("Filter", "/icons/filter.png");
        filterButton.addActionListener(e -> {
            artworkPage = 1;
            filterArtworksByArtistId();
        });
        searchPanel.add(filterLabel);
        searchPanel.add(artistIdFilterField);
        searchPanel.add(filterButton);

        artworkPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        artworkPanel.setBackground(new Color(240, 242, 245));
        JScrollPane artworkScrollPane = new JScrollPane(artworkPanel);
        artworkScrollPane.setBorder(BorderFactory.createEmptyBorder());

        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        paginationPanel.setBackground(new Color(240, 242, 245));
        prevButton = createStyledButton(messages.getString("pagination.prev"), "/icons/prev.png");
        prevButton.addActionListener(e -> {
            if (artworkPage > 1) {
                artworkPage--;
                if (artistIdFilterField.getText().trim().isEmpty()) {
                    loadArtworks();
                } else {
                    filterArtworksByArtistId();
                }
            }
        });
        artworkPageLabel = new JLabel("Page 1");
        artworkPageLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
        nextButton = createStyledButton(messages.getString("pagination.next"), "/icons/next.png");
        nextButton.addActionListener(e -> {
            artworkPage++;
            if (artistIdFilterField.getText().trim().isEmpty()) {
                loadArtworks();
            } else {
                filterArtworksByArtistId();
            }
        });
        paginationPanel.add(prevButton);
        paginationPanel.add(artworkPageLabel);
        paginationPanel.add(nextButton);

        rightPanel.add(searchPanel, BorderLayout.NORTH);
        rightPanel.add(artworkScrollPane, BorderLayout.CENTER);
        rightPanel.add(paginationPanel, BorderLayout.SOUTH);

        artworkTab.add(leftPanel, BorderLayout.WEST);
        artworkTab.add(rightPanel, BorderLayout.CENTER);

        return artworkTab;
    }

    private JPanel createExhibitionTab() {
        JPanel exhibitionTab = new JPanel(new BorderLayout(10, 10));
        exhibitionTab.setBackground(new Color(240, 242, 245));

        // Khu vực tìm kiếm và lọc
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(new Color(240, 242, 245));
        JLabel searchLabel = createStyledLabel(messages.getString("search"));
        exhibitionSearchField = createStyledTextField();
        exhibitionSearchField.setPreferredSize(new Dimension(200, 30));
        searchPanel.add(searchLabel);
        searchPanel.add(exhibitionSearchField);

        JLabel minDateLabel = createStyledLabel("Min Date");
        exhibitionMinDateField = createStyledTextField();
        exhibitionMinDateField.setPreferredSize(new Dimension(100, 30));
        searchPanel.add(minDateLabel);
        searchPanel.add(exhibitionMinDateField);

        JLabel maxDateLabel = createStyledLabel("Max Date");
        exhibitionMaxDateField = createStyledTextField();
        exhibitionMaxDateField.setPreferredSize(new Dimension(100, 30));
        searchPanel.add(maxDateLabel);
        searchPanel.add(exhibitionMaxDateField);

        JLabel statusLabel = createStyledLabel("Status");
        exhibitionStatusFilterField = createStyledTextField();
        exhibitionStatusFilterField.setPreferredSize(new Dimension(100, 30));
        searchPanel.add(statusLabel);
        searchPanel.add(exhibitionStatusFilterField);

        JButton filterButton = createStyledButton("Filter", "/icons/filter.png");
        filterButton.addActionListener(e -> {
            exhibitionPage = 1;
            loadExhibitions();
        });
        searchPanel.add(filterButton);

        JButton refreshButton = createStyledButton(messages.getString("button.refresh"), "/icons/refresh.png");
        refreshButton.addActionListener(e -> {
            exhibitionPage = 1;
            exhibitionSearchField.setText("");
            exhibitionMinDateField.setText("");
            exhibitionMaxDateField.setText("");
            exhibitionStatusFilterField.setText("");
            loadExhibitions();
        });
        searchPanel.add(refreshButton);

        // Khu vực bảng triển lãm
        String[] columnNames = {
                messages.getString("exhibition.id"),
                messages.getString("exhibition.title"),
                messages.getString("exhibition.description"),
                messages.getString("exhibition.startDate"),
                messages.getString("exhibition.endDate"),
                messages.getString("exhibition.status")
        };
        exhibitionTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        exhibitionTable = new JTable(exhibitionTableModel);
        exhibitionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        exhibitionTable.getTableHeader().setBackground(new Color(135, 206, 250));
        exhibitionTable.getTableHeader().setForeground(Color.WHITE);
        exhibitionTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = exhibitionTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int exhibitionId = (int) exhibitionTableModel.getValueAt(selectedRow, 0);
                    try {
                        selectedExhibition = exhibitionController.getExhibitionById(exhibitionId);
                        exhibitionTitleField.setText(selectedExhibition.getTitle());
                        exhibitionDescriptionField.setText(selectedExhibition.getDescription());
                        exhibitionStartDateField.setText(selectedExhibition.getStartDate() != null ? selectedExhibition.getStartDate().toString() : "");
                        exhibitionEndDateField.setText(selectedExhibition.getEndDate() != null ? selectedExhibition.getEndDate().toString() : "");
                        exhibitionStatusField.setText(selectedExhibition.getStatus());
                        loadArtworksForExhibition();
                    } catch (Exception ex) {
                        showErrorDialog(messages.getString("error.loadExhibition") + " " + ex.getMessage());
                    }
                }
            }
        });

        JScrollPane tableScrollPane = new JScrollPane(exhibitionTable);
        tableScrollPane.setPreferredSize(new Dimension(0, 200));

        // Phân trang
        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        paginationPanel.setBackground(new Color(240, 242, 245));
        exhibitionPrevButton = createStyledButton(messages.getString("pagination.prev"), "/icons/prev.png");
        exhibitionPrevButton.addActionListener(e -> {
            if (exhibitionPage > 1) {
                exhibitionPage--;
                loadExhibitions();
            }
        });
        exhibitionPageLabel = new JLabel("Page 1");
        exhibitionPageLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
        exhibitionNextButton = createStyledButton(messages.getString("pagination.next"), "/icons/next.png");
        exhibitionNextButton.addActionListener(e -> {
            exhibitionPage++;
            loadExhibitions();
        });
        paginationPanel.add(exhibitionPrevButton);
        paginationPanel.add(exhibitionPageLabel);
        paginationPanel.add(exhibitionNextButton);

        // Khu vực quản lý triển lãm
        JPanel formPanel = new JPanel(new BorderLayout(10, 10));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createTitledBorder(messages.getString("exhibition.manage")));
        formPanel.setPreferredSize(new Dimension(300, 0));

        JPanel fieldsPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        fieldsPanel.setBackground(Color.WHITE);

        fieldsPanel.add(createStyledLabel(messages.getString("exhibition.title")));
        exhibitionTitleField = createStyledTextField();
        fieldsPanel.add(exhibitionTitleField);

        fieldsPanel.add(createStyledLabel(messages.getString("exhibition.description")));
        exhibitionDescriptionField = createStyledTextField();
        fieldsPanel.add(exhibitionDescriptionField);

        fieldsPanel.add(createStyledLabel(messages.getString("exhibition.startDate")));
        exhibitionStartDateField = createStyledTextField();
        fieldsPanel.add(exhibitionStartDateField);

        fieldsPanel.add(createStyledLabel(messages.getString("exhibition.endDate")));
        exhibitionEndDateField = createStyledTextField();
        fieldsPanel.add(exhibitionEndDateField);

        fieldsPanel.add(createStyledLabel(messages.getString("exhibition.status")));
        exhibitionStatusField = createStyledTextField();
        fieldsPanel.add(exhibitionStatusField);

        artworkSelectionListModel = new DefaultListModel<>();
        artworkSelectionList = new JList<>(artworkSelectionListModel);
        artworkSelectionList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        artworkSelectionList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Artwork art) {
                    label.setText(art.getTitle());
                }
                return label;
            }
        });
        JScrollPane artworkListScrollPane = new JScrollPane(artworkSelectionList);
        artworkListScrollPane.setBorder(BorderFactory.createTitledBorder(messages.getString("exhibition.selectArtworks")));
        fieldsPanel.add(createStyledLabel(messages.getString("artwork.list")));
        fieldsPanel.add(artworkListScrollPane);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton addButton = createStyledButton(messages.getString("button.add"), "/icons/add.png");
        addButton.addActionListener(e -> addExhibition());

        JButton updateButton = createStyledButton(messages.getString("button.update"), "/icons/edit.png");
        updateButton.addActionListener(e -> updateExhibition());

        JButton deleteButton = createStyledButton(messages.getString("button.delete"), "/icons/delete.png");
        deleteButton.addActionListener(e -> deleteExhibition());

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        formPanel.add(fieldsPanel, BorderLayout.CENTER);
        formPanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(tableScrollPane, BorderLayout.CENTER);
        centerPanel.add(paginationPanel, BorderLayout.SOUTH);

        exhibitionTab.add(searchPanel, BorderLayout.NORTH);
        exhibitionTab.add(centerPanel, BorderLayout.CENTER);
        exhibitionTab.add(formPanel, BorderLayout.EAST);

        return exhibitionTab;
    }

    private JPanel createVirtualTourTab() {
        JPanel tourTab = new JPanel(new BorderLayout(10, 10));
        tourTab.setBackground(new Color(240, 242, 245));

        // Khu vực tìm kiếm và lọc
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(new Color(240, 242, 245));
        JLabel searchLabel = createStyledLabel(messages.getString("search"));
        tourSearchField = createStyledTextField();
        tourSearchField.setPreferredSize(new Dimension(200, 30));
        searchPanel.add(searchLabel);
        searchPanel.add(tourSearchField);

        JLabel minPriceLabel = createStyledLabel("Min Price");
        tourMinPriceField = createStyledTextField();
        tourMinPriceField.setPreferredSize(new Dimension(80, 30));
        searchPanel.add(minPriceLabel);
        searchPanel.add(tourMinPriceField);

        JLabel maxPriceLabel = createStyledLabel("Max Price");
        tourMaxPriceField = createStyledTextField();
        tourMaxPriceField.setPreferredSize(new Dimension(80, 30));
        searchPanel.add(maxPriceLabel);
        searchPanel.add(tourMaxPriceField);

        JLabel minArtworksLabel = createStyledLabel("Min Artworks");
        tourMinArtworksField = createStyledTextField();
        tourMinArtworksField.setPreferredSize(new Dimension(80, 30));
        searchPanel.add(minArtworksLabel);
        searchPanel.add(tourMinArtworksField);

        JButton filterButton = createStyledButton("Filter", "/icons/filter.png");
        filterButton.addActionListener(e -> {
            tourPage = 1;
            loadVirtualTours();
        });
        searchPanel.add(filterButton);

        JButton refreshButton = createStyledButton(messages.getString("button.refresh"), "/icons/refresh.png");
        refreshButton.addActionListener(e -> {
            tourPage = 1;
            tourSearchField.setText("");
            tourMinPriceField.setText("");
            tourMaxPriceField.setText("");
            tourMinArtworksField.setText("");
            loadVirtualTours();
        });
        searchPanel.add(refreshButton);

        // Khu vực danh sách chuyến tham quan
        tourListModel = new DefaultListModel<>();
        tourList = new JList<>(tourListModel);
        tourList.setCellRenderer(new TourListCellRenderer());
        tourList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectedTour = tourList.getSelectedValue();
                if (selectedTour != null && !selectedTour.getTitle().equals(messages.getString("noData"))) {
                    tourTitleField.setText(selectedTour.getTitle());
                    tourDescriptionField.setText(selectedTour.getDescription());
                    tourPriceField.setText(String.valueOf(selectedTour.getPrice()));
                    loadArtworksForTourSelection();
                    loadArtworksForTourDetail();
                }
            }
        });
        JScrollPane tourScrollPane = new JScrollPane(tourList);
        tourScrollPane.setBorder(BorderFactory.createTitledBorder(messages.getString("virtualTour.list")));
        tourScrollPane.setPreferredSize(new Dimension(300, 0));

        // Phân trang
        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        paginationPanel.setBackground(new Color(240, 242, 245));
        tourPrevButton = createStyledButton(messages.getString("pagination.prev"), "/icons/prev.png");
        tourPrevButton.addActionListener(e -> {
            if (tourPage > 1) {
                tourPage--;
                loadVirtualTours();
            }
        });
        tourPageLabel = new JLabel("Page 1");
        tourPageLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
        tourNextButton = createStyledButton(messages.getString("pagination.next"), "/icons/next.png");
        tourNextButton.addActionListener(e -> {
            tourPage++;
            loadVirtualTours();
        });
        paginationPanel.add(tourPrevButton);
        paginationPanel.add(tourPageLabel);
        paginationPanel.add(tourNextButton);

        // Khu vực chi tiết chuyến tham quan
        JPanel detailPanel = new JPanel(new BorderLayout(10, 10));
        detailPanel.setBorder(BorderFactory.createTitledBorder("Tour Details"));
        detailPanel.setBackground(new Color(240, 242, 245));

        JLabel imageLabel = new JLabel(messages.getString("loading"), SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(300, 200));
        imageLabel.setBackground(new Color(220, 220, 220));
        imageLabel.setOpaque(true);
        detailPanel.add(imageLabel, BorderLayout.NORTH);

        tourDetailArtworkListModel = new DefaultListModel<>();
        tourDetailArtworkList = new JList<>(tourDetailArtworkListModel);
        tourDetailArtworkList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Artwork art) {
                    label.setText(art.getTitle());
                }
                return label;
            }
        });
        JScrollPane detailScrollPane = new JScrollPane(tourDetailArtworkList);
        detailScrollPane.setBorder(BorderFactory.createTitledBorder("Artwork List"));
        detailPanel.add(detailScrollPane, BorderLayout.CENTER);

        // Khu vực quản lý chuyến tham quan
        JPanel formPanel = new JPanel(new BorderLayout(10, 10));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createTitledBorder(messages.getString("virtualTour.manage")));
        formPanel.setPreferredSize(new Dimension(300, 0));

        JPanel fieldsPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        fieldsPanel.setBackground(Color.WHITE);

        fieldsPanel.add(createStyledLabel(messages.getString("virtualTour.title")));
        tourTitleField = createStyledTextField();
        fieldsPanel.add(tourTitleField);

        fieldsPanel.add(createStyledLabel(messages.getString("virtualTour.description")));
        tourDescriptionField = createStyledTextField();
        fieldsPanel.add(tourDescriptionField);

        fieldsPanel.add(createStyledLabel(messages.getString("virtualTour.price")));
        tourPriceField = createStyledTextField();
        fieldsPanel.add(tourPriceField);

        tourArtworkSelectionListModel = new DefaultListModel<>();
        tourArtworkSelectionList = new JList<>(tourArtworkSelectionListModel);
        tourArtworkSelectionList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tourArtworkSelectionList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Artwork art) {
                    label.setText(art.getTitle());
                }
                return label;
            }
        });
        JScrollPane artworkListScrollPane = new JScrollPane(tourArtworkSelectionList);
        artworkListScrollPane.setBorder(BorderFactory.createTitledBorder(messages.getString("virtualTour.selectArtworks")));
        fieldsPanel.add(createStyledLabel(messages.getString("artwork.list")));
        fieldsPanel.add(artworkListScrollPane);

        JPanel reorderPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        reorderPanel.setBackground(Color.WHITE);
        tourUpButton = createStyledButton("Up", "/icons/up.png");
        tourUpButton.addActionListener(e -> reorderArtworks(-1));
        tourDownButton = createStyledButton("Down", "/icons/down.png");
        tourDownButton.addActionListener(e -> reorderArtworks(1));
        reorderPanel.add(tourUpButton);
        reorderPanel.add(tourDownButton);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton addButton = createStyledButton(messages.getString("button.addTour"), "/icons/add.png");
        addButton.addActionListener(e -> addVirtualTour());

        JButton updateButton = createStyledButton(messages.getString("button.updateTour"), "/icons/edit.png");
        updateButton.addActionListener(e -> updateVirtualTour());

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);

        formPanel.add(fieldsPanel, BorderLayout.CENTER);
        formPanel.add(reorderPanel, BorderLayout.SOUTH);
        formPanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(detailPanel, BorderLayout.CENTER);
        centerPanel.add(paginationPanel, BorderLayout.SOUTH);

        tourTab.add(searchPanel, BorderLayout.NORTH);
        tourTab.add(tourScrollPane, BorderLayout.WEST);
        tourTab.add(centerPanel, BorderLayout.CENTER);
        tourTab.add(formPanel, BorderLayout.EAST);

        return tourTab;
    }

    public void refreshArtists() {
        loadArtistsIntoComboBox();
    }

    private void loadArtistsIntoComboBox() {
        SwingWorker<List<Artist>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Artist> doInBackground() throws Exception {
                return artistController.getAllArtists(1, Integer.MAX_VALUE);
            }

            @Override
            protected void done() {
                try {
                    List<Artist> artists = get();
                    artworkArtistComboBox.removeAllItems();
                    for (Artist artist : artists) {
                        artworkArtistComboBox.addItem(artist);
                    }
                    artworkArtistComboBox.setRenderer(new DefaultListCellRenderer() {
                        @Override
                        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                            if (value instanceof Artist artist) {
                                setText(artist.getName() + " (ID: " + artist.getArtistId() + ")");
                            }
                            return this;
                        }
                    });
                } catch (Exception e) {
                    showErrorDialog(messages.getString("error.loadArtists") + " " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void loadArtworks() {
        artworkPanel.removeAll();
        JProgressBar spinner = createLoadingSpinner();
        artworkPanel.add(spinner);
        artworkPanel.revalidate();
        artworkPanel.repaint();

        SwingWorker<List<Artwork>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Artwork> doInBackground() throws Exception {
                String searchQuery = artworkSearchField.getText().trim();
                return artworkController.getArtworksBySearch(searchQuery, artworkPage, ARTWORKS_PER_PAGE);
            }

            @Override
            protected void done() {
                try {
                    List<Artwork> artworks = get();
                    artworkPanel.removeAll();
                    if (artworks.isEmpty()) {
                        JLabel noDataLabel = new JLabel(messages.getString("noData"), SwingConstants.CENTER);
                        noDataLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
                        noDataLabel.setForeground(Color.GRAY);
                        artworkPanel.add(noDataLabel);
                        prevButton.setEnabled(artworkPage > 1);
                        nextButton.setEnabled(false);
                        artworkPageLabel.setText("Page " + artworkPage);
                    } else {
                        for (int i = 0; i < ARTWORKS_PER_PAGE; i++) {
                            if (i < artworks.size()) {
                                Artwork art = artworks.get(i);
                                JPanel artBox = createArtworkBox(art);
                                artworkPanel.add(artBox);
                            } else {
                                JPanel emptyBox = new JPanel();
                                emptyBox.setBackground(new Color(240, 242, 245));
                                artworkPanel.add(emptyBox);
                            }
                        }
                        prevButton.setEnabled(artworkPage > 1);
                        nextButton.setEnabled(artworks.size() == ARTWORKS_PER_PAGE);
                        artworkPageLabel.setText("Page " + artworkPage);
                    }
                    artworkPanel.revalidate();
                    artworkPanel.repaint();
                } catch (Exception e) {
                    showErrorDialog(messages.getString("error.loadArtworks") + " " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void filterArtworksByArtistId() {
        artworkPanel.removeAll();
        JProgressBar spinner = createLoadingSpinner();
        artworkPanel.add(spinner);
        artworkPanel.revalidate();
        artworkPanel.repaint();

        SwingWorker<List<Artwork>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Artwork> doInBackground() throws Exception {
                try {
                    int artistId = Integer.parseInt(artistIdFilterField.getText().trim());
                    return artworkController.getArtworksByArtistId(artistId, artworkPage, ARTWORKS_PER_PAGE);
                } catch (NumberFormatException e) {
                    return artworkController.getArtworksBySearch("", artworkPage, ARTWORKS_PER_PAGE);
                }
            }

            @Override
            protected void done() {
                try {
                    List<Artwork> artworks = get();
                    artworkPanel.removeAll();
                    if (artworks.isEmpty()) {
                        JLabel noDataLabel = new JLabel(messages.getString("noData"), SwingConstants.CENTER);
                        noDataLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
                        noDataLabel.setForeground(Color.GRAY);
                        artworkPanel.add(noDataLabel);
                        prevButton.setEnabled(artworkPage > 1);
                        nextButton.setEnabled(false);
                        artworkPageLabel.setText("Page " + artworkPage);
                    } else {
                        for (int i = 0; i < ARTWORKS_PER_PAGE; i++) {
                            if (i < artworks.size()) {
                                Artwork art = artworks.get(i);
                                JPanel artBox = createArtworkBox(art);
                                artworkPanel.add(artBox);
                            } else {
                                JPanel emptyBox = new JPanel();
                                emptyBox.setBackground(new Color(240, 242, 245));
                                artworkPanel.add(emptyBox);
                            }
                        }
                        prevButton.setEnabled(artworkPage > 1);
                        nextButton.setEnabled(artworks.size() == ARTWORKS_PER_PAGE);
                        artworkPageLabel.setText("Page " + artworkPage);
                    }
                    artworkPanel.revalidate();
                    artworkPanel.repaint();
                } catch (Exception e) {
                    showErrorDialog(messages.getString("error.loadArtworks") + " " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void loadExhibitions() {
        SwingWorker<List<Exhibition>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Exhibition> doInBackground() throws Exception {
                String searchQuery = exhibitionSearchField.getText().trim();
                String minDate = exhibitionMinDateField.getText().trim();
                String maxDate = exhibitionMaxDateField.getText().trim();
                String status = exhibitionStatusFilterField.getText().trim();

                LocalDate min = minDate.isEmpty() ? null : LocalDate.parse(minDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                LocalDate max = maxDate.isEmpty() ? null : LocalDate.parse(maxDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                List<Exhibition> exhibitions = exhibitionController.getExhibitionsBySearch(searchQuery, exhibitionPage, EXHIBITIONS_PER_PAGE);
                List<Exhibition> filteredExhibitions = new ArrayList<>();
                for (Exhibition ex : exhibitions) {
                    boolean matches = true;
                    if (min != null && ex.getStartDate() != null && ex.getStartDate().isBefore(min)) {
                        matches = false;
                    }
                    if (max != null && ex.getEndDate() != null && ex.getEndDate().isAfter(max)) {
                        matches = false;
                    }
                    if (!status.isEmpty() && (ex.getStatus() == null || !ex.getStatus().equalsIgnoreCase(status))) {
                        matches = false;
                    }
                    if (matches) {
                        filteredExhibitions.add(ex);
                    }
                }
                return filteredExhibitions;
            }

            @Override
            protected void done() {
                try {
                    List<Exhibition> exhibitions = get();
                    exhibitionTableModel.setRowCount(0);
                    if (exhibitions.isEmpty()) {
                        exhibitionTableModel.addRow(new Object[]{"", messages.getString("noData"), "", "", "", ""});
                        exhibitionPrevButton.setEnabled(exhibitionPage > 1);
                        exhibitionNextButton.setEnabled(false);
                        exhibitionPageLabel.setText("Page " + exhibitionPage);
                    } else {
                        for (Exhibition ex : exhibitions) {
                            exhibitionTableModel.addRow(new Object[]{
                                    ex.getExhibitionId(),
                                    ex.getTitle(),
                                    ex.getDescription(),
                                    ex.getStartDate() != null ? ex.getStartDate().toString() : null,
                                    ex.getEndDate() != null ? ex.getEndDate().toString() : null,
                                    ex.getStatus()
                            });
                        }
                        exhibitionPrevButton.setEnabled(exhibitionPage > 1);
                        exhibitionNextButton.setEnabled(exhibitions.size() == EXHIBITIONS_PER_PAGE);
                        exhibitionPageLabel.setText("Page " + exhibitionPage);
                    }
                } catch (Exception e) {
                    showErrorDialog(messages.getString("error.loadExhibitions") + " " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void loadVirtualTours() {
        SwingWorker<List<VirtualTour>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<VirtualTour> doInBackground() throws Exception {
                String searchQuery = tourSearchField.getText().trim();
                String minPriceText = tourMinPriceField.getText().trim();
                String maxPriceText = tourMaxPriceField.getText().trim();
                String minArtworksText = tourMinArtworksField.getText().trim();

                double minPrice = minPriceText.isEmpty() ? Double.MIN_VALUE : Double.parseDouble(minPriceText);
                double maxPrice = maxPriceText.isEmpty() ? Double.MAX_VALUE : Double.parseDouble(maxPriceText);
                int minArtworks = minArtworksText.isEmpty() ? 0 : Integer.parseInt(minArtworksText);

                List<VirtualTour> tours = virtualTourController.getVirtualToursBySearch(searchQuery, tourPage, TOURS_PER_PAGE);
                List<VirtualTour> filteredTours = new ArrayList<>();
                for (VirtualTour tour : tours) {
                    List<Artwork> artworks = artworkTourController.getArtworksByTourId(tour.getTourId());
                    int artworkCount = artworks.size();
                    if (tour.getPrice() >= minPrice && tour.getPrice() <= maxPrice && artworkCount >= minArtworks) {
                        filteredTours.add(tour);
                    }
                }
                return filteredTours;
            }

            @Override
            protected void done() {
                try {
                    List<VirtualTour> tours = get();
                    tourListModel.clear();
                    if (tours.isEmpty()) {
                        tourListModel.addElement(new VirtualTour(0, messages.getString("noData"), "", 0.0));
                        tourPrevButton.setEnabled(tourPage > 1);
                        tourNextButton.setEnabled(false);
                        tourPageLabel.setText("Page " + tourPage);
                    } else {
                        for (VirtualTour tour : tours) {
                            tourListModel.addElement(tour);
                        }
                        tourPrevButton.setEnabled(tourPage > 1);
                        tourNextButton.setEnabled(tours.size() == TOURS_PER_PAGE);
                        tourPageLabel.setText("Page " + tourPage);
                    }
                } catch (Exception e) {
                    showErrorDialog(messages.getString("error.loadTours") + " " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void loadArtworksForExhibition() {
        SwingWorker<List<Artwork>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Artwork> doInBackground() throws Exception {
                return artworkController.getAllArtworks(1, 100);
            }

            @Override
            protected void done() {
                try {
                    List<Artwork> artworks = get();
                    artworkSelectionListModel.clear();
                    if (artworks.isEmpty()) {
                        artworkSelectionListModel.addElement(new Artwork(0, messages.getString("noData"), "", null, 0, "", 0));
                    } else {
                        for (Artwork art : artworks) {
                            artworkSelectionListModel.addElement(art);
                        }
                        if (selectedExhibition != null) {
                            List<Artwork> selectedArtworks = artworkController.getArtworksByExhibitionId(selectedExhibition.getExhibitionId(), 1, 100);
                            for (int i = 0; i < artworkSelectionListModel.size(); i++) {
                                if (selectedArtworks.contains(artworkSelectionListModel.get(i))) {
                                    artworkSelectionList.addSelectionInterval(i, i);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    showErrorDialog(messages.getString("error.loadArtworks") + " " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void loadArtworksForTourSelection() {
        SwingWorker<List<Artwork>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Artwork> doInBackground() throws Exception {
                return artworkController.getAllArtworks(1, 100);
            }

            @Override
            protected void done() {
                try {
                    List<Artwork> artworks = get();
                    tourArtworkSelectionListModel.clear();
                    if (artworks.isEmpty()) {
                        tourArtworkSelectionListModel.addElement(new Artwork(0, messages.getString("noData"), "", null, 0, "", 0));
                    } else {
                        for (Artwork art : artworks) {
                            tourArtworkSelectionListModel.addElement(art);
                        }
                        if (selectedTour != null) {
                            List<Artwork> selectedArtworks = artworkTourController.getArtworksByTourId(selectedTour.getTourId());
                            for (int i = 0; i < tourArtworkSelectionListModel.size(); i++) {
                                if (selectedArtworks.contains(tourArtworkSelectionListModel.get(i))) {
                                    tourArtworkSelectionList.addSelectionInterval(i, i);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    showErrorDialog(messages.getString("error.loadArtworks") + " " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void loadArtworksForTourDetail() {
        tourDetailArtworkListModel.clear();
        if (selectedTour == null || selectedTour.getTitle().equals(messages.getString("noData"))) {
            return;
        }

        SwingWorker<List<Artwork>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Artwork> doInBackground() throws Exception {
                return artworkTourController.getArtworksByTourId(selectedTour.getTourId());
            }

            @Override
            protected void done() {
                try {
                    List<Artwork> artworks = get();
                    tourDetailArtworkListModel.clear();
                    if (artworks.isEmpty()) {
                        tourDetailArtworkListModel.addElement(new Artwork(0, messages.getString("noData"), "", null, 0, "", 0));
                    } else {
                        for (Artwork art : artworks) {
                            tourDetailArtworkListModel.addElement(art);
                        }
                        Artwork firstArtwork = artworks.get(0);
                        JLabel imageLabel = (JLabel) ((JPanel) tourDetailArtworkList.getParent().getParent().getComponent(0)).getComponent(0);
                        try {
                            ImageIcon icon = new ImageIcon(getClass().getResource(firstArtwork.getImagePath()));
                            Image scaledImage = icon.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH);
                            imageLabel.setIcon(new ImageIcon(scaledImage));
                            imageLabel.setText("");
                        } catch (Exception e) {
                            imageLabel.setText(messages.getString("artwork.imageError"));
                        }
                    }
                } catch (Exception e) {
                    showErrorDialog(messages.getString("error.loadArtworks") + " " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void reorderArtworks(int direction) {
        if (selectedTour != null) {
            int selectedIndex = tourArtworkSelectionList.getSelectedIndex();
            if (selectedIndex >= 0 && (direction == -1 && selectedIndex > 0 || direction == 1 && selectedIndex < tourArtworkSelectionListModel.size() - 1)) {
                Artwork artwork = tourArtworkSelectionListModel.remove(selectedIndex);
                tourArtworkSelectionListModel.add(selectedIndex + direction, artwork);
                tourArtworkSelectionList.setSelectedIndex(selectedIndex + direction);
                try {
                    List<Artwork> artworks = new ArrayList<>();
                    for (int i = 0; i < tourArtworkSelectionListModel.size(); i++) {
                        artworks.add(tourArtworkSelectionListModel.get(i));
                    }
                    artworkTourController.reorderArtworks(selectedTour.getTourId(), artworks);
                    loadArtworksForTourDetail();
                } catch (Exception e) {
                    showErrorDialog(messages.getString("error.reorderArtworks") + " " + e.getMessage());
                }
            }
        }
    }

    private JPanel createArtworkBox(Artwork artwork) {
        JPanel box = new JPanel(new BorderLayout(5, 5));
        box.setBackground(Color.WHITE);
        box.setPreferredSize(new Dimension(180, 220));
        box.setOpaque(true);
        box.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        box.setCursor(new Cursor(Cursor.HAND_CURSOR));

        box.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                box.setBackground(new Color(245, 245, 245));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                box.setBackground(Color.WHITE);
            }
        });

        JLabel imageLabel = new JLabel(messages.getString("loading"), SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(180, 120));
        imageLabel.setBackground(new Color(220, 220, 220));
        imageLabel.setOpaque(true);
        box.add(imageLabel, BorderLayout.CENTER);

        SwingWorker<ImageIcon, Void> imageWorker = new SwingWorker<>() {
            @Override
            protected ImageIcon doInBackground() throws Exception {
                String imagePath = artwork.getImagePath();
                ImageIcon icon;
                if (imagePath.startsWith("/images/")) {
                    java.net.URL imageURL = getClass().getResource(imagePath);
                    if (imageURL == null) {
                        throw new Exception("Không tìm thấy tài nguyên: " + imagePath);
                    }
                    icon = new ImageIcon(imageURL);
                } else {
                    icon = new ImageIcon(imagePath);
                }
                Image scaledImage = icon.getImage().getScaledInstance(180, 120, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImage);
            }

            @Override
            protected void done() {
                try {
                    ImageIcon scaledIcon = get();
                    imageLabel.setIcon(scaledIcon);
                    imageLabel.setText("");
                } catch (Exception e) {
                    imageLabel.setText(messages.getString("artwork.imageError"));
                }
            }
        };
        imageWorker.execute();

        JLabel titleLabel = new JLabel(artwork.getTitle(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 12));
        box.add(titleLabel, BorderLayout.NORTH);

        JLabel descLabel = new JLabel("<html>" + artwork.getDescription() + "</html>", SwingConstants.CENTER);
        descLabel.setFont(new Font("Roboto", Font.PLAIN, 10));
        box.add(descLabel, BorderLayout.SOUTH);

        JButton selectButton = createStyledButton(messages.getString("button.select"), "/icons/select.png");
        selectButton.addActionListener(e -> {
            selectedArtwork = artwork;
            artworkTitleField.setText(artwork.getTitle());
            artworkDescriptionField.setText(artwork.getDescription());
            artworkCreationDateField.setText(artwork.getCreationDate() != null ? artwork.getCreationDate().toString() : "");
            for (int i = 0; i < artworkArtistComboBox.getItemCount(); i++) {
                Artist artist = artworkArtistComboBox.getItemAt(i);
                if (artist.getArtistId() == artwork.getArtistId()) {
                    artworkArtistComboBox.setSelectedIndex(i);
                    break;
                }
            }
            artworkImagePathField.setText(artwork.getImagePath());
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(selectButton);
        box.add(buttonPanel, BorderLayout.SOUTH);

        return box;
    }

    private class TourListCellRenderer extends JPanel implements ListCellRenderer<VirtualTour> {
        private JLabel titleLabel;
        private JButton editButton, deleteButton, previewButton;
        private JPanel buttonPanel;
        private VirtualTour currentTour;

        public TourListCellRenderer() {
            setLayout(new BorderLayout(10, 10));
            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            setBackground(new Color(240, 242, 245));

            titleLabel = new JLabel();
            titleLabel.setFont(new Font("Roboto", Font.BOLD, 14));
            add(titleLabel, BorderLayout.CENTER);

            buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.setBackground(new Color(240, 242, 245));

            editButton = createStyledButton(messages.getString("button.edit"), "/icons/edit.png");
            editButton.setPreferredSize(new Dimension(80, 30));

            deleteButton = createStyledButton(messages.getString("button.delete"), "/icons/delete.png");
            deleteButton.setPreferredSize(new Dimension(80, 30));

            previewButton = createStyledButton(messages.getString("button.preview"), "/icons/preview.png");
            previewButton.setPreferredSize(new Dimension(80, 30));

            buttonPanel.add(editButton);
            buttonPanel.add(deleteButton);
            buttonPanel.add(previewButton);
            buttonPanel.setPreferredSize(new Dimension(240, 40));
            add(buttonPanel, BorderLayout.EAST);

            editButton.addActionListener(e -> {
                if (currentTour != null) {
                    selectedTour = currentTour;
                    tourTitleField.setText(currentTour.getTitle());
                    tourDescriptionField.setText(currentTour.getDescription());
                    tourPriceField.setText(String.valueOf(currentTour.getPrice()));
                    loadArtworksForTourSelection();
                    loadArtworksForTourDetail();
                }
            });

            deleteButton.addActionListener(e -> {
                if (currentTour != null) {
                    deleteTour(currentTour);
                }
            });

            previewButton.addActionListener(e -> {
                if (currentTour != null) {
                    previewTour(currentTour);
                }
            });
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends VirtualTour> list, VirtualTour tour, int index, boolean isSelected, boolean cellHasFocus) {
            this.currentTour = tour;
            titleLabel.setText(tour.getTitle() + " - " + messages.getString("virtualTour.priceLabel") + tour.getPrice());
            if (isSelected) {
                setBackground(new Color(135, 206, 250));
                titleLabel.setForeground(Color.WHITE);
                buttonPanel.setBackground(new Color(135, 206, 250));
            } else {
                setBackground(new Color(240, 242, 245));
                titleLabel.setForeground(Color.BLACK);
                buttonPanel.setBackground(new Color(240, 242, 245));
            }
            return this;
        }
    }

    private void addArtwork() {
        try {
            if (artworkTitleField.getText().trim().isEmpty()) {
                throw new IllegalArgumentException(messages.getString("error.artworkTitleEmpty"));
            }
            if (artworkArtistComboBox.getSelectedItem() == null) {
                throw new IllegalArgumentException(messages.getString("error.artistIdEmpty"));
            }

            LocalDate creationDate = null;
            if (!artworkCreationDateField.getText().isEmpty()) {
                try {
                    creationDate = LocalDate.parse(artworkCreationDateField.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                } catch (Exception e) {
                    throw new IllegalArgumentException(messages.getString("error.invalidDateFormat"));
                }
            }

            Artist selectedArtist = (Artist) artworkArtistComboBox.getSelectedItem();
            int artistId = selectedArtist.getArtistId();

            artworkController.createArtwork(
                    artworkTitleField.getText().trim(),
                    artworkDescriptionField.getText().trim(),
                    creationDate,
                    artistId,
                    artworkImagePathField.getText().trim(),
                    null
            );

            artworkPage = 1;
            loadArtworks();
            clearArtworkFields();
            notifyDataChanged();
            showSuccessDialog(messages.getString("success.addArtwork"));
        } catch (IllegalArgumentException e) {
            showErrorDialog(e.getMessage());
        } catch (Exception e) {
            showErrorDialog(messages.getString("error.addArtwork") + " " + e.getMessage());
        }
    }

    private void updateArtwork() {
        try {
            if (selectedArtwork == null) {
                throw new IllegalStateException(messages.getString("error.selectArtwork"));
            }
            if (artworkTitleField.getText().trim().isEmpty()) {
                throw new IllegalArgumentException(messages.getString("error.artworkTitleEmpty"));
            }
            if (artworkArtistComboBox.getSelectedItem() == null) {
                throw new IllegalArgumentException(messages.getString("error.artistIdEmpty"));
            }

            LocalDate creationDate = null;
            if (!artworkCreationDateField.getText().isEmpty()) {
                try {
                    creationDate = LocalDate.parse(artworkCreationDateField.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                } catch (Exception e) {
                    throw new IllegalArgumentException(messages.getString("error.invalidDateFormat"));
                }
            }

            Artist selectedArtist = (Artist) artworkArtistComboBox.getSelectedItem();
            int artistId = selectedArtist.getArtistId();

            selectedArtwork.setTitle(artworkTitleField.getText().trim());
            selectedArtwork.setDescription(artworkDescriptionField.getText().trim());
            selectedArtwork.setCreationDate(creationDate);
            selectedArtwork.setArtistId(artistId);
            selectedArtwork.setImagePath(artworkImagePathField.getText().trim());
            selectedArtwork.setExhibitionId(selectedArtwork.getExhibitionId());

            artworkController.updateArtwork(selectedArtwork);
            artworkPage = 1;
            loadArtworks();
            clearArtworkFields();
            selectedArtwork = null;
            notifyDataChanged();
            showSuccessDialog(messages.getString("success.updateArtwork"));
        } catch (IllegalArgumentException e) {
            showErrorDialog(e.getMessage());
        } catch (Exception e) {
            showErrorDialog(messages.getString("error.updateArtwork") + " " + e.getMessage());
        }
    }

    private void deleteArtwork() {
        try {
            if (selectedArtwork == null) {
                throw new IllegalStateException(messages.getString("error.selectArtwork"));
            }
            boolean confirm = showConfirmDialog(messages.getString("confirm.deleteArtwork"));
            if (confirm) {
                artworkController.deleteArtwork(selectedArtwork.getArtworkId());
                artworkPage = 1;
                loadArtworks();
                clearArtworkFields();
                selectedArtwork = null;
                notifyDataChanged();
                showSuccessDialog(messages.getString("success.deleteArtwork"));
            }
        } catch (Exception e) {
            showErrorDialog(messages.getString("error.deleteArtwork") + " " + e.getMessage());
        }
    }

    private void clearArtworkFields() {
        artworkTitleField.setText("");
        artworkDescriptionField.setText("");
        artworkCreationDateField.setText("");
        artworkArtistComboBox.setSelectedIndex(-1);
        artworkImagePathField.setText("");
    }

    private void addExhibition() {
        try {
            if (exhibitionTitleField.getText().trim().isEmpty()) {
                throw new IllegalArgumentException(messages.getString("error.exhibitionTitleEmpty"));
            }
            LocalDate startDate = exhibitionStartDateField.getText().isEmpty() ? null :
                    LocalDate.parse(exhibitionStartDateField.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate endDate = exhibitionEndDateField.getText().isEmpty() ? null :
                    LocalDate.parse(exhibitionEndDateField.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            Exhibition newExhibition = new Exhibition(
                    0,
                    exhibitionTitleField.getText().trim(),
                    exhibitionDescriptionField.getText().trim(),
                    startDate,
                    endDate,
                    exhibitionStatusField.getText().trim()
            );
            exhibitionController.createExhibition(newExhibition);

            List<Artwork> selectedArtworks = artworkSelectionList.getSelectedValuesList();
            for (Artwork art : selectedArtworks) {
                art.setExhibitionId(newExhibition.getExhibitionId());
                artworkController.updateArtwork(art);
            }

            exhibitionPage = 1;
            loadExhibitions();
            notifyDataChanged();
            showSuccessDialog(messages.getString("success.addExhibition"));
        } catch (IllegalArgumentException e) {
            showErrorDialog(e.getMessage());
        } catch (Exception e) {
            showErrorDialog(messages.getString("error.addExhibition") + " " + e.getMessage());
        }
    }

    private void updateExhibition() {
        try {
            if (selectedExhibition == null) {
                throw new IllegalStateException(messages.getString("error.selectExhibition"));
            }
            if (exhibitionTitleField.getText().trim().isEmpty()) {
                throw new IllegalArgumentException(messages.getString("error.exhibitionTitleEmpty"));
            }
            LocalDate startDate = exhibitionStartDateField.getText().isEmpty() ? null :
                    LocalDate.parse(exhibitionStartDateField.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate endDate = exhibitionEndDateField.getText().isEmpty() ? null :
                    LocalDate.parse(exhibitionEndDateField.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            selectedExhibition.setTitle(exhibitionTitleField.getText().trim());
            selectedExhibition.setDescription(exhibitionDescriptionField.getText().trim());
            selectedExhibition.setStartDate(startDate);
            selectedExhibition.setEndDate(endDate);
            selectedExhibition.setStatus(exhibitionStatusField.getText().trim());

            exhibitionController.updateExhibition(selectedExhibition);

            List<Artwork> allArtworks = artworkController.getArtworksByExhibitionId(selectedExhibition.getExhibitionId(), 1, 100);
            for (Artwork art : allArtworks) {
                art.setExhibitionId(null);
                artworkController.updateArtwork(art);
            }
            List<Artwork> selectedArtworks = artworkSelectionList.getSelectedValuesList();
            for (Artwork art : selectedArtworks) {
                art.setExhibitionId(selectedExhibition.getExhibitionId());
                artworkController.updateArtwork(art);
            }

            exhibitionPage = 1;
            loadExhibitions();
            notifyDataChanged();
            showSuccessDialog(messages.getString("success.updateExhibition"));
        } catch (IllegalArgumentException e) {
            showErrorDialog(e.getMessage());
        } catch (Exception e) {
            showErrorDialog(messages.getString("error.updateExhibition") + " " + e.getMessage());
        }
    }

    private void deleteExhibition() {
        try {
            if (selectedExhibition == null) {
                throw new IllegalStateException(messages.getString("error.selectExhibition"));
            }
            boolean confirm = showConfirmDialog(messages.getString("confirm.deleteExhibition"));
            if (confirm) {
                List<Artwork> artworks = artworkController.getArtworksByExhibitionId(selectedExhibition.getExhibitionId(), 1, 100);
                for (Artwork art : artworks) {
                    art.setExhibitionId(null);
                    artworkController.updateArtwork(art);
                }
                exhibitionController.deleteExhibition(selectedExhibition.getExhibitionId());
                exhibitionPage = 1;
                loadExhibitions();
                selectedExhibition = null;
                notifyDataChanged();
                showSuccessDialog(messages.getString("success.deleteExhibition"));
            }
        } catch (Exception e) {
            showErrorDialog(messages.getString("error.deleteExhibition") + " " + e.getMessage());
        }
    }

    private void addVirtualTour() {
        try {
            String title = tourTitleField.getText().trim();
            String priceText = tourPriceField.getText().trim();

            if (title.isEmpty()) {
                throw new IllegalArgumentException(messages.getString("error.tourTitleEmpty"));
            }
            double price;
            try {
                price = Double.parseDouble(priceText);
                if (price <= 0) {
                    throw new IllegalArgumentException(messages.getString("error.tourPriceInvalid"));
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(messages.getString("error.tourPriceFormat"));
            }

            VirtualTour tour = new VirtualTour();
            tour.setTitle(title);
            tour.setDescription(tourDescriptionField.getText().trim());
            tour.setPrice(price);

            virtualTourController.createVirtualTour(tour);
            List<Artwork> selectedArtworks = tourArtworkSelectionList.getSelectedValuesList();
            for (Artwork art : selectedArtworks) {
                artworkTourController.addArtworkToTour(tour.getTourId(), art.getArtworkId());
            }

            tourPage = 1;
            loadVirtualTours();
            loadArtworksForTourDetail();
            notifyDataChanged();
            showSuccessDialog(messages.getString("success.addTour"));
        } catch (IllegalArgumentException e) {
            showErrorDialog(e.getMessage());
        } catch (Exception e) {
            showErrorDialog(messages.getString("error.addTour") + " " + e.getMessage());
        }
    }

    private void updateVirtualTour() {
        try {
            if (selectedTour == null) {
                throw new IllegalStateException(messages.getString("error.selectTour"));
            }
            String title = tourTitleField.getText().trim();
            String priceText = tourPriceField.getText().trim();

            if (title.isEmpty()) {
                throw new IllegalArgumentException(messages.getString("error.tourTitleEmpty"));
            }
            double price;
            try {
                price = Double.parseDouble(priceText);
                if (price <= 0) {
                    throw new IllegalArgumentException(messages.getString("error.tourPriceInvalid"));
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(messages.getString("error.tourPriceFormat"));
            }

            selectedTour.setTitle(title);
            selectedTour.setDescription(tourDescriptionField.getText().trim());
            selectedTour.setPrice(price);

            virtualTourController.updateVirtualTour(selectedTour);

            List<Artwork> currentArtworks = artworkTourController.getArtworksByTourId(selectedTour.getTourId());
            for (Artwork art : currentArtworks) {
                artworkTourController.removeArtworkFromTour(selectedTour.getTourId(), art.getArtworkId());
            }
            List<Artwork> selectedArtworks = tourArtworkSelectionList.getSelectedValuesList();
            for (Artwork art : selectedArtworks) {
                artworkTourController.addArtworkToTour(selectedTour.getTourId(), art.getArtworkId());
            }

            tourPage = 1;
            loadVirtualTours();
            loadArtworksForTourDetail();
            notifyDataChanged();
            showSuccessDialog(messages.getString("success.updateTour"));
        } catch (IllegalArgumentException e) {
            showErrorDialog(e.getMessage());
        } catch (Exception e) {
            showErrorDialog(messages.getString("error.updateTour") + " " + e.getMessage());
        }
    }

    private void deleteTour(VirtualTour tour) {
        boolean confirm = showConfirmDialog(messages.getString("confirm.deleteTour"));
        if (confirm) {
            try {
                virtualTourController.deleteVirtualTour(tour.getTourId());
                tourPage = 1;
                loadVirtualTours();
                notifyDataChanged();
                showSuccessDialog(messages.getString("success.deleteTour"));
            } catch (Exception e) {
                showErrorDialog(messages.getString("error.deleteTour") + " " + e.getMessage());
            }
        }
    }

    private void previewTour(VirtualTour tour) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), messages.getString("virtualTour.previewTitle") + tour.getTitle(), true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(new Color(240, 242, 245));

        JLabel titleLabel = new JLabel(messages.getString("virtualTour.label") + tour.getTitle(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 16));
        titleLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
        dialog.add(titleLabel, BorderLayout.NORTH);

        DefaultListModel<Artwork> artworkListModel = new DefaultListModel<>();
        try {
            List<Artwork> artworks = artworkTourController.getArtworksByTourId(tour.getTourId());
            for (Artwork art : artworks) {
                artworkListModel.addElement(art);
            }
        } catch (Exception e) {
            showErrorDialog(messages.getString("error.loadArtworks") + " " + e.getMessage());
            dialog.dispose();
            return;
        }

        JList<Artwork> artworkList = new JList<>(artworkListModel);
        artworkList.setCellRenderer(new ListCellRenderer<>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends Artwork> list, Artwork value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = new JLabel(value.getTitle());
                label.setOpaque(true);
                label.setFont(new Font("Roboto", Font.PLAIN, 14));
                label.setBorder(new EmptyBorder(5, 10, 5, 10));
                if (isSelected) {
                    label.setBackground(new Color(135, 206, 250));
                    label.setForeground(Color.WHITE);
                } else {
                    label.setBackground(Color.WHITE);
                    label.setForeground(Color.BLACK);
                }
                return label;
            }
        });
        JScrollPane scrollPane = new JScrollPane(artworkList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        dialog.add(scrollPane, BorderLayout.CENTER);

        if (!artworkListModel.isEmpty()) {
            Artwork firstArtwork = artworkListModel.get(0);
            try {
                ImageIcon icon = new ImageIcon(getClass().getResource(firstArtwork.getImagePath()));
                Image scaledImage = icon.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
                imageLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
                dialog.add(imageLabel, BorderLayout.WEST);
            } catch (Exception e) {
                System.err.println("Lỗi tải hình ảnh: " + e.getMessage());
            }
        }

        JButton closeButton = createStyledButton(messages.getString("button.close"), null);
        closeButton.addActionListener(e -> dialog.dispose());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(240, 242, 245));
        buttonPanel.add(closeButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Roboto", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return textField;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Roboto", Font.PLAIN, 14));
        return label;
    }

    private JButton createStyledButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setFont(new Font("Roboto", Font.PLAIN, 14));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(100, 149, 237));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(70, 130, 180));
            }
        });

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

    private JProgressBar createLoadingSpinner() {
        JProgressBar spinner = new JProgressBar();
        spinner.setIndeterminate(true);
        spinner.setPreferredSize(new Dimension(100, 20));
        return spinner;
    }

    private void showSuccessDialog(String message) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), messages.getString("dialog.success"), true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(new Color(240, 242, 245));

        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
        messageLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
        dialog.add(messageLabel, BorderLayout.CENTER);

        JButton okButton = createStyledButton(messages.getString("button.ok"), null);
        okButton.addActionListener(e -> dialog.dispose());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(240, 242, 245));
        buttonPanel.add(okButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void showErrorDialog(String message) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), messages.getString("dialog.error"), true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(new Color(240, 242, 245));

        JLabel messageLabel = new JLabel("<html>" + message + "</html>", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
        messageLabel.setForeground(new Color(200, 0, 0));
        messageLabel.setBorder(new EmptyBorder(20, 10, 20, 10));
        dialog.add(messageLabel, BorderLayout.CENTER);

        JButton okButton = createStyledButton(messages.getString("button.ok"), null);
        okButton.addActionListener(e -> dialog.dispose());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(240, 242, 245));
        buttonPanel.add(okButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private boolean showConfirmDialog(String message) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), messages.getString("dialog.confirm"), true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(new Color(240, 242, 245));

        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
        messageLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
        dialog.add(messageLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(240, 242, 245));

        final boolean[] result = {false};
        JButton yesButton = createStyledButton(messages.getString("button.yes"), null);
        yesButton.addActionListener(e -> {
            result[0] = true;
            dialog.dispose();
        });
        JButton noButton = createStyledButton(messages.getString("button.no"), null);
        noButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
        return result[0];
    }

    private void notifyDataChanged() {
        artworkPage = 1;
        exhibitionPage = 1;
        tourPage = 1;

        loadArtworks();
        loadExhibitions();
        loadVirtualTours();
        loadArtworksForExhibition();
        loadArtworksForTourSelection();
    }

    public void loadData() {
        loadArtistsIntoComboBox();
        loadArtworks();
        loadExhibitions();
        loadVirtualTours();
        loadArtworksForExhibition();
        loadArtworksForTourSelection();
    }
}