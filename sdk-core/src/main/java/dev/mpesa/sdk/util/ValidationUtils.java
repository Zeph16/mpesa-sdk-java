package dev.mpesa.sdk.util;

import java.util.regex.Pattern;
import java.net.MalformedURLException;
import java.net.URL;

public class ValidationUtils {
    private static final Pattern NUMERIC_PATTERN = Pattern.compile("^\\d+$");
    private static final Pattern BASE64_PATTERN = Pattern.compile("^[A-Za-z0-9+/=]+$");
    private static final Pattern BUSINESS_SHORTCODE_PATTERN = Pattern.compile("\\d{4,9}");
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^(2517\\d{8}|2547\\d{8})$");


    public static void requireNonEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must not be empty.");
        }
    }

    public static void requireNumeric(String value, String fieldName) {
        if (!NUMERIC_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException(fieldName + " must be numeric.");
        }
    }

    public static void requireBase64Encoded(String value, String fieldName) {
        if (value == null || !BASE64_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException(fieldName + " must be a valid base64 encoded string.");
        }
    }

    public static void requireLength(String value, int minLength, int maxLength, String fieldName) {
        int length = value.length();
        if (length < minLength || length > maxLength) {
            throw new IllegalArgumentException(fieldName + " must be between " + minLength + " and " + maxLength + " characters long.");
        }
    }

    public static void requireValidURL(String value, String fieldName) {
        try {
            new URL(value);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(fieldName + " must be a valid URL.");
        }
    }

    public static void requireValidPhoneNumber(String phoneNumber, String fieldName) {
        if (!PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches()) {
            throw new IllegalArgumentException(fieldName + " must be a valid Ethiopian (2517xxxxxxxx) or Kenyan (2547xxxxxxxx) number.");
        }
    }

    public static void requireValidShortCode(String shortCode, String fieldName) {
        if (!BUSINESS_SHORTCODE_PATTERN.matcher(shortCode).matches()) {
            throw new IllegalArgumentException(fieldName + " must be a valid business shortcode (5-7 digits).");
        }
    }
}