package com.dvtsoftware.mealgen.service.validators;

public interface ModelValidator<T> {

    void validate(T t);
}