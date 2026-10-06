package com.indra.notifications.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.indra.notifications.audit.NotificationAuditLog;
import com.indra.notifications.email.EmailSender;

@Service
public class NotificationService {

    private NotificationAuditLog auditLog;
    private EmailSender emailSender;
    private final int retryAttempts;

    public NotificationService(NotificationAuditLog auditLog, EmailSender emailSender,
            @Value("${notification.retry-attempts}") int retryAttempts) {
        this.auditLog = auditLog;
        this.emailSender = emailSender;
        this.retryAttempts = retryAttempts;
    }

    public void notify(String to, String subject, String body) {
        for (int i = 0; i < retryAttempts; i++) {
            emailSender.send(to, subject, body);
            auditLog.record(to, subject);
        }

    }
}
