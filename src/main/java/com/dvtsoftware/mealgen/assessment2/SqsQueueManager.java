package com.dvtsoftware.mealgen.assessment2;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.DeleteQueueRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueAttributesRequest;
import software.amazon.awssdk.services.sqs.model.QueueAttributeName;

@Component
@Slf4j
public class SqsQueueManager {

    private static final String QUEUE_NAME = "NewQueue";
    @Getter
    private String queueUrl;
    private final SqsClient sqsClient;

    public SqsQueueManager(SqsClient sqsClient) {
        this.sqsClient = sqsClient;
    }

    public void createQueue() {
        try {
            CreateQueueRequest createQueueRequest = CreateQueueRequest.builder()
                    .queueName(QUEUE_NAME)
                    .build();

            sqsClient.createQueue(createQueueRequest);

            queueUrl = sqsClient.getQueueUrl(r -> r.queueName(QUEUE_NAME)).queueUrl();
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
}
