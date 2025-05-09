package com.soulvirart.model;

import java.time.LocalDateTime;

public class Ticket {
    private int ticketId;
    private int userId;
    private int tourId;
    private String customerName;
    private int quantity;
    private String ageCategory;
    private LocalDateTime visitDate;
    private double price;
    private String status;

    public Ticket() {}

    public Ticket(int ticketId, int userId, int tourId, String customerName, int quantity, String ageCategory, LocalDateTime visitDate, double price, String status) {
        this.ticketId = ticketId;
        this.userId = userId;
        this.tourId = tourId;
        this.customerName = customerName;
        this.quantity = quantity;
        this.ageCategory = ageCategory;
        this.visitDate = visitDate;
        this.price = price;
        this.status = status;
    }

    // Getters and Setters
    public int getTicketId() { return ticketId; }
    public void setTicketId(int ticketId) { this.ticketId = ticketId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getTourId() { return tourId; }
    public void setTourId(int tourId) { this.tourId = tourId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getAgeCategory() { return ageCategory; }
    public void setAgeCategory(String ageCategory) { this.ageCategory = ageCategory; }

    public LocalDateTime getVisitDate() { return visitDate; }
    public void setVisitDate(LocalDateTime visitDate) { this.visitDate = visitDate; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}