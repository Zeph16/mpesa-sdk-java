package dev.mpesa.sdk;

import dev.mpesa.sdk.auth.AuthService;
import dev.mpesa.sdk.config.MpesaConfig;
import dev.mpesa.sdk.dto.request.*;
import dev.mpesa.sdk.dto.response.*;
import dev.mpesa.sdk.exception.MpesaAuthenticationException;
import dev.mpesa.sdk.exception.MpesaNetworkException;
import dev.mpesa.sdk.exception.MpesaUnexpectedResponseException;
import dev.mpesa.sdk.http.RequestHandler;
import dev.mpesa.sdk.service.*;
import dev.mpesa.sdk.service.impl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * The MpesaSdk class provides a unified interface for interacting with M-Pesa's services.
 * This includes methods for managing account balances, initiating B2C and C2B payments,
 * simulating payments, making STK push requests, and checking transaction status or reversals.
 * The SDK simplifies communication with the M-Pesa API by handling authentication, retries,
 * and response parsing internally.
 *
 * This class is intended to be used as the main entry point for any application integrating M-Pesa's payment solutions.
 * It exposes various methods for performing typical payment operations via M-Pesa.
 */
public class MpesaSdk implements AccountService, B2CService, C2BService, StkPushService, TransactionService {
    private static final Logger logger = LoggerFactory.getLogger(MpesaSdk.class);

    private final AuthService authService;
    private final AccountService accountService;
    private final B2CService b2cService;
    private final C2BService c2bService;
    private final StkPushService stkPushService;
    private final TransactionService transactionService;

    /**
     * Creates an instance of {@code MpesaSdk} using the provided consumer key and secret.
     * Uses the default {@link MpesaConfig} configuration.
     *
     * @param consumerKey    The M-Pesa API consumer key.
     * @param consumerSecret The M-Pesa API consumer secret.
     * @throws IllegalArgumentException if either {@code consumerKey} or {@code consumerSecret} is null.
     */
    public MpesaSdk(String consumerKey, String consumerSecret) {
        if (consumerKey == null || consumerSecret == null) {
            throw new IllegalArgumentException("consumerKey and consumerSecret are required");
        }

        logger.info("Initializing MpesaSdk with default configuration.");

        MpesaConfig config = new MpesaConfig.Builder().build();
        this.authService = new AuthService(consumerKey, consumerSecret, config);

        logger.debug("MpesaConfig initialized: {}", config);

        RequestHandler requestHandler = new RequestHandler(this.authService, config);
        this.c2bService = new C2BServiceImpl(requestHandler, config);
        this.b2cService = new B2CServiceImpl(requestHandler, config);
        this.stkPushService = new StkPushServiceImpl(requestHandler, config);
        this.accountService = new AccountServiceImpl(requestHandler, config);
        this.transactionService = new TransactionServiceImpl(requestHandler, config);

        logger.info("MpesaSdk successfully initialized.");
    }

    /**
     * Creates an instance of {@code MpesaSdk} using the provided consumer key, secret, and configuration.
     *
     * @param consumerKey    The M-Pesa API consumer key.
     * @param consumerSecret The M-Pesa API consumer secret.
     * @param config         The M-Pesa SDK configuration. If null, a default configuration is used.
     * @throws IllegalArgumentException if either {@code consumerKey} or {@code consumerSecret} is null.
     */
    public MpesaSdk(String consumerKey, String consumerSecret, MpesaConfig config) {
        if (consumerKey == null || consumerSecret == null) {
            throw new IllegalArgumentException("consumerKey and consumerSecret are required");
        }

        logger.info("Initializing MpesaSdk with custom configuration.");

        if (config == null) {
            logger.warn("MpesaConfig is null. Falling back to default configuration.");
            config = new MpesaConfig.Builder().build();
        }

        this.authService = new AuthService(consumerKey, consumerSecret, config);
        logger.debug("MpesaConfig initialized: {}", config);

        RequestHandler requestHandler = new RequestHandler(this.authService, config);
        this.c2bService = new C2BServiceImpl(requestHandler, config);
        this.b2cService = new B2CServiceImpl(requestHandler, config);
        this.stkPushService = new StkPushServiceImpl(requestHandler, config);
        this.accountService = new AccountServiceImpl(requestHandler, config);
        this.transactionService = new TransactionServiceImpl(requestHandler, config);

        logger.info("MpesaSdk successfully initialized.");
    }

    /**
     * Tests the authentication configuration by attempting to refresh the authentication token.
     * This method can be used to verify if the SDK is correctly configured and authorized to make API calls.
     *
     * @throws MpesaUnexpectedResponseException If an error occurs while refreshing the token.
     * @throws MpesaAuthenticationException If the user can not be authenticated with the current credentials.
     * @throws MpesaNetworkException If there is a network issue.
     */
    public void testAuth() {
        authService.refreshToken();
    }

    /**
     * Checks the account balance for the M-Pesa account.
     *
     * @param request The request object containing the details needed to retrieve the account balance.
     * @return The response object containing the account balance information.
     * @throws MpesaUnexpectedResponseException If there is an error while processing the request.
     * @throws MpesaAuthenticationException If the user can not be authenticated with the current credentials.
     * @throws MpesaNetworkException If there is a network issue.
     */
    @Override
    public AccountBalanceResponse checkAccountBalance(AccountBalanceRequest request) {
        Objects.requireNonNull(request);
        return this.accountService.checkAccountBalance(request);
    }

    /**
     * Initiates a B2C (Business to Customer) payment.
     *
     * @param request The request object containing the details of the B2C payment to be initiated.
     * @return The response object containing the result of the payment initiation.
     * @throws MpesaUnexpectedResponseException If there is an error while processing the payment request.
     * @throws MpesaAuthenticationException If the user can not be authenticated with the current credentials.
     * @throws MpesaNetworkException If there is a network issue.
     */
    @Override
    public B2CPaymentResponse initiateB2CPayment(B2CPaymentRequest request) {
        Objects.requireNonNull(request);
        return this.b2cService.initiateB2CPayment(request);
    }

    /**
     * Registers a C2B (Customer to Business) payment.
     *
     * @param request The request object containing the C2B registration details.
     * @param apiKey The API key used as a query parameter for authentication.
     * @return The response object indicating the result of the registration.
     * @throws MpesaUnexpectedResponseException If there is an error while processing the registration request.
     * @throws MpesaAuthenticationException If the user can not be authenticated with the current credentials.
     * @throws MpesaNetworkException If there is a network issue.
     */
    @Override
    public C2BRegisterResponse registerC2B(C2BRegisterRequest request, String apiKey) {
        Objects.requireNonNull(request);
        return this.c2bService.registerC2B(request, apiKey);
    }

    /**
     * Initiates a C2B (Customer to Business) payment.
     *
     * @param request The request object containing the details of the C2B payment to be initiated.
     * @return The response object containing the result of the payment initiation.
     * @throws MpesaUnexpectedResponseException If there is an error while processing the payment request.
     * @throws MpesaAuthenticationException If the user can not be authenticated with the current credentials.
     * @throws MpesaNetworkException If there is a network issue.
     */
    @Override
    public C2BPaymentResponse initiatePayment(C2BPaymentRequest request) {
        Objects.requireNonNull(request);
        return this.c2bService.initiatePayment(request);
    }

    /**
     * Simulates a C2B (Customer to Business) payment for testing purposes.
     *
     * @param request The request object containing the details of the C2B payment to be simulated.
     * @return The response object containing the result of the simulated payment.
     * @throws MpesaUnexpectedResponseException If there is an error while processing the simulation request.
     * @throws MpesaAuthenticationException If the user can not be authenticated with the current credentials.
     * @throws MpesaNetworkException If there is a network issue.
     */
    @Override
    public C2BSimulatePaymentResponse simulateC2BPayment(C2BSimulatePaymentRequest request) {
        Objects.requireNonNull(request);
        return this.c2bService.simulateC2BPayment(request);
    }

    /**
     * Requests an STK (Simulate to Pay) push to initiate a payment via M-Pesa.
     * This is typically used for customer-initiated payments via mobile devices.
     *
     * @param request The request object containing the details of the STK push request.
     * @return The response object containing the result of the STK push request.
     * @throws MpesaUnexpectedResponseException If there is an error while processing the STK push request.
     * @throws MpesaAuthenticationException If the user can not be authenticated with the current credentials.
     * @throws MpesaNetworkException If there is a network issue.
     */
    @Override
    public StkPushResponse requestStkPush(StkPushRequest request) {
        Objects.requireNonNull(request);
        return this.stkPushService.requestStkPush(request);
    }

    /**
     * Checks the status of a specific transaction using its reference number.
     *
     * @param request The request object containing the transaction reference and other necessary details.
     * @return The response object containing the status of the transaction.
     * @throws MpesaUnexpectedResponseException If there is an error while processing the transaction status request.
     * @throws MpesaAuthenticationException If the user can not be authenticated with the current credentials.
     * @throws MpesaNetworkException If there is a network issue.
     */
    @Override
    public TransactionStatusResponse checkTransactionStatus(TransactionStatusRequest request) {
        Objects.requireNonNull(request);
        return this.transactionService.checkTransactionStatus(request);
    }

    /**
     * Reverses a previously completed transaction.
     *
     * @param request The request object containing the details of the transaction to be reversed.
     * @return The response object containing the result of the reversal operation.
     * @throws MpesaUnexpectedResponseException If there is an error while processing the transaction reversal.
     * @throws MpesaAuthenticationException If the user can not be authenticated with the current credentials.
     * @throws MpesaNetworkException If there is a network issue.
     */
    @Override
    public TransactionReversalResponse reverseTransaction(TransactionReversalRequest request) {
        Objects.requireNonNull(request);
        return this.transactionService.reverseTransaction(request);
    }
}

