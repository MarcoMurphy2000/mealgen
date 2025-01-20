package com.dvtsoftware.mealgen.aws_sdk;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.DeleteQueueRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueAttributesRequest;
import software.amazon.awssdk.services.sqs.model.QueueAttributeName;
import software.amazon.awssdk.services.sqs.model.SetQueueAttributesRequest;

@Component
@Slf4j
public class SqsQueueManager {

    private static final String QUEUE_NAME = "NewQueue";
    @Getter
    @Setter
    private String queueUrl;
    private final SqsClient sqsClient;

    public SqsQueueManager(SqsClient sqsClient) {
        this.sqsClient = sqsClient;
    }

    public void createQueue() {
        try {
            CreateQueueRequest request = CreateQueueRequest.builder()
                    .queueName(QUEUE_NAME)
                    .build();
            queueUrl = sqsClient.createQueue(request).queueUrl();
            log.info("Created SQS Queue with URL: {}", queueUrl);
        } catch (Exception e) {
            log.error("Error creating SQS Queue: {}", e.getMessage(), e);
        }
    }

    public String getQueueArn() {
        if (queueUrl == null) {
            log.error("Queue URL is null. Cannot retrieve ARN.");
            return null;
        }
        try {
            return sqsClient.getQueueAttributes(GetQueueAttributesRequest.builder()
                    .queueUrl(queueUrl)
                    .attributeNames(QueueAttributeName.QUEUE_ARN)
                    .build()).attributes().get(QueueAttributeName.QUEUE_ARN);
        } catch (Exception e) {
            log.error("Error retrieving Queue ARN: {}", e.getMessage(), e);
            return null;
        }
    }

    public void setQueuePolicy(String queueArn, String topicArn) {
        String policy = generateQueuePolicy(queueArn, topicArn);
        try {
            SetQueueAttributesRequest request = SetQueueAttributesRequest.builder()
                    .queueUrl(queueUrl)
                    .attributes(Map.of(QueueAttributeName.POLICY, policy))
                    .build();

            sqsClient.setQueueAttributes(request);
            log.info("Set policy for SQS queue [{}] to allow messages from SNS topic [{}].", queueArn, topicArn);
        } catch (Exception e) {
            log.error("Error setting policy for SQS queue: {}", e.getMessage(), e);
        }
    }

    public void deleteQueue() {
        try {
            if (queueUrl != null) {
                DeleteQueueRequest deleteQueueRequest = DeleteQueueRequest.builder()
                        .queueUrl(queueUrl)
                        .build();

                sqsClient.deleteQueue(deleteQueueRequest);
                log.info("Deleted SQS Queue with URL: {}", queueUrl);
            }
        } catch (Exception e) {
            log.error("Error deleting SQS Queue: {}", e.getMessage(), e);
        }
    }

    private String generateQueuePolicy(String queueArn, String topicArn) {
        return String.format(
                "{" +
                        "  \"Version\": \"2012-10-17\"," +
                        "  \"Statement\": [" +
                        "    {" +
                        "      \"Effect\": \"Allow\"," +
                        "      \"Principal\": \"*\"," +
                        "      \"Action\": \"sqs:SendMessage\"," +
                        "      \"Resource\": \"%s\"," +
                        "      \"Condition\": {" +
                        "        \"ArnEquals\": {" +
                        "          \"aws:SourceArn\": \"%s\"" +
                        "        }" +
                        "      }" +
                        "    }" +
                        "  ]" +
                        "}",
                queueArn,
                topicArn
        );
    }
}
