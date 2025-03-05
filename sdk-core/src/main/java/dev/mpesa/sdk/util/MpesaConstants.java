package dev.mpesa.sdk.util;

/**
 * Defines constant values used throughout the M-Pesa SDK.
 */
public class MpesaConstants {

    /** Base URL for M-Pesa production environment. */
    public static final String PRODUCTION_BASE_URL = "https://api.safaricom.et";

    /** Base URL for M-Pesa sandbox environment. */
    public static final String SANDBOX_BASE_URL = "https://apisandbox.safaricom.et";

    /** Default connection timeout in milliseconds. */
    public static final long DEFAULT_CONNECT_TIMEOUT = 5000;

    /** Default read timeout in milliseconds. */
    public static final long DEFAULT_READ_TIMEOUT = 15000;

    /** Default write timeout in milliseconds. */
    public static final long DEFAULT_WRITE_TIMEOUT = 10000;

    /** Default retry backoff time in milliseconds. */
    public static final long DEFAULT_RETRY_BACKOFF_TIME = 500;

    /** Default maximum number of retries for failed requests. */
    public static final int DEFAULT_MAX_RETRIES = 3;

    /** Default endpoint paths for different M-Pesa services. */
    public static final String TOKEN_GENERATE = "/v1/token/generate?grant_type=client_credentials";
    public static final String C2B_REGISTER = "/v1/c2b-register-url/register";
    public static final String C2B_PAYMENT = "/c2b/payments";
    public static final String C2B_SIMULATE_PAYMENT = "/mpesa/b2c/simulatetransaction/v1/request";
    public static final String STK_PUSH = "/mpesa/stkpush/v3/processrequest";
    public static final String B2C_PAYMENT = "/mpesa/b2c/v1/paymentrequest";
    public static final String TRANSACTION_STATUS = "/mpesa/transactionstatus/v1/query";
    public static final String TRANSACTION_REVERSAL = "/mpesa/reversal/v2/request";
    public static final String ACCOUNT_BALANCE = "/mpesa/accountbalance/v2/query";

    private MpesaConstants() {
        // Prevents instantiation
    }
}
