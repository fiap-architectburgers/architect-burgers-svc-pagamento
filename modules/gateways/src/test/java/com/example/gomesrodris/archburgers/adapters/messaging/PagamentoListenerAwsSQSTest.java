package com.example.gomesrodris.archburgers.adapters.messaging;

import com.example.gomesrodris.archburgers.controller.PagamentoController;
import com.example.gomesrodris.archburgers.domain.entities.Pedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PagamentoListenerAwsSQSTest {

    private PagamentoListenerAwsSQS pagamentoListenerAws;
    private final AwsSQSApi awsSQSApi = mock(AwsSQSApi.class);
    private final PagamentoController pagamentoController = mock(PagamentoController.class);

    @BeforeEach
    void setUp() {
        pagamentoListenerAws = new PagamentoListenerAwsSQS(awsSQSApi, pagamentoController);
    }

    @Test
    void processarPagamentosEmAberto(){

        List<Message> messageList = new ArrayList<>();
        Message message = Message.builder().messageId("message-id").body("{\n" +
                "    \"id\": 36,\n" +
                "    \"idClienteIdentificado\": 123,\n" +
                "    \"itens\": [\n" +
                "      {\n" +
                "        \"numSequencia\": 1,\n" +
                "        \"itemCardapio\": {\n" +
                "          \"id\": 101,\n" +
                "          \"tipo\": \"LANCHE\",\n" +
                "          \"nome\": \"Bife a milanesa\",\n" +
                "          \"descricao\": \"Bife empanado com batata frita\",\n" +
                "          \"valor\": \"25.99\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"numSequencia\": 2,\n" +
                "        \"itemCardapio\": {\n" +
                "          \"id\": 102,\n" +
                "          \"tipo\": \"BEBIDA\",\n" +
                "          \"nome\": \"Coca-Cola\",\n" +
                "          \"descricao\": \"Lata 350ml\",\n" +
                "          \"valor\": \"5.00\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"numSequencia\": 3,\n" +
                "        \"itemCardapio\": {\n" +
                "          \"id\": 103,\n" +
                "          \"tipo\": \"SOBREMESA\",\n" +
                "          \"nome\": \"Pudim\",\n" +
                "          \"descricao\": \"Pudim de leite\",\n" +
                "          \"valor\": \"6.99\"\n" +
                "        }\n" +
                "      }\n" +
                "    ],\n" +
                "    \"observacoes\": \"Sem sal\",\n" +
                "    \"status\": \"PAGAMENTO\",\n" +
                "    \"formaPagamento\": \"MERCADO_PAGO\",\n" +
                "    \"dataHoraPedido\": \"2024-10-30T23:50:00Z\"\n" +
                " }").build();

        messageList.add(message);

        Mockito.when(awsSQSApi.receiveMessages(any())).thenReturn(messageList);

        pagamentoListenerAws.processarPagamentosEmAberto();

        verify(pagamentoController).iniciarPagamento(Mockito.any(Pedido.class));
        verify(awsSQSApi).deleteMessageFromQueue(any(), eq(message));

        //Mockito.doNothing().when(pagamentoController.iniciarPagamento(any()));

        //Mockito.doNothing().when(pagamentoController.iniciarPagamento(any()));
//        awsSQSApi.deleteMessageFromQueue(any(), message);
//        Mockito.doNothing().when();



    }
}
