package com.example.gomesrodris.archburgers.domain.datagateway;

import com.example.gomesrodris.archburgers.domain.entities.Pagamento;

public interface PagamentoEventMessagingGateway {
    void notificarStatusPagamento(Pagamento pagamento);
}
