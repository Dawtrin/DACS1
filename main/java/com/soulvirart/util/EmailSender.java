package com.soulvirart.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSender {
    private static final String FROM_EMAIL = "dattrandn@gmail.com"; // Thay bằng email của bạn
    private static final String PASSWORD = "bsli mbyu lcxx qnib"; // Thay bằng app password của bạn

    public static void sendEmail(String toEmail, String subject, String body) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        properties.put("mail.debug", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("Email sent successfully to " + toEmail);
        } catch (MessagingException e) {
            System.err.println("Failed to send email to " + toEmail);
            throw e;
        }
    }

    public static void sendVerificationEmail(String toEmail, String username) throws MessagingException {
        if (toEmail == null || toEmail.isEmpty() || username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Email or username cannot be null or empty.");
        }

        String subject = "SoulVirArt - Verify Your Account";
        String body = "Dear " + username + ",\n\n"
                + "Thank you for registering with SoulVirArt. Please click the link below to verify your account:\n\n"
                + "http://localhost:8080/verify?email=" + toEmail + "&username=" + username + "\n\n"
                + "If you did not create an account, please ignore this email.\n\n"
                + "Best regards,\nSoulVirArt Team";

        sendEmail(toEmail, subject, body);
    }



    public static void sendPasswordResetEmail(String toEmail, String newPassword) throws MessagingException {
        String subject = "SoulVirArt - Password Reset";
        String body = "Dear User,\n\n"
                + "Your password has been reset. Here is your new password:\n\n"
                + newPassword + "\n\n"
                + "Please log in and change your password immediately for security reasons.\n\n"
                + "Best regards,\nSoulVirArt Team";

        sendEmail(toEmail, subject, body);
    }
}