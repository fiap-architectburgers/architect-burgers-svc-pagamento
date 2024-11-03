package com.example.gomesrodris.archburgers.domain.entities;

import com.example.gomesrodris.archburgers.domain.valueobjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PedidoTest {

    private Pedido pedido;
    IdFormaPagamento formaPagamento = IdFormaPagamento.DINHEIRO;
    LocalDateTime dataHoraPedido = LocalDateTime.now();

    @BeforeEach
    void setUp() {
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
                formaPagamento,
                dataHoraPedido);
    }

    @Test
    void checkAttributes() {
        assertThat(pedido.id()).isEqualTo(11);
        assertThat(pedido.idClienteIdentificado()).isEqualTo(new IdCliente(1));
        assertThat(pedido.itens().size()).isEqualTo(2);
        assertThat(pedido.status()).isEqualTo(StatusPedido.RECEBIDO);
        assertThat(pedido.formaPagamento()).isEqualTo(formaPagamento);
        assertThat(pedido.dataHoraPedido()).isEqualTo(dataHoraPedido);
    }

    @Test
    void checkAttributesClienteNaoIdentificado() {

        Pedido ped;
        IdFormaPagamento pagamento = IdFormaPagamento.DINHEIRO;
        LocalDateTime dataHora = LocalDateTime.now();

        ItemCardapio itemCardapio01 = new ItemCardapio(21, TipoItemCardapio.LANCHE, "Hamburger Vegetariano",
                "Hamburger de ervilha com queijo vegano",
                new ValorMonetario("23.50"));

        ItemCardapio itemCardapio02 = new ItemCardapio(33, TipoItemCardapio.SOBREMESA, "Torta de chocolate",
                "Torta sem graça",
                new ValorMonetario("53.85"));

        List<ItemPedido> itens = new ArrayList<>();
        ItemPedido ip1 = new ItemPedido(1, itemCardapio01);
        ItemPedido ip2 = new ItemPedido(2, itemCardapio02);
        itens.add(ip1);
        itens.add(ip2);


        ped = new Pedido(11,
                null,
                "João David",
                itens,
                "Sem cebola",
                StatusPedido.RECEBIDO,
                pagamento,
                dataHora);

        assertThat(ped.id()).isEqualTo(11);
        assertThat(ped.idClienteIdentificado()).isNull();
        assertThat(ped.itens().size()).isEqualTo(2);
        assertThat(ped.status()).isEqualTo(StatusPedido.RECEBIDO);
        assertThat(ped.formaPagamento()).isEqualTo(formaPagamento);
        assertThat(ped.dataHoraPedido()).isEqualTo(dataHora);
    }

    @Test
    void testGetValorTotalSemItens() {
        Pedido pedido = new Pedido(null, null, null, Collections.emptyList(), null, StatusPedido.PAGAMENTO, IdFormaPagamento.CARTAO_MAQUINA, LocalDateTime.now());
        assertEquals(ValorMonetario.ZERO, pedido.getValorTotal());
    }

    @Test
    void getValorTotal() {
        assertThat(pedido.getValorTotal()).isEqualTo(new ValorMonetario("77.35"));
    }

    @Test
    void validateIncorrectValues() {
        assertThat(pedido.getValorTotal()).isNotEqualTo(new ValorMonetario("22"));
    }

    @Test
    void validateQuantidadeDeItens() {
        assertThat(pedido.itens().size()).isEqualTo(2);
    }
}