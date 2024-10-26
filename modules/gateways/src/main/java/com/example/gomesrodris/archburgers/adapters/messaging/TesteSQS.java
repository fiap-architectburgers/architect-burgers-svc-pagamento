package com.example.gomesrodris.archburgers.adapters.messaging;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SqsException;

import java.net.URI;

public class TesteSQS {

    public static void main(String[] args) {

        String queueName = "pagamentos_em_aberto"; // Replace with your actual queue name
        String message = "alana";
        SqsClient sqsClient = SqsClient.builder()
                .region(Region.US_EAST_1)
                .endpointOverride(URI.create("http://localhost:4566"))
                .build();



        try {
            sendMessage(sqsClient, queueName, message);
        } catch (SqsException e) {
            System.err.println("Error sending message: " + e.awsErrorDetails().errorMessage());
        }
        System.exit(1);
    }



    public static void sendMessage(SqsClient sqsClient, String queueName, String message) {
        try {


            // Create queue if it doesn't exist
            CreateQueueRequest createRequest = CreateQueueRequest.builder()
                    .queueName(queueName)
                    .build();
            sqsClient.createQueue(createRequest);

            // Get queue URL
            GetQueueUrlRequest getQueueRequest = GetQueueUrlRequest.builder()
                    .queueName(queueName)
                    .build();
//            String queueUrl = sqsClient.getQueueUrl(getQueueRequest).queueUrl();
//
            // Send message
            SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                    .queueUrl("http://localhost:4566/000000000000/pagamentos_em_aberto")
                    .messageBody(message)
                    .delaySeconds(5)
                    .build();
            sqsClient.sendMessage(sendMsgRequest);

            System.out.println("Message sent successfully!");
        } catch (SqsException e) {
            throw e;
        }
    }
}