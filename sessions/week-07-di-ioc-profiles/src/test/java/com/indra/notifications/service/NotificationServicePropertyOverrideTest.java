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

@SpringBootTest(properties = "notification.retry-attempts=2")
@ActiveProfiles("dev")
class NotificationServicePropertyOverrideTest {

    @Autowired
    private NotificationService service;

    @MockBean
    private EmailSender emailSender;

    @MockBean
    private NotificationAuditLog auditLog;

    @Test
    void testPropertyOverridesValueFromDevProperties() {
        service.notify("user@example.com", "Subject", "Body");

        verify(emailSender, times(2)).send("user@example.com", "Subject", "Body");
        verify(auditLog, times(2)).record("user@example.com", "Subject");
    }
}
