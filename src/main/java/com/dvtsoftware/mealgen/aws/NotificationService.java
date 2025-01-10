package com.dvtsoftware.mealgen.aws;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

@Service
public class NotificationService {

    private final SnsClient snsClient;

    public NotificationService(SnsClient snsClient) {
        this.snsClient = snsClient;
    }

    public String sendNotification(String topicArn, String message) {
        PublishRequest request = PublishRequest.builder()
                .topicArn(topicArn)
                .message(message)
                .build();

        PublishResponse response = snsClient.publish(request);

        return response.messageId();
    }
}
