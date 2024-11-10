package com.example.gomesrodris.archburgers.adapters.controllers;

import com.example.gomesrodris.archburgers.adapters.datasource.TransactionManager;
import com.example.gomesrodris.archburgers.adapters.dto.ConfirmacaoPagamentoDto;
import com.example.gomesrodris.archburgers.adapters.dto.PagamentoDto;
import com.example.gomesrodris.archburgers.adapters.presenters.QrCodePresenter;
import com.example.gomesrodris.archburgers.controller.PagamentoController;
import com.example.gomesrodris.archburgers.domain.entities.Pagamento;
import com.example.gomesrodris.archburgers.domain.usecaseparam.DescricaoFormaPagamento;
import com.example.gomesrodris.archburgers.domain.valueobjects.IdFormaPagamento;
import com.example.gomesrodris.archburgers.domain.valueobjects.StatusPagamento;
import com.example.gomesrodris.archburgers.domain.valueobjects.ValorMonetario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PagamentoApiHandlerTest {

    @Mock
    private PagamentoController pagamentoController;

    @InjectMocks
    private PagamentoApiHandler pagamentoApiHandler;

    @Mock
    private TransactionManager transactionManager;

    @Mock
    private QrCodePresenter qrCodePresenter;

    @Test
    public void testListFormasPagamento() {
        // Arrange
        List<DescricaoFormaPagamento> expectedFormasPagamento = List.of(
                new DescricaoFormaPagamento(IdFormaPagamento.DINHEIRO, "Din Din"),
                new DescricaoFormaPagamento(IdFormaPagamento.CARTAO_MAQUINA, "Cartao")
        );

        when(pagamentoController.listarFormasPagamento()).thenReturn(expectedFormasPagamento);

        // Act
        List<DescricaoFormaPagamento> result = pagamentoApiHandler.listFormasPagamento();

        // Assert
        assertEquals(expectedFormasPagamento, result);
    }

    @Test
    public void testConfirmacaoPagamento_Sucesso() throws Exception {
        // Arrange
        ConfirmacaoPagamentoDto param = new ConfirmacaoPagamentoDto(1);

        when(transactionManager.runInTransaction(any())).thenReturn(StatusPagamento.PENDENTE.toString());

        // Act
        ResponseEntity<String> response = pagamentoApiHandler.confirmacaoPagamento(param);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(StatusPagamento.PENDENTE.toString(), response.getBody());
    }

    @Test
    public void testConfirmacaoPagamento_IdNulo() throws Exception {
        // Arrange
        ConfirmacaoPagamentoDto param = new ConfirmacaoPagamentoDto(null);
        // Act
        ResponseEntity<String> response = pagamentoApiHandler.confirmacaoPagamento(param);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testConsultarPagamento_Sucesso() throws Exception {

        Integer idPedido = 1;
        IdFormaPagamento formaPagamento = IdFormaPagamento.CARTAO_MAQUINA;
        ValorMonetario valor = new ValorMonetario("100");
        LocalDateTime dataHora = LocalDateTime.now();
        long dataHoraEmMilissegundos = dataHora.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        String codigoPagamentoCliente = "123456";
        String idPedidoSistemaExterno = "123";
        Pagamento pagamento = new Pagamento("233", idPedido, formaPagamento, StatusPagamento.FINALIZADO, valor, dataHora, dataHora, codigoPagamentoCliente, idPedidoSistemaExterno);


        when(pagamentoController.consultarPagamento(1)).thenReturn(pagamento);

        // Act
        ResponseEntity<PagamentoDto> response = pagamentoApiHandler.consultarPagamento("1");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());

        PagamentoDto pagamentoResponse = response.getBody();

        assertEquals(233, pagamentoResponse.id());
        assertEquals(IdFormaPagamento.CARTAO_MAQUINA.toString(), pagamentoResponse.formaPagamento());
        assertEquals(idPedido, pagamentoResponse.idPedido());
        assertEquals(dataHoraEmMilissegundos, pagamentoResponse.dataHoraCriacao());
        assertEquals(dataHoraEmMilissegundos, pagamentoResponse.dataHoraAtualizacao());
        assertEquals(codigoPagamentoCliente, pagamentoResponse.codigoPagamentoCliente());
        assertEquals(idPedidoSistemaExterno, pagamentoResponse.idPedidoSistemaExterno());
        assertEquals(StatusPagamento.FINALIZADO.toString(), pagamentoResponse.status());
    }

    @Test
    public void testConsultarPagamento_IdNull() throws Exception {

        // Act
        ResponseEntity<PagamentoDto> response = pagamentoApiHandler.consultarPagamento(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testConsultarQrCode_Sucesso() throws Exception {

        Integer idPedido = 1;
        IdFormaPagamento formaPagamento = IdFormaPagamento.CARTAO_MAQUINA;
        ValorMonetario valor = new ValorMonetario("100");
        LocalDateTime dataHora = LocalDateTime.now();
        long dataHoraEmMilissegundos = dataHora.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        String codigoPagamentoCliente = "123456";
        String idPedidoSistemaExterno = "123";
        Pagamento pagamento = new Pagamento("233", idPedido, formaPagamento, StatusPagamento.FINALIZADO, valor, dataHora, dataHora, codigoPagamentoCliente, idPedidoSistemaExterno);


        when(pagamentoController.consultarPagamento(1)).thenReturn(pagamento);

        // Act
        ResponseEntity<byte[]> response = pagamentoApiHandler.consultarPagamentoQrCode("1");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    public void testConsultarQrCode_IdNull() throws Exception {

        // Act
        ResponseEntity<byte[]> response = pagamentoApiHandler.consultarPagamentoQrCode(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


}