package com.indra.notifications.email;

import org.springframework.stereotype.Component;

@Component
public class SmtpEmailSimulator {

    public void simulateSend(String to, String subject, String body) {
        System.out.println("[SMTP SIMULADO] Enviando a " + to + ": " + subject + " -> " + body);
    }
}