package com.banco.santander.annotations.impl;

import com.banco.santander.annotations.Unique;
import com.banco.santander.services.FieldValueExists;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;

@RequiredArgsConstructor
public class UniqueValidator implements ConstraintValidator<Unique, String> {

    private final ApplicationContext applicationContext;
    private FieldValueExists service;
    private String fieldName;

    @Override
    public void initialize(Unique unique) {
        Class<? extends FieldValueExists> clazz = unique.service();
        this.fieldName = unique.fieldName();
        this.service = this.applicationContext.getBean(clazz);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return !this.service.fieldValueExists(value, this.fieldName);
    }
}
