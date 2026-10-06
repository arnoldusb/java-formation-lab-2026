package com.indra.notifications.email;

public interface EmailSender
{
    public void send(String to, String subject, String body);
}