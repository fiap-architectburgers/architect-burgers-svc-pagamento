package com.example.gomesrodris.archburgers.adapters.messaging;

import com.example.gomesrodris.archburgers.domain.entities.Pagamento;
import com.example.gomesrodris.archburgers.domain.valueobjects.IdFormaPagamento;
import com.example.gomesrodris.archburgers.domain.valueobjects.ValorMonetario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PagamentoAwsSQSTest {

    private final AwsSQSApi awsSQSApi = mock(AwsSQSApi.class);
    private PagamentoAwsSQS pagamentoAwsSQS;

    @BeforeEach
    void setUp() {
        pagamentoAwsSQS = new PagamentoAwsSQS(awsSQSApi);
    }

    @Test
    void notificarStatusPagamento() {

        Integer idPedido = 1;
//        IdFormaPagamento formaPagamento = IdFormaPagamento.MERCADO_PAGO;
        IdFormaPagamento formaPagamento = new IdFormaPagamento("MERCADO_PAGO");
        ValorMonetario valor = new ValorMonetario("100");
        LocalDateTime dataHora = LocalDateTime.now();
        String codigoPagamentoCliente = "12345";
        String idPedidoSistemaExterno = "123";

        Pagamento pagamento = Pagamento.registroInicial(idPedido, formaPagamento, valor, dataHora, codigoPagamentoCliente, idPedidoSistemaExterno);

        when(awsSQSApi.getPagamentosConcluidosQueueName()).thenReturn("pagamentos-concluidos");
        when(awsSQSApi.getPagamentosConcluidosQueueUrl()).thenReturn("http://localhostxxx:8080");

        pagamentoAwsSQS.notificarStatusPagamento(pagamento);
        System.out.println(pagamento.getPagamentoJson().toString());
        verify(awsSQSApi).sendMessage(any(), any(), eq(pagamento.getPagamentoJson()));

    }
}
