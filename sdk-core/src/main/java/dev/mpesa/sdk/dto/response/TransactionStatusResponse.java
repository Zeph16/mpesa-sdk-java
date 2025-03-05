package dev.mpesa.sdk.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionStatusResponse implements Serializable {
    @JsonProperty("OriginatorConversationID")
    private String originatorConversationID;

    @JsonProperty("ConversationID")
    private String conversationID;

    @JsonProperty("ResponseCode")
    private String responseCode;

    @JsonProperty("ResponseDescription")
    private String responseDescription;

    public TransactionStatusResponse() {}

    public TransactionStatusResponse(String originatorConversationID, String conversationID, String responseCode, String responseDescription) {
        this.originatorConversationID = originatorConversationID;
        this.conversationID = conversationID;
        this.responseCode = responseCode;
        this.responseDescription = responseDescription;
    }

    public String getOriginatorConversationID() { return originatorConversationID; }
    public String getConversationID() { return conversationID; }
    public String getResponseCode() { return responseCode; }
    public String getResponseDescription() { return responseDescription; }
    public void setOriginatorConversationID(String originatorConversationID) { this.originatorConversationID = originatorConversationID; }
    public void setConversationID(String conversationID) { this.conversationID = conversationID; }
    public void setResponseCode(String responseCode) { this.responseCode = responseCode; }
    public void setResponseDescription(String responseDescription) { this.responseDescription = responseDescription; }

    public boolean isSuccessful() {
        return "0".equals(responseCode);
    }
}
