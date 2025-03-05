package dev.mpesa.sdk.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mpesa.sdk.auth.AuthService;
import dev.mpesa.sdk.config.MpesaConfig;
import dev.mpesa.sdk.exception.MpesaHttpException;
import dev.mpesa.sdk.exception.MpesaNetworkException;
import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RequestHandlerTest {

    private RequestHandler requestHandler;

    @Mock private AuthService mockAuthService;
    @Mock private OkHttpClient mockHttpClient;
    @Mock private Call mockCall;
    @Mock private Response mockResponse;
    @Mock private ResponseBody mockResponseBody;
    @Mock private ObjectMapper mockObjectMapper;

    private MpesaConfig config;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        config = new MpesaConfig.Builder()
                .maxRetries(3)
                .retryBackoffTime(500)
                .build();

        requestHandler = new RequestHandler(mockAuthService, config, mockHttpClient, mockObjectMapper);
    }

    @Test
    void get_SuccessfulResponse_ReturnsBody() throws IOException {
        String expectedResponse = "{\"status\": \"success\"}";
        when(mockResponseBody.string()).thenReturn(expectedResponse);

        when(mockResponse.isSuccessful()).thenReturn(true);
        when(mockResponse.body()).thenReturn(mockResponseBody);
        when(mockCall.execute()).thenReturn(mockResponse);

        when(mockHttpClient.newCall(any(Request.class))).thenReturn(mockCall);

        String result = requestHandler.get("https://example.com/api");

        assertEquals(expectedResponse, result);
        verify(mockHttpClient, times(1)).newCall(any(Request.class));
    }

    @Test
    void post_SuccessfulResponse_ReturnsBody() throws IOException {
        String expectedResponse = "{\"status\": \"created\"}";
        when(mockResponseBody.string()).thenReturn(expectedResponse);
        when(mockResponse.isSuccessful()).thenReturn(true);
        when(mockResponse.body()).thenReturn(mockResponseBody);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockHttpClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockObjectMapper.writeValueAsString(any())).thenReturn(expectedResponse);

        String result = requestHandler.post("https://example.com/api", new Object());

        assertEquals(expectedResponse, result);
    }

    @Test
    void request_AuthenticationFailure_RefreshesTokenAndRetries() throws IOException {
        when(mockAuthService.getAccessToken()).thenReturn("expired-token").thenReturn("new-token");
        when(mockResponse.code()).thenReturn(401);
        when(mockResponse.body()).thenReturn(mockResponseBody);
        when(mockResponseBody.string()).thenReturn("{\"status\": \"success\"}");
        when(mockResponse.message()).thenReturn("Unauthorized");
        when(mockResponse.isSuccessful()).thenReturn(false).thenReturn(true);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockHttpClient.newCall(any(Request.class))).thenReturn(mockCall);

        doNothing().when(mockAuthService).refreshToken();

        requestHandler.get("https://example.com/api");

        verify(mockAuthService, times(1)).refreshToken();
    }

    @Test
    void request_ServerErrorRetries_ThenFails() throws IOException {
        when(mockResponse.code()).thenReturn(500);
        when(mockResponse.message()).thenReturn("Internal Server Error");
        when(mockResponse.isSuccessful()).thenReturn(false);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockHttpClient.newCall(any(Request.class))).thenReturn(mockCall);

        MpesaNetworkException exception = assertThrows(
                MpesaNetworkException.class,
                () -> requestHandler.get("https://example.com/api")
        );

        assertTrue(exception.getMessage().contains("Request failed after all retries."));
        verify(mockHttpClient, times(config.getMaxRetries())).newCall(any(Request.class));
    }

    @Test
    void request_HttpResponse_ThrowsException() throws IOException {
        when(mockResponse.code()).thenReturn(400);
        when(mockResponse.message()).thenReturn("Bad Request");
        when(mockResponse.isSuccessful()).thenReturn(false);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockHttpClient.newCall(any(Request.class))).thenReturn(mockCall);

        assertThrows(MpesaHttpException.class, () -> requestHandler.get("https://example.com/api"));
    }

    @Test
    void request_NetworkFailure_ThrowsExceptionAfterRetries() throws IOException {
        when(mockCall.execute()).thenThrow(new IOException("Network failure"));
        when(mockHttpClient.newCall(any(Request.class))).thenReturn(mockCall);

        MpesaNetworkException exception = assertThrows(
                MpesaNetworkException.class,
                () -> requestHandler.get("https://example.com/api")
        );

        assertTrue(exception.getMessage().contains("Network error after retries"));
    }
}
