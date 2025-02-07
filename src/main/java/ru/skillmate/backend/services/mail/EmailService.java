package ru.skillmate.backend.services.mail;


public interface EmailService {
    void sendConfirmationCode(String email, String code);
}
