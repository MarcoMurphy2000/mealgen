package com.dvtsoftware.mealgen.aws;

import io.awspring.cloud.sns.core.SnsTemplate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

class SnsServiceTest {

    private SnsService snsService;

    @Mock
    private SnsTemplate snsTemplate;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        snsService = new SnsService(snsTemplate);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void publishToSns_ShouldPublishMessageSuccessfully() {
        String topicName = "TestTopic";
        String jsonPayload = "{\"message\":\"Test\"}";
        String subject = "TestSubject";

        snsService.publishToSns(topicName, jsonPayload, subject);

        verify(snsTemplate).sendNotification(topicName, jsonPayload, subject);
    }

    @Test
    void publishToSns_ShouldThrowExceptionWhenPublishFails() {
        String topicName = "TestTopic";
        String jsonPayload = "{\"message\":\"Test\"}";
        String subject = "TestSubject";

        doThrow(new RuntimeException("Publish error")).when(snsTemplate)
                .sendNotification(topicName, jsonPayload, subject);

        try {
            snsService.publishToSns(topicName, jsonPayload, subject);
        } catch (Exception e) {
            verify(snsTemplate).sendNotification(topicName, jsonPayload, subject);
        }
    }
}
