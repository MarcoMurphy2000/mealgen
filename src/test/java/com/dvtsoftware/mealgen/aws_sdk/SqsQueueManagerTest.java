package com.dvtsoftware.mealgen.aws_sdk;

import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.CreateQueueResponse;
import software.amazon.awssdk.services.sqs.model.DeleteQueueRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueAttributesRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueAttributesResponse;
import software.amazon.awssdk.services.sqs.model.QueueAttributeName;
import software.amazon.awssdk.services.sqs.model.SetQueueAttributesRequest;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class SqsQueueManagerTest {

    @Mock
    private SqsClient sqsClient;

    @InjectMocks
    private SqsQueueManager sqsQueueManager;

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
    @DisplayName("Test createQueue - Success")
    void testCreateQueue_Success() {
        String mockQueueUrl = "https://sqs.us-east-1.amazonaws.com/123456789012/NewQueue";
        CreateQueueResponse mockResponse = CreateQueueResponse.builder()
                .queueUrl(mockQueueUrl)
                .build();
        when(sqsClient.createQueue(any(CreateQueueRequest.class))).thenReturn(mockResponse);

        sqsQueueManager.createQueue();

        verify(sqsClient).createQueue(any(CreateQueueRequest.class));
        Assertions.assertEquals(mockQueueUrl, sqsQueueManager.getQueueUrl());
    }

    @Test
    @DisplayName("Test createQueue - Exception Handling")
    void testCreateQueue_Exception() {
        when(sqsClient.createQueue(any(CreateQueueRequest.class)))
                .thenThrow(new RuntimeException("Error creating SQS queue"));

        sqsQueueManager.createQueue();

        verify(sqsClient).createQueue(any(CreateQueueRequest.class));
        Assertions.assertNull(sqsQueueManager.getQueueUrl());
    }

    @Test
    @DisplayName("Test getQueueArn - Success")
    void testGetQueueArn_Success() {
        String mockQueueArn = "arn:aws:sqs:us-east-1:123456789012:NewQueue";
        sqsQueueManager.setQueueUrl("https://sqs.us-east-1.amazonaws.com/123456789012/NewQueue");

        when(sqsClient.getQueueAttributes(any(GetQueueAttributesRequest.class)))
                .thenReturn(GetQueueAttributesResponse.builder()
                        .attributes(Map.of(QueueAttributeName.QUEUE_ARN, mockQueueArn))
                        .build());

        String queueArn = sqsQueueManager.getQueueArn();

        verify(sqsClient).getQueueAttributes(any(GetQueueAttributesRequest.class));
        Assertions.assertEquals(mockQueueArn, queueArn);
    }

    @Test
    @DisplayName("Test getQueueArn - Null Queue URL")
    void testGetQueueArn_NullQueueUrl() {
        sqsQueueManager.setQueueUrl(null);

        String queueArn = sqsQueueManager.getQueueArn();

        verifyNoInteractions(sqsClient);
        Assertions.assertNull(queueArn);
    }

    @Test
    @DisplayName("Test getQueueArn - Exception Handling")
    void testGetQueueArn_Exception() {
        sqsQueueManager.setQueueUrl("https://sqs.us-east-1.amazonaws.com/123456789012/NewQueue");

        when(sqsClient.getQueueAttributes(any(GetQueueAttributesRequest.class)))
                .thenThrow(new RuntimeException("Error retrieving Queue ARN"));

        String queueArn = sqsQueueManager.getQueueArn();

        verify(sqsClient).getQueueAttributes(any(GetQueueAttributesRequest.class));
        Assertions.assertNull(queueArn);
    }

    @Test
    @DisplayName("Test setQueuePolicy - Success")
    void testSetQueuePolicy_Success() {
        String mockQueueArn = "arn:aws:sqs:us-east-1:123456789012:NewQueue";
        String mockTopicArn = "arn:aws:sns:us-east-1:123456789012:NewTopic";
        sqsQueueManager.setQueueUrl("https://sqs.us-east-1.amazonaws.com/123456789012/NewQueue");

        sqsQueueManager.setQueuePolicy(mockQueueArn, mockTopicArn);

        verify(sqsClient).setQueueAttributes(any(SetQueueAttributesRequest.class));
    }

    @Test
    @DisplayName("Test setQueuePolicy - Exception Handling")
    void testSetQueuePolicy_Exception() {
        String mockQueueArn = "arn:aws:sqs:us-east-1:123456789012:NewQueue";
        String mockTopicArn = "arn:aws:sns:us-east-1:123456789012:NewTopic";
        sqsQueueManager.setQueueUrl("https://sqs.us-east-1.amazonaws.com/123456789012/NewQueue");

        when(sqsClient.setQueueAttributes(any(SetQueueAttributesRequest.class)))
                .thenThrow(new RuntimeException("Error setting queue policy"));

        sqsQueueManager.setQueuePolicy(mockQueueArn, mockTopicArn);

        verify(sqsClient).setQueueAttributes(any(SetQueueAttributesRequest.class));
    }

    @Test
    @DisplayName("Test deleteQueue - Success")
    void testDeleteQueue_Success() {
        sqsQueueManager.setQueueUrl("https://sqs.us-east-1.amazonaws.com/123456789012/NewQueue");

        sqsQueueManager.deleteQueue();

        verify(sqsClient).deleteQueue(any(DeleteQueueRequest.class));
    }

    @Test
    @DisplayName("Test deleteQueue - Null Queue URL")
    void testDeleteQueue_NullQueueUrl() {
        sqsQueueManager.setQueueUrl(null);

        sqsQueueManager.deleteQueue();

        verifyNoInteractions(sqsClient);
    }

    @Test
    @DisplayName("Test deleteQueue - Exception Handling")
    void testDeleteQueue_Exception() {
        sqsQueueManager.setQueueUrl("https://sqs.us-east-1.amazonaws.com/123456789012/NewQueue");

        when(sqsClient.deleteQueue(any(DeleteQueueRequest.class)))
                .thenThrow(new RuntimeException("Error deleting SQS queue"));

        sqsQueueManager.deleteQueue();

        verify(sqsClient).deleteQueue(any(DeleteQueueRequest.class));
    }
}
