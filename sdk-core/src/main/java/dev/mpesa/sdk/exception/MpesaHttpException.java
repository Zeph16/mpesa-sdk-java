package dev.mpesa.sdk.exception;

/**
 * Exception thrown when an HTTP error occurs during communication with M-Pesa's API.
 * This can be used to represent situations like 4xx or 5xx HTTP errors returned by M-Pesa's API.
 */
public class MpesaHttpException extends MpesaException {
    private final int statusCode;
    private final String responseBody;

    /**
     * Constructor for MpesaHttpException with HTTP status code, response body, and message.
     *
     * @param statusCode The HTTP status code returned by M-Pesa's API.
     * @param responseBody The response body returned from M-Pesa's API, typically contains error details.
     * @param message A message explaining the error.
     */
    public MpesaHttpException(int statusCode, String responseBody, String message) {
        super(message);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    /**
     * Constructor for MpesaHttpException with HTTP status code, response body, message, and cause of the error.
     *
     * @param statusCode The HTTP status code returned by M-Pesa's API.
     * @param responseBody The response body returned from M-Pesa's API, typically contains error details.
     * @param message A message explaining the error.
     * @param cause The cause of the exception, usually another throwable.
     */
    public MpesaHttpException(int statusCode, String responseBody, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    /**
     * Returns the HTTP status code that was returned by M-Pesa's API.
     *
     * @return The HTTP status code.
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Returns the response body that was received from M-Pesa's API.
     *
     * @return The response body as a string.
     */
    public String getResponseBody() {
        return responseBody;
    }
}
