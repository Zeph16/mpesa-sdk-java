package dev.mpesa.sdk.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mpesa.sdk.config.MpesaConfig;
import dev.mpesa.sdk.dto.request.AccountBalanceRequest;
import dev.mpesa.sdk.dto.response.AccountBalanceResponse;
import dev.mpesa.sdk.exception.MpesaErrorCode;
import dev.mpesa.sdk.exception.MpesaHttpException;
import dev.mpesa.sdk.exception.MpesaUnexpectedResponseException;
import dev.mpesa.sdk.http.RequestHandler;
import dev.mpesa.sdk.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link AccountService}
 */
public class AccountServiceImpl implements AccountService {
    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final RequestHandler requestHandler;
    private final MpesaConfig config;
    private final ObjectMapper objectMapper;

    /**
     * Constructs an {@code AccountServiceImpl} with the given request handler and configuration.
     * A default {@link ObjectMapper} is used for JSON processing.
     *
     * @param requestHandler the HTTP request handler for making API calls
     * @param config the M-Pesa configuration containing API endpoints and timeouts
     */
    public AccountServiceImpl(RequestHandler requestHandler, MpesaConfig config) {
        this.requestHandler = requestHandler;
        this.config = config;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Constructs an {@code AccountServiceImpl} with a custom JSON mapper for injection.
     *
     * @param requestHandler the HTTP request handler for making API calls
     * @param config the M-Pesa configuration containing API endpoints and timeouts
     * @param objectMapper the custom object mapper for JSON serialization/deserialization
     */
    public AccountServiceImpl(RequestHandler requestHandler, MpesaConfig config, ObjectMapper objectMapper) {
        this.requestHandler = requestHandler;
        this.config = config;
        this.objectMapper = objectMapper;
    }

    @Override
    public AccountBalanceResponse checkAccountBalance(AccountBalanceRequest request) {
        String url = config.getAccountBalanceUrl();
        String responseJson = "";
        try {
            logger.info("Checking account balance. URL: {}", url);
            responseJson = requestHandler.post(url, request);
            logger.debug("Account balance response received: {}", responseJson);
            return objectMapper.readValue(responseJson, AccountBalanceResponse.class);
        } catch (MpesaHttpException e) {
            logger.error("Error during account balance request to M-Pesa. URL: {}, Response: {}", url, e.getResponseBody(), e);
            throw new MpesaUnexpectedResponseException(
                    MpesaErrorCode.UNKNOWN_ERROR, e.getResponseBody(),
                    "Unexpected error when checking account balance.", e
            );
        } catch (JsonProcessingException e) {
            logger.error("Failed to parse Account Balance response. Response: {}", responseJson, e);
            throw new MpesaUnexpectedResponseException(
                    MpesaErrorCode.INVALID_RESPONSE, responseJson,
                    "Failed to parse Account Balance response.", e
            );
        }
    }
}
