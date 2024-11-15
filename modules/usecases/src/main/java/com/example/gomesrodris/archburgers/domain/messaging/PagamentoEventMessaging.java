package com.example.gomesrodris.archburgers.domain.messaging;

import com.example.gomesrodris.archburgers.domain.entities.Pagamento;

public interface PagamentoEventMessaging {
    void notificarStatusPagamento(Pagamento pagamento);
}
