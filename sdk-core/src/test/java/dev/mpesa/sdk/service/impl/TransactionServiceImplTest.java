package dev.mpesa.sdk.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mpesa.sdk.config.MpesaConfig;
import dev.mpesa.sdk.dto.request.TransactionReversalRequest;
import dev.mpesa.sdk.dto.request.TransactionStatusRequest;
import dev.mpesa.sdk.dto.response.TransactionReversalResponse;
import dev.mpesa.sdk.dto.response.TransactionStatusResponse;
import dev.mpesa.sdk.exception.MpesaHttpException;
import dev.mpesa.sdk.exception.MpesaUnexpectedResponseException;
import dev.mpesa.sdk.http.RequestHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static dev.mpesa.sdk.exception.MpesaErrorCode.INVALID_RESPONSE;
import static dev.mpesa.sdk.exception.MpesaErrorCode.UNKNOWN_ERROR;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceImplTest {
    @Mock
    private RequestHandler mockRequestHandler;

    @Mock
    private MpesaConfig mockConfig;

    private ObjectMapper objectMapper;
    private TransactionServiceImpl transactionService;
    private TransactionStatusRequest transactionStatusRequest;
    private TransactionReversalRequest transactionReversalRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        when(mockConfig.getTransactionStatusUrl()).thenReturn("sample-url");
        when(mockConfig.getTransactionReversalUrl()).thenReturn("sample-url");
        transactionStatusRequest = new TransactionStatusRequest.Builder()
                .initiator("testInitiator")
                .securityCredential("testSecurity")
                .transactionID("TX123456")
                .partyA("600000")
                .identifierType("4")
                .queueTimeOutURL("https://timeout.url")
                .resultURL("https://result.url")
                .remarks("Test transaction status")
                .occasion("Test Occasion")
                .build();
        transactionReversalRequest = new TransactionReversalRequest.Builder()
                .originatorConversationID("123456")
                .initiator("initiator")
                .securityCredential("secureKey==")
                .transactionID("TX123")
                .amount("100")
                .originalConversationID("OC123456")
                .partyA("600000")
                .receiverIdentifierType("4")
                .receiverParty("251700000000")
                .queueTimeOutURL("https://timeout.url")
                .resultURL("https://result.url")
                .remarks("Reversal Request")
                .occasion("Testing")
                .build();

        transactionService = new TransactionServiceImpl(mockRequestHandler, mockConfig);
    }

    @Test
    void checkTransactionStatus_SuccessfulResponse() throws Exception {
        TransactionStatusResponse mockResponse = new TransactionStatusResponse("123", "456", "0", "Success");
        String jsonResponse = objectMapper.writeValueAsString(mockResponse);

        when(mockRequestHandler.post(anyString(), eq(transactionStatusRequest))).thenReturn(jsonResponse);

        TransactionStatusResponse response = transactionService.checkTransactionStatus(transactionStatusRequest);

        assertNotNull(response);
        assertEquals("0", response.getResponseCode());
        assertTrue(response.isSuccessful());

        verify(mockRequestHandler).post(anyString(), eq(transactionStatusRequest));
    }

    @Test
    void checkTransactionStatus_FailedResponse() throws Exception {
        TransactionStatusResponse mockResponse = new TransactionStatusResponse("123", "456", "1", "Failed");
        String jsonResponse = objectMapper.writeValueAsString(mockResponse);

        when(mockRequestHandler.post(anyString(), eq(transactionStatusRequest))).thenReturn(jsonResponse);

        TransactionStatusResponse response = transactionService.checkTransactionStatus(transactionStatusRequest);

        assertNotNull(response);
        assertEquals("1", response.getResponseCode());
        assertFalse(response.isSuccessful());

        verify(mockRequestHandler).post(anyString(), eq(transactionStatusRequest));
    }

    @Test
    void checkTransactionStatus_InvalidJsonResponse() throws JsonProcessingException {
        String invalidJson = "{invalid json}";

        when(mockRequestHandler.post(anyString(), eq(transactionStatusRequest))).thenReturn(invalidJson);

        MpesaUnexpectedResponseException ex = assertThrows(MpesaUnexpectedResponseException.class, () ->
                transactionService.checkTransactionStatus(transactionStatusRequest));

        assertEquals("Failed to parse Transaction Status response.", ex.getMessage());
        assertEquals(INVALID_RESPONSE, ex.errorCode());

        verify(mockRequestHandler).post(anyString(), eq(transactionStatusRequest));
    }

    @Test
    void checkTransactionStatus_HttpError() throws JsonProcessingException {
        String errorResponse = "{\"errorCode\":\"500\", \"errorMessage\":\"Internal Server Error\"}";
        MpesaHttpException httpException = new MpesaHttpException(500, errorResponse, "Internal Server Error");

        when(mockRequestHandler.post(anyString(), eq(transactionStatusRequest))).thenThrow(httpException);

        MpesaUnexpectedResponseException ex = assertThrows(MpesaUnexpectedResponseException.class, () ->
                transactionService.checkTransactionStatus(transactionStatusRequest));

        assertEquals("Unexpected error when checking Transaction Status.", ex.getMessage());
        assertEquals(UNKNOWN_ERROR, ex.errorCode());
        assertEquals(errorResponse, ex.responseBody());

        verify(mockRequestHandler).post(anyString(), eq(transactionStatusRequest));
    }

    @Test
    void reverseTransaction_Success() throws Exception {
        TransactionReversalResponse expectedResponse = new TransactionReversalResponse(
                "123456", "654321", "0", "Reversal request accepted"
        );

        String responseJson = objectMapper.writeValueAsString(expectedResponse);

        when(mockRequestHandler.post(anyString(), eq(transactionReversalRequest))).thenReturn(responseJson);

        TransactionReversalResponse actualResponse = transactionService.reverseTransaction(transactionReversalRequest);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getOriginatorConversationID(), actualResponse.getOriginatorConversationID());
        assertEquals(expectedResponse.getConversationID(), actualResponse.getConversationID());
        assertEquals(expectedResponse.getResponseCode(), actualResponse.getResponseCode());
        assertEquals(expectedResponse.getResponseDescription(), actualResponse.getResponseDescription());
        assertTrue(actualResponse.isSuccessful());
    }

    @Test
    void reverseTransaction_HandlesMpesaHttpException() throws JsonProcessingException {
        String errorResponse = "{\"errorCode\":\"500.001.1001\", \"errorMessage\":\"Invalid request\"}";

        when(mockRequestHandler.post(anyString(), eq(transactionReversalRequest)))
                .thenThrow(new MpesaHttpException(500, errorResponse, "server error"));

        MpesaUnexpectedResponseException exception = assertThrows(
                MpesaUnexpectedResponseException.class,
                () -> transactionService.reverseTransaction(transactionReversalRequest)
        );

        assertEquals("Unexpected error when reversing transaction.", exception.getMessage());
        assertEquals(errorResponse, exception.responseBody());
        assertEquals(UNKNOWN_ERROR, exception.errorCode());
    }

    @Test
    void reverseTransaction_HandlesJsonProcessingException() throws JsonProcessingException {
        when(mockRequestHandler.post(anyString(), eq(transactionReversalRequest)))
                .thenReturn("invalid response");

        MpesaUnexpectedResponseException exception = assertThrows(
                MpesaUnexpectedResponseException.class,
                () -> transactionService.reverseTransaction(transactionReversalRequest)
        );

        assertEquals("Failed to parse Transaction Reversal response.", exception.getMessage());
        assertEquals("invalid response", exception.responseBody());
        assertEquals(INVALID_RESPONSE, exception.errorCode());
    }
}
