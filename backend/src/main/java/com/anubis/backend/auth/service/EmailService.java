package com.anubis.backend.auth.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String username, String email, String code) {
        String subject = "Verify your email address";
        String htmlContent =
                "<div style=\"font-family: 'Segoe UI', Arial, sans-serif; background: #f9f9f9; padding: 32px;\">" +
                        "<div style=\"max-width: 480px; margin: auto; background: #fff; border-radius: 8px; box-shadow: 0 2px 8px #0001; padding: 32px;\">" +
                        "<h2 style=\"color: #222; margin-bottom: 16px;\">Welcome to <span style=\"color: #4F46E5;\">Anubis</span>!</h2>" +
                        "<p style=\"font-size: 16px; color: #444;\">Hi <b>" + escapeHtml(username) + "</b>,</p>" +
                        "<p style=\"font-size: 15px; color: #444;\">Thank you for signing up. Please use the code below to verify your email address:</p>" +
                        "<div style=\"margin: 24px 0; text-align: center;\">" +
                        "<span style=\"display: inline-block; font-size: 28px; letter-spacing: 6px; background: #f3f4f6; color: #4F46E5; padding: 12px 32px; border-radius: 6px; font-weight: bold;\">" +
                        escapeHtml(code) +
                        "</span>" +
                        "</div>" +
                        "<p style=\"font-size: 14px; color: #888;\">If you did not request this, you can safely ignore this email.</p>" +
                        "<p style=\"margin-top: 32px; font-size: 14px; color: #222;\">Best regards,<br>The Anubis Team</p>" +
                        "</div>" +
                        "</div>";

        sendHtmlEmail(email, subject, htmlContent);
    }

    public void sendForgotPasswordEmail(String email, String code) {
        String subject = "Reset your Anubis password";
        String htmlContent =
                "<div style=\"font-family: 'Segoe UI', Arial, sans-serif; background: #f9f9f9; padding: 32px;\">" +
                        "<div style=\"max-width: 480px; margin: auto; background: #fff; border-radius: 8px; box-shadow: 0 2px 8px #0001; padding: 32px;\">" +
                        "<h2 style=\"color: #222; margin-bottom: 16px;\">Password Reset Request</h2>" +
                        "<p style=\"font-size: 16px; color: #444;\">Hi,</p>" +
                        "<p style=\"font-size: 15px; color: #444;\">We received a request to reset your Anubis account password. Use the code below to proceed:</p>" +
                        "<div style=\"margin: 24px 0; text-align: center;\">" +
                        "<span style=\"display: inline-block; font-size: 28px; letter-spacing: 6px; background: #f3f4f6; color: #4F46E5; padding: 12px 32px; border-radius: 6px; font-weight: bold;\">" +
                        escapeHtml(code) +
                        "</span>" +
                        "</div>" +
                        "<p style=\"font-size: 14px; color: #888;\">If you did not request a password reset, you can safely ignore this email.</p>" +
                        "<p style=\"margin-top: 32px; font-size: 14px; color: #222;\">Best regards,<br>The Anubis Team</p>" +
                        "</div>" +
                        "</div>";

        sendHtmlEmail(email, subject, htmlContent);
    }

    private void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    private String escapeHtml(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
