package com.dvtsoftware.mealgen.assessment2;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AwsResourceManager {

    private final SnsTopicManager snsTopicManager;
    private final SqsQueueManager sqsQueueManager;

    public AwsResourceManager(SnsTopicManager snsTopicManager, SqsQueueManager sqsQueueManager) {
        this.snsTopicManager = snsTopicManager;
        this.sqsQueueManager = sqsQueueManager;
    }

    @PostConstruct
    public void initializeResources() {
        log.info("Initializing AWS resources...");

        snsTopicManager.createSnsTopic();

        sqsQueueManager.createQueue();

        String queueArn = sqsQueueManager.getQueueArn();
        snsTopicManager.subscribeQueue(queueArn);

        log.info("AWS resources initialized.");
    }

    @PreDestroy
    public void cleanupResources() {
        log.info("Cleaning up AWS resources...");

        sqsQueueManager.deleteQueue();

        snsTopicManager.deleteSnsTopic();

        log.info("AWS resources cleaned up.");
    }
}
