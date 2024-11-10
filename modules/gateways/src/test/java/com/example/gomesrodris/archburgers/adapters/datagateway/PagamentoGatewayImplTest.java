package com.example.gomesrodris.archburgers.adapters.datagateway;//import static org.junit.jupiter.api.Assertions.*;

import com.example.gomesrodris.archburgers.domain.datagateway.PagamentoGateway;
import com.example.gomesrodris.archburgers.domain.datasource.PagamentoDataSource;
import com.example.gomesrodris.archburgers.domain.entities.Pagamento;
import com.example.gomesrodris.archburgers.domain.valueobjects.IdFormaPagamento;
import com.example.gomesrodris.archburgers.domain.valueobjects.ValorMonetario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagamentoGatewayImplTest {

    private PagamentoGateway pagamentoGateway;
    private final PagamentoDataSource pagamentoDataSource = mock(PagamentoDataSource.class);

    @BeforeEach
    void setUp() {
        pagamentoGateway = new PagamentoGatewayImpl(pagamentoDataSource);
    }

    @Test
    void findPagamentoByPedido(){

        Integer idPedido = 1;
        IdFormaPagamento formaPagamento = IdFormaPagamento.CARTAO_MAQUINA;
        ValorMonetario valor = new ValorMonetario("100");
        LocalDateTime dataHora = LocalDateTime.now();
        String codigoPagamentoCliente = "1111";
        String idPedidoSistemaExterno = "123";

        Pagamento pagamento = Pagamento.registroInicial(idPedido, formaPagamento, valor, dataHora, codigoPagamentoCliente, idPedidoSistemaExterno);

        when(pagamentoGateway.findPagamentoByPedido(1)).thenReturn(pagamento);

        assertThat(pagamentoGateway.findPagamentoByPedido(1).codigoPagamentoCliente()).isEqualTo("1111");
    }

    @Test
    void excluirPagamentoByPedido(){
        pagamentoGateway.excluirPagamento(1);
    }

    @Test
    void updatePagamentoByPedido(){
        Integer idPedido = 1;
        IdFormaPagamento formaPagamento = IdFormaPagamento.CARTAO_MAQUINA;
        ValorMonetario valor = new ValorMonetario("100");
        LocalDateTime dataHora = LocalDateTime.now();
        String codigoPagamentoCliente = "1111";
        String idPedidoSistemaExterno = "123";

        Pagamento pagamento = Pagamento.registroInicial(idPedido, formaPagamento, valor, dataHora, codigoPagamentoCliente, idPedidoSistemaExterno);

        pagamentoGateway.updateStatus(pagamento);
    }

}