package dev.mpesa.sdk.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mpesa.sdk.dto.response.MpesaErrorResponse;

/**
 * Exception thrown when the response from M-Pesa's API is unexpected or invalid.
 * This could occur when M-Pesa returns an error response that doesn't match the expected format or when the API
 * returns an error code indicating something went wrong.
 */
public class MpesaUnexpectedResponseException extends MpesaException {
    private final MpesaErrorCode errorCode;
    private MpesaErrorResponse errorResponse;
    private final String responseBody;

    /**
     * Constructor for MpesaUnexpectedResponseException with error code, response body, and message.
     *
     * @param errorCode The error code returned by M-Pesa's API.
     * @param responseBody The response body returned from M-Pesa's API.
     * @param message A message explaining the error.
     */
    public MpesaUnexpectedResponseException(MpesaErrorCode errorCode, String responseBody, String message) {
        super(message);
        this.errorCode = errorCode;
        this.responseBody = responseBody;
        parseResponseBody();
    }

    /**
     * Constructor for MpesaUnexpectedResponseException with error code, response body, message, and cause.
     *
     * @param errorCode The error code returned by M-Pesa's API.
     * @param responseBody The response body returned from M-Pesa's API.
     * @param message A message explaining the error.
     * @param cause The cause of the exception, usually another throwable.
     */
    public MpesaUnexpectedResponseException(MpesaErrorCode errorCode, String responseBody, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.responseBody = responseBody;
        parseResponseBody();
    }

    /**
     * Returns the error code associated with the unexpected response.
     *
     * @return The error code.
     */
    public MpesaErrorCode errorCode() {
        return errorCode;
    }

    /**
     * Returns the parsed error response received from M-Pesa's API.
     *
     * @return The parsed MpesaErrorResponse.
     */
    public MpesaErrorResponse errorResponse() {
        return errorResponse;
    }

    /**
     * Returns the raw response body received from M-Pesa's API.
     *
     * @return The raw response body as a string.
     */
    public String responseBody() {
        return responseBody;
    }

    /**
     * Attempts to parse the response body into an MpesaErrorResponse object.
     * If parsing fails, the errorResponse field will be set to null.
     */
    private void parseResponseBody() {
        if (responseBody != null && !responseBody.isEmpty()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                this.errorResponse = objectMapper.readValue(responseBody, MpesaErrorResponse.class);
            } catch (Exception e) {
                this.errorResponse = null;
            }
        }
    }
}
