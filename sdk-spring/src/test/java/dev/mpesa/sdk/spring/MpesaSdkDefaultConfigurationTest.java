package dev.mpesa.sdk.spring;

import dev.mpesa.sdk.MpesaSdk;
import dev.mpesa.sdk.config.MpesaConfig;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MpesaSdkDefaultConfigurationTest {

    @Test
    public void testMpesaSdkBean() {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(MpesaSdkDefaultConfiguration.class)) {

            MpesaSdk mpesaSdk = context.getBean(MpesaSdk.class);

            assertNotNull(mpesaSdk, "MpesaSdk bean should not be null");
        }
    }

    @Test
    public void testMpesaConfigBean() {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(MpesaSdkDefaultConfiguration.class)) {
            MpesaConfig mpesaConfig = context.getBean(MpesaConfig.class);

            assertNotNull(mpesaConfig, "MpesaConfig bean should not be null");

            assertNotNull(mpesaConfig.getAuthUrl(), "Auth URL should not be null");
        }
    }
}
