package com.dvtsoftware.mealgen.aws;

import io.awspring.cloud.sns.core.SnsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SnsService {

    private final SnsTemplate snsTemplate;

    public SnsService(SnsTemplate snsTemplate) {
        this.snsTemplate = snsTemplate;
    }

    public void publishToSns(String topicName, String jsonPayload, String subject) {
        try {
            snsTemplate.sendNotification(topicName, jsonPayload, subject);
            log.info("Message successfully published to SNS topic: {}", topicName);
        } catch (Exception e) {
            log.error("Error publishing message to SNS topic: {}", topicName, e);
            throw e;
        }
    }
}
