package dev.mpesa.sdk.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mpesa.sdk.config.MpesaConfig;
import dev.mpesa.sdk.dto.request.StkPushRequest;
import dev.mpesa.sdk.dto.response.StkPushResponse;
import dev.mpesa.sdk.exception.MpesaErrorCode;
import dev.mpesa.sdk.exception.MpesaHttpException;
import dev.mpesa.sdk.exception.MpesaUnexpectedResponseException;
import dev.mpesa.sdk.http.RequestHandler;
import dev.mpesa.sdk.service.StkPushService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link StkPushService} that handles M-Pesa STK Push transactions.
 */
public class StkPushServiceImpl implements StkPushService {
    private static final Logger logger = LoggerFactory.getLogger(StkPushServiceImpl.class);

    private final RequestHandler requestHandler;
    private final MpesaConfig config;
    private final ObjectMapper objectMapper;

    /**
     * Constructs an instance of {@code StkPushServiceImpl}.
     *
     * @param requestHandler The HTTP request handler for interacting with the M-Pesa API.
     * @param config The M-Pesa configuration containing API URLs and settings.
     */
    public StkPushServiceImpl(RequestHandler requestHandler, MpesaConfig config) {
        this.requestHandler = requestHandler;
        this.config = config;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public StkPushResponse requestStkPush(StkPushRequest request) {
        String url = config.getStkPushUrl();
        String responseJson = "";

        try {
            logger.info("Initiating STK Push request. URL: {}", url);
            responseJson = requestHandler.post(url, request);
            logger.debug("STK Push response received: {}", responseJson);
            return objectMapper.readValue(responseJson, StkPushResponse.class);
        } catch (MpesaHttpException e) {
            logger.error("STK Push HTTP error. URL: {}, Status: {}, Response: {}", url, e.getStatusCode(), e.getResponseBody(), e);
            throw new MpesaUnexpectedResponseException(
                    MpesaErrorCode.UNKNOWN_ERROR,
                    e.getResponseBody(),
                    "Unexpected error in STK Push.",
                    e
            );
        } catch (JsonProcessingException e) {
            logger.error("Failed to parse STK Push response. URL: {}, Response: {}", url, responseJson, e);
            throw new MpesaUnexpectedResponseException(
                    MpesaErrorCode.INVALID_RESPONSE,
                    responseJson,
                    "Failed to parse STK Push response.",
                    e
            );
        }
    }
}
