package com.indra.notifications.service;

import com.indra.notifications.audit.NotificationAuditLog;
import com.indra.notifications.email.EmailSender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("dev")
class NotificationServicePropertiesTest {

    @Autowired
    private NotificationService service;

    @MockBean
    private EmailSender emailSender;

    @MockBean
    private NotificationAuditLog auditLog;

    @Test
    void usesRetryAttemptsFromDevProperties() {
        service.notify("user@example.com", "Subject", "Body");

        verify(emailSender, times(3)).send("user@example.com", "Subject", "Body");
        verify(auditLog, times(3)).record("user@example.com", "Subject");
    }
}
