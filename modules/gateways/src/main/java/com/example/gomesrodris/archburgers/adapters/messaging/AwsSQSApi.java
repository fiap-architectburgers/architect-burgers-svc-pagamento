package com.example.gomesrodris.archburgers.adapters.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.net.URI;
import java.util.List;

@Service
public class AwsSQSApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(AwsSQSApi.class);

    private String sqsEndpoint;
    private String pedidosQueueName;
    private String pedidosQueueUrl;
    private String pagamentosConcluidosQueueName;
    private String pagamentosConcluidosQueueUrl;

    @Autowired
    public AwsSQSApi(Environment environment) {
        this.sqsEndpoint = environment.getProperty("archburgers.integration.sqs.sqsEndpoint");
        this.pedidosQueueName = environment.getProperty("archburgers.integration.sqs.pedidosQueueName");
        this.pedidosQueueUrl = environment.getProperty("archburgers.integration.sqs.pedidosQueueUrl");
        this.pagamentosConcluidosQueueName = environment.getProperty("archburgers.integration.sqs.pagamentosConcluidosQueueName");
        this.pagamentosConcluidosQueueUrl = environment.getProperty("archburgers.integration.sqs.pagamentosConcluidosQueueUrl");
    }

    public void sendMessage(String queueName, String queueUrl, String message) {
        try {
            SqsClient sqsClient = SqsClient.builder()
                    .region(Region.US_EAST_1)
                    .endpointOverride(URI.create(sqsEndpoint))
                    .build();

            // Create queue if it doesn't exist
            CreateQueueRequest createRequest = CreateQueueRequest.builder()
                    .queueName(queueName)
                    .build();
            sqsClient.createQueue(createRequest);

            // Send message
            SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(message)
                    .delaySeconds(5)
                    .build();
            sqsClient.sendMessage(sendMsgRequest);

            System.out.println("Message sent successfully!");
        } catch (SqsException e) {
            throw e;
        }
    }

    public List<Message> receiveMessages(String queueUrl) {

        System.out.println("\nReceive messages");

        SqsClient sqsClient = SqsClient.builder()
                .region(Region.US_EAST_1)
                .endpointOverride(URI.create(sqsEndpoint))
                .build();

        try {
            ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .maxNumberOfMessages(10)
                    .build();
            return sqsClient.receiveMessage(receiveMessageRequest).messages();

        } catch (SqsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return null;
    }

    public void deleteMessageFromQueue(String queueUrl, Message message){

        System.out.println("\nDelete message - " + message.body());

        SqsClient sqsClient = SqsClient.builder()
                .region(Region.US_EAST_1)
                .endpointOverride(URI.create(sqsEndpoint))
                .build();

        String receiptHandle = message.receiptHandle();

        // Delete the message from the queue
        DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
                .queueUrl(queueUrl)
                .receiptHandle(receiptHandle)
                .build();
        sqsClient.deleteMessage(deleteMessageRequest);

        System.out.println("\nMessage deleted");
    }

    public String getPagamentosEmAbertoQueueName() {
        return pedidosQueueName;
    }

    public void setPagamentosEmAbertoQueueName(String pedidosQueueName) {
        this.pedidosQueueName = pedidosQueueName;
    }

    public String getPedidosQueueUrl() {
        return pedidosQueueUrl;
    }

    public void setPedidosQueueUrl(String pedidosQueueUrl) {
        this.pedidosQueueUrl = pedidosQueueUrl;
    }

    public String getPagamentosConcluidosQueueName() {
        return pagamentosConcluidosQueueName;
    }

    public void setPagamentosConcluidosQueueName(String pagamentosConcluidosQueueName) {
        this.pagamentosConcluidosQueueName = pagamentosConcluidosQueueName;
    }

    public String getPagamentosConcluidosQueueUrl() {
        return pagamentosConcluidosQueueUrl;
    }

    public void setPagamentosConcluidosQueueUrl(String pagamentosConcluidosQueueUrl) {
        this.pagamentosConcluidosQueueUrl = pagamentosConcluidosQueueUrl;
    }
}
