package com.dvtsoftware.mealgen.aws_sdk;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.CreateTopicRequest;
import software.amazon.awssdk.services.sns.model.DeleteTopicRequest;

@Component
@Slf4j
public class SnsTopicManager {

    private static final String TOPIC_NAME = "NewTopic";
    private String topicArn;
    private final SnsClient snsClient;

    public SnsTopicManager(SnsClient snsClient) {
        this.snsClient = snsClient;
    }

    @PostConstruct
    public void initialize() {
        createSnsTopic();
    }

    @PreDestroy
    public void cleanup() {
        deleteSnsTopic();
    }

    private void createSnsTopic() {
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

    private void deleteSnsTopic() {
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
}

