package ru.skillmate.backend.services.mail;


import ru.skillmate.backend.entities.ads.Ad;
import ru.skillmate.backend.entities.ads.ExchangeRequest;
import ru.skillmate.backend.entities.users.ResetPasswordToken;
import ru.skillmate.backend.entities.users.Users;

public interface EmailService {
    void sendConfirmationCode(String email, String code);

    void sendExchangeRequestNotification(Users requester, Ad ad, ExchangeRequest exchangeRequest);

    void sendResetPasswordLink(String email, ResetPasswordToken token);
}
