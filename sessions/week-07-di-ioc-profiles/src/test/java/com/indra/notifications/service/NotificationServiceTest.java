package com.indra.notifications.service;

import com.indra.notifications.audit.NotificationAuditLog;
import com.indra.notifications.email.EmailSender;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class NotificationServiceTest {

    @Test
    void notifySendsAndAuditsOnceForEachRetryAttempt() {
        NotificationAuditLog auditLog = mock(NotificationAuditLog.class);
        EmailSender emailSender = mock(EmailSender.class);
        NotificationService service = new NotificationService(auditLog, emailSender, 3);

        service.notify("user@example.com", "Subject", "Body");

        verify(emailSender, times(3)).send("user@example.com", "Subject", "Body");
        verify(auditLog, times(3)).record("user@example.com", "Subject");
    }

    @Test
    void notifyDoesNotSendWhenRetryAttemptsIsZero() {
        NotificationAuditLog auditLog = mock(NotificationAuditLog.class);
        EmailSender emailSender = mock(EmailSender.class);
        NotificationService service = new NotificationService(auditLog, emailSender, 0);

        service.notify("user@example.com", "Subject", "Body");

        verify(emailSender, times(0)).send("user@example.com", "Subject", "Body");
        verify(auditLog, times(0)).record("user@example.com", "Subject");
    }
}
