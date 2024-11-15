package com.example.gomesrodris.archburgers.adapters.messaging;

import com.example.gomesrodris.archburgers.domain.datagateway.PagamentoEventMessagingGateway;
import com.example.gomesrodris.archburgers.domain.entities.Pagamento;
import com.example.gomesrodris.archburgers.domain.messaging.PagamentoEventMessaging;
import org.springframework.stereotype.Service;


public class PagamentoEventMessagingGatewayImpl implements PagamentoEventMessagingGateway {

    private final PagamentoEventMessaging pagamentoEventMessaging;

    public PagamentoEventMessagingGatewayImpl(PagamentoEventMessaging pagamentoEventMessaging) {
        this.pagamentoEventMessaging = pagamentoEventMessaging;
    }

    @Override
    public void notificarStatusPagamento(Pagamento pagamento) {
        this.pagamentoEventMessaging.notificarStatusPagamento(pagamento);
    }
}
