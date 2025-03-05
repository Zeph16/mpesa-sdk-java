package dev.mpesa.sdk.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.mpesa.sdk.dto.KeyValue;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class C2BPaymentResponse implements Serializable {
    @JsonProperty("RequestRefID")
    private String requestRefID;

    @JsonProperty("ResponseCode")
    private String responseCode;

    @JsonProperty("ResponseDesc")
    private String responseDesc;

    @JsonProperty("TransactionID")
    private String transactionID;

    @JsonProperty("AdditionalInfo")
    private List<KeyValue> additionalInfo;

    public C2BPaymentResponse() {}

    public C2BPaymentResponse(String requestRefID, String responseCode, String responseDesc, String transactionID, List<KeyValue> additionalInfo) {
        this.requestRefID = requestRefID;
        this.responseCode = responseCode;
        this.responseDesc = responseDesc;
        this.transactionID = transactionID;
        this.additionalInfo = additionalInfo;
    }

    public String getRequestRefID() { return requestRefID; }
    public String getResponseCode() { return responseCode; }
    public String getResponseDesc() { return responseDesc; }
    public String getTransactionID() { return transactionID; }
    public List<KeyValue> getAdditionalInfo() { return additionalInfo; }
    public void setRequestRefID(String requestRefID) { this.requestRefID = requestRefID; }
    public void setResponseCode(String responseCode) { this.responseCode = responseCode; }
    public void setResponseDesc(String responseDesc) { this.responseDesc = responseDesc; }
    public void setTransactionID(String transactionID) { this.transactionID = transactionID; }
    public void setAdditionalInfo(List<KeyValue> additionalInfo) { this.additionalInfo = additionalInfo; }

    public boolean isSuccessful() {
        return responseCode.equals("0");
    }
}
