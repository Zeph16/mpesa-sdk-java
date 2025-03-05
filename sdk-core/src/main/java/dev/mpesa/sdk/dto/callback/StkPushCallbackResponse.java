package dev.mpesa.sdk.dto.callback;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StkPushCallbackResponse {
    @JsonProperty("Body")
    public StkPushCallbackBody body;

    public StkPushCallbackResponse() {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StkPushCallbackBody {
        public StkCallback stkCallback;
        public StkPushCallbackBody() {}
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StkCallback {
        @JsonProperty("MerchantRequestID")
        public String merchantRequestID;

        @JsonProperty("CheckoutRequestID")
        public String checkoutRequestID;

        @JsonProperty("ResultCode")
        public int resultCode;

        @JsonProperty("ResultDesc")
        public String resultDesc;

        @JsonProperty("CallbackMetadata")
        public CallbackMetadata callbackMetadata;

        public StkCallback() {}
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CallbackMetadata {
        @JsonProperty("Item")
        public List<CallbackItem> items;

        public CallbackMetadata() {}
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CallbackItem {
        @JsonProperty("Name")
        public String name;

        @JsonProperty("Value")
        public Object value;

        public CallbackItem() {}
    }
}


