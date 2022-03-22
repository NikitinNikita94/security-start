package com.example.securitystart.email;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class EmailService implements EmailSender {

    JavaMailSender mailSender;

    @Override
    @Async
    public void send(final String to, final String email) {
        try {
            MimeMessage mineMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mineMessage,"utf-8");
            helper.setText(email,true);
            helper.setTo(to);
            helper.setSubject("Confirm your email");
            helper.setFrom("7jay94@mail.ru");
            mailSender.send(mineMessage);
        } catch (MessagingException e) {
            log.error("failed to send email",e);
            throw new IllegalStateException("failed to send email");
        }
    }
}
