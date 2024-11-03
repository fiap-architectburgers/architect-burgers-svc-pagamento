package com.example.gomesrodris.archburgers.domain.controller;

import com.example.gomesrodris.archburgers.controller.PagamentoController;
import com.example.gomesrodris.archburgers.domain.datagateway.PagamentoEventMessagingGateway;
import com.example.gomesrodris.archburgers.domain.datagateway.PagamentoGateway;
import com.example.gomesrodris.archburgers.domain.entities.ItemCardapio;
import com.example.gomesrodris.archburgers.domain.entities.ItemPedido;
import com.example.gomesrodris.archburgers.domain.entities.Pagamento;
import com.example.gomesrodris.archburgers.domain.entities.Pedido;
import com.example.gomesrodris.archburgers.domain.exception.DomainArgumentException;
import com.example.gomesrodris.archburgers.domain.external.FormaPagamentoRegistry;
import com.example.gomesrodris.archburgers.domain.usecaseparam.DescricaoFormaPagamento;
import com.example.gomesrodris.archburgers.domain.utils.Clock;
import com.example.gomesrodris.archburgers.domain.valueobjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PagamentoControllerTest {

    private PagamentoController pagamentoController;

    private FormaPagamentoRegistry formaPagamentoRegistry = new FormaPagamentoRegistry(new ArrayList<>());

    @Mock
    private PagamentoGateway pagamentoGateway;

    @Mock
    private PagamentoEventMessagingGateway pagamentoEventMessagingGateway;

    private Clock clock = new Clock();

    private Pedido pedido;
    IdFormaPagamento formaPagamento = IdFormaPagamento.DINHEIRO;
    LocalDateTime dataHoraPedido = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        pagamentoController = new PagamentoController(formaPagamentoRegistry, pagamentoGateway, pagamentoEventMessagingGateway, clock);

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
    public void validarFormaPagamento(){
        IdFormaPagamento idFormaPagamento = pagamentoController.validarFormaPagamento("CARTAO_MAQUINA");
        assertEquals(IdFormaPagamento.CARTAO_MAQUINA, idFormaPagamento);
    }

    @Test
    public void iniciarPagamentoPedidoZerado(){

        ItemCardapio itemCardapioFail1 = new ItemCardapio(21, TipoItemCardapio.LANCHE, "Hamburger Vegetariano",
                "Hamburger de ervilha com queijo vegano",
                new ValorMonetario("23.50"));

        ItemCardapio itemCardapioFail2 = new ItemCardapio(33, TipoItemCardapio.SOBREMESA, "Torta de chocolate",
                "Torta sem graça",
                new ValorMonetario("53.85"));

        List<ItemPedido> itensFail = new ArrayList<>();
        ItemPedido ip1 = new ItemPedido(1, itemCardapioFail1);
        ItemPedido ip2 = new ItemPedido(2, itemCardapioFail2);
        itensFail.add(ip1);
        itensFail.add(ip2);


        pedido = new Pedido(0,
                new IdCliente(1),
                null,
                itensFail,
                "Sem cebola",
                StatusPedido.RECEBIDO,
                formaPagamento,
                dataHoraPedido);

        assertNull(pagamentoController.iniciarPagamento(pedido));
    }

    @Test
    public void listarFormasPagamento(){
        List<DescricaoFormaPagamento> list = pagamentoController.listarFormasPagamento();
        assertTrue(list.size() >= 2 );
    }

    @Test
    public void consultarPagamentoPedidoZerado(){
        Pagamento pagamento = pagamentoController.consultarPagamento(0);
        assertNull(pagamento);
    }

    @Test
    public void consultarPagamentoPedidoValido(){

        Integer idPedido = 1;
        IdFormaPagamento formaPagamento = IdFormaPagamento.CARTAO_MAQUINA;
        ValorMonetario valor = new ValorMonetario("100");
        LocalDateTime dataHora = LocalDateTime.now();
        String codigoPagamentoCliente = "ABC123";
        String idPedidoSistemaExterno = "123";

        Pagamento pagamentoValido = Pagamento.registroInicial(idPedido, formaPagamento, valor, dataHora, codigoPagamentoCliente, idPedidoSistemaExterno);

        Mockito.when(pagamentoController.consultarPagamento(1)).thenReturn(pagamentoValido);

        Pagamento pagamento = pagamentoController.consultarPagamento(1);
        assertNotNull(pagamento);
        assertEquals(1, pagamento.idPedido());
    }

    @Test
    void finalizarPagamentoPedidoNaoEncontrado() {
        assertThrows(DomainArgumentException.class, () -> pagamentoController.finalizarPagamento(pedido.id(), "123456"));
    }

    @Test
    void finalizarPagamentoPedidoEncontradoPagamentoFinalizado() {

        String codigoPagamentoCliente = "123456";
        String idPedidoSistemaExterno = "123";
        Pagamento pagamento = new Pagamento(233, pedido.id(), pedido.formaPagamento(), StatusPagamento.FINALIZADO, pedido.getValorTotal(), dataHoraPedido, dataHoraPedido, codigoPagamentoCliente, idPedidoSistemaExterno);

        when(pagamentoGateway.findPagamentoByPedido(pedido.id())).thenReturn(pagamento);

        assertThrows(DomainArgumentException.class, () -> pagamentoController.finalizarPagamento(pedido.id(), "123456"));
    }

    @Test
    void finalizarPagamentoPedidoZerado() {
        assertThrows(DomainArgumentException.class, () -> pagamentoController.finalizarPagamento(0, "123456"));
    }
}
