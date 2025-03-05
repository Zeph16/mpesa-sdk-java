package dev.mpesa.sdk.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mpesa.sdk.config.MpesaConfig;
import dev.mpesa.sdk.exception.MpesaAuthenticationException;
import dev.mpesa.sdk.exception.MpesaErrorCode;
import dev.mpesa.sdk.exception.MpesaNetworkException;
import dev.mpesa.sdk.exception.MpesaUnexpectedResponseException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Base64;

/**
 * Handles authentication with the M-Pesa API.
 * <p>
 * This service is responsible for generating and refreshing access tokens
 * required for making API requests.
 */
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final String consumerKey;
    private final String consumerSecret;
    private final MpesaConfig config;
    private final ObjectMapper objectMapper;
    private final OkHttpClient client;
    private String accessToken;
    private long tokenExpiryTime;

    /**
     * Creates a new instance of AuthService with a default OkHttpClient.
     *
     * @param consumerKey    The M-Pesa API consumer key.
     * @param consumerSecret The M-Pesa API consumer secret.
     * @param config         The SDK configuration settings.
     */
    public AuthService(String consumerKey, String consumerSecret, MpesaConfig config) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.config = config;
        this.objectMapper = new ObjectMapper();
        this.tokenExpiryTime = 0;
        this.client = new OkHttpClient();
    }

    /**
     * Creates a new instance of AuthService with a custom OkHttpClient.
     *
     * @param consumerKey    The M-Pesa API consumer key.
     * @param consumerSecret The M-Pesa API consumer secret.
     * @param config         The SDK configuration settings.
     * @param client         The HTTP client to use for API requests.
     */
    public AuthService(String consumerKey, String consumerSecret, MpesaConfig config, OkHttpClient client) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.config = config;
        this.objectMapper = new ObjectMapper();
        this.tokenExpiryTime = 0;
        this.client = client;
    }

    /**
     * Retrieves the current access token. If the token has expired, a new one is fetched automatically.
     *
     * @return The valid access token.
     */
    public String getAccessToken() {
        if (System.currentTimeMillis() >= tokenExpiryTime) {
            logger.debug("Access token has expired or is about to expire, refreshing...");
            refreshToken();
        }
        return accessToken;
    }

    /**
     * Refreshes the access token by making an authentication request to M-Pesa.
     *
     * @throws MpesaAuthenticationException       If the API credentials are invalid.
     * @throws MpesaUnexpectedResponseException   If the response format is unexpected.
     * @throws MpesaNetworkException              If a network error occurs.
     */
    public synchronized void refreshToken() {
        String basicAuth = Base64.getEncoder().encodeToString((consumerKey + ":" + consumerSecret).getBytes());
        Request request = new Request.Builder()
                .url(config.getAuthUrl())
                .addHeader("Authorization", "Basic " + basicAuth)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.code() == 401) {
                logger.error("Authentication failed with M-Pesa: 401 Unauthorized - Invalid API credentials");
                throw new MpesaAuthenticationException(response.body() != null ? response.body().string() : "",
                        "Invalid API credentials: " + response.code() + " - " + response.message());
            } else if (response.body() == null) {
                logger.error("Unexpected response from M-Pesa: Response body is null");
                throw new MpesaUnexpectedResponseException(MpesaErrorCode.INVALID_RESPONSE, null, "Response body is null");
            }

            String responseBody = response.body().string();
            try {
                TokenResponse tokenResponse = objectMapper.readValue(responseBody, TokenResponse.class);
                this.accessToken = tokenResponse.getAccessToken();
                this.tokenExpiryTime = System.currentTimeMillis() + (tokenResponse.getExpiresIn() * 1000);
                logger.info("Successfully refreshed access token. It will expire at {}", tokenExpiryTime);
            } catch (JsonProcessingException e) {
                logger.error("Failed to parse the authentication response: {}", responseBody, e);
                throw new MpesaUnexpectedResponseException(MpesaErrorCode.INVALID_RESPONSE, responseBody,
                        "Failed to authenticate with M-Pesa, can't cast to TokenResponse; Response: " + response, e);
            }
        } catch (IOException e) {
            logger.error("Failed to authenticate with M-Pesa", e);
            throw new MpesaNetworkException("Failed to authenticate with M-Pesa", e);
        }
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public MpesaConfig getConfig() {
        return config;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public long getTokenExpiryTime() {
        return tokenExpiryTime;
    }

    public void setTokenExpiryTime(long tokenExpiryTime) {
        this.tokenExpiryTime = tokenExpiryTime;
    }
}
