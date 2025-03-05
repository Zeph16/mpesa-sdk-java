package dev.mpesa.sdk.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class B2CPaymentResponse implements Serializable {
    @JsonProperty("OriginatorConversationID")
    private String originatorConversationID;

    @JsonProperty("ConversationID")
    private String conversationID;

    @JsonProperty("ResponseCode")
    private String responseCode;

    @JsonProperty("ResponseDescription")
    private String responseDescription;

    public B2CPaymentResponse() {}

    public B2CPaymentResponse(String conversationID, String originatorConversationID, String responseCode, String responseDescription) {
        this.conversationID = conversationID;
        this.originatorConversationID = originatorConversationID;
        this.responseCode = responseCode;
        this.responseDescription = responseDescription;
    }

    public String getConversationID() { return conversationID; }
    public String getOriginatorConversationID() { return originatorConversationID; }
    public String getResponseCode() { return responseCode; }
    public String getResponseDescription() { return responseDescription; }
    public void setConversationID(String conversationID) {
        this.conversationID = conversationID;
    }
    public void setOriginatorConversationID(String originatorConversationID) {
        this.originatorConversationID = originatorConversationID;
    }
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }
    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    public boolean isSuccessful() {
        return "0".equals(responseCode);
    }
}
