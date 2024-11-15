package com.example.gomesrodris.archburgers.adapters.datagateway;//import static org.junit.jupiter.api.Assertions.*;

import com.example.gomesrodris.archburgers.domain.datagateway.PagamentoGateway;
import com.example.gomesrodris.archburgers.domain.datasource.PagamentoDataSource;
import com.example.gomesrodris.archburgers.domain.entities.Pagamento;
import com.example.gomesrodris.archburgers.domain.valueobjects.IdFormaPagamento;
import com.example.gomesrodris.archburgers.domain.valueobjects.StatusPagamento;
import com.example.gomesrodris.archburgers.domain.valueobjects.ValorMonetario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
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

    @Test
    void salvarPagamento(){
        Integer idPedido = 1;
        IdFormaPagamento formaPagamento = IdFormaPagamento.CARTAO_MAQUINA;
        ValorMonetario valor = new ValorMonetario("100");
        LocalDateTime dataHora = LocalDateTime.now();
        LocalDateTime dataHoraAtualizacao = LocalDateTime.now();
        String codigoPagamentoCliente = "1111";
        String idPedidoSistemaExterno = "123";

        Pagamento pagamento = Pagamento.registroInicial(idPedido, formaPagamento, valor, dataHora, codigoPagamentoCliente, idPedidoSistemaExterno);


        Pagamento pagamentoSalvo = new Pagamento(
                "id001",
                idPedido,
                formaPagamento,
                StatusPagamento.PENDENTE,
                valor,
                dataHora,
                dataHoraAtualizacao,
                codigoPagamentoCliente,
                idPedidoSistemaExterno);

        when(pagamentoGateway.salvarPagamento(pagamento)).thenReturn(pagamentoSalvo);

        Pagamento pagamentoResult = pagamentoGateway.salvarPagamento(pagamento);

        assertThat(pagamentoResult).isNotNull();
        assertThat(pagamentoResult.id()).isNotNull();
        assertThat(pagamentoResult.id()).isEqualTo("id001");
        assertThat(pagamentoResult.dataHoraAtualizacao()).isEqualTo(dataHoraAtualizacao);

    }

    @Test
    void salvarPagamentoComNull(){
        Pagamento pagamentoResult = pagamentoGateway.salvarPagamento(null);
        assertThat(pagamentoResult).isNull();
    }

    @Test
    void findPagamentoByPedidoNull(){
        assertThat(pagamentoGateway.findPagamentoByPedido(null)).isNull();
    }

    @Test
    void findPagamentoByPedidoCodigoInvalido(){
        assertThat(pagamentoGateway.findPagamentoByPedido(123)).isNull();
    }

}