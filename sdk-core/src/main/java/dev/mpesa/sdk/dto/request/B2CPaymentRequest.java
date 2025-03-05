package dev.mpesa.sdk.dto.request;

import dev.mpesa.sdk.util.ValidationUtils;

import java.io.Serializable;

public class B2CPaymentRequest implements Serializable {
    private final String InitiatorName;
    private final String SecurityCredential;
    private final String Occassion;
    private final String CommandID;
    private final String PartyA;
    private final String PartyB;
    private final String Remarks;
    private final String Amount;
    private final String QueueTimeOutURL;
    private final String ResultURL;

    private B2CPaymentRequest(Builder builder) {
        this.InitiatorName = builder.initiatorName;
        this.SecurityCredential = builder.securityCredential;
        this.Occassion = builder.occassion;
        this.CommandID = builder.commandID;
        this.PartyA = builder.partyA;
        this.PartyB = builder.partyB;
        this.Remarks = builder.remarks;
        this.Amount = builder.amount;
        this.QueueTimeOutURL = builder.queueTimeOutURL;
        this.ResultURL = builder.resultURL;
    }

    public String getInitiatorName() { return InitiatorName; }
    public String getSecurityCredential() { return SecurityCredential; }
    public String getOccassion() { return Occassion; }
    public String getCommandID() { return CommandID; }
    public String getPartyA() { return PartyA; }
    public String getPartyB() { return PartyB; }
    public String getRemarks() { return Remarks; }
    public String getAmount() { return Amount; }
    public String getQueueTimeOutURL() { return QueueTimeOutURL; }
    public String getResultURL() { return ResultURL; }

    public static class Builder {
        private String initiatorName;
        private String securityCredential;
        private String occassion;
        private String commandID;
        private String partyA;
        private String partyB;
        private String remarks;
        private String amount;
        private String queueTimeOutURL;
        private String resultURL;

        public Builder initiatorName(String initiatorName) {
            ValidationUtils.requireNonEmpty(initiatorName, "InitiatorName");
            this.initiatorName = initiatorName;
            return this;
        }

        public Builder securityCredential(String securityCredential) {
            ValidationUtils.requireNonEmpty(securityCredential, "SecurityCredential");
            ValidationUtils.requireBase64Encoded(securityCredential, "SecurityCredential");
            this.securityCredential = securityCredential;
            return this;
        }

        public Builder occassion(String occassion) {
            ValidationUtils.requireNonEmpty(occassion, "Occasion");
            this.occassion = occassion;
            return this;
        }

        public Builder commandID(String commandID) {
            ValidationUtils.requireNonEmpty(commandID, "CommandID");
            if (!commandID.equals("SalaryPayment") &&
                !commandID.equals("BusinessPayment") &&
                !commandID.equals("PromotionPayment")) {
                throw new IllegalArgumentException("Command ID must be one of 'SalaryPayment', 'BusinessPayment', and 'PromotionPayment'.");
            }
            this.commandID = commandID;
            return this;
        }

        public Builder partyA(String partyA) {
            ValidationUtils.requireNonEmpty(partyA, "PartyA");
            ValidationUtils.requireValidShortCode(partyA, "PartyA");
            this.partyA = partyA;
            return this;
        }

        public Builder partyB(String partyB) {
            ValidationUtils.requireNonEmpty(partyB, "PartyB");
            ValidationUtils.requireValidPhoneNumber(partyB, "PartyB");
            this.partyB = partyB;
            return this;
        }

        public Builder remarks(String remarks) {
            ValidationUtils.requireNonEmpty(remarks, "Remarks");
            this.remarks = remarks;
            return this;
        }

        public Builder amount(String amount) {
            ValidationUtils.requireNonEmpty(amount, "Amount");
            ValidationUtils.requireNumeric(amount, "Amount");
            this.amount = amount;
            return this;
        }

        public Builder queueTimeOutURL(String queueTimeOutURL) {
            ValidationUtils.requireNonEmpty(queueTimeOutURL, "QueueTimeOutURL");
            ValidationUtils.requireValidURL(queueTimeOutURL, "QueueTimeOutURL");
            this.queueTimeOutURL = queueTimeOutURL;
            return this;
        }

        public Builder resultURL(String resultURL) {
            ValidationUtils.requireNonEmpty(resultURL, "ResultURL");
            ValidationUtils.requireValidURL(resultURL, "ResultURL");
            this.resultURL = resultURL;
            return this;
        }

        public B2CPaymentRequest build() {
            validateBeforeBuild();
            return new B2CPaymentRequest(this);
        }

        private void validateBeforeBuild() {
            ValidationUtils.requireNonEmpty(initiatorName, "InitiatorName");
            ValidationUtils.requireNonEmpty(securityCredential, "SecurityCredential");
            ValidationUtils.requireNonEmpty(commandID, "CommandID");
            ValidationUtils.requireNonEmpty(partyA, "PartyA");
            ValidationUtils.requireNonEmpty(partyB, "PartyB");
            ValidationUtils.requireNonEmpty(remarks, "Remarks");
            ValidationUtils.requireNonEmpty(amount, "Amount");
            ValidationUtils.requireNonEmpty(queueTimeOutURL, "QueueTimeOutURL");
            ValidationUtils.requireNonEmpty(resultURL, "ResultURL");
        }
    }

    @Override
    public String toString() {
        return "{" +
                "InitiatorName='" + InitiatorName + '\'' +
                ", SecurityCredential='***HIDDEN***'" +
                ", Occassion='" + Occassion + '\'' +
                ", CommandID='" + CommandID + '\'' +
                ", PartyA='" + PartyA + '\'' +
                ", PartyB='" + PartyB + '\'' +
                ", Remarks='" + Remarks + '\'' +
                ", Amount='" + Amount + '\'' +
                ", QueueTimeOutURL='" + QueueTimeOutURL + '\'' +
                ", ResultURL='" + ResultURL + '\'' +
                '}';
    }

}
