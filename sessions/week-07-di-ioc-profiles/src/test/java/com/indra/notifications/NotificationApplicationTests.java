package com.indra.notifications;

import com.indra.notifications.email.EmailSender;
import com.indra.notifications.email.FakeEmailSender;
import com.indra.notifications.email.SmtpEmailSender;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("dev")
class NotificationApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void devProfileActivatesOnlyFakeEmailSender(ApplicationContext applicationContext) {
        assertInstanceOf(FakeEmailSender.class, applicationContext.getBean(EmailSender.class));
        assertEquals(1, applicationContext.getBeansOfType(EmailSender.class).size());
    }

    @Test
    void noDebeCargarBeanEnDev(ApplicationContext applicationContext) {
        assertThrows(
            NoSuchBeanDefinitionException.class,
            () -> applicationContext.getBean(SmtpEmailSender.class)
        );
    }
}
