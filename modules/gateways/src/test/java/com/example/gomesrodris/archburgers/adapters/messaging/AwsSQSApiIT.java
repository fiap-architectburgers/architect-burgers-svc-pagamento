package com.example.gomesrodris.archburgers.adapters.messaging;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.Environment;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SQS;

public class AwsSQSApiIT {
    private AwsSQSApi awsSQSApi;

    private static String pagamentosEmAbertoQueueUrl;
    private static String pagamentosConcluidosQueueUrl;

    private static LocalStackContainer localstack;

    @BeforeAll
    static void beforeAll() {
        DockerImageName localstackImage = DockerImageName.parse("localstack/localstack:3.5.0");

        localstack = new LocalStackContainer(localstackImage).withServices(SQS);
        localstack.start();

        try (SqsClient sqsClient = SqsClient.builder()
                .region(Region.US_EAST_1)
                .endpointOverride(localstack.getEndpoint())
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(localstack.getAccessKey(), localstack.getSecretKey())
                        )
                )
                .build()) {

            pagamentosEmAbertoQueueUrl = sqsClient.createQueue(CreateQueueRequest.builder().queueName("pagamentosEmAberto").build()).queueUrl();
            pagamentosConcluidosQueueUrl = sqsClient.createQueue(CreateQueueRequest.builder().queueName("pagamentos_concluidos").build()).queueUrl();

            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @AfterAll
    static void afterAll() {
        localstack.stop();
    }

    @BeforeEach
    void setUp() {
        System.setProperty("aws.accessKeyId", localstack.getAccessKey());
        System.setProperty("aws.secretAccessKey", localstack.getSecretKey());

        Environment environment = mock();
        when(environment.getProperty("archburgers.integration.sqs.sqsEndpoint")).thenReturn(localstack.getEndpoint().toString());
        when(environment.getProperty("archburgers.integration.sqs.pagamentosEmAbertoQueueName")).thenReturn("pagamentosEmAberto");
        when(environment.getProperty("archburgers.integration.sqs.pagamentosEmAbertoQueueUrl")).thenReturn(pagamentosEmAbertoQueueUrl);
        when(environment.getProperty("archburgers.integration.sqs.pagamentosConcluidosQueueName")).thenReturn("pagamentos_concluidos");
        when(environment.getProperty("archburgers.integration.sqs.pagamentosConcluidosQueueUrl")).thenReturn(pagamentosConcluidosQueueUrl);

        awsSQSApi = new AwsSQSApi(environment);
    }

    @Test
    void sendMessage() throws Exception {
        String testMessage = "Teste_" + System.currentTimeMillis();

        awsSQSApi.sendMessage("pagamentos_concluidos", pagamentosConcluidosQueueUrl, testMessage);

        Thread.sleep(1100L);

        try (SqsClient sqsClient = SqsClient.builder()
                .region(Region.US_EAST_1)
                .endpointOverride(localstack.getEndpoint())
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(localstack.getAccessKey(), localstack.getSecretKey())
                        )
                )
                .build()) {

            ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                    .queueUrl(pagamentosConcluidosQueueUrl)
                    .maxNumberOfMessages(10)
                    .build();

            List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).messages();

            assertThat(messages).hasSize(1);
            Message message = messages.getFirst();
            assertThat(message.body()).isEqualTo(testMessage);
        }
    }

    @Test
    public void receiveMessageQueueConfirmacao() throws InterruptedException {
        String testMessage1 = "Teste_1_" + System.currentTimeMillis();
        String testMessage2 = "Teste_2_" + System.currentTimeMillis();

        try (SqsClient sqsClient = SqsClient.builder()
                .region(Region.US_EAST_1)
                .endpointOverride(localstack.getEndpoint())
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(localstack.getAccessKey(), localstack.getSecretKey())
                        )
                )
                .build()) {

            for (String testMessage : List.of(testMessage1, testMessage2)) {
                SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                        .queueUrl(pagamentosEmAbertoQueueUrl)
                        .messageBody(testMessage)
                        .build();

                sqsClient.sendMessage(sendMessageRequest);
            }
            Thread.sleep(100L);
        }

        List<Message> messages = awsSQSApi.receiveMessages(pagamentosEmAbertoQueueUrl);

        assertThat(messages).hasSize(2);

        List<String> messageContents = messages.stream().map(Message::body).toList();
        assertThat(messageContents).containsExactlyInAnyOrder(testMessage1, testMessage2);

        messages.forEach(message -> awsSQSApi.deleteMessageFromQueue(pagamentosEmAbertoQueueUrl, message));
    }
}
