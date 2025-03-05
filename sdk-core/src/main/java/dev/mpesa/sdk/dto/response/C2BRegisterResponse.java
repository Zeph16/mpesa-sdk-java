package dev.mpesa.sdk.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class C2BRegisterResponse implements Serializable {
    private C2BRegisterResponseHeader header;

    public C2BRegisterResponse() {}

    public C2BRegisterResponse(C2BRegisterResponseHeader header) {
        this.header = header;
    }

    public C2BRegisterResponseHeader getHeader() { return header; }
    public void setHeader(C2BRegisterResponseHeader header) { this.header = header; }

    public boolean isSuccessful() {
        return header.getResponseCode().equals("0");
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class C2BRegisterResponseHeader implements Serializable {
        private String responseCode;
        private String responseMessage;
        private String customerMessage;
        private String timestamp;

        public C2BRegisterResponseHeader() {}

        public C2BRegisterResponseHeader(String responseCode, String responseMessage, String customerMessage, String timestamp) {
            this.responseCode = responseCode;
            this.responseMessage = responseMessage;
            this.customerMessage = customerMessage;
            this.timestamp = timestamp;
        }

        public String getResponseCode() { return responseCode; }
        public String getResponseMessage() { return responseMessage; }
        public String getCustomerMessage() { return customerMessage; }
        public String getTimestamp() { return timestamp; }
        public void setResponseCode(String responseCode) { this.responseCode = responseCode; }
        public void setResponseMessage(String responseMessage) { this.responseMessage = responseMessage; }
        public void setCustomerMessage(String customerMessage) { this.customerMessage = customerMessage; }
        public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    }
}
