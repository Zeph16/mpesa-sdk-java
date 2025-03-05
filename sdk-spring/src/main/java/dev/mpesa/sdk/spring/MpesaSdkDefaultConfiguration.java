package dev.mpesa.sdk.spring;

import dev.mpesa.sdk.MpesaSdk;
import dev.mpesa.sdk.config.MpesaConfig;
import dev.mpesa.sdk.config.MpesaConfig.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Configuration class to set up the M-Pesa SDK with the required configuration values.
 */
@Configuration
@PropertySource("classpath:application.properties")
public class MpesaSdkDefaultConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(MpesaSdkDefaultConfiguration.class);

    @Value("${mpesa.consumer-key:#{null}}")
    private String consumerKey;

    @Value("${mpesa.consumer-secret:#{null}}")
    private String consumerSecret;

    @Value("${mpesa.config.connect-timeout:#{null}}")
    private Integer connectTimeout;

    @Value("${mpesa.config.read-timeout:#{null}}")
    private Integer readTimeout;

    @Value("${mpesa.config.write-timeout:#{null}}")
    private Integer writeTimeout;

    @Value("${mpesa.config.retry-backoff-time:#{null}}")
    private Integer retryBackoffTime;

    @Value("${mpesa.config.max-retries:#{null}}")
    private Integer maxRetries;

    @Value("${mpesa.config.environment:#{null}}")
    private Environment environment;

    @Value("${mpesa.config.auth-url:#{null}}")
    private String authUrl;

    @Value("${mpesa.config.c2b-register-url:#{null}}")
    private String c2bRegisterUrl;

    @Value("${mpesa.config.c2b-payment-url:#{null}}")
    private String c2bPaymentUrl;

    @Value("${mpesa.config.c2b-simulate-payment-url:#{null}}")
    private String c2bSimulatePaymentUrl;

    @Value("${mpesa.config.stk-push-url:#{null}}")
    private String stkPushUrl;

    @Value("${mpesa.config.b2c-payment-url:#{null}}")
    private String b2cPaymentUrl;

    @Value("${mpesa.config.transaction-status-url:#{null}}")
    private String transactionStatusUrl;

    @Value("${mpesa.config.transaction-reversal-url:#{null}}")
    private String transactionReversalUrl;

    @Value("${mpesa.config.account-balance-url:#{null}}")
    private String accountBalanceUrl;

    @Bean
    public MpesaSdk mpesaSdk(MpesaConfig mpesaConfig) {
        logger.info("Initializing MpesaSdk bean...");

        if (consumerKey == null || consumerKey.trim().isEmpty()) {
            logger.error("Consumer Key is missing in configuration. Cannot initialize MpesaSdk.");
            throw new IllegalArgumentException("Consumer Key not provided in configuration file. Can't bootstrap MpesaSdk bean.");
        }

        if (consumerSecret == null || consumerSecret.trim().isEmpty()) {
            logger.error("Consumer Secret is missing in configuration. Cannot initialize MpesaSdk.");
            throw new IllegalArgumentException("Consumer Secret not provided in configuration file. Can't bootstrap MpesaSdk bean.");
        }

        MpesaSdk sdk = new MpesaSdk(consumerKey, consumerSecret, mpesaConfig);
        logger.info("MpesaSdk bean successfully initialized.");
        return sdk;
    }

    @Bean
    public MpesaConfig mpesaConfig() {
        logger.info("Creating MpesaConfig bean with provided configuration...");

        MpesaConfig config = new MpesaConfig.Builder()
                .environment(environment)
                .authUrl(authUrl)
                .c2bRegisterUrl(c2bRegisterUrl)
                .c2bPaymentUrl(c2bPaymentUrl)
                .c2bSimulatePaymentUrl(c2bSimulatePaymentUrl)
                .stkPushUrl(stkPushUrl)
                .b2cPaymentUrl(b2cPaymentUrl)
                .transactionStatusUrl(transactionStatusUrl)
                .transactionReversalUrl(transactionReversalUrl)
                .accountBalanceUrl(accountBalanceUrl)
                .connectTimeout(connectTimeout)
                .readTimeout(readTimeout)
                .writeTimeout(writeTimeout)
                .retryBackoffTime(retryBackoffTime)
                .maxRetries(maxRetries)
                .build();

        logger.info("MpesaConfig bean successfully created with environment: {}", config.getEnvironment().toString());
        return config;
    }
}
