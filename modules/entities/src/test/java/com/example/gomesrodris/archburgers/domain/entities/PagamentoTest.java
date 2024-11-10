package com.example.gomesrodris.archburgers.domain.entities;

import com.example.gomesrodris.archburgers.domain.valueobjects.IdFormaPagamento;
import com.example.gomesrodris.archburgers.domain.valueobjects.StatusPagamento;
import com.example.gomesrodris.archburgers.domain.valueobjects.ValorMonetario;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PagamentoTest {

    @Test
    void testRegistroInicial() {
        Integer idPedido = 1;
        IdFormaPagamento formaPagamento = IdFormaPagamento.CARTAO_MAQUINA;
        ValorMonetario valor = new ValorMonetario("100");
        LocalDateTime dataHora = LocalDateTime.now();
        String codigoPagamentoCliente = "ABC123";
        String idPedidoSistemaExterno = "123";

        Pagamento pagamento = Pagamento.registroInicial(idPedido, formaPagamento, valor, dataHora, codigoPagamentoCliente, idPedidoSistemaExterno);

        assertNull(pagamento.id());
        assertEquals(idPedido, pagamento.idPedido());
        assertEquals(formaPagamento, pagamento.formaPagamento());
        assertEquals(StatusPagamento.PENDENTE, pagamento.status());
        assertEquals(valor, pagamento.valor());
        assertEquals(dataHora, pagamento.dataHoraCriacao());
        assertEquals(dataHora, pagamento.dataHoraAtualizacao());
        assertEquals(codigoPagamentoCliente, pagamento.codigoPagamentoCliente());
        assertEquals(idPedidoSistemaExterno, pagamento.idPedidoSistemaExterno());
    }

    @Test
    void testFinalizar() {
        Pagamento pagamento = Pagamento.registroInicial(1, IdFormaPagamento.CARTAO_MAQUINA, new ValorMonetario("10.00"), LocalDateTime.now(), null, null);
        LocalDateTime novaDataHora = LocalDateTime.now().plusHours(1);

        Pagamento pagamentoFinalizado = pagamento.finalizar(novaDataHora);

        assertEquals(pagamento.id(), pagamentoFinalizado.id()); // Id deve permanecer o mesmo
        assertEquals(pagamento.idPedido(), pagamentoFinalizado.idPedido());
        assertEquals(pagamento.formaPagamento(), pagamentoFinalizado.formaPagamento());
        assertEquals(StatusPagamento.FINALIZADO, pagamentoFinalizado.status());
        assertEquals(pagamento.valor(), pagamentoFinalizado.valor());
        assertEquals(pagamento.dataHoraCriacao(), pagamentoFinalizado.dataHoraCriacao());
        assertEquals(novaDataHora, pagamentoFinalizado.dataHoraAtualizacao());
        assertEquals(pagamento.codigoPagamentoCliente(), pagamentoFinalizado.codigoPagamentoCliente());
        assertEquals(pagamento.idPedidoSistemaExterno(), pagamentoFinalizado.idPedidoSistemaExterno());
    }

    @Test
    void testFinalizarComIdExterno() {
        Pagamento pagamento = Pagamento.registroInicial(1, IdFormaPagamento.CARTAO_MAQUINA, new ValorMonetario("10.00"), LocalDateTime.now(), null, null);
        LocalDateTime novaDataHora = LocalDateTime.now().plusHours(1);
        String novoIdExterno = "1234";

        Pagamento pagamentoFinalizado = pagamento.finalizar(novaDataHora, novoIdExterno);

        assertEquals(pagamento.id(), pagamentoFinalizado.id()); // Id deve permanecer o mesmo
        assertEquals(pagamento.idPedido(), pagamentoFinalizado.idPedido());
        assertEquals(pagamento.formaPagamento(), pagamentoFinalizado.formaPagamento());
        assertEquals(StatusPagamento.FINALIZADO, pagamentoFinalizado.status());
    }

    @Test
    void testGetPagamentoJson() throws JSONException {
        Integer idPedido = 1;
        IdFormaPagamento formaPagamento = IdFormaPagamento.CARTAO_MAQUINA;
        ValorMonetario valor = new ValorMonetario("100");
        LocalDateTime dataHora = LocalDateTime.now();
        String codigoPagamentoCliente = "ABC123";
        String idPedidoSistemaExterno = "123";

        Pagamento pagamento = Pagamento.registroInicial(idPedido, formaPagamento, valor, dataHora, codigoPagamentoCliente, idPedidoSistemaExterno);
        String json = pagamento.getPagamentoJson();

        JSONObject jsonObject = new JSONObject(json);
        assertEquals(1, jsonObject.getInt("idPedido"));
        assertEquals(IdFormaPagamento.CARTAO_MAQUINA.toString(), jsonObject.getString("formaPagamento"));
        assertEquals(new ValorMonetario("100").toString(), jsonObject.getString("valor"));
        assertEquals(dataHora.toString(), jsonObject.getString("dataHoraCriacao"));
        assertEquals("ABC123", jsonObject.getString("codigoPagamentoCliente"));
        assertEquals(123, jsonObject.getInt("idPedidoSistemaExterno"));
        assertEquals(StatusPagamento.PENDENTE.toString(), jsonObject.getString("status"));
    }

    @Test
    void testWithId() {
        Integer idPedido = 1;
        IdFormaPagamento formaPagamento = IdFormaPagamento.CARTAO_MAQUINA;
        ValorMonetario valor = new ValorMonetario("100");
        LocalDateTime dataHora = LocalDateTime.now();
        String codigoPagamentoCliente = "ABC123";
        String idPedidoSistemaExterno = "123";

        Pagamento pagamento = Pagamento.registroInicial(idPedido, formaPagamento, valor, dataHora, codigoPagamentoCliente, idPedidoSistemaExterno);
        String novoId = "10";

        Pagamento pagamentoComNovoId = pagamento.withId(novoId);

        assertEquals(novoId, pagamentoComNovoId.id());
        assertEquals(pagamento.idPedido(), pagamentoComNovoId.idPedido());

    }
}