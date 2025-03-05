package dev.mpesa.sdk.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mpesa.sdk.config.MpesaConfig;
import dev.mpesa.sdk.exception.MpesaAuthenticationException;
import dev.mpesa.sdk.exception.MpesaNetworkException;
import dev.mpesa.sdk.exception.MpesaUnexpectedResponseException;
import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private AuthService authService;

    @Mock
    private OkHttpClient mockHttpClient;

    @Mock
    private Call mockCall;

    @Mock
    private Response mockResponse;

    @Mock
    private ResponseBody mockResponseBody;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        MpesaConfig config = new MpesaConfig.Builder()
                .authUrl("https://api.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials")
                .build();
        authService = new AuthService("valid-key", "valid-secret", config, mockHttpClient);
    }

    @Test
    void refreshToken_SuccessfulResponse_SetsAccessToken() throws IOException {
        String tokenJson = "{\"access_token\": \"test-token\", \"token_type\": \"Bearer\", \"expires_in\": 3600}";
        when(mockResponseBody.string()).thenReturn(tokenJson);

        when(mockResponse.isSuccessful()).thenReturn(true);
        when(mockResponse.body()).thenReturn(mockResponseBody);
        when(mockCall.execute()).thenReturn(mockResponse);

        when(mockHttpClient.newCall(any(Request.class))).thenReturn(mockCall);

        authService.refreshToken();

        assertEquals("test-token", authService.getAccessToken());
    }

    @Test
    void refreshToken_ExpiredToken_RefreshesToken() throws IOException {
        String firstTokenJson = "{\"access_token\": \"old-token\", \"token_type\": \"Bearer\", \"expires_in\": 1}";
        when(mockResponseBody.string()).thenReturn(firstTokenJson);
        when(mockResponse.isSuccessful()).thenReturn(true);
        when(mockResponse.body()).thenReturn(mockResponseBody);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockHttpClient.newCall(any(Request.class))).thenReturn(mockCall);

        authService.refreshToken();

        authService.setTokenExpiryTime(System.currentTimeMillis() - 1000);

        String newTokenJson = "{\"access_token\": \"new-token\", \"token_type\": \"Bearer\", \"expires_in\": 3600}";
        when(mockResponseBody.string()).thenReturn(newTokenJson);
        when(mockResponse.isSuccessful()).thenReturn(true);
        when(mockResponse.body()).thenReturn(mockResponseBody);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockHttpClient.newCall(any(Request.class))).thenReturn(mockCall);

        String token = authService.getAccessToken();

        assertEquals("new-token", token);
    }

    @Test
    void refreshToken_InvalidCredentials_ThrowsAuthenticationException() throws IOException {
        when(mockResponse.code()).thenReturn(401);
        when(mockResponse.message()).thenReturn("Unauthorized");
        when(mockResponse.isSuccessful()).thenReturn(false);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockHttpClient.newCall(any(Request.class))).thenReturn(mockCall);

        MpesaAuthenticationException exception = assertThrows(
                MpesaAuthenticationException.class,
                () -> authService.refreshToken()
        );

        assertTrue(exception.getMessage().contains("Invalid API credentials"));
    }

    @Test
    void refreshToken_NetworkFailure_ThrowsNetworkException() throws IOException {
        when(mockCall.execute()).thenThrow(new IOException("Network error"));
        when(mockHttpClient.newCall(any(Request.class))).thenReturn(mockCall);

        MpesaNetworkException exception = assertThrows(
                MpesaNetworkException.class,
                () -> authService.refreshToken()
        );

        assertTrue(exception.getMessage().contains("Failed to authenticate with M-Pesa"));
    }

    @Test
    void refreshToken_MalformedResponse_ThrowsUnexpectedResponseException() throws IOException {
        String invalidJson = "{\"invalid_field\": \"unexpected_data\"}";
        when(mockResponseBody.string()).thenReturn(invalidJson);
        when(mockResponse.isSuccessful()).thenReturn(true);
        when(mockResponse.body()).thenReturn(mockResponseBody);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockHttpClient.newCall(any(Request.class))).thenReturn(mockCall);

        MpesaUnexpectedResponseException exception = assertThrows(
                MpesaUnexpectedResponseException.class,
                () -> authService.refreshToken()
        );

        assertTrue(exception.getMessage().contains("can't cast to TokenResponse"));
    }
}
