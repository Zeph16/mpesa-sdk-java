package dev.mpesa.sdk.dto.request;

import dev.mpesa.sdk.util.ValidationUtils;

import java.io.Serializable;

public class TransactionReversalRequest implements Serializable {
    private final String OriginatorConversationID;
    private final String Initiator;
    private final String SecurityCredential;
    private final String CommandID;
    private final String TransactionID;
    private final String Amount;
    private final String OriginalConversationID;
    private final String PartyA;
    private final String ReceiverIdentifierType;
    private final String ReceiverParty;
    private final String ResultURL;
    private final String QueueTimeOutURL;
    private final String Remarks;
    private final String Occasion;

    private TransactionReversalRequest(Builder builder) {
        this.OriginatorConversationID = builder.originatorConversationID;
        this.Initiator = builder.initiator;
        this.SecurityCredential = builder.securityCredential;
        this.CommandID = "TransactionReversal"; // Fixed command
        this.TransactionID = builder.transactionID;
        this.Amount = builder.amount;
        this.OriginalConversationID = builder.originalConversationID;
        this.PartyA = builder.partyA;
        this.ReceiverIdentifierType = builder.receiverIdentifierType;
        this.ReceiverParty = builder.receiverParty;
        this.ResultURL = builder.resultURL;
        this.QueueTimeOutURL = builder.queueTimeOutURL;
        this.Remarks = builder.remarks;
        this.Occasion = builder.occasion;
    }

    public String getOriginatorConversationID() { return OriginatorConversationID; }
    public String getInitiator() { return Initiator; }
    public String getSecurityCredential() { return SecurityCredential; }
    public String getCommandID() { return CommandID; }
    public String getTransactionID() { return TransactionID; }
    public String getAmount() { return Amount; }
    public String getOriginalConversationID() { return OriginalConversationID; }
    public String getPartyA() { return PartyA; }
    public String getReceiverIdentifierType() { return ReceiverIdentifierType; }
    public String getReceiverParty() { return ReceiverParty; }
    public String getResultURL() { return ResultURL; }
    public String getQueueTimeOutURL() { return QueueTimeOutURL; }
    public String getRemarks() { return Remarks; }
    public String getOccasion() { return Occasion; }

    public static class Builder {
        private String originatorConversationID;
        private String initiator;
        private String securityCredential;
        private String transactionID;
        private String amount;
        private String originalConversationID;
        private String partyA;
        private String receiverIdentifierType;
        private String receiverParty;
        private String resultURL;
        private String queueTimeOutURL;
        private String remarks;
        private String occasion;

        public Builder originatorConversationID(String originatorConversationID) {
            ValidationUtils.requireNonEmpty(originatorConversationID, "OriginatorConversationID");
            this.originatorConversationID = originatorConversationID;
            return this;
        }

        public Builder initiator(String initiator) {
            ValidationUtils.requireNonEmpty(initiator, "Initiator");
            this.initiator = initiator;
            return this;
        }

        public Builder securityCredential(String securityCredential) {
            ValidationUtils.requireNonEmpty(securityCredential, "SecurityCredential");
            ValidationUtils.requireBase64Encoded(securityCredential, "SecurityCredential");
            this.securityCredential = securityCredential;
            return this;
        }

        public Builder transactionID(String transactionID) {
            ValidationUtils.requireNonEmpty(transactionID, "TransactionID");
            this.transactionID = transactionID;
            return this;
        }

        public Builder amount(String amount) {
            ValidationUtils.requireNonEmpty(amount, "Amount");
            this.amount = amount;
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

        public Builder receiverIdentifierType(String receiverIdentifierType) {
            ValidationUtils.requireNonEmpty(receiverIdentifierType, "ReceiverIdentifierType");
            this.receiverIdentifierType = receiverIdentifierType;
            return this;
        }

        public Builder receiverParty(String receiverParty) {
            ValidationUtils.requireNonEmpty(receiverParty, "ReceiverParty");
            this.receiverParty = receiverParty;
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
            this.remarks = remarks;
            return this;
        }

        public Builder occasion(String occasion) {
            this.occasion = occasion;
            return this;
        }

        public TransactionReversalRequest build() {
            validate();
            return new TransactionReversalRequest(this);
        }

        private void validate() {
            ValidationUtils.requireNonEmpty(originatorConversationID, "OriginatorConversationID");
            ValidationUtils.requireNonEmpty(initiator, "Initiator");
            ValidationUtils.requireNonEmpty(securityCredential, "SecurityCredential");
            ValidationUtils.requireNonEmpty(transactionID, "TransactionID");
            ValidationUtils.requireNonEmpty(amount, "Amount");
            ValidationUtils.requireNonEmpty(originalConversationID, "OriginalConversationID");
            ValidationUtils.requireNonEmpty(partyA, "PartyA");
            ValidationUtils.requireNonEmpty(receiverIdentifierType, "ReceiverIdentifierType");
            ValidationUtils.requireNonEmpty(receiverParty, "ReceiverParty");
            ValidationUtils.requireNonEmpty(remarks, "Remarks");
            ValidationUtils.requireNonEmpty(resultURL, "ResultURL");
            ValidationUtils.requireNonEmpty(queueTimeOutURL, "QueueTimeOutURL");
        }
    }

    @Override
    public String toString() {
        return "{" +
                "OriginatorConversationID='" + OriginatorConversationID + '\'' +
                ", Initiator='" + Initiator + '\'' +
                ", SecurityCredential='***HIDDEN***'" +
                ", CommandID='" + CommandID + '\'' +
                ", TransactionID='" + TransactionID + '\'' +
                ", Amount='" + Amount + '\'' +
                ", OriginalConversationID='" + OriginalConversationID + '\'' +
                ", PartyA='" + PartyA + '\'' +
                ", ReceiverIdentifierType='" + ReceiverIdentifierType + '\'' +
                ", ReceiverParty='" + ReceiverParty + '\'' +
                ", ResultURL='" + ResultURL + '\'' +
                ", QueueTimeOutURL='" + QueueTimeOutURL + '\'' +
                ", Remarks='" + Remarks + '\'' +
                ", Occasion='" + Occasion + '\'' +
                '}';
    }

}
