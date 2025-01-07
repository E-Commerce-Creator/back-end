package com.e_commerce_creator.common.generator.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailGenerator {
    final JavaMailSender mailSender;

    @Value("${spring.mail.gmail.host}")
    private final String gmailHost;
    @Value("${spring.mail.gmail.port}")
    private int gmailPort;
    @Value("${spring.mail.gmail.username}")
    private final String gmailUsername;
    @Value("${spring.mail.gmail.password}")
    private final String gmailPassword;

    @Value("${spring.mail.outlook.host}")
    private final String outlookHost;
    @Value("${spring.mail.outlook.port}")
    private int outlookPort;
    @Value("${spring.mail.outlook.username}")
    private final String outlookUsername;
    @Value("${spring.mail.outlook.password}")
    private final String outlookPassword;

    @Value("${spring.mail.yahoo.host}")
    private final String yahooHost;
    @Value("${spring.mail.yahoo.port}")
    private int yahooPort;
    @Value("${spring.mail.yahoo.username}")
    private final String yahooUsername;
    @Value("${spring.mail.yahoo.password}")
    private final String yahooPassword;

    public void sendBasicEmail(String to, String subject, String text, String provider) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        switch (provider.toLowerCase()) {
            case "gmail":
                helper.setFrom(gmailUsername);
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(text);
                // Add any additional Gmail-specific configuration if needed

                break;
            case "outlook":
                helper.setFrom(outlookUsername);
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(text);
                // Add any additional Outlook-specific configuration if needed
                break;
            case "yahoo":
                helper.setFrom(yahooUsername);
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(text);
                // Add any additional Yahoo-specific configuration if needed
                break;
            default:
                throw new MessagingException("Unsupported email provider: " + provider);
        }
        try {
            mailSender.send(message);
            System.out.println("Email sent successfully!");
        } catch (MailException e) {
            e.printStackTrace();
            throw new MessagingException("Error sending email: " + e.getMessage());
        }
    }
}
