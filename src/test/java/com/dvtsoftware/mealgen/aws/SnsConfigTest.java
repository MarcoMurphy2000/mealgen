package com.dvtsoftware.mealgen.aws;

import io.awspring.cloud.sns.core.SnsTemplate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.sns.SnsClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class SnsConfigTest {

    private SnsConfig snsConfig;

    @Mock
    private SnsClient snsClient;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        snsConfig = new SnsConfig();
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    @DisplayName("Should create SnsTemplate bean")
    void testSnsTemplate() {
        SnsTemplate snsTemplate = snsConfig.snsTemplate(snsClient);
        assertNotNull(snsTemplate, "SnsTemplate bean should not be null");
    }

    @Test
    @DisplayName("Should create SnsClient bean")
    void testSnsClient() {
        SnsClient client = snsConfig.snsClient();
        assertNotNull(client, "SnsClient bean should not be null");
    }
}