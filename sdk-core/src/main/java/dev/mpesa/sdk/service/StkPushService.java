package dev.mpesa.sdk.service;

import dev.mpesa.sdk.dto.request.StkPushRequest;
import dev.mpesa.sdk.dto.response.StkPushResponse;
import dev.mpesa.sdk.exception.MpesaAuthenticationException;
import dev.mpesa.sdk.exception.MpesaNetworkException;
import dev.mpesa.sdk.exception.MpesaUnexpectedResponseException;

/**
 * Service interface for handling STK Push transactions in M-Pesa.
 * <p>
 * STK Push allows initiating a payment request where a user is prompted on their phone
 * to enter their M-Pesa PIN to complete a transaction.
 * </p>
 */
public interface StkPushService {

    /**
     * Sends an STK Push request to M-Pesa to initiate a payment transaction.
     *
     * @param request The STK Push request details, including phone number and amount.
     * @return The response from M-Pesa containing transaction details.
     * @throws MpesaUnexpectedResponseException If the API response is invalid or cannot be parsed.
     * @throws MpesaAuthenticationException If the user can not be authenticated with the current credentials.
     * @throws MpesaNetworkException If there is a network issue.
     */
    StkPushResponse requestStkPush(StkPushRequest request);
}
