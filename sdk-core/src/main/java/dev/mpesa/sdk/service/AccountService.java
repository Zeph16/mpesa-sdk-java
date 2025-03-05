package dev.mpesa.sdk.service;

import dev.mpesa.sdk.dto.request.AccountBalanceRequest;
import dev.mpesa.sdk.dto.response.AccountBalanceResponse;
import dev.mpesa.sdk.exception.MpesaAuthenticationException;
import dev.mpesa.sdk.exception.MpesaNetworkException;
import dev.mpesa.sdk.exception.MpesaUnexpectedResponseException;

/*
 * Service interface for interacting with the M-Pesa API to deal with accounts, currently to check account balances.
 */
public interface AccountService {

    /**
     * Checks the account balance by making a request to the M-Pesa API.
     *
     * @param request the account balance request containing necessary details
     * @return the account balance response from M-Pesa
     * @throws MpesaUnexpectedResponseException if the response cannot be parsed or an unexpected error occurs
     * @throws MpesaAuthenticationException If the user can not be authenticated with the current credentials.
     * @throws MpesaNetworkException If there is a network issue.
     */
    AccountBalanceResponse checkAccountBalance(AccountBalanceRequest request);
}
