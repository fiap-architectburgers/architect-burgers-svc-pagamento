package com.example.gomesrodris.archburgers.adapters.datagateway;

import com.example.gomesrodris.archburgers.domain.datagateway.PagamentoGateway;
import com.example.gomesrodris.archburgers.domain.datasource.PagamentoDataSource;
import com.example.gomesrodris.archburgers.domain.entities.Pagamento;
import com.example.gomesrodris.archburgers.domain.entities.Pedido;
import com.example.gomesrodris.archburgers.domain.messaging.PagamentoEventMessaging;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PagamentoGatewayImpl implements PagamentoGateway {
    private final PagamentoDataSource pagamentoDataSource;
    private final PagamentoEventMessaging pagamentoEventMessaging;

    public PagamentoGatewayImpl(PagamentoDataSource pagamentoDataSource, PagamentoEventMessaging pagamentoEventMessaging) {
        this.pagamentoDataSource = pagamentoDataSource;
        this.pagamentoEventMessaging = pagamentoEventMessaging;
    }

    @Override
    public Pagamento findPagamentoByPedido(Integer idPedido) {
        return pagamentoDataSource.findPagamentoByPedido(idPedido);
    }

    @Override
    public Pagamento salvarPagamento(Pagamento pagamento) {
        return pagamentoDataSource.salvarPagamento(pagamento);
    }

    @Override
    public void updateStatus(Pagamento pagamento) {
        pagamentoDataSource.updateStatus(pagamento);
    }

    @Override
    public void excluirPagamento(Integer idPedido) {
        pagamentoDataSource.deletePagamentoByIdPedido(idPedido);
    }


    @Override
    public List<Pedido> verificarPedidosComPagamentoEmAberto() {
        return this.pagamentoEventMessaging.verificarPedidosComPagamentoEmAberto();
    }

    @Override
    public void notificarStatusPagamento(Pagamento pagamento) {
        this.pagamentoEventMessaging.notificarStatusPagamento(pagamento);
    }

}
