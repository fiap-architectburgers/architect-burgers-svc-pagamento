package com.example.gomesrodris.archburgers.adapters.messaging;

import com.example.gomesrodris.archburgers.controller.PagamentoController;
import com.example.gomesrodris.archburgers.domain.entities.Pedido;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.List;

@Service
public class PagamentoListenerAwsSQS {
    private static final Logger LOGGER = LoggerFactory.getLogger(PagamentoListenerAwsSQS.class);
    private final AwsSQSApi awsSQSApi;
    private PagamentoController pagamentoController;

    public PagamentoListenerAwsSQS(AwsSQSApi awsSQSApi, PagamentoController pagamentoController) {

        this.awsSQSApi = awsSQSApi;
        this.pagamentoController = pagamentoController;
    }

    @Scheduled(fixedRate = 10000, initialDelay = 10000)
    public void processarPagamentosEmAberto() {
        LOGGER.debug("PagamentoListenerAwsSQS - processarPagamentosEmAberto");

        try {
            List<Message> messages = awsSQSApi.receiveMessages(awsSQSApi.getPedidosQueueUrl());

            for (Message message : messages) {
                String messageBody = message.body();
                LOGGER.debug("Received message: " + messageBody);

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
                objectMapper.registerModule(new JavaTimeModule()); // Para lidar com LocalDateTime

                // Deserializar o JSON para o objeto Pedido
                Pedido pedido = objectMapper.readValue(messageBody, Pedido.class);
                pagamentoController.iniciarPagamento(pedido);

                awsSQSApi.deleteMessageFromQueue(awsSQSApi.getPedidosQueueUrl(), message);
            }
        } catch(JsonProcessingException e) {
            LOGGER.error(e.getMessage());
        }
    }


}
