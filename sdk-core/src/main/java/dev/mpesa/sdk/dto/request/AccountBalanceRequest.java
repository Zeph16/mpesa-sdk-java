package dev.mpesa.sdk.dto.request;

import dev.mpesa.sdk.util.ValidationUtils;

import java.util.UUID;

public class AccountBalanceRequest {
    private final String originatorConversationID;
    private final String initiator;
    private final String securityCredential;
    private final String commandID;
    private final String partyA;
    private final String identifierType;
    private final String remarks;
    private final String queueTimeoutURL;
    private final String resultURL;

    private AccountBalanceRequest(Builder builder) {
        this.originatorConversationID = builder.originatorConversationID != null ? builder.originatorConversationID : UUID.randomUUID().toString();
        this.initiator = builder.initiator;
        this.securityCredential = builder.securityCredential;
        this.commandID = builder.commandID;
        this.partyA = builder.partyA;
        this.identifierType = builder.identifierType;
        this.remarks = builder.remarks;
        this.queueTimeoutURL = builder.queueTimeoutURL;
        this.resultURL = builder.resultURL;
    }

    public static class Builder {
        private String originatorConversationID;
        private String initiator;
        private String securityCredential;
        private final String commandID = "AccountBalance";
        private String partyA;
        private String identifierType;
        private String remarks;
        private String queueTimeoutURL;
        private String resultURL;

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

        public Builder partyA(String partyA) {
            ValidationUtils.requireNonEmpty(partyA, "PartyA");
            ValidationUtils.requireValidShortCode(partyA, "PartyA");
            this.partyA = partyA;
            return this;
        }

        public Builder identifierType(String identifierType) {
            ValidationUtils.requireNonEmpty(identifierType, "IdentifierType");
            this.identifierType = identifierType;
            return this;
        }

        public Builder remarks(String remarks) {
            this.remarks = remarks;
            return this;
        }

        public Builder queueTimeoutURL(String queueTimeoutURL) {
            ValidationUtils.requireNonEmpty(queueTimeoutURL, "QueueTimeoutURL");
            ValidationUtils.requireValidURL(queueTimeoutURL, "QueueTimeoutURL");
            this.queueTimeoutURL = queueTimeoutURL;
            return this;
        }

        public Builder resultURL(String resultURL) {
            ValidationUtils.requireNonEmpty(resultURL, "ResultURL");
            ValidationUtils.requireValidURL(resultURL, "ResultURL");
            this.resultURL = resultURL;
            return this;
        }

        public AccountBalanceRequest build() {
            validateBeforeBuild();
            return new AccountBalanceRequest(this);
        }

        private void validateBeforeBuild() {
            ValidationUtils.requireNonEmpty(initiator, "Initiator");
            ValidationUtils.requireNonEmpty(securityCredential, "SecurityCredential");
            ValidationUtils.requireNonEmpty(partyA, "PartyA");
            ValidationUtils.requireNonEmpty(identifierType, "IdentifierType");
            ValidationUtils.requireNonEmpty(queueTimeoutURL, "QueueTimeoutURL");
            ValidationUtils.requireNonEmpty(resultURL, "ResultURL");
        }
    }

    public String getOriginatorConversationID() { return originatorConversationID; }
    public String getInitiator() { return initiator; }
    public String getSecurityCredential() { return securityCredential; }
    public String getCommandID() { return commandID; }
    public String getPartyA() { return partyA; }
    public String getIdentifierType() { return identifierType; }
    public String getRemarks() { return remarks; }
    public String getQueueTimeoutURL() { return queueTimeoutURL; }
    public String getResultURL() { return resultURL; }

    @Override
    public String toString() {
        return "{" +
                "originatorConversationID='" + originatorConversationID + '\'' +
                ", initiator='" + initiator + '\'' +
                ", securityCredential='***HIDDEN***'" +
                ", commandID='" + commandID + '\'' +
                ", partyA='" + partyA + '\'' +
                ", identifierType='" + identifierType + '\'' +
                ", remarks='" + remarks + '\'' +
                ", queueTimeoutURL='" + queueTimeoutURL + '\'' +
                ", resultURL='" + resultURL + '\'' +
                '}';
    }

}
