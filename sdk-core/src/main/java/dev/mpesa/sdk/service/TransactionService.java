package dev.mpesa.sdk.service;

import dev.mpesa.sdk.dto.request.TransactionReversalRequest;
import dev.mpesa.sdk.dto.request.TransactionStatusRequest;
import dev.mpesa.sdk.dto.response.TransactionReversalResponse;
import dev.mpesa.sdk.dto.response.TransactionStatusResponse;
import dev.mpesa.sdk.exception.MpesaAuthenticationException;
import dev.mpesa.sdk.exception.MpesaNetworkException;
import dev.mpesa.sdk.exception.MpesaUnexpectedResponseException;

/**
 * Service interface for handling M-Pesa transaction operations.
 * <p>
 * This service provides methods for checking the status of a transaction
 * and performing transaction reversals.
 * </p>
 */
public interface TransactionService {

    /**
     * Checks the status of a transaction.
     *
     * @param request The transaction status request details, including the transaction ID.
     * @return The response from M-Pesa containing the transaction status details.
     * @throws MpesaUnexpectedResponseException If the API response is invalid or cannot be parsed.
     * @throws MpesaAuthenticationException If the user can not be authenticated with the current credentials.
     * @throws MpesaNetworkException If there is a network issue.
     */
    TransactionStatusResponse checkTransactionStatus(TransactionStatusRequest request);

    /**
     * Reverses a previously completed transaction.
     *
     * @param request The transaction reversal request details, including the transaction ID.
     * @return The response from M-Pesa indicating whether the reversal was successful.
     * @throws MpesaUnexpectedResponseException If the API response is invalid or cannot be parsed.
     * @throws MpesaAuthenticationException If the user can not be authenticated with the current credentials.
     * @throws MpesaNetworkException If there is a network issue.
     */
    TransactionReversalResponse reverseTransaction(TransactionReversalRequest request);
}

