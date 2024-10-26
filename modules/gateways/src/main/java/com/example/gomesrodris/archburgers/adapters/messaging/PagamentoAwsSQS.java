package com.example.gomesrodris.archburgers.adapters.messaging;

import com.example.gomesrodris.archburgers.domain.entities.Pagamento;
import com.example.gomesrodris.archburgers.domain.entities.Pedido;
import com.example.gomesrodris.archburgers.domain.messaging.PagamentoEventMessaging;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.List;

/**
 * Implementation of the Repository based on AWS SQS service
 */
@Repository
public class PagamentoAwsSQS implements PagamentoEventMessaging {

    private final AwsSQSApi awsSQSApi;

    public PagamentoAwsSQS(AwsSQSApi awsSQSApi) {
        this.awsSQSApi = awsSQSApi;
    }

    @Override
    public void notificarStatusPagamento(Pagamento pagamento) {
        awsSQSApi.sendMessage(awsSQSApi.getPagamentosConcluidosQueueName(), awsSQSApi.getPagamentosConcluidosQueueUrl(), pagamento.toString());
    }

    @Override
    public List<Pedido> verificarPedidosComPagamentoEmAberto() {
        System.out.println("PagamentoAwsSQS - verificarPedidosComPagamentosEmAberto");
        List<Message> messages = awsSQSApi.receiveMessages(awsSQSApi.getPedidosQueueUrl());
        System.out.println("messages - " + messages.toString());
        return null;
    }


}
