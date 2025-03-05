package dev.mpesa.sdk.service;

import dev.mpesa.sdk.dto.request.C2BPaymentRequest;
import dev.mpesa.sdk.dto.request.C2BRegisterRequest;
import dev.mpesa.sdk.dto.request.C2BSimulatePaymentRequest;
import dev.mpesa.sdk.dto.response.C2BPaymentResponse;
import dev.mpesa.sdk.dto.response.C2BRegisterResponse;
import dev.mpesa.sdk.dto.response.C2BSimulatePaymentResponse;
import dev.mpesa.sdk.exception.MpesaAuthenticationException;
import dev.mpesa.sdk.exception.MpesaNetworkException;
import dev.mpesa.sdk.exception.MpesaUnexpectedResponseException;


/**
 * Service interface for handling Customer-to-Business (C2B) transactions via the M-Pesa API.
 * <p>
 * This service allows merchants to:
 * <ul>
 *     <li>Register a C2B URL to receive payments</li>
 *     <li>Initiate a C2B payment</li>
 *     <li>Simulate a C2B payment for testing purposes</li>
 * </ul>
 */
public interface C2BService {

    /**
     * Registers a C2B URL for receiving customer payments.
     *
     * @param request the C2B registration request containing the short code and callback URLs
     * @param apiKey the API key required for authentication
     * @return the response containing registration details
     * @throws MpesaUnexpectedResponseException If the API response is invalid or cannot be parsed.
     * @throws MpesaAuthenticationException If the user can not be authenticated with the current credentials.
     * @throws MpesaNetworkException If there is a network issue.
     */
    C2BRegisterResponse registerC2B(C2BRegisterRequest request, String apiKey);

    /**
     * Initiates a C2B payment request.
     *
     * @param request the payment request containing customer details and transaction amount
     * @return the response containing payment details
     * @throws MpesaUnexpectedResponseException If the API response is invalid or cannot be parsed.
     * @throws MpesaAuthenticationException If the user can not be authenticated with the current credentials.
     * @throws MpesaNetworkException If there is a network issue.
     */
    C2BPaymentResponse initiatePayment(C2BPaymentRequest request);

    /**
     * Simulates a C2B payment for testing purposes.
     *
     * @param request the simulation request containing transaction details
     * @return the response containing simulated payment details
     * @throws MpesaUnexpectedResponseException If the API response is invalid or cannot be parsed.
     * @throws MpesaAuthenticationException If the user can not be authenticated with the current credentials.
     * @throws MpesaNetworkException If there is a network issue.
     */
    C2BSimulatePaymentResponse simulateC2BPayment(C2BSimulatePaymentRequest request);
}
