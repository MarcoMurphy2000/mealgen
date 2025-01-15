package com.dvtsoftware.mealgen.assessment2;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.CreateTopicRequest;
import software.amazon.awssdk.services.sns.model.DeleteTopicRequest;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;
import software.amazon.awssdk.services.sns.model.SubscribeResponse;

@Component
@Slf4j
public class SnsTopicManager {

    private static final String TOPIC_NAME = "NewTopic";
    @Getter
    private String topicArn;
    private final SnsClient snsClient;

    public SnsTopicManager(SnsClient snsClient) {
        this.snsClient = snsClient;
    }

    public void createSnsTopic() {
        try {
            CreateTopicRequest request = CreateTopicRequest.builder()
                    .name(TOPIC_NAME)
                    .build();
            topicArn = snsClient.createTopic(request).topicArn();
            log.info("Created SNS Topic with ARN: {}", topicArn);
        } catch (Exception e) {
            log.error("Error creating SNS Topic: {}", e.getMessage(), e);
        }
    }

    public void deleteSnsTopic() {
        try {
            if (topicArn != null) {
                DeleteTopicRequest request = DeleteTopicRequest.builder()
                        .topicArn(topicArn)
                        .build();
                snsClient.deleteTopic(request);
                log.info("Deleted SNS Topic with ARN: {}", topicArn);
            }
        } catch (Exception e) {
            log.error("Error deleting SNS Topic: {}", e.getMessage(), e);
        }
    }

    public void subscribeQueue(String queueArn) {
        if (topicArn == null || queueArn == null) {
            log.error("Cannot subscribe queue to topic: Topic ARN or Queue ARN is null.");
            return;
        }

        try {
            SubscribeRequest subscribeRequest = SubscribeRequest.builder()
                    .topicArn(topicArn)
                    .endpoint(queueArn)
                    .protocol("sqs")
                    .build();

            SubscribeResponse response = snsClient.subscribe(subscribeRequest);
            log.info("Subscribed SQS queue to SNS topic. Subscription ARN: {}", response.subscriptionArn());
        } catch (Exception e) {
            log.error("Error subscribing SQS queue to SNS topic: {}", e.getMessage(), e);
        }
    }
}
