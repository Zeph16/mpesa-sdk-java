package dev.mpesa.sdk.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mpesa.sdk.auth.AuthService;
import dev.mpesa.sdk.config.MpesaConfig;
import dev.mpesa.sdk.exception.*;
import okhttp3.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Handles HTTP requests within the M-Pesa SDK.
 * This class is responsible for making authenticated HTTP requests, handling retries, and processing responses.
 * It handles serialization of request objects but does NOT deserialize response bodies.
 * It is **internal to the SDK** and should not be accessed by external users.
 */
public class RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final AuthService authService;
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final MpesaConfig config;

    /**
     * Creates a new {@code RequestHandler} with default OkHttpClient and ObjectMapper.
     *
     * @param authService the authentication service for retrieving access tokens
     * @param config      the configuration settings for timeouts and retries
     */
    public RequestHandler(AuthService authService, MpesaConfig config) {
        this(authService, config,
                new OkHttpClient.Builder()
                        .connectTimeout(config.getConnectTimeout(), TimeUnit.MILLISECONDS)
                        .readTimeout(config.getReadTimeout(), TimeUnit.MILLISECONDS)
                        .writeTimeout(config.getWriteTimeout(), TimeUnit.MILLISECONDS)
                        .build(),
                new ObjectMapper()
        );
    }

    /**
     * Creates a new {@code RequestHandler} with a custom OkHttpClient and ObjectMapper.
     * This constructor allows for dependency injection of these components.
     *
     * @param authService   the authentication service
     * @param config        the configuration settings
     * @param httpClient    a custom HTTP client
     * @param objectMapper  a custom JSON object mapper
     */
    public RequestHandler(AuthService authService, MpesaConfig config, OkHttpClient httpClient, ObjectMapper objectMapper) {
        this.authService = authService;
        this.config = config;
        this.objectMapper = objectMapper;
        this.httpClient = httpClient;
    }

    /**
     * Sends an authenticated GET request.
     *
     * @param url the endpoint URL
     * @return the response body as a string
     * @throws MpesaNetworkException if a network error occurs
     * @throws JsonProcessingException if JSON processing fails
     */
    public String get(String url) throws MpesaNetworkException, JsonProcessingException {
        logger.debug("Sending GET request to: {}", url);
        return execute(buildRequest(url, "GET", null));
    }

    /**
     * Sends an authenticated POST request.
     *
     * @param url         the endpoint URL
     * @param requestBody the request payload
     * @return the response body as a string
     * @throws MpesaNetworkException if a network error occurs
     * @throws JsonProcessingException if JSON processing fails
     */
    public String post(String url, Object requestBody) throws MpesaNetworkException, JsonProcessingException {
        logger.debug("Sending POST request to: {}, Body: {}", url, requestBody);
        return execute(buildRequest(url, "POST", requestBody));
    }

    /**
     * Sends an authenticated PUT request.
     *
     * @param url         the endpoint URL
     * @param requestBody the request payload
     * @return the response body as a string
     * @throws MpesaNetworkException if a network error occurs
     * @throws JsonProcessingException if JSON processing fails
     */
    public String put(String url, Object requestBody) throws MpesaNetworkException, JsonProcessingException {
        logger.debug("Sending PUT request to: {}, Body: {}", url, requestBody);
        return execute(buildRequest(url, "PUT", requestBody));
    }

    /**
     * Sends an authenticated DELETE request.
     *
     * @param url the endpoint URL
     * @return the response body as a string
     * @throws MpesaNetworkException if a network error occurs
     * @throws JsonProcessingException if JSON processing fails
     */
    public String delete(String url) throws MpesaNetworkException, JsonProcessingException {
        logger.debug("Sending DELETE request to: {}", url);
        return execute(buildRequest(url, "DELETE", null));
    }

    /**
     * Builds an HTTP request with authentication headers.
     *
     * @param url         the request URL
     * @param method      the HTTP method (GET, POST, etc.)
     * @param requestBody the request body (if applicable)
     * @return the constructed {@link Request} object
     * @throws JsonProcessingException if JSON serialization fails
     */
    private Request buildRequest(String url, String method, Object requestBody) throws JsonProcessingException {
        Request.Builder builder = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + authService.getAccessToken())
                .addHeader("Content-Type", "application/json");

        if ("POST".equals(method) || "PUT".equals(method)) {
            String json = objectMapper.writeValueAsString(requestBody);
            RequestBody body = RequestBody.create(json, MediaType.get("application/json"));
            builder.method(method, body);
        } else {
            builder.method(method, null);
        }

        return builder.build();
    }

    /**
     * Executes the HTTP request, handling retries and authentication failures.
     *
     * @param request the request to execute
     * @return the response body as a string
     * @throws MpesaNetworkException if all retries fail
     */
    private String execute(Request request) throws MpesaNetworkException {
        int attempt = 0;
        boolean initialAuthAttempt = true;

        while (attempt < config.getMaxRetries()) {
            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    logger.info("Request to {} successful with status code {}", request.url(), response.code());
                    return Objects.requireNonNull(response.body()).string();
                }

                if (response.code() == 401) {
                    logger.warn("Authentication failed for request to {}: 401 Unauthorized", request.url());
                    if (!initialAuthAttempt) {
                        throw new MpesaAuthenticationException(response.body() != null ? response.body().string() : "",
                                "Failed to authenticate despite having a valid token.");
                    }
                    initialAuthAttempt = false;
                    authService.refreshToken();
                    request = buildRequest(request.url().toString(), request.method(), null);
                    continue;
                }

                if (shouldRetry(response.code())) {
                    logger.warn("Request to {} failed with status code {}. Retrying...", request.url(), response.code());
                    attempt++;
                    sleepBeforeRetry(attempt);
                    continue;
                }

                String responseBody = response.body() != null ? response.body().string() : "";
                throw new MpesaHttpException(response.code(), responseBody, "HTTP error: " + response.code());
            } catch (IOException e) {
                logger.error("Network error during request to {}: {}", request.url(), e.getMessage());
                if (attempt < config.getMaxRetries() - 1) {
                    attempt++;
                    sleepBeforeRetry(attempt);
                    continue;
                }
                throw new MpesaNetworkException("Network error after retries: " + e.getMessage(), e);
            }
        }

        throw new MpesaNetworkException("Request failed after all retries.");
    }

    /**
     * Determines if the request should be retried based on the HTTP response code.
     *
     * @param responseCode the HTTP status code
     * @return {@code true} if the request should be retried; {@code false} otherwise
     */
    private boolean shouldRetry(int responseCode) {
        return responseCode == 500 || responseCode == 502 || responseCode == 503 || responseCode == 429;
    }

    /**
     * Sleeps for a calculated backoff time before retrying a failed request.
     *
     * @param attempt the current retry attempt (used for exponential backoff)
     */
    private void sleepBeforeRetry(int attempt) {
        try {
            long backoffTime = (long) (config.getRetryBackoffTime() * Math.pow(2, attempt)); // Exponential backoff
            logger.debug("Sleeping for {} ms before retrying", backoffTime);
            Thread.sleep(backoffTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
