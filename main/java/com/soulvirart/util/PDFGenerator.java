package com.soulvirart.util;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;

public class PDFGenerator {
    private static final Logger logger = LoggerFactory.getLogger(PDFGenerator.class);

    public static void generateTicketPDF(String filePath, String ticketDetails) {
        // Kiểm tra đầu vào
        if (filePath == null || filePath.trim().isEmpty()) {
            logger.error("File path cannot be null or empty");
            throw new IllegalArgumentException("File path cannot be null or empty");
        }
        if (ticketDetails == null) {
            logger.error("Ticket details cannot be null");
            throw new IllegalArgumentException("Ticket details cannot be null");
        }

        // Kiểm tra và tạo thư mục đích
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (!created) {
                logger.error("Failed to create directory: {}", parentDir.getAbsolutePath());
                throw new RuntimeException("Failed to create directory: " + parentDir.getAbsolutePath());
            }
        }

        try {
            PdfWriter writer = new PdfWriter(file);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Tiêu đề
            Paragraph title = new Paragraph("SoulVirArt - Ticket")
                    .setFontSize(18)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            // Chi tiết vé
            Paragraph details = new Paragraph(ticketDetails)
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setMarginTop(20);
            document.add(details);

            document.close();
            logger.info("PDF created successfully at: {}", filePath);
        } catch (FileNotFoundException e) {
            logger.error("Failed to create PDF at {}: {}", filePath, e.getMessage(), e);
            throw new RuntimeException("Failed to create PDF: " + e.getMessage(), e);
        }
    }

    public static void generateReportPDF(String filePath, String reportDetails) {
        // Kiểm tra đầu vào
        if (filePath == null || filePath.trim().isEmpty()) {
            logger.error("File path cannot be null or empty");
            throw new IllegalArgumentException("File path cannot be null or empty");
        }
        if (reportDetails == null) {
            logger.error("Report details cannot be null");
            throw new IllegalArgumentException("Report details cannot be null");
        }

        // Kiểm tra và tạo thư mục đích
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (!created) {
                logger.error("Failed to create directory: {}", parentDir.getAbsolutePath());
                throw new RuntimeException("Failed to create directory: " + parentDir.getAbsolutePath());
            }
        }

        try {
            PdfWriter writer = new PdfWriter(file);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Tiêu đề
            Paragraph title = new Paragraph("SoulVirArt - Report")
                    .setFontSize(18)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            // Chi tiết báo cáo
            Paragraph details = new Paragraph(reportDetails)
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setMarginTop(20);
            document.add(details);

            document.close();
            logger.info("PDF created successfully at: {}", filePath);
        } catch (FileNotFoundException e) {
            logger.error("Failed to create PDF at {}: {}", filePath, e.getMessage(), e);
            throw new RuntimeException("Failed to create PDF: " + e.getMessage(), e);
        }
    }
}