package com.soulvirart.database;

import com.soulvirart.model.*;
import com.soulvirart.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DatabaseDAO {

    // User DAO methods
    public List<User> getAllUsers(int page, int pageSize) throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM Users ORDER BY user_id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                throw new SQLException("Failed to establish database connection");
            }
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, (page - 1) * pageSize); // OFFSET
                stmt.setInt(2, pageSize); // FETCH NEXT
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        users.add(mapToUser(rs));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getAllUsers: " + e.getMessage());
            throw e;
        }
        return users;
    }

    public void createUser(User user) throws SQLException {
        String query = "INSERT INTO Users (username, password, email, full_name, role, active) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                throw new SQLException("Failed to establish database connection");
            }
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, user.getUsername());
                stmt.setString(2, user.getPassword());
                stmt.setString(3, user.getEmail());
                stmt.setString(4, user.getFullName());
                stmt.setString(5, user.getRole());
                stmt.setBoolean(6, user.isActive());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error in createUser for username " + user.getUsername() + ": " + e.getMessage());
            throw e;
        }
    }

    public void updateUser(User user) throws SQLException {
        String query = "UPDATE Users SET username = ?, password = ?, email = ?, full_name = ?, role = ?, active = ? WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                throw new SQLException("Failed to establish database connection");
            }
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, user.getUsername());
                stmt.setString(2, user.getPassword());
                stmt.setString(3, user.getEmail());
                stmt.setString(4, user.getFullName());
                stmt.setString(5, user.getRole());
                stmt.setBoolean(6, user.isActive());
                stmt.setInt(7, user.getUserId());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error in updateUser for user_id " + user.getUserId() + ": " + e.getMessage());
            throw e;
        }
    }

    public void deleteUser(int userId) throws SQLException {
        String query = "DELETE FROM Users WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                throw new SQLException("Failed to establish database connection");
            }
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, userId);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error in deleteUser for user_id " + userId + ": " + e.getMessage());
            throw e;
        }
    }

    public User findUserById(int userId) throws SQLException {
        String query = "SELECT * FROM Users WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                throw new SQLException("Failed to establish database connection");
            }
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, userId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return mapToUser(rs);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in findUserById for user_id " + userId + ": " + e.getMessage());
            throw e;
        }
        return null;
    }

    public User findUserByUsername(String username) throws SQLException {
        String query = "SELECT * FROM Users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                throw new SQLException("Failed to establish database connection");
            }
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return mapToUser(rs);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in findUserByUsername for username " + username + ": " + e.getMessage());
            throw e;
        }
        return null;
    }

    public User findUserByEmail(String email) throws SQLException {
        String query = "SELECT * FROM Users WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                throw new SQLException("Failed to establish database connection");
            }
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, email);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return mapToUser(rs);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in findUserByEmail for email " + email + ": " + e.getMessage());
            throw e;
        }
        return null;
    }

    public boolean checkEmailExists(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM Users WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                throw new SQLException("Failed to establish database connection");
            }
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, email);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1) > 0;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in checkEmailExists for email " + email + ": " + e.getMessage());
            throw e;
        }
        return false;
    }

    private User mapToUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("user_id"),
                rs.getString("username") != null ? rs.getString("username") : "",
                rs.getString("password") != null ? rs.getString("password") : "",
                rs.getString("email") != null ? rs.getString("email") : "",
                rs.getString("full_name") != null ? rs.getString("full_name") : "",
                rs.getString("role") != null ? rs.getString("role") : "user",
                rs.getBoolean("active")
        );
    }

    // Artist DAO methods
    public List<Artist> getAllArtists(int page, int pageSize) throws SQLException {
        List<Artist> artists = new ArrayList<>();
        String query = "SELECT * FROM Artists ORDER BY artist_id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, (page - 1) * pageSize);
            stmt.setInt(2, pageSize);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    artists.add(mapToArtist(rs));
                }
            }
        }
        return artists;
    }

    public void createArtist(Artist artist) throws SQLException {
        String query = "INSERT INTO Artists (name, biography, birth_date, nationality) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, artist.getName());
            stmt.setString(2, artist.getBiography());
            stmt.setDate(3, artist.getBirthDate() != null ? java.sql.Date.valueOf(artist.getBirthDate()) : null);
            stmt.setString(4, artist.getNationality());
            stmt.executeUpdate();
        }
    }

    public void updateArtist(Artist artist) throws SQLException {
        String query = "UPDATE Artists SET name = ?, biography = ?, birth_date = ?, nationality = ? WHERE artist_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, artist.getName());
            stmt.setString(2, artist.getBiography());
            stmt.setDate(3, artist.getBirthDate() != null ? java.sql.Date.valueOf(artist.getBirthDate()) : null);
            stmt.setString(4, artist.getNationality());
            stmt.setInt(5, artist.getArtistId());
            stmt.executeUpdate();
        }
    }

    public void deleteArtist(int artistId) throws SQLException {
        String query = "DELETE FROM Artists WHERE artist_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, artistId);
            stmt.executeUpdate();
        }
    }

    private Artist mapToArtist(ResultSet rs) throws SQLException {
        java.sql.Date birthDate = rs.getDate("birth_date");
        return new Artist(
                rs.getInt("artist_id"),
                rs.getString("name") != null ? rs.getString("name") : "",
                rs.getString("biography") != null ? rs.getString("biography") : "",
                birthDate != null ? birthDate.toLocalDate() : null,
                rs.getString("nationality") != null ? rs.getString("nationality") : ""
        );
    }

    // Artwork DAO methods
    public List<Artwork> getArtworksByExhibitionId(int exhibitionId, int page, int pageSize) throws SQLException {
        List<Artwork> artworks = new ArrayList<>();
        String query = "SELECT * FROM Artworks WHERE exhibition_id = ? ORDER BY artwork_id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, exhibitionId);
            stmt.setInt(2, (page - 1) * pageSize);
            stmt.setInt(3, pageSize);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    artworks.add(mapToArtwork(rs));
                }
            }
        }
        return artworks;
    }

    public List<Artwork> getArtworksByTourId(int tourId) throws SQLException {
        if (tourId <= 0) {
            throw new IllegalArgumentException("Invalid tour ID: " + tourId);
        }
        List<Artwork> artworks = new ArrayList<>();
        String query = "SELECT a.* FROM Artworks a JOIN ArtworkTours at ON a.artwork_id = at.artwork_id WHERE at.tour_id = ? ORDER BY at.sequence, a.artwork_id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, tourId);
            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("Executing getArtworksByTourId for tourId: " + tourId);
                while (rs.next()) {
                    artworks.add(mapToArtwork(rs));
                }
                System.out.println("Found " + artworks.size() + " artworks for tourId: " + tourId);
            }
        } catch (SQLException e) {
            System.err.println("SQL error in getArtworksByTourId for tourId: " + tourId + " - " + e.getMessage());
            throw e;
        }
        return artworks;
    }

    public Artwork findArtworkById(int artworkId) throws SQLException {
        String query = "SELECT * FROM Artworks WHERE artwork_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, artworkId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapToArtwork(rs);
                }
            }
        }
        return null;
    }

    public List<Artwork> getAllArtworks(int page, int pageSize) throws SQLException {
        List<Artwork> artworks = new ArrayList<>();
        String query = "SELECT * FROM Artworks ORDER BY artwork_id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, (page - 1) * pageSize);
            stmt.setInt(2, pageSize);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    artworks.add(mapToArtwork(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getAllArtworks: " + e.getMessage());
            throw e;
        }
        return artworks;
    }

    public List<Artwork> searchArtworks(String searchQuery, int page, int pageSize) throws SQLException {
        List<Artwork> artworks = new ArrayList<>();
        String query = "SELECT * FROM Artworks WHERE title LIKE ? OR description LIKE ? " +
                "ORDER BY artwork_id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            String searchPattern = "%" + searchQuery + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setInt(3, (page - 1) * pageSize);
            stmt.setInt(4, pageSize);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    artworks.add(mapToArtwork(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in searchArtworks: " + e.getMessage());
            throw e;
        }
        return artworks;
    }

    public void createArtwork(Artwork artwork) throws SQLException {
        String query = "INSERT INTO Artworks (title, description, creation_date, artist_id, image_path, exhibition_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, artwork.getTitle());
            stmt.setString(2, artwork.getDescription());
            stmt.setDate(3, artwork.getCreationDate() != null ? java.sql.Date.valueOf(artwork.getCreationDate()) : null);
            stmt.setInt(4, artwork.getArtistId());
            stmt.setString(5, artwork.getImagePath());
            if (artwork.getExhibitionId() != null) {
                int exhibitionId = artwork.getExhibitionId().intValue(); // Lưu vào biến để tránh unboxing trực tiếp
                if (exhibitionId <= 0) {
                    throw new SQLException("Invalid exhibition ID: " + exhibitionId);
                }
                stmt.setInt(6, exhibitionId);
            } else {
                stmt.setNull(6, java.sql.Types.INTEGER);
            }
            stmt.executeUpdate();
        }
    }

    public void updateArtwork(Artwork artwork) throws SQLException {
        String query = "UPDATE Artworks SET title = ?, description = ?, creation_date = ?, artist_id = ?, image_path = ?, exhibition_id = ? WHERE artwork_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, artwork.getTitle());
            stmt.setString(2, artwork.getDescription());
            stmt.setDate(3, artwork.getCreationDate() != null ? java.sql.Date.valueOf(artwork.getCreationDate()) : null);
            stmt.setInt(4, artwork.getArtistId());
            stmt.setString(5, artwork.getImagePath());
            if (artwork.getExhibitionId() != null) {
                int exhibitionId = artwork.getExhibitionId().intValue(); // Lưu vào biến để tránh unboxing trực tiếp
                if (exhibitionId <= 0) {
                    throw new SQLException("Invalid exhibition ID: " + exhibitionId);
                }
                stmt.setInt(6, exhibitionId);
            } else {
                stmt.setNull(6, java.sql.Types.INTEGER);
            }
            stmt.setInt(7, artwork.getArtworkId());
            stmt.executeUpdate();
        }
    }

    public void deleteArtwork(int artworkId) throws SQLException {
        String query = "DELETE FROM Artworks WHERE artwork_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, artworkId);
            stmt.executeUpdate();
        }
    }

    public List<Artwork> getArtworksByArtistId(int artistId, int page, int pageSize) throws SQLException {
        List<Artwork> artworks = new ArrayList<>();
        String query = "SELECT * FROM Artworks WHERE artist_id = ? ORDER BY artwork_id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, artistId);
            stmt.setInt(2, (page - 1) * pageSize);
            stmt.setInt(3, pageSize);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    artworks.add(mapToArtwork(rs));
                }
            }
        }
        return artworks;
    }

    private Artwork mapToArtwork(ResultSet rs) throws SQLException {
        java.sql.Date creationDate = rs.getDate("creation_date");
        Integer exhibitionId = rs.getObject("exhibition_id") != null ? rs.getInt("exhibition_id") : null;
        return new Artwork(
                rs.getInt("artwork_id"),
                rs.getString("title") != null ? rs.getString("title") : "",
                rs.getString("description") != null ? rs.getString("description") : "",
                creationDate != null ? creationDate.toLocalDate() : null,
                rs.getInt("artist_id"),
                rs.getString("image_path"),
                exhibitionId
        );
    }

    // Exhibition DAO methods
    public List<Exhibition> getAllExhibitions(int page, int pageSize) throws SQLException {
        List<Exhibition> exhibitions = new ArrayList<>();
        String query = "SELECT * FROM Exhibitions ORDER BY exhibition_id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, (page - 1) * pageSize);
            stmt.setInt(2, pageSize);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    exhibitions.add(mapToExhibition(rs));
                }
            }
        }
        return exhibitions;
    }

    public int createExhibition(Exhibition exhibition) throws SQLException {
        if (exhibition == null || exhibition.getTitle() == null || exhibition.getTitle().trim().isEmpty()) {
            throw new SQLException("Title is required for creating an exhibition");
        }
        if (exhibition.getStartDate() != null && exhibition.getEndDate() != null && exhibition.getStartDate().isAfter(exhibition.getEndDate())) {
            throw new SQLException("Start date must be before end date");
        }
        String query = "INSERT INTO Exhibitions (title, description, start_date, end_date, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                throw new SQLException("Failed to establish database connection");
            }
            try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, exhibition.getTitle());
                stmt.setString(2, exhibition.getDescription());
                stmt.setDate(3, exhibition.getStartDate() != null ? java.sql.Date.valueOf(exhibition.getStartDate()) : null);
                stmt.setDate(4, exhibition.getEndDate() != null ? java.sql.Date.valueOf(exhibition.getEndDate()) : null);
                stmt.setString(5, exhibition.getStatus());
                stmt.executeUpdate();
                // Lấy exhibition_id được tạo tự động
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int exhibitionId = generatedKeys.getInt(1);
                        exhibition.setExhibitionId(exhibitionId); // Gán ID cho đối tượng Exhibition
                        return exhibitionId;
                    } else {
                        throw new SQLException("Failed to retrieve generated exhibition ID");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in createExhibition for title " + exhibition.getTitle() + ": " + e.getMessage());
            throw e;
        }
    }


    public void updateExhibition(Exhibition exhibition) throws SQLException {
        if (exhibition == null || exhibition.getExhibitionId() <= 0) {
            throw new SQLException("Invalid exhibition ID");
        }
        if (exhibition.getTitle() == null || exhibition.getTitle().trim().isEmpty()) {
            throw new SQLException("Title is required for updating an exhibition");
        }
        if (exhibition.getStartDate() != null && exhibition.getEndDate() != null && exhibition.getStartDate().isAfter(exhibition.getEndDate())) {
            throw new SQLException("Start date must be before end date");
        }
        String query = "UPDATE Exhibitions SET title = ?, description = ?, start_date = ?, end_date = ?, status = ? WHERE exhibition_id = ?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                throw new SQLException("Failed to establish database connection");
            }
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, exhibition.getTitle());
                stmt.setString(2, exhibition.getDescription());
                stmt.setDate(3, exhibition.getStartDate() != null ? java.sql.Date.valueOf(exhibition.getStartDate()) : null);
                stmt.setDate(4, exhibition.getEndDate() != null ? java.sql.Date.valueOf(exhibition.getEndDate()) : null);
                stmt.setString(5, exhibition.getStatus());
                stmt.setInt(6, exhibition.getExhibitionId());
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("No exhibition found with ID: " + exhibition.getExhibitionId());
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in updateExhibition for exhibition_id " + exhibition.getExhibitionId() + ": " + e.getMessage());
            throw e;
        }
    }


    public void deleteExhibition(int exhibitionId) throws SQLException {
        String query = "DELETE FROM Exhibitions WHERE exhibition_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, exhibitionId);
            stmt.executeUpdate();
        }
    }

    private Exhibition mapToExhibition(ResultSet rs) throws SQLException {
        java.sql.Date startDate = rs.getDate("start_date");
        java.sql.Date endDate = rs.getDate("end_date");
        return new Exhibition(
                rs.getInt("exhibition_id"),
                rs.getString("title") != null ? rs.getString("title") : "",
                rs.getString("description") != null ? rs.getString("description") : "",
                startDate != null ? startDate.toLocalDate() : null,
                endDate != null ? endDate.toLocalDate() : null,
                rs.getString("status") != null ? rs.getString("status") : "Draft"
        );
    }

    public Exhibition findExhibitionById(int exhibitionId) throws SQLException {
        String query = "SELECT * FROM Exhibitions WHERE exhibition_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, exhibitionId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapToExhibition(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in findExhibitionById for exhibition_id " + exhibitionId + ": " + e.getMessage());
            throw e;
        }
        return null;
    }

    public List<Exhibition> searchExhibitions(String searchQuery, int page, int pageSize) throws SQLException {
        List<Exhibition> exhibitions = new ArrayList<>();
        String query = "SELECT * FROM Exhibitions WHERE title LIKE ? OR description LIKE ? " +
                "ORDER BY exhibition_id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            String searchPattern = "%" + searchQuery + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setInt(3, (page - 1) * pageSize);
            stmt.setInt(4, pageSize);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    exhibitions.add(mapToExhibition(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in searchExhibitions: " + e.getMessage());
            throw e;
        }
        return exhibitions;
    }

    // VirtualTour DAO methods
    public List<VirtualTour> getAllVirtualTours(int page, int pageSize) throws SQLException {
        List<VirtualTour> tours = new ArrayList<>();
        String query = "SELECT * FROM VirtualTours ORDER BY tour_id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, (page - 1) * pageSize);
            stmt.setInt(2, pageSize);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tours.add(mapToVirtualTour(rs));
                }
            }
        }
        return tours;
    }

    public VirtualTour findVirtualTourById(int tourId) throws SQLException {
        String query = "SELECT * FROM VirtualTours WHERE tour_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, tourId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapToVirtualTour(rs);
                }
            }
        }
        return null;
    }

    public void createVirtualTour(VirtualTour tour) throws SQLException {
        String query = "INSERT INTO VirtualTours (title, description, price) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, tour.getTitle());
            stmt.setString(2, tour.getDescription());
            stmt.setDouble(3, tour.getPrice());
            stmt.executeUpdate();
        }
    }

    public void updateVirtualTour(VirtualTour tour) throws SQLException {
        String query = "UPDATE VirtualTours SET title = ?, description = ?, price = ? WHERE tour_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, tour.getTitle());
            stmt.setString(2, tour.getDescription());
            stmt.setDouble(3, tour.getPrice());
            stmt.setInt(4, tour.getTourId());
            stmt.executeUpdate();
        }
    }

    public void deleteVirtualTour(int tourId) throws SQLException {
        String query = "DELETE FROM VirtualTours WHERE tour_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, tourId);
            stmt.executeUpdate();
        }
    }

    public List<VirtualTour> searchVirtualTours(String searchQuery, int page, int pageSize) throws SQLException {
        List<VirtualTour> tours = new ArrayList<>();
        String query = "SELECT * FROM VirtualTours WHERE title LIKE ? OR description LIKE ? " +
                "ORDER BY tour_id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            String searchPattern = "%" + searchQuery + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setInt(3, (page - 1) * pageSize);
            stmt.setInt(4, pageSize);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tours.add(mapToVirtualTour(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in searchVirtualTours: " + e.getMessage());
            throw e;
        }
        return tours;
    }

    private VirtualTour mapToVirtualTour(ResultSet rs) throws SQLException {
        double price = rs.getDouble("price");
        return new VirtualTour(
                rs.getInt("tour_id"),
                rs.getString("title") != null ? rs.getString("title") : "",
                rs.getString("description") != null ? rs.getString("description") : "",
                price
        );
    }

    // ArtworkTour DAO methods
    public void addArtworkToTour(int tourId, int artworkId) throws SQLException {
        String query = "INSERT INTO ArtworkTours (tour_id, artwork_id, sequence) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, tourId);
            stmt.setInt(2, artworkId);
            // Tạm thời gán sequence là 0, sẽ được cập nhật bởi reorderArtworks nếu cần
            stmt.setInt(3, 0);
            stmt.executeUpdate();
        }
    }

    public void removeArtworkFromTour(int tourId, int artworkId) throws SQLException {
        String query = "DELETE FROM ArtworkTours WHERE tour_id = ? AND artwork_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, tourId);
            stmt.setInt(2, artworkId);
            stmt.executeUpdate();
        }
    }

    public void reorderArtworks(int tourId, List<Artwork> artworks) throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) {
                throw new SQLException("Không thể thiết lập kết nối cơ sở dữ liệu");
            }
            conn.setAutoCommit(false); // Bắt đầu giao dịch

            // Xóa tất cả các bản ghi hiện tại cho tourId
            String deleteQuery = "DELETE FROM ArtworkTours WHERE tour_id = ?";
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
                deleteStmt.setInt(1, tourId);
                deleteStmt.executeUpdate();
            }

            // Thêm lại các bản ghi với thứ tự mới
            String insertQuery = "INSERT INTO ArtworkTours (tour_id, artwork_id, sequence) VALUES (?, ?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                for (int i = 0; i < artworks.size(); i++) {
                    Artwork artwork = artworks.get(i);
                    insertStmt.setInt(1, tourId);
                    insertStmt.setInt(2, artwork.getArtworkId());
                    insertStmt.setInt(3, i); // Gán sequence theo thứ tự trong danh sách
                    insertStmt.addBatch();
                }
                insertStmt.executeBatch();
            }

            conn.commit(); // Cam kết giao dịch
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Hoàn tác nếu có lỗi
                } catch (SQLException rollbackEx) {
                    System.err.println("Lỗi khi rollback giao dịch: " + rollbackEx.getMessage());
                }
            }
            System.err.println("Lỗi trong reorderArtworks cho tourId: " + tourId + " - " + e.getMessage());
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException closeEx) {
                    System.err.println("Lỗi khi đóng kết nối: " + closeEx.getMessage());
                }
            }
        }
    }

    // Ticket DAO methods
    public List<Ticket> getAllTickets(int page, int pageSize) throws SQLException {
        List<Ticket> tickets = new ArrayList<>();
        String query = "SELECT * FROM Tickets ORDER BY ticket_id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, (page - 1) * pageSize);
            stmt.setInt(2, pageSize);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tickets.add(mapToTicket(rs));
                }
            }
        }
        return tickets;
    }

    public List<Ticket> searchTickets(int searchId) throws SQLException {
        List<Ticket> tickets = new ArrayList<>();
        String query = "SELECT * FROM Tickets WHERE ticket_id = ? OR user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, searchId);
            stmt.setInt(2, searchId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tickets.add(mapToTicket(rs));
                }
            }
        }
        return tickets;
    }

    public Ticket findTicketById(int ticketId) throws SQLException {
        String query = "SELECT * FROM Tickets WHERE ticket_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, ticketId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapToTicket(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in findTicketById for ticket_id " + ticketId + ": " + e.getMessage());
            throw e;
        }
        return null;
    }

    public void createTicket(Ticket ticket) throws SQLException {
        String query = "INSERT INTO Tickets (user_id, tour_id, customer_name, quantity, age_category, visit_date, price, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, ticket.getUserId());
            stmt.setInt(2, ticket.getTourId());
            stmt.setString(3, ticket.getCustomerName());
            stmt.setInt(4, ticket.getQuantity());
            stmt.setString(5, ticket.getAgeCategory());
            stmt.setTimestamp(6, ticket.getVisitDate() != null ? Timestamp.valueOf(ticket.getVisitDate()) : null);
            stmt.setDouble(7, ticket.getPrice());
            stmt.setString(8, ticket.getStatus());
            stmt.executeUpdate();
        }
    }

    public void updateTicket(Ticket ticket) throws SQLException {
        String query = "UPDATE Tickets SET user_id = ?, tour_id = ?, customer_name = ?, quantity = ?, age_category = ?, visit_date = ?, price = ?, status = ? WHERE ticket_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, ticket.getUserId());
            stmt.setInt(2, ticket.getTourId());
            stmt.setString(3, ticket.getCustomerName());
            stmt.setInt(4, ticket.getQuantity());
            stmt.setString(5, ticket.getAgeCategory());
            stmt.setTimestamp(6, ticket.getVisitDate() != null ? Timestamp.valueOf(ticket.getVisitDate()) : null);
            stmt.setDouble(7, ticket.getPrice());
            stmt.setString(8, ticket.getStatus());
            stmt.setInt(9, ticket.getTicketId());
            stmt.executeUpdate();
        }
    }

    public void deleteTicket(int ticketId) throws SQLException {
        String query = "DELETE FROM Tickets WHERE ticket_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, ticketId);
            stmt.executeUpdate();
        }
    }

    private Ticket mapToTicket(ResultSet rs) throws SQLException {
        Timestamp visitDate = rs.getTimestamp("visit_date");
        return new Ticket(
                rs.getInt("ticket_id"),
                rs.getInt("user_id"),
                rs.getInt("tour_id"),
                rs.getString("customer_name") != null ? rs.getString("customer_name") : "",
                rs.getInt("quantity"),
                rs.getString("age_category") != null ? rs.getString("age_category") : "",
                visitDate != null ? visitDate.toLocalDateTime() : null,
                rs.getDouble("price"),
                rs.getString("status") != null ? rs.getString("status") : ""
        );
    }

    // Thongke DAO methods
    public List<Thongke> getAllThongke(int page, int pageSize) throws SQLException {
        List<Thongke> thongkes = new ArrayList<>();
        String query = "SELECT * FROM Thongke ORDER BY stat_id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, (page - 1) * pageSize);
            stmt.setInt(2, pageSize);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    thongkes.add(mapToThongke(rs));
                }
            }
        }
        return thongkes;
    }

    public void createThongke(Thongke thongke) throws SQLException {
        String query = "INSERT INTO Thongke (date, exhibition_id, tour_id, artwork_count, avg_duration, exhibition_count, tour_count, artist_count, ticket_count, revenue) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDate(1, thongke.getDate() != null ? java.sql.Date.valueOf(thongke.getDate()) : null);
            stmt.setObject(2, thongke.getExhibitionId(), Types.INTEGER);
            stmt.setObject(3, thongke.getTourId(), Types.INTEGER);
            stmt.setInt(4, thongke.getArtworkCount());
            stmt.setInt(5, thongke.getAvgDuration());
            stmt.setInt(6, thongke.getExhibitionCount());
            stmt.setInt(7, thongke.getTourCount());
            stmt.setInt(8, thongke.getArtistCount());
            stmt.setInt(9, thongke.getTicketCount());
            stmt.setDouble(10, thongke.getRevenue());
            stmt.executeUpdate();
        }
    }

    public void updateThongke(Thongke thongke) throws SQLException {
        String query = "UPDATE Thongke SET date = ?, exhibition_id = ?, tour_id = ?, artwork_count = ?, avg_duration = ?, exhibition_count = ?, tour_count = ?, artist_count = ?, ticket_count = ?, revenue = ? WHERE stat_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDate(1, thongke.getDate() != null ? java.sql.Date.valueOf(thongke.getDate()) : null);
            stmt.setObject(2, thongke.getExhibitionId(), Types.INTEGER);
            stmt.setObject(3, thongke.getTourId(), Types.INTEGER);
            stmt.setInt(4, thongke.getArtworkCount());
            stmt.setInt(5, thongke.getAvgDuration());
            stmt.setInt(6, thongke.getExhibitionCount());
            stmt.setInt(7, thongke.getTourCount());
            stmt.setInt(8, thongke.getArtistCount());
            stmt.setInt(9, thongke.getTicketCount());
            stmt.setDouble(10, thongke.getRevenue());
            stmt.setInt(11, thongke.getStatId());
            stmt.executeUpdate();
        }
    }

    public void deleteThongke(int statId) throws SQLException {
        String query = "DELETE FROM Thongke WHERE stat_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, statId);
            stmt.executeUpdate();
        }
    }

    public List<Thongke> findThongkeByDateRange(LocalDate startDate, LocalDate endDate) throws SQLException {
        List<Thongke> thongkes = new ArrayList<>();
        String query = "SELECT * FROM Thongke WHERE date BETWEEN ? AND ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDate(1, java.sql.Date.valueOf(startDate));
            stmt.setDate(2, java.sql.Date.valueOf(endDate));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    thongkes.add(mapToThongke(rs));
                }
            }
        }
        return thongkes;
    }

    public int getTicketCountByTourId(int tourId) throws SQLException {
        String query = "SELECT COUNT(*) FROM Tickets WHERE tour_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, tourId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public int getTicketCountByArtworkId(int artworkId) throws SQLException {
        String query = "SELECT COUNT(*) FROM Tickets t JOIN ArtworkTours at ON t.tour_id = at.tour_id WHERE at.artwork_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, artworkId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public List<Object[]> getRevenueByDateRange(LocalDate startDate, LocalDate endDate) throws SQLException {
        List<Object[]> revenueData = new ArrayList<>();
        String query = "SELECT CAST(visit_date AS DATE) as visit_date, SUM(price * quantity) as total_revenue " +
                "FROM Tickets WHERE visit_date BETWEEN ? AND ? GROUP BY CAST(visit_date AS DATE)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDate(1, java.sql.Date.valueOf(startDate));
            stmt.setDate(2, java.sql.Date.valueOf(endDate));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    revenueData.add(new Object[]{rs.getDate("visit_date").toLocalDate(), rs.getDouble("total_revenue")});
                }
            }
        }
        return revenueData;
    }

    private Thongke mapToThongke(ResultSet rs) throws SQLException {
        java.sql.Date date = rs.getDate("date");
        return new Thongke(
                rs.getInt("stat_id"),
                date != null ? date.toLocalDate() : null,
                rs.getObject("exhibition_id") != null ? rs.getInt("exhibition_id") : null,
                rs.getObject("tour_id") != null ? rs.getInt("tour_id") : null,
                rs.getInt("artwork_count"),
                rs.getInt("avg_duration"),
                rs.getInt("exhibition_count"),
                rs.getInt("tour_count"),
                rs.getInt("artist_count"),
                rs.getInt("ticket_count"),
                rs.getDouble("revenue")
        );
    }

    // ActivityLog DAO methods
    public List<ActivityLog> getAllActivityLogs(int page, int pageSize) throws SQLException {
        List<ActivityLog> logs = new ArrayList<>();
        String query = "SELECT * FROM ActivityLogs ORDER BY log_id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, (page - 1) * pageSize);
            stmt.setInt(2, pageSize);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    logs.add(mapToActivityLog(rs));
                }
            }
        }
        return logs;
    }

    public void createActivityLog(ActivityLog log) throws SQLException {
        String query = "INSERT INTO ActivityLogs (user_id, action, timestamp) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, log.getUserId());
            stmt.setString(2, log.getAction());
            stmt.setTimestamp(3, Timestamp.valueOf(log.getTimestamp()));
            stmt.executeUpdate();
        }
    }

    public void deleteActivityLog(int logId) throws SQLException {
        String query = "DELETE FROM ActivityLogs WHERE log_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, logId);
            stmt.executeUpdate();
        }
    }

    private ActivityLog mapToActivityLog(ResultSet rs) throws SQLException {
        Timestamp timestamp = rs.getTimestamp("timestamp");
        return new ActivityLog(
                rs.getInt("log_id"),
                rs.getInt("user_id"),
                rs.getString("action") != null ? rs.getString("action") : "",
                timestamp != null ? timestamp.toLocalDateTime() : null
        );
    }

    // Notification DAO methods
    public List<Notification> getAllNotifications(int page, int pageSize) throws SQLException {
        List<Notification> notifications = new ArrayList<>();
        String query = "SELECT * FROM Notifications ORDER BY notification_id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, (page - 1) * pageSize);
            stmt.setInt(2, pageSize);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    notifications.add(mapToNotification(rs));
                }
            }
        }
        return notifications;
    }

    public Notification findNotificationById(int notificationId) throws SQLException {
        String query = "SELECT * FROM Notifications WHERE notification_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, notificationId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapToNotification(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in findNotificationById for notification_id " + notificationId + ": " + e.getMessage());
            throw e;
        }
        return null;
    }

    public void createNotification(Notification notification) throws SQLException {
        String query = "INSERT INTO Notifications (user_id, message, created_at, is_read) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, notification.getUserId());
            stmt.setString(2, notification.getMessage());
            stmt.setTimestamp(3, Timestamp.valueOf(notification.getCreatedAt()));
            stmt.setBoolean(4, notification.isRead());
            stmt.executeUpdate();
        }
    }

    public void updateNotification(Notification notification) throws SQLException {
        String query = "UPDATE Notifications SET user_id = ?, message = ?, created_at = ?, is_read = ? WHERE notification_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, notification.getUserId());
            stmt.setString(2, notification.getMessage());
            stmt.setTimestamp(3, Timestamp.valueOf(notification.getCreatedAt()));
            stmt.setBoolean(4, notification.isRead());
            stmt.setInt(5, notification.getNotificationId());
            stmt.executeUpdate();
        }
    }

    public void deleteNotification(int notificationId) throws SQLException {
        String query = "DELETE FROM Notifications WHERE notification_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, notificationId);
            stmt.executeUpdate();
        }
    }

    private Notification mapToNotification(ResultSet rs) throws SQLException {
        Timestamp createdAt = rs.getTimestamp("created_at");
        return new Notification(
                rs.getInt("notification_id"),
                rs.getInt("user_id"),
                rs.getString("message") != null ? rs.getString("message") : "",
                createdAt != null ? createdAt.toLocalDateTime() : null,
                rs.getBoolean("is_read")
        );
    }
}