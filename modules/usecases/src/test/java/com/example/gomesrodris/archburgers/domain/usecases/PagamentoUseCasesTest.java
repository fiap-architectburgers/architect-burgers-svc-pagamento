package com.example.gomesrodris.archburgers.domain.usecases;

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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagamentoUseCasesTest {

    private PagamentoUseCases pagamentoUseCases;
    private Pedido pedido;
    IdFormaPagamento formaPagamento = IdFormaPagamento.DINHEIRO;
    LocalDateTime dataHoraPedido = LocalDateTime.now();

    private FormaPagamentoRegistry formaPagamentoRegistry = new FormaPagamentoRegistry(new ArrayList<>());

    @Mock
    private PagamentoGateway pagamentoGateway;

    @Mock
    private PagamentoEventMessagingGateway pagamentoEventMessagingGateway;

    private Clock clock= new Clock();

    @BeforeEach
    void setUp() {
        pagamentoUseCases = new PagamentoUseCases(formaPagamentoRegistry, pagamentoGateway, pagamentoEventMessagingGateway, clock);

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
        IdFormaPagamento idFormaPagamento = pagamentoUseCases.validarFormaPagamento("CARTAO_MAQUINA");
        assertEquals(IdFormaPagamento.CARTAO_MAQUINA, idFormaPagamento);
    }

    @Test
    public void listarFormasPagamento(){
        List<DescricaoFormaPagamento> list = pagamentoUseCases.listarFormasPagamento();
        assertTrue(list.size() >= 2 );
    }

    @Test
    public void consultarPagamentoPedidoZerado(){
        Pagamento pagamento = pagamentoUseCases.consultarPagamento(0);
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

        Mockito.when(pagamentoUseCases.consultarPagamento(1)).thenReturn(pagamentoValido);

        Pagamento pagamento = pagamentoUseCases.consultarPagamento(1);
        assertNotNull(pagamento);
        assertEquals(1, pagamento.idPedido());
    }

//    @Test
//    public void iniciarPagamentoValido(){
//
//
//        Pagamento pagamentoValido = Pagamento.registroInicial(pedido.id(), pedido.formaPagamento(), pedido.getValorTotal(), dataHoraPedido, null,
//                null);
//
//        FormaPagamento.InfoPagamentoExterno infoPagamentoExterno = new FormaPagamento.InfoPagamentoExterno(null, null);
//
////        Pagamento pagamentoValidoSaved = Pagamento.registroInicial(idPedido, formaPagamento, valor, dataHora, infoPagamentoExterno.codigoPagamentoCliente(),
////                infoPagamentoExterno.idPedidoSistemaExterno());
//
//        Pagamento pagamentoValidoSaved = new Pagamento(233, pedido.id(), pedido.formaPagamento(), StatusPagamento.PENDENTE, pedido.getValorTotal(), dataHoraPedido, dataHoraPedido, infoPagamentoExterno.codigoPagamentoCliente(), infoPagamentoExterno.idPedidoSistemaExterno());
//
//        Clock clock1 = Mockito.mock(Clock.class);
//        Mockito.when(clock1.localDateTime()).thenReturn(LocalDateTime.of(2024,11,02,0,0,0));
//
////        Mockito.when(pagamentoGateway.salvarPagamento(pagamentoValido)).thenReturn(pagamentoValidoSaved);
//
//        Pagamento pagamento = pagamentoUseCases.iniciarPagamento(pedido);
//        assertNotNull(pagamento);
//        assertEquals(1, pagamento.idPedido());
//    }

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

        assertNull(pagamentoUseCases.iniciarPagamento(pedido));
    }

    @Test
    void excluirPagamentoDoPedido() {
        doNothing().when(pagamentoGateway).excluirPagamento(pedido.id());
        pagamentoUseCases.excluirPagamentoDoPedido(pedido.id());
        verify(pagamentoGateway).excluirPagamento(pedido.id());
    }

    @Test
    void finalizarPagamentoPedidoNaoEncontrado() {
        assertThrows(DomainArgumentException.class, () -> pagamentoUseCases.finalizarPagamento(pedido.id(), "123456"));
    }

    @Test
    void finalizarPagamentoPedidoEncontradoPagamentoFinalizado() {

        String codigoPagamentoCliente = "123456";
        String idPedidoSistemaExterno = "123";
//        Pagamento pagamentoValido = Pagamento.registroInicial(pedido.id(), pedido.formaPagamento(), pedido.getValorTotal(), dataHoraPedido, codigoPagamentoCliente, idPedidoSistemaExterno);
        Pagamento pagamento = new Pagamento("233", pedido.id(), pedido.formaPagamento(), StatusPagamento.FINALIZADO, pedido.getValorTotal(), dataHoraPedido, dataHoraPedido, codigoPagamentoCliente, idPedidoSistemaExterno);
//
        when(pagamentoGateway.findPagamentoByPedido(pedido.id())).thenReturn(pagamento);

        assertThrows(DomainArgumentException.class, () -> pagamentoUseCases.finalizarPagamento(pedido.id(), "123456"));
    }

    @Test
    void finalizarPagamentoPedidoZerado() {
        assertThrows(DomainArgumentException.class, () -> pagamentoUseCases.finalizarPagamento(0, "123456"));
    }

}