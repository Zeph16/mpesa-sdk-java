package dev.mpesa.sdk.exception;

/**
 * EXPERIMENTAL (NOT EXHAUSTIVE)
 * Enum representing various error codes that can occur during interaction with M-Pesa's API.
 * These error codes help identify specific issues with the request or response.
 */
public enum MpesaErrorCode {
    SHORT_CODE_REGISTERED,   // The short code is already registered.
    INVALID_RESPONSE,        // The response received was not valid or expected.
    INVALID_REQUEST,         // The request sent to M-Pesa's API is invalid.
    INVALID_INITIATOR,       // The initiator of the request is invalid or unauthorized.
    UNKNOWN_ERROR            // An unknown error occurred.
}
