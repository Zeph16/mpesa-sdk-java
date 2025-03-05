package dev.mpesa.sdk.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.mpesa.sdk.config.MpesaConfig;
import dev.mpesa.sdk.dto.request.B2CPaymentRequest;
import dev.mpesa.sdk.dto.response.B2CPaymentResponse;
import dev.mpesa.sdk.exception.MpesaHttpException;
import dev.mpesa.sdk.exception.MpesaUnexpectedResponseException;
import dev.mpesa.sdk.http.RequestHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static dev.mpesa.sdk.exception.MpesaErrorCode.INVALID_RESPONSE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class B2CServiceImplTest {

    @Mock
    private RequestHandler mockRequestHandler;

    @Mock
    private MpesaConfig mockConfig;

    private B2CServiceImpl b2CService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockConfig.getB2cPaymentUrl()).thenReturn("sample-url");
        b2CService = new B2CServiceImpl(mockRequestHandler, mockConfig);
    }

    @Test
    void testInitiateB2CPayment_Success() throws JsonProcessingException {
        String expectedJsonResponse = "{ \"OriginatorConversationID\": \"12345\", \"ConversationID\": \"67890\", \"ResponseCode\": \"0\", \"ResponseDescription\": \"Success\" }";

        B2CPaymentRequest request = new B2CPaymentRequest.Builder()
                .initiatorName("TestInitiator")
                .securityCredential("testCredential")
                .occassion("TestOccasion")
                .commandID("SalaryPayment")
                .partyA("12345")
                .partyB("251700000000")
                .remarks("Test Remarks")
                .amount("1000")
                .queueTimeOutURL("https://timeout.url")
                .resultURL("https://result.url")
                .build();

        B2CPaymentResponse expectedResponse = new B2CPaymentResponse("67890", "12345", "0", "Success");

        when(mockRequestHandler.post(anyString(), eq(request))).thenReturn(expectedJsonResponse);

        B2CPaymentResponse actualResponse = b2CService.initiateB2CPayment(request);

        assertEquals(expectedResponse.getOriginatorConversationID(), actualResponse.getOriginatorConversationID());
        assertEquals(expectedResponse.getConversationID(), actualResponse.getConversationID());
        assertEquals(expectedResponse.getResponseCode(), actualResponse.getResponseCode());
        assertEquals(expectedResponse.getResponseDescription(), actualResponse.getResponseDescription());
        assertTrue(actualResponse.isSuccessful());
    }

    @Test
    void testInitiateB2CPayment_Failure_InvalidResponse() throws JsonProcessingException {
        String invalidJsonResponse = "{ invalid json }";
        B2CPaymentRequest request = new B2CPaymentRequest.Builder()
                .initiatorName("TestInitiator")
                .securityCredential("testCredential")
                .occassion("TestOccasion")
                .commandID("SalaryPayment")
                .partyA("12345")
                .partyB("251700000000")
                .remarks("Test Remarks")
                .amount("1000")
                .queueTimeOutURL("https://timeout.url")
                .resultURL("https://result.url")
                .build();

        when(mockRequestHandler.post(anyString(), eq(request))).thenReturn(invalidJsonResponse);

        MpesaUnexpectedResponseException exception = assertThrows(MpesaUnexpectedResponseException.class, () -> {
            b2CService.initiateB2CPayment(request);
        });

        assertEquals("Failed to parse B2C Payment response.", exception.getMessage());
        assertEquals(INVALID_RESPONSE, exception.errorCode());
        assertNull(exception.errorResponse());
        assertEquals("{ invalid json }", exception.responseBody());
    }

    @Test
    void testInitiateB2CPayment_HttpError() throws Exception {
        B2CPaymentRequest request = new B2CPaymentRequest.Builder()
                .initiatorName("TestInitiator")
                .securityCredential("testCredential")
                .occassion("TestOccasion")
                .commandID("SalaryPayment")
                .partyA("12345")
                .partyB("251700000000")
                .remarks("Test Remarks")
                .amount("1000")
                .queueTimeOutURL("https://timeout.url")
                .resultURL("https://result.url")
                .build();

        MpesaHttpException mockHttpException = new MpesaHttpException(400, "mock-error-body", "Some error");
        when(mockRequestHandler.post(anyString(), eq(request))).thenThrow(mockHttpException);

        MpesaUnexpectedResponseException exception = assertThrows(MpesaUnexpectedResponseException.class, () -> {
            b2CService.initiateB2CPayment(request);
        });

        assertTrue(exception.getMessage().contains("Unexpected error in B2C Payment"));
        assertInstanceOf(MpesaHttpException.class, exception.getCause());
    }
}
