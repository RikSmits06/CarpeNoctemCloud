package org.carpenoctemcloud.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendConfirmationEmail(String to, String token) {
        sendSimpleMail(to, "Email Confirmation CNCloud",
                       "Thank you for registering at Carpe Noctem Cloud!\nPlease enter this token in the website to confirm your account: " +
                               token + "\n\n With kind regards,\n" + "Carpe Noctem");
    }

    private void sendSimpleMail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
