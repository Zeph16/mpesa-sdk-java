package dev.mpesa.sdk.dto.request;

import dev.mpesa.sdk.util.ValidationUtils;

import java.io.Serializable;

public class TransactionStatusRequest implements Serializable {
    private final String Initiator;
    private final String SecurityCredential;
    private final String CommandID;
    private final String TransactionID;
    private final String OriginalConversationID;
    private final String PartyA;
    private final String IdentifierType;
    private final String ResultURL;
    private final String QueueTimeOutURL;
    private final String Remarks;
    private final String Occasion;

    private TransactionStatusRequest(Builder builder) {
        this.Initiator = builder.initiator;
        this.SecurityCredential = builder.securityCredential;
        this.CommandID = "TransactionStatusQuery";
        this.TransactionID = builder.transactionID;
        this.OriginalConversationID = builder.originalConversationID;
        this.PartyA = builder.partyA;
        this.IdentifierType = builder.identifierType;
        this.ResultURL = builder.resultURL;
        this.QueueTimeOutURL = builder.queueTimeOutURL;
        this.Remarks = builder.remarks;
        this.Occasion = builder.occasion;
    }

    public String getInitiator() { return Initiator; }
    public String getSecurityCredential() { return SecurityCredential; }
    public String getCommandID() { return CommandID; }
    public String getTransactionID() { return TransactionID; }
    public String getOriginalConversationID() { return OriginalConversationID; }
    public String getPartyA() { return PartyA; }
    public String getIdentifierType() { return IdentifierType; }
    public String getResultURL() { return ResultURL; }
    public String getQueueTimeOutURL() { return QueueTimeOutURL; }
    public String getRemarks() { return Remarks; }
    public String getOccasion() { return Occasion; }

    public static class Builder {
        private String initiator;
        private String securityCredential;
        private String transactionID;
        private String originalConversationID;
        private String partyA;
        private String identifierType;
        private String resultURL;
        private String queueTimeOutURL;
        private String remarks;
        private String occasion;

        public Builder initiator(String initiator) {
            ValidationUtils.requireNonEmpty(initiator, "Initiator");
            this.initiator = initiator;
            return this;
        }

        public Builder securityCredential(String securityCredential) {
            ValidationUtils.requireNonEmpty(securityCredential, "SecurityCredential");
            this.securityCredential = securityCredential;
            return this;
        }

        public Builder transactionID(String transactionID) {
            ValidationUtils.requireNonEmpty(transactionID, "TransactionID");
            this.transactionID = transactionID;
            return this;
        }

        public Builder originalConversationID(String originalConversationID) {
            ValidationUtils.requireNonEmpty(originalConversationID, "OriginalConversationID");
            this.originalConversationID = originalConversationID;
            return this;
        }

        public Builder partyA(String partyA) {
            ValidationUtils.requireNonEmpty(partyA, "PartyA");
            this.partyA = partyA;
            return this;
        }

        public Builder identifierType(String identifierType) {
            ValidationUtils.requireNonEmpty(identifierType, "IdentifierType");
            ValidationUtils.requireNumeric(identifierType, "IdentifierType");
            this.identifierType = identifierType;
            return this;
        }

        public Builder resultURL(String resultURL) {
            ValidationUtils.requireNonEmpty(resultURL, "ResultURL");
            ValidationUtils.requireValidURL(resultURL, "ResultURL");
            this.resultURL = resultURL;
            return this;
        }

        public Builder queueTimeOutURL(String queueTimeOutURL) {
            ValidationUtils.requireNonEmpty(queueTimeOutURL, "QueueTimeOutURL");
            ValidationUtils.requireValidURL(queueTimeOutURL, "QueueTimeOutURL");
            this.queueTimeOutURL = queueTimeOutURL;
            return this;
        }

        public Builder remarks(String remarks) {
            ValidationUtils.requireLength(remarks, 0, 100, "Remarks");
            this.remarks = remarks;
            return this;
        }

        public Builder occasion(String occasion) {
            ValidationUtils.requireLength(occasion, 0, 100, "Occasion");
            this.occasion = occasion;
            return this;
        }

        public TransactionStatusRequest build() {
            validateBeforeBuild();
            return new TransactionStatusRequest(this);
        }

        private void validateBeforeBuild() {
            ValidationUtils.requireNonEmpty(initiator, "Initiator");
            ValidationUtils.requireNonEmpty(securityCredential, "SecurityCredential");
            try {
                ValidationUtils.requireNonEmpty(transactionID, "TransactionID");
            } catch (IllegalArgumentException e) {
                ValidationUtils.requireNonEmpty(originalConversationID, "OriginalConversationID");
            }
            ValidationUtils.requireNonEmpty(partyA, "PartyA");
            ValidationUtils.requireNonEmpty(identifierType, "IdentifierType");
            ValidationUtils.requireNonEmpty(resultURL, "ResultURL");
            ValidationUtils.requireNonEmpty(queueTimeOutURL, "QueueTimeOutURL");
        }
    }

    @Override
    public String toString() {
        return "{" +
                "Initiator='" + Initiator + '\'' +
                ", SecurityCredential='***HIDDEN***'" +
                ", CommandID='" + CommandID + '\'' +
                ", TransactionID='" + TransactionID + '\'' +
                ", OriginalConversationID='" + OriginalConversationID + '\'' +
                ", PartyA='" + PartyA + '\'' +
                ", IdentifierType='" + IdentifierType + '\'' +
                ", ResultURL='" + ResultURL + '\'' +
                ", QueueTimeOutURL='" + QueueTimeOutURL + '\'' +
                ", Remarks='" + Remarks + '\'' +
                ", Occasion='" + Occasion + '\'' +
                '}';
    }

}
