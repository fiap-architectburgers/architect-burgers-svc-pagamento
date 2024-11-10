package com.example.gomesrodris.archburgers.adapters.messaging;

import com.example.gomesrodris.archburgers.adapters.pagamento.MercadoPagoApi;
import com.example.gomesrodris.archburgers.adapters.pagamento.MercadoPagoGateway;
import com.example.gomesrodris.archburgers.domain.entities.ItemCardapio;
import com.example.gomesrodris.archburgers.domain.entities.ItemPedido;
import com.example.gomesrodris.archburgers.domain.entities.Pedido;
import com.example.gomesrodris.archburgers.domain.valueobjects.*;
import com.example.gomesrodris.archburgers.testUtils.StaticEnvironment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class AwsSQSApiTest {

    @Test
    void validarVariaveisDeAmbiente(){
        Map<String, String> env = new HashMap<>();

        assertThatThrownBy(() -> {
            new AwsSQSApi(new StaticEnvironment(env));
        }).hasMessageContaining("archburgers.integration.sqs.sqsEndpoint not set");

        env.put("archburgers.integration.sqs.sqsEndpoint", "http://localhost:4566");

        assertThatThrownBy(() -> {
            new AwsSQSApi(new StaticEnvironment(env));
        }).hasMessageContaining("archburgers.integration.sqs.pagamentosEmAbertoQueueName not set");

        env.put("archburgers.integration.sqs.pagamentosEmAbertoQueueName", "pedidos");

        assertThatThrownBy(() -> {
            new AwsSQSApi(new StaticEnvironment(env));
        }).hasMessageContaining("archburgers.integration.sqs.pagamentosEmAbertoQueueUrl not set");

        env.put("archburgers.integration.sqs.pagamentosEmAbertoQueueUrl", "http://localhost:4566/000000000000/pedidos");

        assertThatThrownBy(() -> {
            new AwsSQSApi(new StaticEnvironment(env));
        }).hasMessageContaining("archburgers.integration.sqs.pagamentosConcluidosQueueName not set");

        env.put("archburgers.integration.sqs.pagamentosConcluidosQueueName", "pagamentos_concluidos");

        assertThatThrownBy(() -> {
            new AwsSQSApi(new StaticEnvironment(env));
        }).hasMessageContaining("archburgers.integration.sqs.pagamentosConcluidosQueueUrl not set");

        env.put("archburgers.integration.sqs.pagamentosConcluidosQueueUrl", "http://localhost:4566/000000000000/pagamentos_concluidos");

        AwsSQSApi awsSQSApi = new AwsSQSApi(new StaticEnvironment(env));
        Assertions.assertThat(awsSQSApi.getPagamentosEmAbertoQueueName()).isEqualTo("pedidos");
        Assertions.assertThat(awsSQSApi.getPagamentosEmAbertoQueueUrl()).isEqualTo("http://localhost:4566/000000000000/pedidos");
        Assertions.assertThat(awsSQSApi.getPagamentosConcluidosQueueName()).isEqualTo("pagamentos_concluidos");
        Assertions.assertThat(awsSQSApi.getPagamentosConcluidosQueueUrl()).isEqualTo("http://localhost:4566/000000000000/pagamentos_concluidos");
    }

}
