package dev.mpesa.sdk.dto.callback;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ValidationConfirmationResponse {
    @JsonProperty("ResultCode")
    public String resultCode;

    @JsonProperty("ResultDesc")
    public String resultDesc;

    @JsonProperty("ThirdPartyTransID")
    public String thirdPartyTransID;

    public ValidationConfirmationResponse() {}
}
