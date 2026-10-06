package com.indra.notifications.email;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class FakeEmailSender implements EmailSender {

    @Override
    public void send(String to, String subject, String body) {
        System.out.println("Fake email sent to: " + to);
    }

}