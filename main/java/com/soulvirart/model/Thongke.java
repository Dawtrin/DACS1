package com.soulvirart.model;

import java.time.LocalDate;

public class Thongke {
    private int statId;
    private LocalDate date;
    private Integer exhibitionId;
    private Integer tourId;
    private int artworkCount;
    private int avgDuration;
    private int exhibitionCount;
    private int tourCount;
    private int artistCount;
    private int ticketCount;
    private double revenue;

    public Thongke() {}

    public Thongke(int statId, LocalDate date, Integer exhibitionId, Integer tourId, int artworkCount, int avgDuration, int exhibitionCount, int tourCount, int artistCount, int ticketCount, double revenue) {
        this.statId = statId;
        this.date = date;
        this.exhibitionId = exhibitionId;
        this.tourId = tourId;
        this.artworkCount = artworkCount;
        this.avgDuration = avgDuration;
        this.exhibitionCount = exhibitionCount;
        this.tourCount = tourCount;
        this.artistCount = artistCount;
        this.ticketCount = ticketCount;
        this.revenue = revenue;
    }

    // Getters and Setters
    public int getStatId() { return statId; }
    public void setStatId(int statId) { this.statId = statId; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Integer getExhibitionId() { return exhibitionId; }
    public void setExhibitionId(Integer exhibitionId) { this.exhibitionId = exhibitionId; }

    public Integer getTourId() { return tourId; }
    public void setTourId(Integer tourId) { this.tourId = tourId; }

    public int getArtworkCount() { return artworkCount; }
    public void setArtworkCount(int artworkCount) { this.artworkCount = artworkCount; }

    public int getAvgDuration() { return avgDuration; }
    public void setAvgDuration(int avgDuration) { this.avgDuration = avgDuration; }

    public int getExhibitionCount() { return exhibitionCount; }
    public void setExhibitionCount(int exhibitionCount) { this.exhibitionCount = exhibitionCount; }

    public int getTourCount() { return tourCount; }
    public void setTourCount(int tourCount) { this.tourCount = tourCount; }

    public int getArtistCount() { return artistCount; }
    public void setArtistCount(int artistCount) { this.artistCount = artistCount; }

    public int getTicketCount() { return ticketCount; }
    public void setTicketCount(int ticketCount) { this.ticketCount = ticketCount; }

    public double getRevenue() { return revenue; }
    public void setRevenue(double revenue) { this.revenue = revenue; }
}