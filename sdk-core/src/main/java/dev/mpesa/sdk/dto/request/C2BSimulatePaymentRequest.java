package dev.mpesa.sdk.dto.request;

import dev.mpesa.sdk.util.ValidationUtils;

import java.io.Serializable;
import java.util.UUID;

public class C2BSimulatePaymentRequest implements Serializable {
    private final String CommandID;
    private final String Amount;
    private final String Msisdn;
    private final String BillRefNumber;
    private final String ShortCode;

    private C2BSimulatePaymentRequest(Builder builder) {
        this.CommandID = builder.commandId != null ? builder.commandId : "CustomerPayBillOnline";
        this.Amount = builder.amount;
        this.Msisdn = builder.msisdn;
        this.BillRefNumber = builder.billRefNumber != null ? builder.billRefNumber : UUID.randomUUID().toString();
        this.ShortCode = builder.shortCode;
    }

    public String getCommandID() { return CommandID; }
    public String getAmount() { return Amount; }
    public String getMsisdn() { return Msisdn; }
    public String getBillRefNumber() { return BillRefNumber; }
    public String getShortCode() { return ShortCode; }

    public static class Builder {
        private String commandId;
        private String amount;
        private String msisdn;
        private String billRefNumber;
        private String shortCode;

        public Builder amount(String amount) {
            ValidationUtils.requireNonEmpty(amount, "Amount");
            ValidationUtils.requireNumeric(amount, "Amount");
            this.amount = amount;
            return this;
        }

        public Builder msisdn(String msisdn) {
            ValidationUtils.requireNonEmpty(msisdn, "Msisdn");
            ValidationUtils.requireValidPhoneNumber(msisdn, "Msisdn");
            this.msisdn = msisdn;
            return this;
        }

        public Builder billRefNumber(String billRefNumber) {
            ValidationUtils.requireNonEmpty(billRefNumber, "BillRefNumber");
            this.billRefNumber = billRefNumber;
            return this;
        }

        public Builder shortCode(String shortCode) {
            ValidationUtils.requireNonEmpty(shortCode, "ShortCode");
            ValidationUtils.requireValidShortCode(shortCode, "ShortCode");
            this.shortCode = shortCode;
            return this;
        }

        public C2BSimulatePaymentRequest build() {
            validateBeforeBuild();
            return new C2BSimulatePaymentRequest(this);
        }

        public void validateBeforeBuild() {
            ValidationUtils.requireNonEmpty(amount, "Amount");
            ValidationUtils.requireNonEmpty(msisdn, "Msisdn");
            ValidationUtils.requireNonEmpty(shortCode, "ShortCode");
        }
    }

    @Override
    public String toString() {
        return "{" +
                "CommandID='" + CommandID + '\'' +
                ", Amount='" + Amount + '\'' +
                ", Msisdn='" + Msisdn + '\'' +
                ", BillRefNumber='" + BillRefNumber + '\'' +
                ", ShortCode='" + ShortCode + '\'' +
                '}';
    }

}
