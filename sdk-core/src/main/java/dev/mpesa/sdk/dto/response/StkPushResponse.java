package dev.mpesa.sdk.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StkPushResponse implements Serializable {
    @JsonProperty("MerchantRequestID")
    private String merchantRequestID;

    @JsonProperty("CheckoutRequestID")
    private String checkoutRequestID;

    @JsonProperty("ResponseCode")
    private String responseCode;

    @JsonProperty("ResponseDescription")
    private String responseDescription;

    @JsonProperty("CustomerMessage")
    private String customerMessage;

    public StkPushResponse() {}
    public StkPushResponse(String merchantRequestID, String checkoutRequestID, String responseCode, String responseDescription, String customerMessage) {
        this.merchantRequestID = merchantRequestID;
        this.checkoutRequestID = checkoutRequestID;
        this.responseCode = responseCode;
        this.responseDescription = responseDescription;
        this.customerMessage = customerMessage;
    }

    public String getMerchantRequestID() { return merchantRequestID; }
    public String getCheckoutRequestID() { return checkoutRequestID; }
    public String getResponseCode() { return responseCode; }
    public String getResponseDescription() { return responseDescription; }
    public String getCustomerMessage() { return customerMessage; }
    public void setMerchantRequestID(String merchantRequestID) { this.merchantRequestID = merchantRequestID; }
    public void setCheckoutRequestID(String checkoutRequestID) { this.checkoutRequestID = checkoutRequestID; }
    public void setResponseCode(String responseCode) { this.responseCode = responseCode; }
    public void setResponseDescription(String responseDescription) { this.responseDescription = responseDescription; }
    public void setCustomerMessage(String customerMessage) { this.customerMessage = customerMessage; }

    public boolean isSuccessful() {
        return "0".equals(responseCode);
    }
}
