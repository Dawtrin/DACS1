package com.soulvirart.view;

import com.soulvirart.controller.ArtistController;
import com.soulvirart.model.Artist;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class ArtistView extends JPanel {
    private final ArtistController artistController;
    private Artist selectedArtist;

    // Table and pagination for artists
    private JTable artistTable;
    private DefaultTableModel artistTableModel;
    private JTextField searchField;
    private int artistPage = 1;
    private final int ARTISTS_PER_PAGE = 10;

    // Form fields for artist management
    private JTextField nameField, biographyField, birthDateField, nationalityField;

    // ResourceBundle for internationalization (i18n)
    private ResourceBundle messages;

    public ArtistView() {
        this.artistController = new ArtistController();
        // Load ResourceBundle với fallback
        try {
            messages = ResourceBundle.getBundle("messages");
        } catch (Exception e) {
            System.err.println("Không thể tải ResourceBundle: " + e.getMessage());
            messages = ResourceBundle.getBundle("messages", new Locale("en")); // Fallback về tiếng Anh nếu cần
        }
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 242, 245));

        // Thanh tìm kiếm
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(new Color(240, 242, 245));
        JLabel searchLabel = createStyledLabel(messages.getString("search"));
        searchField = createStyledTextField();
        searchField.setPreferredSize(new Dimension(200, 30));
        searchField.addActionListener(e -> loadArtists());
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);

        JButton refreshButton = createStyledButton(messages.getString("button.refresh"), "/icons/refresh.png");
        refreshButton.addActionListener(e -> loadArtists());
        searchPanel.add(refreshButton);

        // Bảng danh sách nghệ sĩ
        String[] columnNames = {
                messages.getString("artist.id"),
                messages.getString("artist.name"),
                messages.getString("artist.biography"),
                messages.getString("artist.birthDate"),
                messages.getString("artist.nationality")
        };
        artistTableModel = new DefaultTableModel(columnNames, 0);
        artistTable = new JTable(artistTableModel);
        artistTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        artistTable.getTableHeader().setBackground(new Color(135, 206, 250));
        artistTable.getTableHeader().setForeground(Color.WHITE);
        artistTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = artistTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int artistId = (int) artistTableModel.getValueAt(selectedRow, 0);
                    try {
                        List<Artist> artists = artistController.getAllArtists(1, Integer.MAX_VALUE);
                        selectedArtist = artists.stream()
                                .filter(artist -> artist.getArtistId() == artistId)
                                .findFirst()
                                .orElse(null);
                    } catch (Exception ex) {
                        showErrorDialog(messages.getString("error.loadArtists") + ex.getMessage());
                        return;
                    }
                    if (selectedArtist != null) {
                        nameField.setText(selectedArtist.getName());
                        biographyField.setText(selectedArtist.getBiography());
                        birthDateField.setText(selectedArtist.getBirthDate() != null ? selectedArtist.getBirthDate().toString() : "");
                        nationalityField.setText(selectedArtist.getNationality());
                    }
                }
            }
        });

        JScrollPane tableScrollPane = new JScrollPane(artistTable);
        tableScrollPane.setPreferredSize(new Dimension(0, 300));

        // Phân trang
        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        paginationPanel.setBackground(new Color(240, 242, 245));
        JButton prevButton = createStyledButton(messages.getString("pagination.prev"), "/icons/prev.png");
        prevButton.addActionListener(e -> {
            if (artistPage > 1) {
                artistPage--;
                loadArtists();
            }
        });
        JButton nextButton = createStyledButton(messages.getString("pagination.next"), "/icons/next.png");
        nextButton.addActionListener(e -> {
            artistPage++;
            loadArtists();
        });
        paginationPanel.add(prevButton);
        paginationPanel.add(nextButton);

        // Form nhập liệu
        JPanel formPanel = new JPanel(new BorderLayout(10, 10));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createTitledBorder(messages.getString("artist.manage")));

        JPanel fieldsPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        fieldsPanel.setBackground(Color.WHITE);

        fieldsPanel.add(createStyledLabel(messages.getString("artist.name")));
        nameField = createStyledTextField();
        fieldsPanel.add(nameField);

        fieldsPanel.add(createStyledLabel(messages.getString("artist.biography")));
        biographyField = createStyledTextField();
        fieldsPanel.add(biographyField);

        fieldsPanel.add(createStyledLabel(messages.getString("artist.birthDate")));
        birthDateField = createStyledTextField();
        fieldsPanel.add(birthDateField);

        fieldsPanel.add(createStyledLabel(messages.getString("artist.nationality")));
        nationalityField = createStyledTextField();
        fieldsPanel.add(nationalityField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton addButton = createStyledButton(messages.getString("button.add"), "/icons/add.png");
        addButton.addActionListener(e -> addArtist());

        JButton updateButton = createStyledButton(messages.getString("button.update"), "/icons/edit.png");
        updateButton.addActionListener(e -> updateArtist());

        JButton deleteButton = createStyledButton(messages.getString("button.delete"), "/icons/delete.png");
        deleteButton.addActionListener(e -> deleteArtist());

        JButton clearButton = createStyledButton(messages.getString("button.clear"), "/icons/clear.png");
        clearButton.addActionListener(e -> clearFields());

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        formPanel.add(fieldsPanel, BorderLayout.CENTER);
        formPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(searchPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(paginationPanel, BorderLayout.SOUTH);
        add(formPanel, BorderLayout.SOUTH);
    }

    private void loadArtists() {
        SwingWorker<List<Artist>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Artist> doInBackground() throws Exception {
                return artistController.getAllArtists(artistPage, ARTISTS_PER_PAGE);
            }

            @Override
            protected void done() {
                try {
                    List<Artist> artists = get();
                    artistTableModel.setRowCount(0);
                    if (artists.isEmpty()) {
                        artistTableModel.addRow(new Object[]{
                                "", messages.getString("noData"), "", "", ""
                        });
                    } else {
                        for (Artist artist : artists) {
                            artistTableModel.addRow(new Object[]{
                                    artist.getArtistId(),
                                    artist.getName(),
                                    artist.getBiography(),
                                    artist.getBirthDate() != null ? artist.getBirthDate().toString() : "",
                                    artist.getNationality()
                            });
                        }
                    }
                } catch (Exception e) {
                    showErrorDialog(messages.getString("error.loadArtists") + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void addArtist() {
        try {
            if (nameField.getText().trim().isEmpty()) {
                throw new IllegalArgumentException(messages.getString("error.artistNameEmpty"));
            }
            if (nationalityField.getText().trim().isEmpty()) {
                throw new IllegalArgumentException(messages.getString("error.artistNationalityEmpty"));
            }

            LocalDate birthDate = null;
            if (!birthDateField.getText().isEmpty()) {
                birthDate = LocalDate.parse(birthDateField.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }

            artistController.createArtist(
                    nameField.getText().trim(),
                    biographyField.getText().trim(),
                    birthDate,
                    nationalityField.getText().trim()
            );

            loadArtists();
            clearFields();
            showSuccessDialog(messages.getString("success.addArtist"));
        } catch (Exception e) {
            showErrorDialog(messages.getString("error.addArtist") + e.getMessage());
        }
    }

    private void updateArtist() {
        try {
            if (selectedArtist == null) {
                throw new IllegalStateException(messages.getString("error.selectArtist"));
            }
            if (nameField.getText().trim().isEmpty()) {
                throw new IllegalArgumentException(messages.getString("error.artistNameEmpty"));
            }
            if (nationalityField.getText().trim().isEmpty()) {
                throw new IllegalArgumentException(messages.getString("error.artistNationalityEmpty"));
            }

            LocalDate birthDate = null;
            if (!birthDateField.getText().isEmpty()) {
                birthDate = LocalDate.parse(birthDateField.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }

            selectedArtist.setName(nameField.getText().trim());
            selectedArtist.setBiography(biographyField.getText().trim());
            selectedArtist.setBirthDate(birthDate);
            selectedArtist.setNationality(nationalityField.getText().trim());

            artistController.updateArtist(selectedArtist);
            loadArtists();
            clearFields();
            selectedArtist = null;
            showSuccessDialog(messages.getString("success.updateArtist"));
        } catch (Exception e) {
            showErrorDialog(messages.getString("error.updateArtist") + e.getMessage());
        }
    }

    private void deleteArtist() {
        try {
            if (selectedArtist == null) {
                throw new IllegalStateException(messages.getString("error.selectArtist"));
            }
            boolean confirm = showConfirmDialog(messages.getString("confirm.deleteArtist"));
            if (confirm) {
                artistController.deleteArtist(selectedArtist.getArtistId());
                loadArtists();
                clearFields();
                selectedArtist = null;
                showSuccessDialog(messages.getString("success.deleteArtist"));
            }
        } catch (Exception e) {
            showErrorDialog(messages.getString("error.deleteArtist") + e.getMessage());
        }
    }

    private void clearFields() {
        nameField.setText("");
        biographyField.setText("");
        birthDateField.setText("");
        nationalityField.setText("");
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

    public void loadData() {
        artistPage = 1;
        loadArtists();
    }
}