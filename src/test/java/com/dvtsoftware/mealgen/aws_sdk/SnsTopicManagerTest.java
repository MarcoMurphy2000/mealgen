package com.dvtsoftware.mealgen.aws_sdk;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.CreateTopicRequest;
import software.amazon.awssdk.services.sns.model.CreateTopicResponse;
import software.amazon.awssdk.services.sns.model.DeleteTopicRequest;
import software.amazon.awssdk.services.sns.model.SubscribeRequest;
import software.amazon.awssdk.services.sns.model.SubscribeResponse;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class SnsTopicManagerTest {

    @Mock
    private SnsClient snsClient;

    @InjectMocks
    private SnsTopicManager snsTopicManager;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    @DisplayName("Test createSnsTopic - Success")
    void testCreateSnsTopic_Success() {
        String mockTopicArn = "arn:aws:sns:us-east-1:123456789012:NewTopic";
        CreateTopicResponse mockResponse = CreateTopicResponse.builder()
                .topicArn(mockTopicArn)
                .build();
        when(snsClient.createTopic(any(CreateTopicRequest.class))).thenReturn(mockResponse);

        snsTopicManager.createSnsTopic();

        verify(snsClient).createTopic(any(CreateTopicRequest.class));
        Assertions.assertEquals(mockTopicArn, snsTopicManager.getTopicArn());
    }

    @Test
    @DisplayName("Test createSnsTopic - Exception Handling")
    void testCreateSnsTopic_Exception() {
        when(snsClient.createTopic(any(CreateTopicRequest.class)))
                .thenThrow(new RuntimeException("Error creating SNS topic"));

        snsTopicManager.createSnsTopic();

        verify(snsClient).createTopic(any(CreateTopicRequest.class));
        Assertions.assertNull(snsTopicManager.getTopicArn());
    }

    @Test
    @DisplayName("Test deleteSnsTopic - Success")
    void testDeleteSnsTopic_Success() {
        snsTopicManager.setTopicArn("arn:aws:sns:us-east-1:123456789012:NewTopic");

        snsTopicManager.deleteSnsTopic();

        verify(snsClient).deleteTopic(any(DeleteTopicRequest.class));
    }

    @Test
    @DisplayName("Test deleteSnsTopic - No Topic ARN")
    void testDeleteSnsTopic_NoArn() {
        snsTopicManager.deleteSnsTopic();

        verifyNoInteractions(snsClient);
    }

    @Test
    @DisplayName("Test deleteSnsTopic - Exception Handling")
    void testDeleteSnsTopic_Exception() {
        snsTopicManager.setTopicArn("arn:aws:sns:us-east-1:123456789012:NewTopic");
        when(snsClient.deleteTopic(any(DeleteTopicRequest.class)))
                .thenThrow(new RuntimeException("Error deleting SNS topic"));

        snsTopicManager.deleteSnsTopic();

        verify(snsClient).deleteTopic(any(DeleteTopicRequest.class));
    }

    @Test
    @DisplayName("Test subscribeQueue - Success")
    void testSubscribeQueue_Success() {
        String mockQueueArn = "arn:aws:sqs:us-east-1:123456789012:MyQueue";
        String mockSubscriptionArn = "arn:aws:sns:us-east-1:123456789012:SubscriptionId";
        snsTopicManager.setTopicArn("arn:aws:sns:us-east-1:123456789012:NewTopic");

        SubscribeResponse mockResponse = SubscribeResponse.builder()
                .subscriptionArn(mockSubscriptionArn)
                .build();
        when(snsClient.subscribe(any(SubscribeRequest.class))).thenReturn(mockResponse);

        snsTopicManager.subscribeQueue(mockQueueArn);

        verify(snsClient).subscribe(any(SubscribeRequest.class));
    }

    @Test
    @DisplayName("Test subscribeQueue - Missing ARNs")
    void testSubscribeQueue_MissingArns() {
        snsTopicManager.subscribeQueue(null);

        verifyNoInteractions(snsClient);
    }

    @Test
    @DisplayName("Test subscribeQueue - Exception Handling")
    void testSubscribeQueue_Exception() {
        String mockQueueArn = "arn:aws:sqs:us-east-1:123456789012:MyQueue";
        snsTopicManager.setTopicArn("arn:aws:sns:us-east-1:123456789012:NewTopic");

        when(snsClient.subscribe(any(SubscribeRequest.class)))
                .thenThrow(new RuntimeException("Error subscribing SQS to SNS"));

        snsTopicManager.subscribeQueue(mockQueueArn);

        verify(snsClient).subscribe(any(SubscribeRequest.class));
    }
}
