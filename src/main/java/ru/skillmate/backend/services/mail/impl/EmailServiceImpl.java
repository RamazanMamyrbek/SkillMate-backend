package ru.skillmate.backend.services.mail.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import ru.skillmate.backend.entities.ads.Ad;
import ru.skillmate.backend.entities.ads.ExchangeRequest;
import ru.skillmate.backend.entities.users.ResetPasswordToken;
import ru.skillmate.backend.entities.users.Users;
import ru.skillmate.backend.services.mail.EmailService;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

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

    @Override
    @Async
    public void sendResetPasswordLink(String email, ResetPasswordToken token) {
        try {
            String subject = "Reset password request";
            sendResetPasswordLink(subject, email, token);
        } catch (Exception e) {
            handleMailException(e);
        }
    }

    private void sendResetPasswordLink(String subject, String email, ResetPasswordToken token) {
        SimpleMailMessage message = new SimpleMailMessage();
        String text = String.format(
                "You have requested to reset your password on Skillmate.\n\n" +
                        "Please follow the link below to set a new password:\n%s\n\n" +
                        "This link will be valid for 15 minutes.\n\n" +
                        "If you did not request a password reset, you can safely ignore this email.",
                token.getLink()
        );
        message.setSubject(subject);
        message.setTo(email);
        message.setText(text);
        message.setFrom("noreply@skillmate.com");
        javaMailSender.send(message);
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
