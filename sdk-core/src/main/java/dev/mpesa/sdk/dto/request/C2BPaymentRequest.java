package dev.mpesa.sdk.dto.request;

import dev.mpesa.sdk.dto.KeyValue;
import dev.mpesa.sdk.util.ValidationUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class C2BPaymentRequest {
    private final String RequestRefID;
    private final String CommandID;
    private final String Remark;
    private final String ChannelSessionID;
    private final String SourceSystem;
    private final String Timestamp;
    private final List<KeyValue> Parameters;
    private final List<KeyValue> ReferenceData;
    private final Initiator Initiator;
    private final Party PrimaryParty;
    private final Party ReceiverParty;

    private C2BPaymentRequest(Builder builder) {
        this.RequestRefID = builder.requestRefID;
        this.CommandID = "CustomerPayBillOnline";
        this.Remark = builder.remark;
        this.ChannelSessionID = builder.channelSessionID;
        this.SourceSystem = builder.sourceSystem;

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        this.Timestamp = now.format(formatter);

        this.Parameters = builder.parameters;
        this.ReferenceData = builder.referenceData;
        this.Initiator = builder.initiator;
        this.PrimaryParty = builder.primaryParty;
        this.ReceiverParty = builder.receiverParty;
    }

    public static class Builder {
        private final String requestRefID = UUID.randomUUID().toString();
        private String remark;
        private String channelSessionID;
        private String sourceSystem;
        private final List<KeyValue> parameters = new ArrayList<>();
        private final List<KeyValue> referenceData = new ArrayList<>();
        private Initiator initiator;
        private Party primaryParty;
        private Party receiverParty;

        public Builder remark(String remark) {
            ValidationUtils.requireNonEmpty(remark, "Remark");
            this.remark = remark;
            return this;
        }

        public Builder channelSessionID(String channelSessionID) {
            ValidationUtils.requireNonEmpty(channelSessionID, "ChannelSessionID");
            this.channelSessionID = channelSessionID;
            return this;
        }

        public Builder sourceSystem(String sourceSystem) {
            ValidationUtils.requireNonEmpty(sourceSystem, "SourceSystem");
            this.sourceSystem = sourceSystem;
            return this;
        }

        public Builder addParameter(String key, String value) {
            ValidationUtils.requireNonEmpty(key, "Parameter Key");
            ValidationUtils.requireNonEmpty(value, "Parameter Value");
            this.parameters.add(new KeyValue(key, value));
            return this;
        }

        public Builder addReferenceData(String key, String value) {
            ValidationUtils.requireNonEmpty(key, "ReferenceData Key");
            ValidationUtils.requireNonEmpty(value, "ReferenceData Value");
            this.referenceData.add(new KeyValue(key, value));
            return this;
        }

        public Builder initiator(String identifier, String securityCredential, String secretKey) {
            ValidationUtils.requireNonEmpty(identifier, "Initiator Identifier");
            ValidationUtils.requireNonEmpty(securityCredential, "Security Credential");
            ValidationUtils.requireNonEmpty(secretKey, "Secret Key");

            this.initiator = new Initiator(1, identifier, securityCredential, secretKey);
            return this;
        }

        public Builder primaryParty(String identifier) {
            ValidationUtils.requireNonEmpty(identifier, "PrimaryParty Identifier");
            ValidationUtils.requireValidPhoneNumber(identifier, "PrimaryParty Identifier");
            this.primaryParty = new Party(1, identifier, null);
            return this;
        }

        public Builder receiverParty(String identifier, String shortCode) {
            ValidationUtils.requireNonEmpty(identifier, "ReceiverParty Identifier");
            ValidationUtils.requireValidShortCode(identifier, "ReceiverParty Identifier");
            ValidationUtils.requireValidShortCode(shortCode, "ReceiverParty ShortCode");

            this.receiverParty = new Party(4, identifier, shortCode);
            return this;
        }

        public C2BPaymentRequest build() {
            validateBeforeBuild();
            return new C2BPaymentRequest(this);
        }

        public void validateBeforeBuild() {
            ValidationUtils.requireNonEmpty(remark, "Remark");
            ValidationUtils.requireNonEmpty(channelSessionID, "ChannelSessionID");
            ValidationUtils.requireNonEmpty(sourceSystem, "SourceSystem");

            if (parameters.isEmpty()) {
                throw new IllegalArgumentException("At least one parameter (Amount, AccountReference, etc.) is required.");
            }

            if (initiator == null) {
                throw new IllegalArgumentException("Initiator details must be provided.");
            }

            if (primaryParty == null) {
                throw new IllegalArgumentException("Primary party details must be provided.");
            }

            if (receiverParty == null) {
                throw new IllegalArgumentException("Receiver party details must be provided.");
            }
        }
    }

    public static class Initiator {
        private final int IdentifierType;
        private final String Identifier;
        private final String SecurityCredential;
        private final String SecretKey;

        public Initiator(int identifierType, String identifier, String securityCredential, String secretKey) {
            this.IdentifierType = identifierType;
            this.Identifier = identifier;
            this.SecurityCredential = securityCredential;
            this.SecretKey = secretKey;
        }

        public int getIdentifierType() { return IdentifierType; }
        public String getIdentifier() { return Identifier; }
        public String getSecurityCredential() { return SecurityCredential; }
        public String getSecretKey() { return SecretKey; }
    }

    public static class Party {
        private final int IdentifierType;
        private final String Identifier;
        private final String ShortCode;

        public Party(int identifierType, String identifier, String shortCode) {
            this.IdentifierType = identifierType;
            this.Identifier = identifier;
            this.ShortCode = shortCode;
        }

        public int getIdentifierType() { return IdentifierType; }
        public String getIdentifier() { return Identifier; }
        public String getShortCode() { return ShortCode; }
    }

    public String getRequestRefID() { return RequestRefID; }
    public String getCommandID() { return CommandID; }
    public String getRemark() { return Remark; }
    public String getChannelSessionID() { return ChannelSessionID; }
    public String getSourceSystem() { return SourceSystem; }
    public String getTimestamp() { return Timestamp; }
    public List<KeyValue> getParameters() { return Parameters; }
    public List<KeyValue> getReferenceData() { return ReferenceData; }
    public Initiator getInitiator() { return Initiator; }
    public Party getPrimaryParty() { return PrimaryParty; }
    public Party getReceiverParty() { return ReceiverParty; }

    @Override
    public String toString() {
        return "{" +
                "RequestRefID='" + RequestRefID + '\'' +
                ", CommandID='" + CommandID + '\'' +
                ", Remark='" + Remark + '\'' +
                ", ChannelSessionID='" + ChannelSessionID + '\'' +
                ", SourceSystem='" + SourceSystem + '\'' +
                ", Timestamp='" + Timestamp + '\'' +
                ", Parameters=" + Parameters +
                ", ReferenceData=" + ReferenceData +
                ", Initiator=" + Initiator +
                ", PrimaryParty=" + PrimaryParty +
                ", ReceiverParty=" + ReceiverParty +
                '}';
    }

}
