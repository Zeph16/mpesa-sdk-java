package dev.mpesa.sdk.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mpesa.sdk.config.MpesaConfig;
import dev.mpesa.sdk.dto.request.B2CPaymentRequest;
import dev.mpesa.sdk.dto.response.B2CPaymentResponse;
import dev.mpesa.sdk.exception.MpesaErrorCode;
import dev.mpesa.sdk.exception.MpesaHttpException;
import dev.mpesa.sdk.exception.MpesaUnexpectedResponseException;
import dev.mpesa.sdk.http.RequestHandler;
import dev.mpesa.sdk.service.B2CService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link B2CService}
 */
public class B2CServiceImpl implements B2CService {
    private static final Logger logger = LoggerFactory.getLogger(B2CServiceImpl.class);

    private final RequestHandler requestHandler;
    private final MpesaConfig config;
    private final ObjectMapper objectMapper;

    /**
     * Constructs a {@code B2CServiceImpl} with the given request handler and configuration.
     * A default {@link ObjectMapper} is used for JSON processing.
     *
     * @param requestHandler the HTTP request handler for making API calls
     * @param config the M-Pesa configuration containing API endpoints and timeouts
     */
    public B2CServiceImpl(RequestHandler requestHandler, MpesaConfig config) {
        this.requestHandler = requestHandler;
        this.config = config;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public B2CPaymentResponse initiateB2CPayment(B2CPaymentRequest request) {
        String url = config.getB2cPaymentUrl();
        String responseJson = "";

        try {
            logger.info("Initiating B2C Payment request. URL: {}", url);
            responseJson = requestHandler.post(url, request);
            logger.debug("B2C Payment response received: {}", responseJson);
            return objectMapper.readValue(responseJson, B2CPaymentResponse.class);
        } catch (MpesaHttpException e) {
            logger.error("Error during B2C Payment request. URL: {}, Response: {}", url, e.getResponseBody(), e);
            throw new MpesaUnexpectedResponseException(
                    MpesaErrorCode.UNKNOWN_ERROR, e.getResponseBody(),
                    "Unexpected error in B2C Payment.", e
            );
        } catch (JsonProcessingException e) {
            logger.error("Failed to parse B2C Payment response. Response: {}", responseJson, e);
            throw new MpesaUnexpectedResponseException(
                    MpesaErrorCode.INVALID_RESPONSE, responseJson,
                    "Failed to parse B2C Payment response.", e
            );
        }
    }
}
