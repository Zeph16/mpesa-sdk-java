package dev.mpesa.sdk.dto.request;
import dev.mpesa.sdk.util.ValidationUtils;

import java.io.Serializable;

public class C2BRegisterRequest implements Serializable {
    private final String ShortCode;
    private final String ResponseType;
    private final String CommandID;
    private final String ConfirmationURL;
    private final String ValidationURL;

    private C2BRegisterRequest(Builder builder) {
        this.ShortCode = builder.shortCode;
        this.ResponseType = builder.responseType;
        this.CommandID = "RegisterURL";
        this.ConfirmationURL = builder.confirmationURL;
        this.ValidationURL = builder.validationURL;
    }

    public String getShortCode() { return ShortCode; }
    public String getResponseType() { return ResponseType; }
    public String getCommandID() { return CommandID; }
    public String getConfirmationURL() { return ConfirmationURL; }
    public String getValidationURL() { return ValidationURL; }

    public static class Builder {
        private String responseType;
        private String shortCode;
        private String confirmationURL;
        private String validationURL;

        public Builder shortCode(String shortCode) {
            ValidationUtils.requireNonEmpty(shortCode, "ShortCode");
            ValidationUtils.requireValidShortCode(shortCode, "ShortCode");
            this.shortCode = shortCode;
            return this;
        }

        public Builder responseType(String responseType) {
            ValidationUtils.requireNonEmpty(responseType, "ResponseType");
            if (!responseType.equals("Completed") && !responseType.equals("Cancelled")) {
                throw new IllegalArgumentException("ResponseType must be 'Completed' or 'Cancelled'.");
            }
            this.responseType = responseType;
            return this;
        }

        public Builder confirmationURL(String confirmationURL) {
            ValidationUtils.requireNonEmpty(confirmationURL, "ConfirmationURL");
            ValidationUtils.requireValidURL(confirmationURL, "ConfirmationURL");
            this.confirmationURL = confirmationURL;
            return this;
        }

        public Builder validationURL(String validationURL) {
            ValidationUtils.requireNonEmpty(validationURL, "ValidationURL");
            ValidationUtils.requireValidURL(validationURL, "ValidationURL");
            this.validationURL = validationURL;
            return this;
        }

        public C2BRegisterRequest build() {
            validateBeforeBuild();
            return new C2BRegisterRequest(this);
        }

        public void validateBeforeBuild() {
            ValidationUtils.requireNonEmpty(shortCode, "ShortCode");
            ValidationUtils.requireNonEmpty(responseType, "ResponseType");
            ValidationUtils.requireNonEmpty(confirmationURL, "ConfirmationURL");
            ValidationUtils.requireNonEmpty(validationURL, "ValidationURL");
        }
    }

    @Override
    public String toString() {
        return "{" +
                "ShortCode='" + ShortCode + '\'' +
                ", ResponseType='" + ResponseType + '\'' +
                ", CommandID='" + CommandID + '\'' +
                ", ConfirmationURL='" + ConfirmationURL + '\'' +
                ", ValidationURL='" + ValidationURL + '\'' +
                '}';
    }

}
