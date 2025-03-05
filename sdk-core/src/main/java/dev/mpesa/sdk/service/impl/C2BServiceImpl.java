package dev.mpesa.sdk.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mpesa.sdk.config.MpesaConfig;
import dev.mpesa.sdk.dto.request.C2BPaymentRequest;
import dev.mpesa.sdk.dto.request.C2BRegisterRequest;
import dev.mpesa.sdk.dto.request.C2BSimulatePaymentRequest;
import dev.mpesa.sdk.dto.response.C2BPaymentResponse;
import dev.mpesa.sdk.dto.response.C2BRegisterResponse;
import dev.mpesa.sdk.dto.response.C2BSimulatePaymentResponse;
import dev.mpesa.sdk.exception.MpesaErrorCode;
import dev.mpesa.sdk.exception.MpesaHttpException;
import dev.mpesa.sdk.exception.MpesaUnexpectedResponseException;
import dev.mpesa.sdk.http.RequestHandler;
import dev.mpesa.sdk.service.C2BService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link C2BService}
 */
public class C2BServiceImpl implements C2BService {
    private static final Logger logger = LoggerFactory.getLogger(C2BServiceImpl.class);

    private final RequestHandler requestHandler;
    private final MpesaConfig config;
    private final ObjectMapper objectMapper;

    /**
     * Constructs a {@code C2BServiceImpl} with the given request handler and configuration.
     * A default {@link ObjectMapper} is used for JSON processing.
     *
     * @param requestHandler the HTTP request handler for making API calls
     * @param config the M-Pesa configuration containing API endpoints and timeouts
     */
    public C2BServiceImpl(RequestHandler requestHandler, MpesaConfig config) {
        this.requestHandler = requestHandler;
        this.config = config;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public C2BRegisterResponse registerC2B(C2BRegisterRequest request, String apiKey) {
        String url = config.getC2bRegisterUrl() + "?apikey=" + apiKey;
        String responseJson = "";

        try {
            logger.info("Initiating C2B Registration request. URL: {}", url);
            responseJson = requestHandler.post(url, request);
            logger.debug("C2B Registration response received: {}", responseJson);
            return objectMapper.readValue(responseJson, C2BRegisterResponse.class);
        } catch (MpesaHttpException e) {
            if (e.getStatusCode() == 400 && e.getResponseBody().contains("Short Code already Registered")) {
                logger.error("Short Code already registered. URL: {}, Response: {}", url, e.getResponseBody(), e);
                throw new MpesaUnexpectedResponseException(
                        MpesaErrorCode.SHORT_CODE_REGISTERED, e.getResponseBody(),
                        "Short Code is already registered.", e
                );
            }
            logger.error("Unexpected error during Register C2B. URL: {}, Response: {}", url, e.getResponseBody(), e);
            throw new MpesaUnexpectedResponseException(
                    MpesaErrorCode.UNKNOWN_ERROR, e.getResponseBody(),
                    "Unexpected error in Register C2B.", e
            );
        } catch (JsonProcessingException e) {
            logger.error("Failed to parse Register C2B response. URL: {}, Response: {}", url, responseJson, e);
            throw new MpesaUnexpectedResponseException(
                    MpesaErrorCode.INVALID_RESPONSE, responseJson,
                    "Failed to parse Register C2B response.", e
            );
        }
    }

    @Override
    public C2BPaymentResponse initiatePayment(C2BPaymentRequest request) {
        String url = config.getC2bPaymentUrl();
        String responseJson = "";

        try {
            logger.info("Initiating C2B Payment request. URL: {}", url);
            responseJson = requestHandler.post(url, request);
            logger.debug("C2B Payment response received: {}", responseJson);
            return objectMapper.readValue(responseJson, C2BPaymentResponse.class);
        } catch (MpesaHttpException e) {
            if (e.getStatusCode() == 400 && e.getResponseBody().contains("The initiator information is invalid.")) {
                logger.error("Invalid initiator information during C2B Payment. URL: {}, Response: {}", url, e.getResponseBody(), e);
                throw new MpesaUnexpectedResponseException(
                        MpesaErrorCode.INVALID_INITIATOR, e.getResponseBody(),
                        "Invalid initiator information.", e
                );
            }
            logger.error("Unexpected error during C2B Payment. URL: {}, Response: {}", url, e.getResponseBody(), e);
            throw new MpesaUnexpectedResponseException(
                    MpesaErrorCode.UNKNOWN_ERROR, e.getResponseBody(),
                    "Unexpected error in C2B Payment.", e
            );
        } catch (JsonProcessingException e) {
            logger.error("Failed to parse C2B Payment response. URL: {}, Response: {}", url, responseJson, e);
            throw new MpesaUnexpectedResponseException(
                    MpesaErrorCode.INVALID_RESPONSE, responseJson,
                    "Failed to parse C2B Payment response.", e
            );
        }
    }

    @Override
    public C2BSimulatePaymentResponse simulateC2BPayment(C2BSimulatePaymentRequest request) {
        String url = config.getC2bSimulatePaymentUrl();
        String responseJson = "";

        try {
            logger.info("Initiating C2B Payment Simulation. URL: {}", url);
            responseJson = requestHandler.post(url, request);
            logger.debug("C2B Payment Simulation Response received: {}", responseJson);
            return objectMapper.readValue(responseJson, C2BSimulatePaymentResponse.class);
        } catch (MpesaHttpException e) {
            if (e.getStatusCode() == 400 && e.getResponseBody().contains("invalid")) {
                logger.error("Invalid request parameters during C2B Payment Simulation. URL: {}, Response: {}", url, e.getResponseBody(), e);
                throw new MpesaUnexpectedResponseException(
                        MpesaErrorCode.INVALID_REQUEST, e.getResponseBody(),
                        "Invalid request parameters.", e
                );
            }
            logger.error("Unexpected error during C2B Payment Simulation. URL: {}, Response: {}", url, e.getResponseBody(), e);
            throw new MpesaUnexpectedResponseException(
                    MpesaErrorCode.UNKNOWN_ERROR, e.getResponseBody(),
                    "Unexpected error in C2B Payment Simulation.", e
            );
        } catch (JsonProcessingException e) {
            logger.error("Failed to parse C2B Payment Simulation response. URL: {}, Response: {}", url, responseJson, e);
            throw new MpesaUnexpectedResponseException(
                    MpesaErrorCode.INVALID_RESPONSE, "",
                    "Failed to parse C2B Payment Simulation response.", e
            );
        }
    }
}
