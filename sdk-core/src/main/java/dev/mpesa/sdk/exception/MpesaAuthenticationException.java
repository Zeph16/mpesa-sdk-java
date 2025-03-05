package dev.mpesa.sdk.exception;

/**
 * Exception thrown when there is an issue with authentication while communicating with M-Pesa's API.
 * This typically occurs when authentication fails, such as due to invalid credentials or expired tokens.
 */
public class MpesaAuthenticationException extends MpesaException {
    private final String responseBody;

    /**
     * Constructor for MpesaAuthenticationException with a response body and message.
     *
     * @param responseBody The response body returned from M-Pesa's API, typically contains error details.
     * @param message A message explaining the error.
     */
    public MpesaAuthenticationException(String responseBody, String message) {
        super(message);
        this.responseBody = responseBody;
    }

    /**
     * Constructor for MpesaAuthenticationException with a response body, message, and cause of the error.
     *
     * @param responseBody The response body returned from M-Pesa's API, typically contains error details.
     * @param message A message explaining the error.
     * @param cause The cause of the exception, usually another throwable.
     */
    public MpesaAuthenticationException(String responseBody, String message, Throwable cause) {
        super(message, cause);
        this.responseBody = responseBody;
    }

    /**
     * Returns the response body that was received from M-Pesa's API.
     *
     * @return The response body as a string.
     */
    public String responseBody() {
        return responseBody;
    }
}
