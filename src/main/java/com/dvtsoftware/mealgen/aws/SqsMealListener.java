package com.dvtsoftware.mealgen.aws;

import com.dvtsoftware.mealgen.generated.model.MealRequest;
import com.dvtsoftware.mealgen.mapper.MealRequestMapper;
import com.dvtsoftware.mealgen.model.domain.MealRequestDomainObject;
import com.dvtsoftware.mealgen.service.impl.MealServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SqsMealListener {

    private final ObjectMapper objectMapper;
    private final MealRequestMapper mealRequestMapper;
    private final MealServiceImpl mealServiceImpl;

    public SqsMealListener(ObjectMapper objectMapper, MealRequestMapper mealRequestMapper, MealServiceImpl mealServiceImpl) {
        this.objectMapper = objectMapper;
        this.mealRequestMapper = mealRequestMapper;
        this.mealServiceImpl = mealServiceImpl;
    }

    @SqsListener("NewQueue")
    public void processMealRequest(@Payload String message) {
        try {
            log.info("Received message from SQS: {}", message);

            MealRequest mealRequestDTO = objectMapper.readValue(message, MealRequest.class);

            MealRequestDomainObject mealRequestDomainObject = mealRequestMapper.mapMealRequestDTOToMealRequestDO(mealRequestDTO);

            mealServiceImpl.generateMeal(mealRequestDomainObject);

            log.info("Processed meal request successfully.");
        } catch (Exception e) {
            log.error("Error processing meal request from SQS", e);
        }
    }
}
