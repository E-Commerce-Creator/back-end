package com.e_commerce_creator.common.generator.email;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailGenerator {
    final JavaMailSender mailSender;
    final Environment environment;
    @Value("${spring.mail.username}")
    String username;

    public void sendSimpleMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(username);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

    public void sendHtmlMail(String to, String subject, String htmlContent) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(username);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("Failed to send HTML email :: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendMailWithAttachment(String to, String subject, String text, String attachmentPath) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(username);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

//            FileSystemResource file = new FileSystemResource(System.getProperty("user.home") + "/Documents/" + attachmentPath);
            FileSystemResource file = new FileSystemResource(attachmentPath);
            helper.addAttachment(file.getFilename(), file);

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("Failed to send email with attachment", e);
            e.printStackTrace();
        }
    }


    public void sendMailWithEmbeddedImages(String to, String subject, String text, String attachmentPath) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(username);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            FileSystemResource file = new FileSystemResource(attachmentPath);
            helper.addInline("<" + file.getFilename() + ">", file);

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("Failed to send email with attachment", e);
            e.printStackTrace();
        }
    }
}
