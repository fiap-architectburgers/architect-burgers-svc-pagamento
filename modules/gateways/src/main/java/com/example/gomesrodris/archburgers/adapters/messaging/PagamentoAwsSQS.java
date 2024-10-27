package com.example.gomesrodris.archburgers.adapters.messaging;

import com.example.gomesrodris.archburgers.domain.entities.Pagamento;
import com.example.gomesrodris.archburgers.domain.entities.Pedido;
import com.example.gomesrodris.archburgers.domain.messaging.PagamentoEventMessaging;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.ArrayList;
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
        List<Pedido> pedidos = new ArrayList<Pedido>();
        try {
            List<Message> messages = awsSQSApi.receiveMessages(awsSQSApi.getPedidosQueueUrl());

            for (Message message : messages) {
                String messageBody = message.body();
                System.out.println("Received message: " + messageBody);

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
                objectMapper.registerModule(new JavaTimeModule()); // Para lidar com LocalDateTime

                // Deserializar o JSON para o objeto Pedido
                Pedido pedido = objectMapper.readValue(messageBody, Pedido.class);
                pedidos.add(pedido);
                awsSQSApi.deleteMessageFromQueue(awsSQSApi.getPedidosQueueUrl(), message);
            }
        } catch(JsonProcessingException e) {
            System.out.println(e);
            return null;
        }
        return pedidos;
    }


}
