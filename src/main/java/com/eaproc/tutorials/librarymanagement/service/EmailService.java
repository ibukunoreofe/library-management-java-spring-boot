package com.eaproc.tutorials.librarymanagement.service;

import com.eaproc.tutorials.librarymanagement.mail.IMailable;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, IMailable mailable) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(mailable.getSubject());
        helper.setFrom("admin@scadware.com");

        helper.setText(mailable.toString(), true);

        mailSender.send(message);
    }
}
