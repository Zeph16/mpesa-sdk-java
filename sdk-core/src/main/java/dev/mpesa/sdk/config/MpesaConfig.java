package dev.mpesa.sdk.config;

import dev.mpesa.sdk.util.MpesaConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configuration class for the M-Pesa SDK.
 * <p>
 * This class holds all configuration settings, including API endpoints, timeouts, and retry policies.
 * It is immutable and must be instantiated using the {@link Builder} class.
 */
public class MpesaConfig {
    private static final Logger logger = LoggerFactory.getLogger(MpesaConfig.class);

    /**
     * Enum representing the M-Pesa environment.
     * Determines whether requests are sent to the production or sandbox environment.
     */
    public enum Environment { SANDBOX, PRODUCTION }

    private final String authUrl;
    private final String c2bRegisterUrl;
    private final String c2bPaymentUrl;
    private final String c2bSimulatePaymentUrl;
    private final long connectTimeout;
    private final long readTimeout;
    private final long writeTimeout;
    private final long retryBackoffTime;
    private final int maxRetries;
    private final String stkPushUrl;
    private final String b2cPaymentUrl;
    private final String transactionStatusUrl;
    private final String transactionReversalUrl;
    private final String accountBalanceUrl;
    private final Environment environment;

    /**
     * Private constructor to enforce the use of the {@link Builder} class.
     *
     * @param builder The builder instance with user-defined or default values.
     */
    private MpesaConfig(Builder builder) {
        this.environment = builder.environment != null ? builder.environment : Environment.SANDBOX;
        String baseUrl = (this.environment == Environment.PRODUCTION)
                ? MpesaConstants.PRODUCTION_BASE_URL
                : MpesaConstants.SANDBOX_BASE_URL;

        this.authUrl = builder.authUrl != null ? builder.authUrl : baseUrl + MpesaConstants.TOKEN_GENERATE;
        this.c2bRegisterUrl = builder.c2bRegisterUrl != null ? builder.c2bRegisterUrl : baseUrl + MpesaConstants.C2B_REGISTER;
        this.c2bPaymentUrl = builder.c2bPaymentUrl != null ? builder.c2bPaymentUrl : baseUrl + MpesaConstants.C2B_PAYMENT;
        this.c2bSimulatePaymentUrl = builder.c2bSimulatePaymentUrl != null ? builder.c2bSimulatePaymentUrl : baseUrl + MpesaConstants.C2B_SIMULATE_PAYMENT;
        this.stkPushUrl = builder.stkPushUrl != null ? builder.stkPushUrl : baseUrl + MpesaConstants.STK_PUSH;
        this.b2cPaymentUrl = builder.b2cPaymentUrl != null ? builder.b2cPaymentUrl : baseUrl + MpesaConstants.B2C_PAYMENT;
        this.transactionStatusUrl = builder.transactionStatusUrl != null ? builder.transactionStatusUrl : baseUrl + MpesaConstants.TRANSACTION_STATUS;
        this.transactionReversalUrl = builder.transactionReversalUrl != null ? builder.transactionReversalUrl : baseUrl + MpesaConstants.TRANSACTION_REVERSAL;
        this.accountBalanceUrl = builder.accountBalanceUrl != null ? builder.accountBalanceUrl : baseUrl + MpesaConstants.ACCOUNT_BALANCE;
        this.connectTimeout = builder.connectTimeout != null ? builder.connectTimeout : MpesaConstants.DEFAULT_CONNECT_TIMEOUT;
        this.readTimeout = builder.readTimeout != null ? builder.readTimeout : MpesaConstants.DEFAULT_READ_TIMEOUT;
        this.writeTimeout = builder.writeTimeout != null ? builder.writeTimeout : MpesaConstants.DEFAULT_WRITE_TIMEOUT;
        this.retryBackoffTime = builder.retryBackoffTime != null ? builder.retryBackoffTime : MpesaConstants.DEFAULT_RETRY_BACKOFF_TIME;
        this.maxRetries = builder.maxRetries != null ? builder.maxRetries : MpesaConstants.DEFAULT_MAX_RETRIES;
    }

    /** @return Authentication URL for obtaining access tokens. */
    public String getAuthUrl() { return authUrl; }

    /** @return URL for C2B registration. */
    public String getC2bRegisterUrl() { return c2bRegisterUrl; }

    /** @return URL for C2B payments. */
    public String getC2bPaymentUrl() { return c2bPaymentUrl; }

    /** @return URL for simulating C2B payments. */
    public String getC2bSimulatePaymentUrl() { return c2bSimulatePaymentUrl; }

    /** @return URL for STK Push requests. */
    public String getStkPushUrl() { return stkPushUrl; }

    /** @return URL for B2C payments. */
    public String getB2cPaymentUrl() { return b2cPaymentUrl; }

    /** @return URL for transaction status queries. */
    public String getTransactionStatusUrl() { return transactionStatusUrl; }

    /** @return URL for transaction reversals. */
    public String getTransactionReversalUrl() { return transactionReversalUrl; }

    /** @return URL for account balance queries. */
    public String getAccountBalanceUrl() { return accountBalanceUrl; }

    /** @return Connection timeout in milliseconds. */
    public long getConnectTimeout() { return connectTimeout; }

    /** @return Read timeout in milliseconds. */
    public long getReadTimeout() { return readTimeout; }

    /** @return Write timeout in milliseconds. */
    public long getWriteTimeout() { return writeTimeout; }

    /** @return Time in milliseconds before retrying a failed request. */
    public long getRetryBackoffTime() { return retryBackoffTime; }

    /** @return Maximum number of retries for failed API requests. */
    public int getMaxRetries() { return maxRetries; }

    /** @return The configured M-Pesa environment (sandbox or production). */
    public Environment getEnvironment() { return environment; }

    /**
     * Builder class for {@link MpesaConfig}.
     * Provides a flexible way to construct an immutable configuration object.
     */
    public static class Builder {
        private Environment environment = Environment.SANDBOX;
        private String authUrl;
        private String c2bRegisterUrl;
        private String c2bPaymentUrl;
        private String c2bSimulatePaymentUrl;
        private String stkPushUrl;
        private String b2cPaymentUrl;
        private String transactionStatusUrl;
        private String transactionReversalUrl;
        private String accountBalanceUrl;
        private Integer connectTimeout;
        private Integer readTimeout;
        private Integer writeTimeout;
        private Integer retryBackoffTime;
        private Integer maxRetries;

        public Builder environment(Environment environment) {
            this.environment = environment;
            return this;
        }

        public Builder authUrl(String authUrl) {
            this.authUrl = authUrl;
            return this;
        }

        public Builder c2bRegisterUrl(String c2bRegisterUrl) {
            this.c2bRegisterUrl = c2bRegisterUrl;
            return this;
        }

        public Builder c2bPaymentUrl(String c2bPaymentUrl) {
            this.c2bPaymentUrl = c2bPaymentUrl;
            return this;
        }

        public Builder c2bSimulatePaymentUrl(String c2bSimulatePaymentUrl) {
            this.c2bSimulatePaymentUrl = c2bSimulatePaymentUrl;
            return this;
        }

        public Builder stkPushUrl(String stkPushUrl) {
            this.stkPushUrl = stkPushUrl;
            return this;
        }

        public Builder b2cPaymentUrl(String b2cPaymentUrl) {
            this.b2cPaymentUrl = b2cPaymentUrl;
            return this;
        }

        public Builder transactionStatusUrl(String transactionStatusUrl) {
            this.transactionStatusUrl = transactionStatusUrl;
            return this;
        }

        public Builder transactionReversalUrl(String transactionReversalUrl) {
            this.transactionReversalUrl = transactionReversalUrl;
            return this;
        }

        public Builder accountBalanceUrl(String accountBalanceUrl) {
            this.accountBalanceUrl = accountBalanceUrl;
            return this;
        }

        public Builder connectTimeout(Integer timeout) {
            this.connectTimeout = timeout;
            return this;
        }

        public Builder readTimeout(Integer timeout) {
            this.readTimeout = timeout;
            return this;
        }

        public Builder writeTimeout(Integer timeout) {
            this.writeTimeout = timeout;
            return this;
        }

        public Builder maxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public Builder retryBackoffTime(Integer retryBackoffTime) {
            this.retryBackoffTime = retryBackoffTime;
            return this;
        }


        /**
         * Builds the {@link MpesaConfig} object.
         *
         * @return Configured instance of {@link MpesaConfig}.
         */
        public MpesaConfig build() {
            return new MpesaConfig(this);
        }
    }

    @Override
    public String toString() {
        return "MpesaConfig{" +
                "authUrl='" + authUrl + '\'' +
                ", c2bRegisterUrl='" + c2bRegisterUrl + '\'' +
                ", c2bPaymentUrl='" + c2bPaymentUrl + '\'' +
                ", c2bSimulatePaymentUrl='" + c2bSimulatePaymentUrl + '\'' +
                ", connectTimeout=" + connectTimeout +
                ", readTimeout=" + readTimeout +
                ", writeTimeout=" + writeTimeout +
                ", retryBackoffTime=" + retryBackoffTime +
                ", maxRetries=" + maxRetries +
                ", stkPushUrl='" + stkPushUrl + '\'' +
                ", b2cPaymentUrl='" + b2cPaymentUrl + '\'' +
                ", transactionStatusUrl='" + transactionStatusUrl + '\'' +
                ", transactionReversalUrl='" + transactionReversalUrl + '\'' +
                ", accountBalanceUrl='" + accountBalanceUrl + '\'' +
                ", environment=" + environment +
                '}';
    }

}
