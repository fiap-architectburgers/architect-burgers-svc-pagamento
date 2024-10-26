package com.example.gomesrodris.archburgers.controller;

import com.example.gomesrodris.archburgers.domain.datagateway.ItemCardapioGateway;
import com.example.gomesrodris.archburgers.domain.datagateway.PagamentoGateway;
//import com.example.gomesrodris.archburgers.domain.datagateway.PedidoGateway;
import com.example.gomesrodris.archburgers.domain.entities.Pagamento;
import com.example.gomesrodris.archburgers.domain.entities.Pedido;
import com.example.gomesrodris.archburgers.domain.external.FormaPagamentoRegistry;
import com.example.gomesrodris.archburgers.domain.usecaseparam.DescricaoFormaPagamento;
import com.example.gomesrodris.archburgers.domain.usecases.PagamentoUseCases;
import com.example.gomesrodris.archburgers.domain.utils.Clock;
import com.example.gomesrodris.archburgers.domain.valueobjects.IdFormaPagamento;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

public class PagamentoController {
    private final PagamentoUseCases pagamentoUseCases;

    public PagamentoController(FormaPagamentoRegistry formaPagamentoRegistry,
                               PagamentoGateway pagamentoGateway,
//                               PedidoGateway pedidoGateway,

                               Clock clock) {
        pagamentoUseCases = new PagamentoUseCases(formaPagamentoRegistry, pagamentoGateway,
                clock);
    }

    public IdFormaPagamento validarFormaPagamento(String idFormaPagamento) {
        return pagamentoUseCases.validarFormaPagamento(idFormaPagamento);
    }

    public Pagamento iniciarPagamento(Pedido pedido) {
        return pagamentoUseCases.iniciarPagamento(pedido);
    }

    /**
     * @param idPedido
     * @param idPedidoSistemaExterno Opcional, caso a forma de pagamento utilizada forneça esta informação no final
     */
    public String finalizarPagamento(int idPedido, String idPedidoSistemaExterno) {
        return pagamentoUseCases.finalizarPagamento(idPedido, idPedidoSistemaExterno);
    }

    public List<DescricaoFormaPagamento> listarFormasPagamento() {
        return pagamentoUseCases.listarFormasPagamento();
    }

    public Pagamento consultarPagamento(int idPedido) {
        return pagamentoUseCases.consultarPagamento(idPedido);
    }

    @Scheduled(fixedRate = 10000)
    public void processarPagamentos(){
        System.out.println("processarPagamentos");
        System.out.println("chamar use case iniciarPagamento ou finalizar pagamento");
        pagamentoUseCases.processarPagamentos();
    }

}
