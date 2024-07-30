package com.iss.auth.service.impl;

import com.iss.auth.service.IMailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;

@Service
@Slf4j
public class MailServiceImpl implements IMailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String MailSender;

    /**
     * Send simple text email.
     *
     * @param mailRecipient Email recipient
     * @param subject       Email subject
     * @param messageText   Email text content
     */
    @Override
    public void sendSimpleMail(String mailRecipient, String subject, String messageText) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(subject);
        message.setFrom(MailSender);
        message.setTo(mailRecipient);
        message.setText(messageText);

        try {
            javaMailSender.send(message);
            log.info("Text Email Sent Successfully");
        } catch (MailException e) {
            e.printStackTrace();
            throw new RuntimeException("Email Sending Error");
        }
    }

    /**
     * Send HTML format email.
     *
     * @param mailRecipient Email recipient
     * @param subject       Email subject
     * @param htmlText      HTML formatted email content
     */
    @Override
    public void sendHtmlMail(String mailRecipient, String subject, String htmlText) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(MailSender);
            mimeMessageHelper.setTo(mailRecipient);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(htmlText, true);

            javaMailSender.send(mimeMessage);
            log.info("HTML Format Email Sent Successfully");

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Email Sending Error");
        }
    }

    /**
     * Send email with attachments.
     *
     * @param mailRecipient Email recipient
     * @param subject       Email subject
     * @param messageText   Email text content
     * @param filePathList  List of file paths to attach
     */
    @Override
    public void sendAppendixMail(String mailRecipient, String subject, String messageText, List<String> filePathList) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(MailSender);
            mimeMessageHelper.setTo(mailRecipient);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(messageText, true);

            for (String filePath : filePathList) {
                FileSystemResource file = new FileSystemResource(new File(filePath));
                String fileName = file.getFilename();
                log.info("Attachment Name: " + fileName);
                mimeMessageHelper.addAttachment(fileName, file);
            }

            javaMailSender.send(mimeMessage);
            log.info("Email with Attachments Sent Successfully");

        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Email Sending Error");
        }
    }
}
