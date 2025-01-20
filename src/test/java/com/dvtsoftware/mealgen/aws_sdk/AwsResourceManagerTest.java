package com.dvtsoftware.mealgen.aws_sdk;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class AwsResourceManagerTest {

    @Mock
    private SnsTopicManager snsTopicManager;

    @Mock
    private SqsQueueManager sqsQueueManager;

    @InjectMocks
    private AwsResourceManager awsResourceManager;

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
    @DisplayName("Test initializeResources - Success")
    void testInitializeResources_Success() {
        String mockQueueArn = "arn:aws:sqs:us-east-1:123456789012:MyQueue";
        String mockTopicArn = "arn:aws:sns:us-east-1:123456789012:MyTopic";

        when(sqsQueueManager.getQueueArn()).thenReturn(mockQueueArn);
        when(snsTopicManager.getTopicArn()).thenReturn(mockTopicArn);

        awsResourceManager.initializeResources();

        verify(snsTopicManager).createSnsTopic();
        verify(sqsQueueManager).createQueue();
        verify(sqsQueueManager).getQueueArn();
        verify(snsTopicManager).subscribeQueue(mockQueueArn);
        verify(sqsQueueManager).setQueuePolicy(mockQueueArn, mockTopicArn);
    }

    @Test
    @DisplayName("Test initializeResources - Exception Handling")
    void testInitializeResources_ExceptionHandling() {

        doThrow(new RuntimeException("SNS Topic creation failed"))
                .when(snsTopicManager).createSnsTopic();

        Assertions.assertThrows(RuntimeException.class, () -> awsResourceManager.initializeResources());

        verify(snsTopicManager).createSnsTopic();
        verifyNoInteractions(sqsQueueManager);
    }

    @Test
    @DisplayName("Test cleanupResources - Success")
    void testCleanupResources_Success() {
        awsResourceManager.cleanupResources();

        verify(sqsQueueManager).deleteQueue();
        verify(snsTopicManager).deleteSnsTopic();
    }

    @Test
    @DisplayName("Test cleanupResources - Exception Handling")
    void testCleanupResources_ExceptionHandling() {
        doThrow(new RuntimeException("SQS Queue deletion failed"))
                .when(sqsQueueManager).deleteQueue();

        Assertions.assertThrows(RuntimeException.class, () -> awsResourceManager.cleanupResources());

        verify(sqsQueueManager).deleteQueue();
        verifyNoInteractions(snsTopicManager);
    }
}
