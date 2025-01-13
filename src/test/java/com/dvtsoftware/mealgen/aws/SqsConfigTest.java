package com.dvtsoftware.mealgen.aws;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.sqs.SqsClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class SqsConfigTest {

    private SqsConfig sqsConfig;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        sqsConfig = new SqsConfig();
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    @DisplayName("Should create SqsClient bean")
    void testSqsClient() {
        SqsClient sqsClient = sqsConfig.sqsClient();
        assertNotNull(sqsClient);
    }
}
