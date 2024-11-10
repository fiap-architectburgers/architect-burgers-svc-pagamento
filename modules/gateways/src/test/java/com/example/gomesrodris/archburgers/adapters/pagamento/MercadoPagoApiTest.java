package com.example.gomesrodris.archburgers.adapters.pagamento;

import com.example.gomesrodris.archburgers.testUtils.StaticEnvironment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.postgresql.hostchooser.HostRequirement.any;

@ExtendWith(MockitoExtension.class)
class MercadoPagoApiTest {

//    @Mock
//    HttpClient httpClient;

    @Test
    void validarVariaveisDeAmbiente(){
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
    void postOrder(){
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
            mercadoPagoApi.getOrder("url","");
        }).hasMessageContaining("URI with undefined scheme");


        assertThatThrownBy(() -> {
            env.put("archburgers.integration.mercadopago.apiBaseUrl", "https://api.mercadopago.com");
            MercadoPagoApi mercadoPagoApi = new MercadoPagoApi(new StaticEnvironment(env));
            mercadoPagoApi.getOrder("https://api.mercadopago.com","");
        }).hasMessageContaining("Error in order request");

//        try {
//            Mockito.when(httpClient.send(any(), any())).thenReturn(getResponse());
//
//            env.put("archburgers.integration.mercadopago.apiBaseUrl", "https://api.mercadopago.com");
//            MercadoPagoApi mercadoPagoApi = new MercadoPagoApi(new StaticEnvironment(env));
//
//
//            assertThat(mercadoPagoApi.getOrder("https://api.mercadopago.com","").values().size()).isEqualTo(2);
//
//        }catch (IOException ioException){
//
//        } catch (InterruptedException interruptedException){
//
//        }
    }

//    private HttpResponse getResponse(){
//        HttpResponse response = new HttpResponse<>() {
//            @Override
//            public int statusCode() {
//                return 200;
//            }
//
//            @Override
//            public HttpRequest request() {
//                return null;
//            }
//
//            @Override
//            public Optional<HttpResponse<Object>> previousResponse() {
//                return Optional.empty();
//            }
//
//            @Override
//            public HttpHeaders headers() {
//                return null;
//            }
//
//            @Override
//            public Object body() {
//
//                Map<String, Object> data = new HashMap<>();
//                data.put("index1", "valor1");
//                data.put("index2", "valor2");
//
//                ObjectMapper mapper = new ObjectMapper();
//                String payload = "";
//                try {
//                    payload = mapper.writeValueAsString(data);
//                } catch (JsonProcessingException e) {
//                    e.printStackTrace();
//                }
//                return payload;
//            }
//
//            @Override
//            public Optional<SSLSession> sslSession() {
//                return Optional.empty();
//            }
//
//            @Override
//            public URI uri() {
//                return null;
//            }
//
//            @Override
//            public HttpClient.Version version() {
//                return null;
//            }
//        };
//
//        return response;
//    }
}
