package com.soulvirart.controller;

import com.soulvirart.database.DatabaseDAO;
import com.soulvirart.model.Ticket;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class TicketController {
    private final DatabaseDAO databaseDAO;

    public TicketController() {
        this.databaseDAO = new DatabaseDAO();
    }

    public List<Ticket> getAllTickets(int page, int pageSize) throws Exception {
        if (page < 1 || pageSize < 1) {
            throw new IllegalArgumentException("Page and pageSize must be positive integers");
        }
        try {
            return databaseDAO.getAllTickets(page, pageSize);
        } catch (SQLException e) {
            throw new Exception("Failed to retrieve tickets: " + e.getMessage(), e);
        }
    }

    public List<Ticket> searchTickets(int searchId) throws Exception {
        if (searchId <= 0) {
            throw new IllegalArgumentException("Invalid search ID");
        }
        try {
            return databaseDAO.searchTickets(searchId);
        } catch (SQLException e) {
            throw new Exception("Failed to search tickets: " + e.getMessage(), e);
        }
    }

    public Ticket findTicketById(int ticketId) throws Exception {
        if (ticketId <= 0) {
            throw new IllegalArgumentException("Invalid ticket ID");
        }
        try {
            Ticket ticket = databaseDAO.findTicketById(ticketId);
            if (ticket == null) {
                throw new Exception("Ticket with ID " + ticketId + " not found");
            }
            return ticket;
        } catch (SQLException e) {
            throw new Exception("Failed to find ticket with ID " + ticketId + ": " + e.getMessage(), e);
        }
    }

    public void createTicket(int userId, int tourId, String customerName, int quantity, String ageCategory, LocalDateTime visitDate, double price, String status) throws Exception {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        if (tourId <= 0) {
            throw new IllegalArgumentException("Invalid tour ID");
        }
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be null or empty");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be a positive integer");
        }
        if (ageCategory == null || ageCategory.trim().isEmpty()) {
            throw new IllegalArgumentException("Age category cannot be null or empty");
        }
        if (visitDate == null) {
            throw new IllegalArgumentException("Visit date cannot be null");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty");
        }

        Ticket ticket = new Ticket();
        ticket.setUserId(userId);
        ticket.setTourId(tourId);
        ticket.setCustomerName(customerName.trim());
        ticket.setQuantity(quantity);
        ticket.setAgeCategory(ageCategory.trim());
        ticket.setVisitDate(visitDate);
        ticket.setPrice(price);
        ticket.setStatus(status.trim());

        try {
            databaseDAO.createTicket(ticket);
        } catch (SQLException e) {
            throw new Exception("Failed to create ticket: " + e.getMessage(), e);
        }
    }

    public void updateTicket(Ticket ticket) throws Exception {
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket cannot be null");
        }
        if (ticket.getTicketId() <= 0) {
            throw new IllegalArgumentException("Invalid ticket ID");
        }
        if (ticket.getUserId() <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        if (ticket.getTourId() <= 0) {
            throw new IllegalArgumentException("Invalid tour ID");
        }
        if (ticket.getCustomerName() == null || ticket.getCustomerName().trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be null or empty");
        }
        if (ticket.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be a positive integer");
        }
        if (ticket.getAgeCategory() == null || ticket.getAgeCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("Age category cannot be null or empty");
        }
        if (ticket.getVisitDate() == null) {
            throw new IllegalArgumentException("Visit date cannot be null");
        }
        if (ticket.getPrice() < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        if (ticket.getStatus() == null || ticket.getStatus().trim().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty");
        }

        try {
            databaseDAO.updateTicket(ticket);
        } catch (SQLException e) {
            throw new Exception("Failed to update ticket with ID " + ticket.getTicketId() + ": " + e.getMessage(), e);
        }
    }

    public void deleteTicket(int ticketId) throws Exception {
        if (ticketId <= 0) {
            throw new IllegalArgumentException("Invalid ticket ID");
        }
        try {
            databaseDAO.deleteTicket(ticketId);
        } catch (SQLException e) {
            throw new Exception("Failed to delete ticket with ID " + ticketId + ": " + e.getMessage(), e);
        }
    }

    public void createTicket(Ticket ticket) {
    }
}