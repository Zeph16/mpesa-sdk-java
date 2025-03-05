package dev.mpesa.sdk.dto.request;

import dev.mpesa.sdk.dto.KeyValue;
import dev.mpesa.sdk.util.ValidationUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class StkPushRequest implements Serializable {
    private final String MerchantRequestID;
    private final String BusinessShortCode;
    private final String Password;
    private final String Timestamp;
    private final String TransactionType;
    private final String Amount;
    private final String PartyA;
    private final String PartyB;
    private final String PhoneNumber;
    private final String CallBackURL;
    private final String AccountReference;
    private final String TransactionDesc;
    private final List<KeyValue> ReferenceData;

    private StkPushRequest(Builder builder) {
        this.MerchantRequestID = builder.merchantRequestID;
        this.BusinessShortCode = builder.businessShortCode;
        this.Password = builder.password;

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        this.Timestamp = now.format(formatter);

        this.TransactionType = "CustomerPayBillOnline";
        this.Amount = builder.amount;
        this.PartyA = builder.partyA;
        this.PartyB = builder.partyB;
        this.PhoneNumber = builder.phoneNumber;
        this.CallBackURL = builder.callBackURL;
        this.AccountReference = builder.accountReference;
        this.TransactionDesc = builder.transactionDesc;
        this.ReferenceData = builder.referenceData;
    }

    public static class Builder {
        private final String merchantRequestID = UUID.randomUUID().toString();
        private String businessShortCode;
        private String transactionType;
        private String password;
        private String amount;
        private String partyA;
        private String partyB;
        private String phoneNumber;
        private String callBackURL;
        private String accountReference;
        private String transactionDesc;
        private final List<KeyValue> referenceData = new ArrayList<>();

        public Builder businessShortCode(String businessShortCode) {
            ValidationUtils.requireNonEmpty(businessShortCode, "BusinessShortCode");
            ValidationUtils.requireValidShortCode(businessShortCode, "BusinessShortCode");
            this.businessShortCode = businessShortCode;
            return this;
        }

        public Builder transactionType(TransactionType transactionType) {
            assert transactionType != null;
            this.transactionType = transactionType.toString();
            return this;
        }

        public Builder password(String password) {
            ValidationUtils.requireNonEmpty(password, "Password");
            ValidationUtils.requireBase64Encoded(password, "Password");

            this.password = password;
            return this;
        }

        public Builder amount(String amount) {
            ValidationUtils.requireNonEmpty(amount, "Amount");
            ValidationUtils.requireNumeric(amount, "Amount");
            this.amount = amount;
            return this;
        }

        public Builder partyA(String partyA) {
            ValidationUtils.requireNonEmpty(partyA, "PartyA");
            ValidationUtils.requireValidPhoneNumber(partyA, "PartyA");
            ValidationUtils.requireNumeric(partyA, "PartyA");

            this.partyA = partyA;
            return this;
        }

        public Builder partyB(String partyB) {
            ValidationUtils.requireNonEmpty(partyB, "PartyB");
            ValidationUtils.requireValidShortCode(partyB, "PartyB");
            this.partyB = partyB;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            ValidationUtils.requireNonEmpty(phoneNumber, "PhoneNumber");
            ValidationUtils.requireValidPhoneNumber(phoneNumber, "PhoneNumber");
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder callBackURL(String callBackURL) {
            ValidationUtils.requireNonEmpty(callBackURL, "CallBackURL");
            ValidationUtils.requireValidURL(callBackURL, "CallBackURL");
            this.callBackURL = callBackURL;
            return this;
        }

        public Builder accountReference(String accountReference) {
            ValidationUtils.requireNonEmpty(accountReference, "AccountReference");
            ValidationUtils.requireLength(accountReference, 1, 12, "AccountReference");
            this.accountReference = accountReference;

            return this;
        }

        public Builder transactionDesc(String transactionDesc) {
            ValidationUtils.requireNonEmpty(transactionDesc, "TransactionDesc");
            ValidationUtils.requireLength(transactionDesc, 1, 13, "TransactionDesc");

            this.transactionDesc = transactionDesc;
            return this;
        }

        public Builder addReferenceData(String key, String value) {
            ValidationUtils.requireNonEmpty(key, "ReferenceData Key");
            ValidationUtils.requireNonEmpty(value, "ReferenceData Value");
            this.referenceData.add(new KeyValue(key, value));
            return this;
        }

        public StkPushRequest build() {
            validateBeforeBuild();
            return new StkPushRequest(this);
        }

        private void validateBeforeBuild() {
            ValidationUtils.requireNonEmpty(businessShortCode, "BusinessShortCode");
            ValidationUtils.requireNonEmpty(password, "Password");
            ValidationUtils.requireNonEmpty(transactionType, "TransactionType");
            ValidationUtils.requireNonEmpty(amount, "Amount");
            ValidationUtils.requireNonEmpty(partyA, "PartyA");
            ValidationUtils.requireNonEmpty(partyB, "PartyB");
            ValidationUtils.requireNonEmpty(phoneNumber, "PhoneNumber");
            ValidationUtils.requireNonEmpty(callBackURL, "CallBackURL");
            ValidationUtils.requireNonEmpty(accountReference, "AccountReference");
            ValidationUtils.requireNonEmpty(transactionDesc, "TransactionDesc");
        }

    }

    public String getMerchantRequestID() { return MerchantRequestID; }
    public String getBusinessShortCode() { return BusinessShortCode; }
    public String getPassword() { return Password; }
    public String getTimestamp() { return Timestamp; }
    public String getTransactionType() { return TransactionType; }
    public String getAmount() { return Amount; }
    public String getPartyA() { return PartyA; }
    public String getPartyB() { return PartyB; }
    public String getPhoneNumber() { return PhoneNumber; }
    public String getCallBackURL() { return CallBackURL; }
    public String getAccountReference() { return AccountReference; }
    public String getTransactionDesc() { return TransactionDesc; }
    public List<KeyValue> getReferenceData() { return ReferenceData; }

    @Override
    public String toString() {
        return "{" +
                "MerchantRequestID='" + MerchantRequestID + '\'' +
                ", BusinessShortCode='" + BusinessShortCode + '\'' +
                ", Password='***HIDDEN***'" +
                ", Timestamp='" + Timestamp + '\'' +
                ", TransactionType='" + TransactionType + '\'' +
                ", Amount='" + Amount + '\'' +
                ", PartyA='" + PartyA + '\'' +
                ", PartyB='" + PartyB + '\'' +
                ", PhoneNumber='" + PhoneNumber + '\'' +
                ", CallBackURL='" + CallBackURL + '\'' +
                ", AccountReference='" + AccountReference + '\'' +
                ", TransactionDesc='" + TransactionDesc + '\'' +
                ", ReferenceData=" + ReferenceData.stream().map(Object::toString).collect(Collectors.joining(",")) +
                '}';
    }



    public enum TransactionType {
        CustomerBuyGoodsOnline, CustomerPayBillOnline
    }


}
