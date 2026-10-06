package com.indra.notifications.email;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
public class SmtpEmailSender implements EmailSender {

    private final SmtpEmailSimulator smtpEmailSimulator;

    public SmtpEmailSender(SmtpEmailSimulator smtpEmailSimulator) {
        this.smtpEmailSimulator = smtpEmailSimulator;
    }

    @Override
    public void send(String to, String subject, String body) {
        smtpEmailSimulator.simulateSend(to, subject, body);
    }
}