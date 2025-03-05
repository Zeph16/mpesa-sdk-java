package dev.mpesa.sdk.exception;

/**
 * Exception thrown when a network error occurs, such as when the SDK is unable to communicate with M-Pesa's API.
 * This could happen due to connectivity issues, timeouts, or DNS resolution failures.
 */
public class MpesaNetworkException extends MpesaException {

    /**
     * Constructor for MpesaNetworkException with a message.
     *
     * @param message A message explaining the network error.
     */
    public MpesaNetworkException(String message) {
        super(message);
    }

    /**
     * Constructor for MpesaNetworkException with a message and the cause of the error.
     *
     * @param message A message explaining the network error.
     * @param cause The cause of the exception, usually another throwable.
     */
    public MpesaNetworkException(String message, Throwable cause) {
        super(message, cause);
    }
}
