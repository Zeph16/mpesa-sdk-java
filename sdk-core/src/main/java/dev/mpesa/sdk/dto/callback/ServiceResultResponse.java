package dev.mpesa.sdk.dto.callback;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceResultResponse {
    @JsonProperty("Result")
    public Result result;

    public ServiceResultResponse() {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {
        @JsonProperty("ResultType")
        public int resultType;

        @JsonProperty("ResultCode")
        public int resultCode;

        @JsonProperty("ResultDesc")
        public String resultDesc;

        @JsonProperty("OriginatorConversationID")
        public String originatorConversationID;

        @JsonProperty("ConversationID")
        public String conversationID;

        @JsonProperty("TransactionID")
        public String transactionID;

        @JsonProperty("ResultParameters")
        public ResultParameters resultParameters;

        @JsonProperty("ReferenceData")
        public ReferenceData referenceData;

        public Result() {}

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class ResultParameters {
            @JsonProperty("ResultParameter")
            public List<ResultParameter> resultParameter;

            public ResultParameters() {}

            public static class ResultParameter {
                @JsonProperty("Key")
                public String key;

                @JsonProperty("Value")
                public Object value;

                public ResultParameter() {}
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class ReferenceData {
            @JsonProperty("ReferenceItem")
            public ReferenceItem referenceItem;

            public ReferenceData() {}

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class ReferenceItem {
                @JsonProperty("Key")
                public String key;

                @JsonProperty("Value")
                public Object value;

                public ReferenceItem() {}
            }
        }
    }
}
