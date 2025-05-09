package com.soulvirart.controller;

import com.soulvirart.database.DatabaseDAO;
import com.soulvirart.model.Artwork;
import com.soulvirart.model.Thongke;
import com.soulvirart.model.Ticket;
import com.soulvirart.model.VirtualTour;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ThongkeController {
    private final DatabaseDAO databaseDAO;

    public ThongkeController() {
        this.databaseDAO = new DatabaseDAO();
    }

    public List<Thongke> getAllThongke(int page, int pageSize) throws Exception {
        if (page < 1 || pageSize < 1) {
            throw new IllegalArgumentException("Page and pageSize must be positive integers");
        }
        try {
            return databaseDAO.getAllThongke(page, pageSize);
        } catch (SQLException e) {
            throw new Exception("Failed to retrieve statistics: " + e.getMessage(), e);
        }
    }

    public void createThongke(LocalDate date, Integer exhibitionId, Integer tourId, int artworkCount, int avgDuration, int exhibitionCount, int tourCount, int artistCount, int ticketCount, double revenue) throws Exception {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        if (exhibitionId != null && exhibitionId <= 0) {
            throw new IllegalArgumentException("Invalid exhibition ID");
        }
        if (tourId != null && tourId <= 0) {
            throw new IllegalArgumentException("Invalid tour ID");
        }
        if (artworkCount < 0 || avgDuration < 0 || exhibitionCount < 0 || tourCount < 0 || artistCount < 0 || ticketCount < 0) {
            throw new IllegalArgumentException("Count values cannot be negative");
        }
        if (revenue < 0) {
            throw new IllegalArgumentException("Revenue cannot be negative");
        }

        Thongke thongke = new Thongke();
        thongke.setDate(date);
        thongke.setExhibitionId(exhibitionId);
        thongke.setTourId(tourId);
        thongke.setArtworkCount(artworkCount);
        thongke.setAvgDuration(avgDuration);
        thongke.setExhibitionCount(exhibitionCount);
        thongke.setTourCount(tourCount);
        thongke.setArtistCount(artistCount);
        thongke.setTicketCount(ticketCount);
        thongke.setRevenue(revenue);

        try {
            databaseDAO.createThongke(thongke);
        } catch (SQLException e) {
            throw new Exception("Failed to create statistic: " + e.getMessage(), e);
        }
    }

    public void updateThongke(Thongke thongke) throws Exception {
        if (thongke == null) {
            throw new IllegalArgumentException("Statistic cannot be null");
        }
        if (thongke.getStatId() <= 0) {
            throw new IllegalArgumentException("Invalid statistic ID");
        }
        if (thongke.getDate() == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        if (thongke.getExhibitionId() != null && thongke.getExhibitionId() <= 0) {
            throw new IllegalArgumentException("Invalid exhibition ID");
        }
        if (thongke.getTourId() != null && thongke.getTourId() <= 0) {
            throw new IllegalArgumentException("Invalid tour ID");
        }
        if (thongke.getArtworkCount() < 0 || thongke.getAvgDuration() < 0 || thongke.getExhibitionCount() < 0 || thongke.getTourCount() < 0 || thongke.getArtistCount() < 0 || thongke.getTicketCount() < 0) {
            throw new IllegalArgumentException("Count values cannot be negative");
        }
        if (thongke.getRevenue() < 0) {
            throw new IllegalArgumentException("Revenue cannot be negative");
        }

        try {
            databaseDAO.updateThongke(thongke);
        } catch (SQLException e) {
            throw new Exception("Failed to update statistic with ID " + thongke.getStatId() + ": " + e.getMessage(), e);
        }
    }

    public void deleteThongke(int statId) throws Exception {
        if (statId <= 0) {
            throw new IllegalArgumentException("Invalid statistic ID");
        }
        try {
            databaseDAO.deleteThongke(statId);
        } catch (SQLException e) {
            throw new Exception("Failed to delete statistic with ID " + statId + ": " + e.getMessage(), e);
        }
    }

    public List<Thongke> findThongkeByDateRange(LocalDate startDate, LocalDate endDate) throws Exception {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        try {
            return databaseDAO.findThongkeByDateRange(startDate, endDate);
        } catch (SQLException e) {
            throw new Exception("Failed to retrieve statistics by date range: " + e.getMessage(), e);
        }
    }

    public List<Ticket> getAllTickets() throws Exception {
        try {
            return databaseDAO.getAllTickets(1, Integer.MAX_VALUE);
        } catch (SQLException e) {
            throw new Exception("Failed to retrieve all tickets: " + e.getMessage(), e);
        }
    }

    public VirtualTour getMostPurchasedTour(List<VirtualTour> tours) throws Exception {
        if (tours == null) {
            throw new IllegalArgumentException("Tour list cannot be null");
        }
        try {
            VirtualTour mostPurchased = null;
            int maxTickets = 0;
            for (VirtualTour tour : tours) {
                int ticketCount = databaseDAO.getTicketCountByTourId(tour.getTourId());
                if (ticketCount > maxTickets) {
                    maxTickets = ticketCount;
                    mostPurchased = tour;
                }
            }
            return mostPurchased;
        } catch (SQLException e) {
            throw new Exception("Failed to determine most purchased tour: " + e.getMessage(), e);
        }
    }

    public Artwork getMostPurchasedArtwork(List<Artwork> artworks) throws Exception {
        if (artworks == null) {
            throw new IllegalArgumentException("Artwork list cannot be null");
        }
        try {
            Artwork mostPurchased = null;
            int maxTickets = 0;
            for (Artwork artwork : artworks) {
                int ticketCount = databaseDAO.getTicketCountByArtworkId(artwork.getArtworkId());
                if (ticketCount > maxTickets) {
                    maxTickets = ticketCount;
                    mostPurchased = artwork;
                }
            }
            return mostPurchased;
        } catch (SQLException e) {
            throw new Exception("Failed to determine most purchased artwork: " + e.getMessage(), e);
        }
    }

    public List<Object[]> getRevenueData(LocalDate startDate, LocalDate endDate) throws Exception {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        try {
            return databaseDAO.getRevenueByDateRange(startDate, endDate);
        } catch (SQLException e) {
            throw new Exception("Failed to retrieve revenue data: " + e.getMessage(), e);
        }
    }
}