package com.example.gomesrodris.archburgers.adapters.pagamento;

import com.example.gomesrodris.archburgers.domain.entities.ItemCardapio;
import com.example.gomesrodris.archburgers.domain.entities.ItemPedido;
import com.example.gomesrodris.archburgers.domain.entities.Pedido;
import com.example.gomesrodris.archburgers.domain.valueobjects.*;
import com.example.gomesrodris.archburgers.testUtils.StaticEnvironment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class MercadoPagoGatewayTest {

    private MercadoPagoGateway mercadoPagoGateway;
    private final MercadoPagoApi mercadoPagoApi = mock(MercadoPagoApi.class);

    private Pedido pedido;
    IdFormaPagamento formaPagamento = IdFormaPagamento.DINHEIRO;
    LocalDateTime dataHoraPedido = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        mercadoPagoGateway = new MercadoPagoGateway(mercadoPagoApi);

        ItemCardapio itemCardapio = new ItemCardapio(21, TipoItemCardapio.LANCHE, "Hamburger Vegetariano",
                "Hamburger de ervilha com queijo vegano",
                new ValorMonetario("23.50"));

        ItemCardapio itemCardapio2 = new ItemCardapio(33, TipoItemCardapio.SOBREMESA, "Torta de chocolate",
                "Torta sem graça",
                new ValorMonetario("53.85"));

        List<ItemPedido> itens = new ArrayList<>();
        ItemPedido ip1 = new ItemPedido(1, itemCardapio);
        ItemPedido ip2 = new ItemPedido(1, itemCardapio2);
        itens.add(ip1);
        itens.add(ip2);


        pedido = new Pedido(11,
                new IdCliente(1),
                null,
                itens,
                "Sem cebola",
                StatusPedido.RECEBIDO,
                formaPagamento.codigo(),
                dataHoraPedido);
    }

    @Test
    void validarIntegracaoExterna(){
        assertThat(mercadoPagoGateway.isIntegracaoExterna()).isTrue();
    }

    @Test
    void validarId(){
//        assertThat(mercadoPagoGateway.id()).isEqualTo(IdFormaPagamento.valueOf("MERCADO_PAGO"));
        assertThat(mercadoPagoGateway.id().codigo()).isEqualTo(new IdFormaPagamento("MERCADO_PAGO").codigo());
    }

    @Test
    void descricao(){
        assertThat(mercadoPagoGateway.descricao().toString()).contains("QrCode");
    }

    @Test
    void iniciarRegistroPagamentoComPedidoNulo(){
        assertThatThrownBy(() -> {
            mercadoPagoGateway.iniciarRegistroPagamento(null);
        }).hasMessageContaining("is null");
    }

    @Test
    void iniciarRegistroPagamento() throws JsonProcessingException {
        ItemCardapio itemCardapio = new ItemCardapio(21, TipoItemCardapio.LANCHE, "Hamburger Vegetariano",
                "Hamburger de ervilha com queijo vegano",
                new ValorMonetario("23.50"));

        ItemPedido itemPedido = new ItemPedido(1, itemCardapio);

        ObjectMapper objectMapper = new ObjectMapper();
//        Map<String, Object> postOrderObj = new HashMap<>();
        Map<String, Object> jsonData = objectMapper.readValue(
                "{\"codigoPagamentoCliente\": 9999, \"idPedidoSistemaExterno\": 7777}",
                Map.class
        );
//        postOrderObj.put("qr_data", jsonData);

//        MercadoPagoApi mercadoPagoApi = Mockito.mock(MercadoPagoApi.class);
//        Mockito.when(mercadoPagoApi.postOrder(any())).thenReturn(postOrderObj);

        // Mock MercadoPagoApi.postOrder to return the expected response
        Mockito.when(mercadoPagoApi.postOrder(any())).thenReturn(jsonData);


        assertThat(mercadoPagoGateway.iniciarRegistroPagamento(pedido).codigoPagamentoCliente()).isNull();
        assertThat(mercadoPagoGateway.iniciarRegistroPagamento(pedido).idPedidoSistemaExterno()).isNull();

    }


    Map<String, String> getEnv(){
        Map<String, String> env = new HashMap<>();
        env.put("archburgers.integration.mercadopago.apiBaseUrl", "https://api.mercadopago.com");
        env.put("archburgers.integration.mercadopago.userId", "userId");
        env.put("archburgers.integration.mercadopago.accessToken", "token");
        env.put("archburgers.integration.mercadopago.posId", "posId");
        env.put("archburgers.integration.mercadopago.notificationUrl", "notificationUrl");

        return env;
    }

}
