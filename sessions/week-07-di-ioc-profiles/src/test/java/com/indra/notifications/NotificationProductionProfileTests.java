package com.indra.notifications;

import com.indra.notifications.email.EmailSender;
import com.indra.notifications.email.FakeEmailSender;
import com.indra.notifications.email.SmtpEmailSender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("prod")
class NotificationProductionProfileTests {

    @Test
    void prodProfileActivatesOnlySmtpEmailSender(ApplicationContext applicationContext) {
        assertInstanceOf(SmtpEmailSender.class, applicationContext.getBean(EmailSender.class));
        assertEquals(1, applicationContext.getBeansOfType(EmailSender.class).size());
    }

    @Test
    void noDebeCargarBeanEnDev(ApplicationContext applicationContext) {
        assertThrows(
            NoSuchBeanDefinitionException.class,
            () -> applicationContext.getBean(FakeEmailSender.class)
        );
    }
}