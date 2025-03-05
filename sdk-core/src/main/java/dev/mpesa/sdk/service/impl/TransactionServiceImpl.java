package dev.mpesa.sdk.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mpesa.sdk.config.MpesaConfig;
import dev.mpesa.sdk.dto.request.TransactionReversalRequest;
import dev.mpesa.sdk.dto.request.TransactionStatusRequest;
import dev.mpesa.sdk.dto.response.TransactionReversalResponse;
import dev.mpesa.sdk.dto.response.TransactionStatusResponse;
import dev.mpesa.sdk.exception.MpesaErrorCode;
import dev.mpesa.sdk.exception.MpesaHttpException;
import dev.mpesa.sdk.exception.MpesaUnexpectedResponseException;
import dev.mpesa.sdk.http.RequestHandler;
import dev.mpesa.sdk.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link TransactionService} for handling M-Pesa transaction-related operations.
 */
public class TransactionServiceImpl implements TransactionService {
    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final RequestHandler requestHandler;
    private final MpesaConfig config;
    private final ObjectMapper objectMapper;

    /**
     * Constructs an instance of {@code TransactionServiceImpl}.
     *
     * @param requestHandler The HTTP request handler for communicating with the M-Pesa API.
     * @param config The M-Pesa configuration containing API URLs and settings.
     */
    public TransactionServiceImpl(RequestHandler requestHandler, MpesaConfig config) {
        this.requestHandler = requestHandler;
        this.config = config;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public TransactionStatusResponse checkTransactionStatus(TransactionStatusRequest request) {
        String url = config.getTransactionStatusUrl();
        String responseJson = "";

        try {
            logger.info("Checking transaction status. URL: {}", url);
            responseJson = requestHandler.post(url, request);
            logger.debug("Transaction status response received: {}", responseJson);
            return objectMapper.readValue(responseJson, TransactionStatusResponse.class);
        } catch (MpesaHttpException e) {
            logger.error("Transaction status HTTP error. URL: {}, Status: {}, Response: {}", url, e.getStatusCode(), e.getResponseBody(), e);
            throw new MpesaUnexpectedResponseException(
                    MpesaErrorCode.UNKNOWN_ERROR,
                    e.getResponseBody(),
                    "Unexpected error when checking Transaction Status.",
                    e
            );
        } catch (JsonProcessingException e) {
            logger.error("Failed to parse transaction status response. URL: {}, Response: {}", url, responseJson, e);
            throw new MpesaUnexpectedResponseException(
                    MpesaErrorCode.INVALID_RESPONSE,
                    responseJson,
                    "Failed to parse Transaction Status response.",
                    e
            );
        }
    }

    @Override
    public TransactionReversalResponse reverseTransaction(TransactionReversalRequest request) {
        String url = config.getTransactionReversalUrl();
        String responseJson = "";

        try {
            logger.info("Initiating transaction reversal. URL: {}", url);
            responseJson = requestHandler.post(url, request);
            logger.debug("Transaction reversal response received: {}", responseJson);
            return objectMapper.readValue(responseJson, TransactionReversalResponse.class);
        } catch (MpesaHttpException e) {
            logger.error("Transaction reversal HTTP error. URL: {}, Status: {}, Response: {}", url, e.getStatusCode(), e.getResponseBody(), e);
            throw new MpesaUnexpectedResponseException(
                    MpesaErrorCode.UNKNOWN_ERROR,
                    e.getResponseBody(),
                    "Unexpected error when reversing transaction.",
                    e
            );
        } catch (JsonProcessingException e) {
            logger.error("Failed to parse transaction reversal response. URL: {}, Response: {}", url, responseJson, e);
            throw new MpesaUnexpectedResponseException(
                    MpesaErrorCode.INVALID_RESPONSE,
                    responseJson,
                    "Failed to parse Transaction Reversal response.",
                    e
            );
        }
    }
}
