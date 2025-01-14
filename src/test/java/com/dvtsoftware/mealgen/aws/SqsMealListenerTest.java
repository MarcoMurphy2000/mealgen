package com.dvtsoftware.mealgen.aws;

import com.dvtsoftware.mealgen.generated.model.MealRequest;
import com.dvtsoftware.mealgen.mapper.MealRequestMapper;
import com.dvtsoftware.mealgen.model.domain.MealRequestDomainObject;
import com.dvtsoftware.mealgen.service.impl.MealServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class SqsMealListenerTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private MealRequestMapper mealRequestMapper;

    @Mock
    private MealServiceImpl mealServiceImpl;

    @InjectMocks
    private SqsMealListener sqsMealListener;

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
    @DisplayName("Should process meal request successfully")
    void testProcessMealRequest() throws Exception {
        String message = "{\"mealName\":\"Test Meal\"}";

        MealRequest mealRequest = new MealRequest();
        MealRequestDomainObject mealRequestDomainObject = new MealRequestDomainObject();

        when(objectMapper.readValue(message, MealRequest.class)).thenReturn(mealRequest);
        when(mealRequestMapper.mapMealRequestDTOToMealRequestDO(mealRequest)).thenReturn(mealRequestDomainObject);

        sqsMealListener.processMealRequest(message);

        verify(objectMapper).readValue(message, MealRequest.class);
        verify(mealRequestMapper).mapMealRequestDTOToMealRequestDO(mealRequest);
        verify(mealServiceImpl).generateMeal(mealRequestDomainObject);
    }

    @Test
    @DisplayName("Should handle exception when processing meal request")
    void testProcessMealRequest_Exception() throws Exception {
        String message = "{\"invalidJson\":\"Test\"}";

        when(objectMapper.readValue(message, MealRequest.class)).thenThrow(new RuntimeException("JSON parsing error"));

        sqsMealListener.processMealRequest(message);

        verify(objectMapper).readValue(message, MealRequest.class);
        verifyNoInteractions(mealRequestMapper, mealServiceImpl);
    }
}
