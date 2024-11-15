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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AwsSQSApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(AwsSQSApi.class);

    private String sqsEndpoint;
    private String pagamentosEmAbertoQueueName;
    private String pagamentosEmAbertoQueueUrl;
    private String pagamentosConcluidosQueueName;
    private String pagamentosConcluidosQueueUrl;

    @Autowired
    public AwsSQSApi(Environment environment) {
        this.sqsEndpoint = environment.getProperty("archburgers.integration.sqs.sqsEndpoint");
        this.pagamentosEmAbertoQueueName = environment.getProperty("archburgers.integration.sqs.pagamentosEmAbertoQueueName");
        this.pagamentosEmAbertoQueueUrl = environment.getProperty("archburgers.integration.sqs.pagamentosEmAbertoQueueUrl");
        this.pagamentosConcluidosQueueName = environment.getProperty("archburgers.integration.sqs.pagamentosConcluidosQueueName");
        this.pagamentosConcluidosQueueUrl = environment.getProperty("archburgers.integration.sqs.pagamentosConcluidosQueueUrl");

        this.sqsEndpoint = Objects.requireNonNull(sqsEndpoint, "archburgers.integration.sqs.sqsEndpoint not set");
        this.pagamentosEmAbertoQueueName = Objects.requireNonNull(pagamentosEmAbertoQueueName, "archburgers.integration.sqs.pagamentosEmAbertoQueueName not set");
        this.pagamentosEmAbertoQueueUrl = Objects.requireNonNull(pagamentosEmAbertoQueueUrl, "archburgers.integration.sqs.pagamentosEmAbertoQueueUrl not set");
        this.pagamentosConcluidosQueueName = Objects.requireNonNull(pagamentosConcluidosQueueName, "archburgers.integration.sqs.pagamentosConcluidosQueueName not set");
        this.pagamentosConcluidosQueueUrl = Objects.requireNonNull(pagamentosConcluidosQueueUrl, "archburgers.integration.sqs.pagamentosConcluidosQueueUrl not set");
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

            LOGGER.info("Body - " + message);
            LOGGER.info("Message sent successfully!");

        } catch (SqsException e) {
            LOGGER.error(e.getMessage());
            System.exit(1);
        }
    }

    public List<Message> receiveMessages(String queueUrl) {

        LOGGER.info("\nReceive messages");

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
            LOGGER.error(e.awsErrorDetails().errorMessage());
            System.exit(1);
            return new ArrayList<Message>();
        }

    }

    public void deleteMessageFromQueue(String queueUrl, Message message){

        LOGGER.info("Delete message - " + message.body());

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

        LOGGER.info("\nMessage deleted");
    }

    public String getPagamentosEmAbertoQueueName() {
        return pagamentosEmAbertoQueueName;
    }

    public String getPagamentosEmAbertoQueueUrl() {
        return pagamentosEmAbertoQueueUrl;
    }

    public String getPagamentosConcluidosQueueName() {
        return pagamentosConcluidosQueueName;
    }

    public String getPagamentosConcluidosQueueUrl() {
        return pagamentosConcluidosQueueUrl;
    }


}
