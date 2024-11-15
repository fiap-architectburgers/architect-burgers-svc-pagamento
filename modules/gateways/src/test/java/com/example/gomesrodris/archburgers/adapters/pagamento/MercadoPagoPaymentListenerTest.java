package com.example.gomesrodris.archburgers.adapters.pagamento;

import com.example.gomesrodris.archburgers.controller.PagamentoController;
import com.example.gomesrodris.archburgers.domain.datagateway.PagamentoEventMessagingGateway;
import com.example.gomesrodris.archburgers.domain.datagateway.PagamentoGateway;
import com.example.gomesrodris.archburgers.domain.external.FormaPagamentoRegistry;
import com.example.gomesrodris.archburgers.domain.utils.Clock;
import com.example.gomesrodris.archburgers.domain.valueobjects.IdFormaPagamento;
import com.example.gomesrodris.archburgers.testUtils.StaticEnvironment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class MercadoPagoPaymentListenerTest {

    private PagamentoController pagamentoController;
    private MercadoPagoPaymentListener mercadoPagoPaymentListener;

    private final PagamentoEventMessagingGateway pagamentoEventMessagingGateway = mock(PagamentoEventMessagingGateway.class);
    private final PagamentoGateway pagamentoGateway = mock(PagamentoGateway.class);

    private final MercadoPagoApi mercadoPagoApi = mock(MercadoPagoApi.class);

    IdFormaPagamento formaPagamento = IdFormaPagamento.DINHEIRO;
    LocalDateTime dataHoraPedido = LocalDateTime.now();
    Clock clock = new Clock();

    private FormaPagamentoRegistry formaPagamentoRegistry = new FormaPagamentoRegistry(new ArrayList<>());

    @BeforeEach
    void setUp() {
        pagamentoController = new PagamentoController(formaPagamentoRegistry, pagamentoGateway, pagamentoEventMessagingGateway, clock);
        mercadoPagoPaymentListener = new MercadoPagoPaymentListener(mercadoPagoApi, pagamentoController);
    }


    @Test
    public void testNotificarUpdate_MissingExternalReference() throws JsonProcessingException {
        // Mock data
        String urlId = "notification_id";
        Map<String, String> headers = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        data.put("resource", "resource_url");

        Map<String, Object> updatedOrder = new HashMap<>();
        updatedOrder.put("order_status", "paid");;

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonData = objectMapper.readValue(
                "{\"order_status\": \"paid\"}",
                Map.class
        );

        // Mock behavior
        Mockito.when(mercadoPagoApi.getOrder((String) data.get("resource"), urlId)).thenReturn(jsonData);

        // Call the method
        Assertions.assertThrows(IllegalStateException.class, () -> mercadoPagoPaymentListener.notificarUpdate(urlId, headers, data));

        assertThatThrownBy(() -> {
            mercadoPagoPaymentListener.notificarUpdate(urlId, headers, data);
        }).hasMessageContaining("Missing external_reference");
    }

    @Test
    public void testNotificarUpdate_InvalidExternalReference() throws JsonProcessingException {
        // Mock data
        String urlId = "notification_id";
        Map<String, String> headers = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        data.put("resource", "resource_url");

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonData = objectMapper.readValue(
                "{\"order_status\": \"paid\", \"external_reference\": \"invalid\"}",
                Map.class
        );

        // Mock behavior
        Mockito.when(mercadoPagoApi.getOrder((String) data.get("resource"), urlId)).thenReturn(jsonData);

        // Call the method
        Assertions.assertThrows(IllegalStateException.class, () -> mercadoPagoPaymentListener.notificarUpdate(urlId, headers, data));

        assertThatThrownBy(() -> {
            mercadoPagoPaymentListener.notificarUpdate(urlId, headers, data);
        }).hasMessageContaining("Invalid external_reference");
    }

    @Test
    public void testNotificarUpdate_OrderStatusIsNotPaid() throws JsonProcessingException {
        // Mock data
        String urlId = "notification_id";
        Map<String, String> headers = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        data.put("resource", "resource_url");

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonData = objectMapper.readValue(
                "{\"order_status\": \"not_paid\", \"external_reference\": \"invalid\"}",
                Map.class
        );

        // Mock behavior
        Mockito.when(mercadoPagoApi.getOrder((String) data.get("resource"), urlId)).thenReturn(jsonData);

        // Call the method
       mercadoPagoPaymentListener.notificarUpdate(urlId, headers, data);

    }
}
