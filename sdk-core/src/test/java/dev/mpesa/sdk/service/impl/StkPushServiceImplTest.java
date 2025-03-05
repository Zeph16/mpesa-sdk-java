package dev.mpesa.sdk.service.impl;

import dev.mpesa.sdk.config.MpesaConfig;
import dev.mpesa.sdk.dto.request.StkPushRequest;
import dev.mpesa.sdk.dto.response.StkPushResponse;
import dev.mpesa.sdk.exception.MpesaHttpException;
import dev.mpesa.sdk.exception.MpesaUnexpectedResponseException;
import dev.mpesa.sdk.http.RequestHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static dev.mpesa.sdk.exception.MpesaErrorCode.INVALID_RESPONSE;
import static dev.mpesa.sdk.exception.MpesaErrorCode.UNKNOWN_ERROR;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StkPushServiceImplTest {

    @Mock
    private RequestHandler mockRequestHandler;

    @Mock
    private MpesaConfig mockConfig;

    private StkPushServiceImpl stkPushService;
    private StkPushRequest stkPushRequest;

    @BeforeEach
    void setUp() {
        mockRequestHandler = mock(RequestHandler.class);
        mockConfig = mock(MpesaConfig.class);
        when(mockConfig.getStkPushUrl()).thenReturn("sample-url");
        stkPushRequest = new StkPushRequest.Builder()
                .businessShortCode("123456")
                .password("base64encodedpassword")
                .amount("100")
                .partyA("254700000000")
                .partyB("123456")
                .phoneNumber("254700000000")
                .callBackURL("https://callback.url")
                .accountReference("12345")
                .transactionDesc("Payment")
                .transactionType(StkPushRequest.TransactionType.CustomerPayBillOnline)
                .build();

        stkPushService = new StkPushServiceImpl(mockRequestHandler, mockConfig);
    }

    @Test
    void testRequestStkPush_Success() throws Exception {
        String mockResponseJson = "{\"MerchantRequestID\":\"1234\", \"CheckoutRequestID\":\"5678\", \"ResponseCode\":\"0\", \"ResponseDescription\":\"Success\", \"CustomerMessage\":\"Request Successful\"}";

        when(mockRequestHandler.post(anyString(), eq(stkPushRequest))).thenReturn(mockResponseJson);

        StkPushResponse response = stkPushService.requestStkPush(stkPushRequest);

        assertNotNull(response);
        assertEquals("1234", response.getMerchantRequestID());
        assertEquals("5678", response.getCheckoutRequestID());
        assertEquals("0", response.getResponseCode());
        assertTrue(response.isSuccessful());
    }

    @Test
    void testRequestStkPush_HttpException() throws Exception {
        when(mockRequestHandler.post(anyString(), eq(stkPushRequest))).thenThrow(new MpesaHttpException(500, "Error", "Internal Server Error"));

        MpesaUnexpectedResponseException exception = assertThrows(MpesaUnexpectedResponseException.class, () -> {
            stkPushService.requestStkPush(stkPushRequest);
        });

        assertTrue(exception.getMessage().contains("Unexpected error in STK Push"));
        assertEquals(UNKNOWN_ERROR, exception.errorCode());
    }

    @Test
    void testRequestStkPush_JsonProcessingException() throws Exception {
        when(mockRequestHandler.post(anyString(), eq(stkPushRequest))).thenReturn("invalid json");

        MpesaUnexpectedResponseException exception = assertThrows(MpesaUnexpectedResponseException.class, () -> {
            stkPushService.requestStkPush(stkPushRequest);
        });

        assertTrue(exception.getMessage().contains("Failed to parse STK Push response"));
        assertEquals(INVALID_RESPONSE, exception.errorCode());
    }
}
