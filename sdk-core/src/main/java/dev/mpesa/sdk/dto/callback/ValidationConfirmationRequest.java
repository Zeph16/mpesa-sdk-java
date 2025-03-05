package dev.mpesa.sdk.dto.callback;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ValidationConfirmationRequest implements Serializable {
    @JsonProperty("RequestType")
    public String requestType;

    @JsonProperty("TransactionType")
    public String transactionType;

    @JsonProperty("TransID")
    public String transID;

    @JsonProperty("TransTime")
    public String transTime;

    @JsonProperty("TransAmount")
    public String transAmount;

    @JsonProperty("BusinessShortCode")
    public String businessShortCode;

    @JsonProperty("BillRefNumber")
    public String billRefNumber;

    @JsonProperty("InvoiceNumber")
    public String invoiceNumber;

    @JsonProperty("OrgAccountBalance")
    public String orgAccountBalance;

    @JsonProperty("ThirdPartyTransID")
    public String thirdPartyTransID;

    @JsonProperty("MSISDN")
    public String msisdn;

    @JsonProperty("FirstName")
    public String firstName;

    @JsonProperty("MiddleName")
    public String middleName;

    @JsonProperty("LastName")
    public String lastName;

    public ValidationConfirmationRequest() {}
}
