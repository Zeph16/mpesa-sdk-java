package dev.mpesa.sdk.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.mpesa.sdk.config.MpesaConfig;
import dev.mpesa.sdk.dto.request.C2BPaymentRequest;
import dev.mpesa.sdk.dto.request.C2BRegisterRequest;
import dev.mpesa.sdk.dto.request.C2BSimulatePaymentRequest;
import dev.mpesa.sdk.dto.response.C2BPaymentResponse;
import dev.mpesa.sdk.dto.response.C2BRegisterResponse;
import dev.mpesa.sdk.dto.response.C2BSimulatePaymentResponse;
import dev.mpesa.sdk.exception.MpesaErrorCode;
import dev.mpesa.sdk.exception.MpesaHttpException;
import dev.mpesa.sdk.exception.MpesaUnexpectedResponseException;
import dev.mpesa.sdk.http.RequestHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class C2BServiceImplTest {

    @Mock
    private RequestHandler mockRequestHandler;

    @Mock
    private MpesaConfig mockConfig;

    private C2BServiceImpl c2bService;

    private C2BRegisterRequest registerRequest;
    private C2BPaymentRequest paymentRequest;
    private C2BSimulatePaymentRequest simulateRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        registerRequest = new C2BRegisterRequest.Builder()
                .shortCode("123456")
                .responseType("Completed")
                .confirmationURL("https://confirmation.url")
                .validationURL("https://validation.url")
                .build();
        paymentRequest = new C2BPaymentRequest.Builder()
                .remark("Payment for service")
                .channelSessionID("session123")
                .sourceSystem("SystemA")
                .addParameter("Amount", "1000")
                .addParameter("AccountReference", "12345")
                .initiator("initiator", "secret", "key")
                .primaryParty("251700000000")
                .receiverParty("306030", "306030")
                .build();
        simulateRequest = new C2BSimulatePaymentRequest.Builder()
                .amount("1000")
                .msisdn("251700000000")
                .billRefNumber("12345")
                .shortCode("123456")
                .build();

        when(mockConfig.getC2bRegisterUrl()).thenReturn("sample-url");
        when(mockConfig.getC2bPaymentUrl()).thenReturn("sample-url");
        when(mockConfig.getC2bSimulatePaymentUrl()).thenReturn("sample-url");

        c2bService = new C2BServiceImpl(mockRequestHandler, mockConfig);
    }

    @Test
    void testRegisterC2B_success() throws JsonProcessingException {
        String responseJson = "{\"header\":{\"responseCode\":\"0\",\"responseMessage\":\"Success\",\"customerMessage\":\"Customer message\",\"timestamp\":\"2025-03-05T12:00:00\"}}";

        when(mockRequestHandler.post(anyString(), eq(registerRequest))).thenReturn(responseJson);

        C2BRegisterResponse response = c2bService.registerC2B(registerRequest, "api-key");

        assertNotNull(response);
        assertTrue(response.isSuccessful());
        assertEquals("Success", response.getHeader().getResponseMessage());
    }

    @Test
    void testRegisterC2B_httpException_invalidShortCode() throws JsonProcessingException {
        MpesaHttpException httpException = new MpesaHttpException(400, "Short Code already Registered", "some error");

        when(mockRequestHandler.post(anyString(), eq(registerRequest))).thenThrow(httpException);

        MpesaUnexpectedResponseException exception = assertThrows(MpesaUnexpectedResponseException.class, () -> {
            c2bService.registerC2B(registerRequest, "api-key");
        });

        assertEquals(MpesaErrorCode.SHORT_CODE_REGISTERED, exception.errorCode());
        assertTrue(exception.getMessage().contains("Short Code is already registered"));
    }

    @Test
    void testRegisterC2B_httpException_genericError() throws JsonProcessingException {
        MpesaHttpException httpException = new MpesaHttpException(500, "some-body", "Internal Server Error");

        when(mockRequestHandler.post(anyString(), eq(registerRequest))).thenThrow(httpException);

        MpesaUnexpectedResponseException exception = assertThrows(MpesaUnexpectedResponseException.class, () -> {
            c2bService.registerC2B(registerRequest, "api-key");
        });

        assertEquals(MpesaErrorCode.UNKNOWN_ERROR, exception.errorCode());
        assertEquals("some-body", exception.responseBody());
        assertTrue(exception.getMessage().contains("Unexpected error in Register C2B."));
    }

    @Test
    void testRegisterC2B_jsonProcessingException() throws JsonProcessingException {
        String invalidJson = "invalid json";

        when(mockRequestHandler.post(anyString(), eq(registerRequest))).thenReturn(invalidJson);

        MpesaUnexpectedResponseException exception = assertThrows(MpesaUnexpectedResponseException.class, () -> {
            c2bService.registerC2B(registerRequest, "api-key");
        });

        assertEquals(MpesaErrorCode.INVALID_RESPONSE, exception.errorCode());
        assertEquals("invalid json", exception.responseBody());
        assertTrue(exception.getMessage().contains("Failed to parse Register C2B response."));
    }

    @Test
    void testInitiatePayment_success() throws JsonProcessingException {
        String responseJson = "{\"RequestRefID\":\"" + paymentRequest.getRequestRefID() + "\",\"ResponseCode\":\"0\",\"ResponseDesc\":\"Success\",\"TransactionID\":\"TX123456\"}";

        when(mockRequestHandler.post(anyString(), eq(paymentRequest))).thenReturn(responseJson);

        C2BPaymentResponse response = c2bService.initiatePayment(paymentRequest);

        assertNotNull(response);
        assertTrue(response.isSuccessful());
        assertEquals("TX123456", response.getTransactionID());
        assertEquals("Success", response.getResponseDesc());
    }

    @Test
    void testInitiatePayment_invalidInitiator() throws JsonProcessingException {
        MpesaHttpException mpesaHttpException = new MpesaHttpException(400, "The initiator information is invalid.", "some-error");

        when(mockRequestHandler.post(anyString(), eq(paymentRequest))).thenThrow(mpesaHttpException);

        MpesaUnexpectedResponseException exception = assertThrows(MpesaUnexpectedResponseException.class, () -> {
            c2bService.initiatePayment(paymentRequest);
        });

        assertEquals(MpesaErrorCode.INVALID_INITIATOR, exception.errorCode());
        assertTrue(exception.getMessage().contains("Invalid initiator information."));
    }

    @Test
    void testInitiatePayment_genericError() throws JsonProcessingException {
        MpesaHttpException mpesaHttpException = new MpesaHttpException(500, "", "Internal Server Error");

        when(mockRequestHandler.post(anyString(), eq(paymentRequest))).thenThrow(mpesaHttpException);

        MpesaUnexpectedResponseException exception = assertThrows(MpesaUnexpectedResponseException.class, () -> {
            c2bService.initiatePayment(paymentRequest);
        });

        assertEquals(MpesaErrorCode.UNKNOWN_ERROR, exception.errorCode());
        assertTrue(exception.getMessage().contains("Unexpected error in C2B Payment."));
    }

    @Test
    void testInitiatePayment_jsonProcessingException() throws JsonProcessingException {
        String invalidJson = "invalid json";
        when(mockRequestHandler.post(anyString(), eq(paymentRequest))).thenReturn(invalidJson);

        MpesaUnexpectedResponseException exception = assertThrows(MpesaUnexpectedResponseException.class, () -> {
            c2bService.initiatePayment(paymentRequest);
        });

        assertEquals(MpesaErrorCode.INVALID_RESPONSE, exception.errorCode());
        assertTrue(exception.getMessage().contains("Failed to parse C2B Payment response."));
    }

    @Test
    void testSimulateC2BPayment_success() throws JsonProcessingException {
        String responseJson = "{\"ConversationID\":\"Conversation123\",\"OriginatorConversationID\":\"Originator123\",\"ResponseCode\":\"0\",\"ResponseDescription\":\"Payment simulated successfully\"}";

        when(mockRequestHandler.post(anyString(), eq(simulateRequest))).thenReturn(responseJson);

        C2BSimulatePaymentResponse response = c2bService.simulateC2BPayment(simulateRequest);

        assertNotNull(response);
        assertTrue(response.isSuccessful());
        assertEquals("Conversation123", response.getConversationID());
        assertEquals("Payment simulated successfully", response.getResponseDescription());
    }

    @Test
    void testSimulateC2BPayment_invalidRequest() throws JsonProcessingException {
        MpesaHttpException mpesaHttpException = new MpesaHttpException(400, "invalid request parameters", "some message");

        when(mockRequestHandler.post(anyString(), eq(simulateRequest))).thenThrow(mpesaHttpException);

        MpesaUnexpectedResponseException exception = assertThrows(MpesaUnexpectedResponseException.class, () -> {
            c2bService.simulateC2BPayment(simulateRequest);
        });

        assertEquals(MpesaErrorCode.INVALID_REQUEST, exception.errorCode());
        assertTrue(exception.getMessage().contains("Invalid request parameters."));
    }

    @Test
    void testSimulateC2BPayment_genericError() throws JsonProcessingException {
        MpesaHttpException mpesaHttpException = new MpesaHttpException(500, "", "Internal Server Error");

        when(mockRequestHandler.post(anyString(), eq(simulateRequest))).thenThrow(mpesaHttpException);

        MpesaUnexpectedResponseException exception = assertThrows(MpesaUnexpectedResponseException.class, () -> {
            c2bService.simulateC2BPayment(simulateRequest);
        });

        assertEquals(MpesaErrorCode.UNKNOWN_ERROR, exception.errorCode());
        assertTrue(exception.getMessage().contains("Unexpected error in C2B Payment Simulation."));
    }

    @Test
    void testSimulateC2BPayment_jsonProcessingException() throws JsonProcessingException {
        String invalidJson = "invalid json";
        when(mockRequestHandler.post(anyString(), eq(simulateRequest))).thenReturn(invalidJson);

        MpesaUnexpectedResponseException exception = assertThrows(MpesaUnexpectedResponseException.class, () -> {
            c2bService.simulateC2BPayment(simulateRequest);
        });

        assertEquals(MpesaErrorCode.INVALID_RESPONSE, exception.errorCode());
        assertTrue(exception.getMessage().contains("Failed to parse C2B Payment Simulation response."));
    }
}
