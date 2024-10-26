package com.example.gomesrodris.archburgers.domain.datagateway;

import com.example.gomesrodris.archburgers.domain.entities.Pagamento;
import com.example.gomesrodris.archburgers.domain.entities.Pedido;

import java.util.List;

public interface PagamentoGateway {
    List<Pedido> verificarPedidosComPagamentoEmAberto();
    void notificarStatusPagamento(Pagamento pagamento);
    Pagamento findPagamentoByPedido(Integer idPedido);
    Pagamento salvarPagamento(Pagamento pagamento);
    void updateStatus(Pagamento pagamento);
    void excluirPagamento(Integer idPedido);
}
