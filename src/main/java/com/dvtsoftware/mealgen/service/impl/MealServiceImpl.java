package com.dvtsoftware.mealgen.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.dvtsoftware.mealgen.mapper.MealMapper;
import com.dvtsoftware.mealgen.model.domain.MealDomainObject;
import com.dvtsoftware.mealgen.model.domain.MealRequestDomainObject;
import com.dvtsoftware.mealgen.model.entity.MealEntity;
import com.dvtsoftware.mealgen.openai.MealGenerationService;
import com.dvtsoftware.mealgen.repository.MealRepository;
import com.dvtsoftware.mealgen.service.interfaces.MealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

@Service
public class MealServiceImpl implements MealService {

    private static final String MEAL_NOT_FOUND = "Meal not found";

    private final MealRepository mealRepository;
    private final MealMapper mealMapper;
    private final MealGenerationService mealGenerationService;
    private final SnsClient snsClient;

    @Autowired
    public MealServiceImpl(MealRepository mealRepository,
                           MealMapper mealMapper,
                           MealGenerationService mealGenerationService,
                           SnsClient snsClient) {
        this.mealRepository = mealRepository;
        this.mealMapper = mealMapper;
        this.mealGenerationService = mealGenerationService;
        this.snsClient = snsClient;
    }

    @Override
    public MealDomainObject generateMeal(MealRequestDomainObject mealRequestDomainObject) throws IOException {
        MealDomainObject mealDomainObject = mealGenerationService.generateMeal(mealRequestDomainObject);

        MealEntity mealEntity = mealMapper.mapMealDOToMealEntity(mealDomainObject);
        mealRepository.save(mealEntity);

        sendMealNotification(mealDomainObject);

        return mealDomainObject;
    }

    @Override
    public List<MealDomainObject> getAllMeals() {
        List<MealEntity> mealEntities = mealRepository.findAll();
        return mealEntities.stream()
                .map(mealMapper::mapMealEntityToMealDO)
                .toList();
    }

    @Override
    public MealDomainObject getMealById(final Long id) throws NoSuchElementException {
        Optional<MealEntity> mealEntity = mealRepository.findById(id);
        return mealEntity.map(mealMapper::mapMealEntityToMealDO)
                .orElseThrow(() -> new NoSuchElementException(MEAL_NOT_FOUND));
    }

    @Override
    public void updateMeal(Long id, MealDomainObject mealDomainObject) throws NoSuchElementException {
        checkMealExists(id);
        MealEntity updatedMealEntity = mealMapper.mapMealDOToMealEntity(mealDomainObject);
        updatedMealEntity.setId(id);
        mealRepository.save(updatedMealEntity);
    }

    @Override
    public void deleteMeal(Long id) {
        mealRepository.deleteById(id);
    }

    @Override
    public void deleteAllMeals() {
        mealRepository.deleteAll();
    }

    @Override
    public void sendMealNotification(MealDomainObject mealDomainObject) {
        String topicArn = "arn:aws:sns:us-east-1:324037301453:MyTopic";
        String message = "A new meal has been generated: " + mealDomainObject.getMealName();

        PublishRequest request = PublishRequest.builder()
                .topicArn(topicArn)
                .message(message)
                .build();

        snsClient.publish(request);
    }

    private void checkMealExists(final Long id) throws NoSuchElementException {
        if (!mealRepository.existsById(id)) {
            throw new NoSuchElementException(MEAL_NOT_FOUND);
        }
    }
}
