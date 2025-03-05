package dev.mpesa.sdk.service;

import dev.mpesa.sdk.dto.request.B2CPaymentRequest;
import dev.mpesa.sdk.dto.response.B2CPaymentResponse;
import dev.mpesa.sdk.exception.MpesaAuthenticationException;
import dev.mpesa.sdk.exception.MpesaNetworkException;
import dev.mpesa.sdk.exception.MpesaUnexpectedResponseException;

/*
 * Service interface for handling all Business-to-Customer (B2C) operations done via the M-Pesa API.
 */
public interface B2CService {

    /**
     * Initiates a B2C payment request by sending the request to the M-Pesa API.
     *
     * @param request the B2C payment request containing necessary details
     * @return the response from the M-Pesa API
     * @throws MpesaUnexpectedResponseException if the response cannot be parsed or an unexpected error occurs
     * @throws MpesaAuthenticationException If the user can not be authenticated with the current credentials.
     * @throws MpesaNetworkException If there is a network issue.
     */
    B2CPaymentResponse initiateB2CPayment(B2CPaymentRequest request);
}
