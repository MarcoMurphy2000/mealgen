package com.dvtsoftware.mealgen.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.dvtsoftware.mealgen.mapper.MealMapper;
import com.dvtsoftware.mealgen.model.domain.MealDomainObject;
import com.dvtsoftware.mealgen.model.domain.MealRequestDomainObject;
import com.dvtsoftware.mealgen.model.entity.MealEntity;
import com.dvtsoftware.mealgen.openai.MealGenerator;
import com.dvtsoftware.mealgen.repository.MealRepository;
import com.dvtsoftware.mealgen.service.interfaces.MealService;
import io.awspring.cloud.sns.core.SnsTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MealServiceImpl implements MealService {

    private static final String MEAL_NOT_FOUND = "Meal not found";

    private final MealRepository mealRepository;
    private final MealMapper mealMapper;
    private final MealGenerator mealGenerator;
    private final SnsTemplate snsTemplate;

    @Autowired
    public MealServiceImpl(MealRepository mealRepository,
                           MealMapper mealMapper,
                           MealGenerator mealGenerator,
                           SnsTemplate snsTemplate) {
        this.mealRepository = mealRepository;
        this.mealMapper = mealMapper;
        this.mealGenerator = mealGenerator;
        this.snsTemplate = snsTemplate;
    }

    @Override
    public MealDomainObject generateMeal(MealRequestDomainObject mealRequestDomainObject) throws IOException {
        MealDomainObject mealDomainObject = mealGenerator.generateMeal(mealRequestDomainObject);

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
        String topicName = "MealTopic";
        snsTemplate.sendNotification(topicName, mealDomainObject, "New Meal Created");
    }

    private void checkMealExists(final Long id) throws NoSuchElementException {
        if (!mealRepository.existsById(id)) {
            throw new NoSuchElementException(MEAL_NOT_FOUND);
        }
    }
}
