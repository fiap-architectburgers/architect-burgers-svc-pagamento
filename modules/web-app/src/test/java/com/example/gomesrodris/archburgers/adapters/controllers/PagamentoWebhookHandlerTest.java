package com.example.gomesrodris.archburgers.adapters.controllers;

import com.example.gomesrodris.archburgers.adapters.datasource.TransactionManager;
import com.example.gomesrodris.archburgers.adapters.pagamento.MercadoPagoApi;
import com.example.gomesrodris.archburgers.adapters.pagamento.MercadoPagoPaymentListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PagamentoWebhookHandlerTest {

    @Mock
    private TransactionManager transactionManager;

    private final MercadoPagoPaymentListener mercadoPagoPaymentListener = mock(MercadoPagoPaymentListener.class);

    @InjectMocks
    private PagamentoWebhookHandler pagamentoWebhookHandler;

    @BeforeEach
    void setUp() {
        pagamentoWebhookHandler = new PagamentoWebhookHandler(mercadoPagoPaymentListener, transactionManager);
    }

    @Test
    public void testWebhookMercadoPago_ExternalId1() throws Exception {
        // Arrange
        String externalId = "12345";
        Map<String, String> headers = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        // Act
        String result = pagamentoWebhookHandler.webHookMercadoPago(externalId, null, headers, data);

        // Assert
        assertEquals("OK", result);
        //verify(mercadoPagoPaymentListener).notificarUpdate(externalId, headers, data);
        verify(transactionManager).runInTransaction(any());
    }


}

