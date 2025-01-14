package com.dvtsoftware.mealgen.aws;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

class SnsControllerTest {

    @Mock
    private SnsService snsService;

    private SnsController snsController;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        snsController = new SnsController(snsService);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    @DisplayName("Should successfully publish a message to the SNS topic")
    void testPublishToSns_Success() {
        String topicName = "TestTopic";
        String subject = "TestSubject";
        String requestPayload = "TestPayload";

        String response = snsController.publishToSns(topicName, subject, requestPayload);

        assertEquals("Message successfully published to SNS topic: " + topicName, response);
        verify(snsService).publishToSns(topicName, requestPayload, subject);
    }

    @Test
    @DisplayName("Should successfully publish a message with default subject when subject is null")
    void testPublishToSns_DefaultSubject() {
        String topicName = "TestTopic";
        String requestPayload = "TestPayload";

        String response = snsController.publishToSns(topicName, null, requestPayload);

        assertEquals("Message successfully published to SNS topic: " + topicName, response);
        verify(snsService).publishToSns(topicName, requestPayload, "Default Subject");
    }

    @Test
    @DisplayName("Should return error message when publishing to SNS fails")
    void testPublishToSns_Failure() {
        String topicName = "TestTopic";
        String subject = "TestSubject";
        String requestPayload = "TestPayload";

        doThrow(new RuntimeException("Test exception")).when(snsService)
                .publishToSns(topicName, requestPayload, subject);

        String response = snsController.publishToSns(topicName, subject, requestPayload);

        assertEquals("Failed to publish message: Test exception", response);
        verify(snsService).publishToSns(topicName, requestPayload, subject);
    }
}
