package com.indra.notifications.service;

import org.springframework.stereotype.Service;

import com.indra.notifications.audit.NotificationAuditLog;
import com.indra.notifications.email.EmailSender;

@Service
public class NotificationService {

    
    private NotificationAuditLog auditLog;
    private EmailSender emailSender;

    public NotificationService(NotificationAuditLog auditLog, EmailSender emailSender) {
        this.auditLog = auditLog;
        this.emailSender = emailSender;
    }

    public void notify(String to, String subject, String body) {
 
        emailSender.send(to, subject, body);
        auditLog.record(to, subject);
    }
}
