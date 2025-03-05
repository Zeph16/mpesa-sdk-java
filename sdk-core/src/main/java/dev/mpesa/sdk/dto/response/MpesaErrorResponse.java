package dev.mpesa.sdk.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MpesaErrorResponse implements Serializable {
    private String requestId;
    private String errorCode;
    private String errorMessage;

    public MpesaErrorResponse() {}

    public MpesaErrorResponse(String requestId, String errorCode, String errorMessage) {
        this.requestId = requestId;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getRequestId() { return requestId; }
    public String getErrorCode() { return errorCode; }
    public String getErrorMessage() { return errorMessage; }
    public void setRequestId(String requestId) { this.requestId = requestId; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}
