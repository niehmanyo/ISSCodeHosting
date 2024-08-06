package com.iss.auth.service.impl;

import com.iss.auth.service.impl.MailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.util.ReflectionTestUtils;

import javax.mail.internet.MimeMessage;

import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MailServiceImplTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private MailServiceImpl mailService;

    private final String mailSender = "your_email@example.com";

    @BeforeEach
    public void setUp() {
        // Inject javaMailSender into mailService using ReflectionTestUtils
        ReflectionTestUtils.setField(mailService, "javaMailSender", javaMailSender);
        // Set the mailSender field in mailService using ReflectionTestUtils
        ReflectionTestUtils.setField(mailService, "MailSender", mailSender);
    }

    @Test
    public void testSendSimpleMail() {
        // Create a SimpleMailMessage
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailSender);
        message.setTo("test@example.com");
        message.setSubject("Test Subject");
        message.setText("Test Text");

        // Call sendSimpleMail on mailService
        mailService.sendSimpleMail("test@example.com", "Test Subject", "Test Text");

        // Verify that javaMailSender.send was called exactly once with message
        verify(javaMailSender, times(1)).send(message);
    }

    @Test
    public void testSendHtmlMail() throws Exception {
        // Create a MimeMessage
        MimeMessage mimeMessage = new MimeMessage((javax.mail.Session) null);
        // Stub javaMailSender.createMimeMessage() to return mimeMessage
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(mailSender);
        helper.setTo("test@example.com");
        helper.setSubject("Test Subject");
        helper.setText("<h1>Test</h1>", true);

        // Call sendHtmlMail on mailService
        mailService.sendHtmlMail("test@example.com", "Test Subject", "<h1>Test</h1>");

        // Verify that javaMailSender.send was called exactly once with mimeMessage
        verify(javaMailSender, times(1)).send(mimeMessage);
    }

    @Test
    public void testSendAppendixMail() throws Exception {
        // Create a MimeMessage
        MimeMessage mimeMessage = new MimeMessage((javax.mail.Session) null);
        // Stub javaMailSender.createMimeMessage() to return mimeMessage
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(mailSender);
        helper.setTo("test@example.com");
        helper.setSubject("Test Subject");
        helper.setText("Test Text", true);

        // Call sendAppendixMail on mailService
        mailService.sendAppendixMail("test@example.com", "Test Subject", "Test Text", Collections.emptyList());

        // Verify that javaMailSender.send was called exactly once with mimeMessage
        verify(javaMailSender, times(1)).send(mimeMessage);
    }
}