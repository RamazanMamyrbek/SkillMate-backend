package ru.skillmate.backend.services.mail.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import ru.skillmate.backend.entities.ads.Ad;
import ru.skillmate.backend.entities.ads.ExchangeRequest;
import ru.skillmate.backend.entities.users.Users;
import ru.skillmate.backend.services.mail.EmailService;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    @Async
    public void sendConfirmationCode(String email, String code) {
        try {
            String subject = "Confirm registration";
            sendHtmlConfirmationCode(subject,email, code);
        }catch (Exception e) {
            handleMailException(e);
        }
    }

    @Override
    @Async
    public void sendExchangeRequestNotification(Users requester, Ad ad, ExchangeRequest exchangeRequest) {
        try {
            String subject = "Skill exchange proposal";
            sendHtmlExchangeRequestNotification(subject, requester, ad, exchangeRequest);
        } catch (Exception e) {
            handleMailException(e);
        }
    }

    private void sendHtmlExchangeRequestNotification(String subject, Users requester, Ad ad, ExchangeRequest exchangeRequest) throws MessagingException {
        Context context = new Context();
        context.setVariable("requesterName", requester.getFullName());
        context.setVariable("requesterEmail", requester.getEmail());
        context.setVariable("adTitle", ad.getSkillName());
        context.setVariable("requestMessage", exchangeRequest.getMessage());
        String templateName = "exchange-request";
        sendHtmlMessage(subject, ad.getUser().getEmail(), context, templateName);
    }

    private void sendHtmlConfirmationCode(String subject, String email, String code) throws MessagingException {
        Context context = new Context();
        context.setVariable("code", code);
        String templateName = "confirm-registration";
        sendHtmlMessage(subject, email, context, templateName);
    }

    private void sendHtmlMessage(String subject, String toEmail, Context context, String templateName) throws MessagingException {
        String process = templateEngine.process(templateName, context);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        ClassPathResource logo = new ClassPathResource("static/images/logo.png");
        mimeMessageHelper.setFrom("noreply@skillmate.com");
        mimeMessageHelper.setTo(toEmail);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(process, true);
        mimeMessageHelper.addInline("logo", logo);
        javaMailSender.send(mimeMessageHelper.getMimeMessage());
    }

    private void handleMailException(Exception ex) {
        log.error("Error during sending email: {}", ex.getMessage());
    }
}
