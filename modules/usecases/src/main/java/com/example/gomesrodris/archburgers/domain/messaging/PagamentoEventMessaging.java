package com.example.gomesrodris.archburgers.domain.messaging;

import com.example.gomesrodris.archburgers.domain.entities.Pagamento;
import com.example.gomesrodris.archburgers.domain.entities.Pedido;

import java.util.List;

public interface PagamentoEventMessaging {
    void notificarStatusPagamento(Pagamento pagamento);

    List<Pedido> verificarPedidosComPagamentoEmAberto();
}
