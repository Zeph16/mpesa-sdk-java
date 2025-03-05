package dev.mpesa.sdk.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mpesa.sdk.config.MpesaConfig;
import dev.mpesa.sdk.dto.request.AccountBalanceRequest;
import dev.mpesa.sdk.dto.response.AccountBalanceResponse;
import dev.mpesa.sdk.exception.MpesaHttpException;
import dev.mpesa.sdk.exception.MpesaUnexpectedResponseException;
import dev.mpesa.sdk.http.RequestHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static dev.mpesa.sdk.exception.MpesaErrorCode.INVALID_RESPONSE;
import static dev.mpesa.sdk.exception.MpesaErrorCode.UNKNOWN_ERROR;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AccountServiceImplTest {

    private RequestHandler mockRequestHandler;
    private MpesaConfig mockConfig;
    private ObjectMapper mockObjectMapper;
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        mockRequestHandler = mock(RequestHandler.class);
        mockConfig = mock(MpesaConfig.class);
        when(mockConfig.getAccountBalanceUrl()).thenReturn("sample-url");
        mockObjectMapper = mock(ObjectMapper.class);

        accountService = new AccountServiceImpl(mockRequestHandler, mockConfig, mockObjectMapper);
    }

    @Test
    void testCheckAccountBalance_successfulResponse() throws Exception {
        AccountBalanceRequest request = new AccountBalanceRequest.Builder()
                .originatorConversationID("12345")
                .initiator("TestInitiator")
                .securityCredential("Base64EncodedSecurityCredential")
                .partyA("123456")
                .identifierType("MSISDN")
                .queueTimeoutURL("https://timeout.url")
                .resultURL("https://result.url")
                .build();

        String jsonResponse = "{ \"originatorConversationID\": \"12345\", \"ConversationID\": \"54321\", \"ResponseCode\": \"0\", \"ResponseDescription\": \"Success\" }";

        AccountBalanceResponse expectedResponse = new AccountBalanceResponse("12345", "54321", "0", "Success");
        when(mockRequestHandler.post(anyString(), any())).thenReturn(jsonResponse);
        when(mockObjectMapper.readValue(jsonResponse, AccountBalanceResponse.class)).thenReturn(expectedResponse);

        AccountBalanceResponse actualResponse = accountService.checkAccountBalance(request);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testCheckAccountBalance_requestHandlerThrowsMpesaHttpException() throws Exception {
        AccountBalanceRequest request = new AccountBalanceRequest.Builder()
                .originatorConversationID("12345")
                .initiator("TestInitiator")
                .securityCredential("Base64EncodedSecurityCredential")
                .partyA("123456")
                .identifierType("MSISDN")
                .queueTimeoutURL("https://timeout.url")
                .resultURL("https://result.url")
                .build();

        when(mockRequestHandler.post(anyString(), eq(request)))
                .thenThrow(new MpesaHttpException(404, "response-body", "Error response"));

        MpesaUnexpectedResponseException thrown = assertThrows(MpesaUnexpectedResponseException.class, () -> {
            accountService.checkAccountBalance(request);
        });

        assertEquals("Unexpected error when checking account balance.", thrown.getMessage());
        assertEquals("response-body", thrown.responseBody());
        assertNull(thrown.errorResponse());
        assertEquals(UNKNOWN_ERROR, thrown.errorCode());
    }

    @Test
    void testCheckAccountBalance_jsonProcessingException() throws Exception {
        AccountBalanceRequest request = new AccountBalanceRequest.Builder()
                .originatorConversationID("12345")
                .initiator("TestInitiator")
                .securityCredential("Base64EncodedSecurityCredential")
                .partyA("123456")
                .identifierType("MSISDN")
                .queueTimeoutURL("https://timeout.url")
                .resultURL("https://result.url")
                .build();

        String invalidJsonResponse = "invalid json";
        AccountServiceImpl accountService1 = new AccountServiceImpl(mockRequestHandler, mockConfig);

        when(mockRequestHandler.post(anyString(), eq(request)))
                .thenReturn(invalidJsonResponse);

        MpesaUnexpectedResponseException thrown = assertThrows(MpesaUnexpectedResponseException.class, () -> {
            accountService1.checkAccountBalance(request);
        });

        assertEquals("Failed to parse Account Balance response.", thrown.getMessage());
        assertEquals(INVALID_RESPONSE, thrown.errorCode());
        assertEquals("invalid json", thrown.responseBody());
        assertNull(thrown.errorResponse());
    }

    @Test
    void testCheckAccountBalance_isSuccessful() throws Exception {
        AccountBalanceRequest request = new AccountBalanceRequest.Builder()
                .originatorConversationID("12345")
                .initiator("TestInitiator")
                .securityCredential("Base64EncodedSecurityCredential")
                .partyA("123456")
                .identifierType("MSISDN")
                .queueTimeoutURL("https://timeout.url")
                .resultURL("https://result.url")
                .build();
        AccountServiceImpl accountService1 = new AccountServiceImpl(mockRequestHandler, mockConfig);

        String jsonResponse = "{ \"OriginatorConversationID\": \"12345\", \"ConversationID\": \"54321\", \"ResponseCode\": \"0\", \"ResponseDescription\": \"Success\" }";

        when(mockRequestHandler.post(anyString(), eq(request))).thenReturn(jsonResponse);

        AccountBalanceResponse actualResponse = accountService1.checkAccountBalance(request);

        assertTrue(actualResponse.isSuccessful());
    }

    @Test
    void testCheckAccountBalance_isUnsuccessful() throws Exception {
        AccountBalanceRequest request = new AccountBalanceRequest.Builder()
                .originatorConversationID("12345")
                .initiator("TestInitiator")
                .securityCredential("Base64EncodedSecurityCredential")
                .partyA("123456")
                .identifierType("MSISDN")
                .queueTimeoutURL("https://timeout.url")
                .resultURL("https://result.url")
                .build();
        AccountServiceImpl accountService1 = new AccountServiceImpl(mockRequestHandler, mockConfig);

        String jsonResponse = "{ \"OriginatorConversationID\": \"12345\", \"ConversationID\": \"54321\", \"ResponseCode\": \"1\", \"ResponseDescription\": \"Success\" }";

        when(mockRequestHandler.post(anyString(), eq(request))).thenReturn(jsonResponse);

        AccountBalanceResponse actualResponse = accountService1.checkAccountBalance(request);

        assertFalse(actualResponse.isSuccessful());
    }
}
