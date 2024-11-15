package com.example.gomesrodris.archburgers.adapters.pagamento;

import com.example.gomesrodris.archburgers.testUtils.StaticEnvironment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class MercadoPagoApiTest {

//    @Mock
//    HttpClient httpClient;

    @Test
    void validarVariaveisDeAmbiente() {
        Map<String, String> env = new HashMap<>();

        assertThatThrownBy(() -> {
            new MercadoPagoApi(new StaticEnvironment(env));
        }).hasMessageContaining("archburgers.integration.mercadopago.apiBaseUrl not set");

        env.put("archburgers.integration.mercadopago.apiBaseUrl", "url");

        assertThatThrownBy(() -> {
            new MercadoPagoApi(new StaticEnvironment(env));
        }).hasMessageContaining("archburgers.integration.mercadopago.userId not set");

        env.put("archburgers.integration.mercadopago.userId", "userId");

        assertThatThrownBy(() -> {
            new MercadoPagoApi(new StaticEnvironment(env));
        }).hasMessageContaining("archburgers.integration.mercadopago.accessToken not set");

        env.put("archburgers.integration.mercadopago.accessToken", "token");

        assertThatThrownBy(() -> {
            new MercadoPagoApi(new StaticEnvironment(env));
        }).hasMessageContaining("archburgers.integration.mercadopago.posId not set");

        env.put("archburgers.integration.mercadopago.posId", "posId");

        assertThatThrownBy(() -> {
            new MercadoPagoApi(new StaticEnvironment(env));
        }).hasMessageContaining("archburgers.integration.mercadopago.notificationUrl not set");

    }

    @Test
    void postOrder() {
        Map<String, String> env = new HashMap<>();

        env.put("archburgers.integration.mercadopago.userId", "userId");
        env.put("archburgers.integration.mercadopago.accessToken", "token");
        env.put("archburgers.integration.mercadopago.posId", "posId");
        env.put("archburgers.integration.mercadopago.notificationUrl", "notificationUrl");


        Map<String, Object> data = new HashMap<>();
        data.put("index1", "valor1");
        data.put("index2", "valor2");


        assertThatThrownBy(() -> {
            env.put("archburgers.integration.mercadopago.apiBaseUrl", "url");
            MercadoPagoApi mercadoPagoApi = new MercadoPagoApi(new StaticEnvironment(env));
            mercadoPagoApi.postOrder(data);
        }).hasMessageContaining("URI with undefined scheme");

        assertThatThrownBy(() -> {
            env.put("archburgers.integration.mercadopago.apiBaseUrl", "https://api.mercadopago.com");
            MercadoPagoApi mercadoPagoApi = new MercadoPagoApi(new StaticEnvironment(env));
            Map<String, Object> dataNull = new HashMap<>();
            dataNull.put(null, null);
            mercadoPagoApi.postOrder(dataNull);
        }).hasMessageContaining("Error creating payload");

        assertThatThrownBy(() -> {
            env.put("archburgers.integration.mercadopago.apiBaseUrl", "https://api.mercadopago.com");
            MercadoPagoApi mercadoPagoApi = new MercadoPagoApi(new StaticEnvironment(env));
            mercadoPagoApi.postOrder(data);
        }).hasMessageContaining("Error in order request");
    }

    @Test
    void getOrder() {
        Map<String, String> env = new HashMap<>();
        env.put("archburgers.integration.mercadopago.userId", "userId");
        env.put("archburgers.integration.mercadopago.accessToken", "token");
        env.put("archburgers.integration.mercadopago.posId", "posId");
        env.put("archburgers.integration.mercadopago.notificationUrl", "notificationUrl");

        assertThatThrownBy(() -> {
            env.put("archburgers.integration.mercadopago.apiBaseUrl", "url");
            MercadoPagoApi mercadoPagoApi = new MercadoPagoApi(new StaticEnvironment(env));
            mercadoPagoApi.getOrder("url", "");
        }).hasMessageContaining("URI with undefined scheme");


        assertThatThrownBy(() -> {
            env.put("archburgers.integration.mercadopago.apiBaseUrl", "https://api.mercadopago.com");
            MercadoPagoApi mercadoPagoApi = new MercadoPagoApi(new StaticEnvironment(env));
            mercadoPagoApi.getOrder("https://api.mercadopago.com", "");
        }).hasMessageContaining("Error in order request");

    }


}
