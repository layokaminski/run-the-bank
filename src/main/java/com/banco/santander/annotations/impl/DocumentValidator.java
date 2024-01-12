package com.banco.santander.annotations.impl;

import com.banco.santander.annotations.Document;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class DocumentValidator implements ConstraintValidator<Document, String> {
    private final Integer lengthCPF = 11;
    private final Integer lengthCNPJ = 14;

    @Override
    public void initialize(Document constraintAnnotation) {
    }


    @Override
    public boolean isValid(String document, ConstraintValidatorContext context) {
        if (document == null || document.isEmpty()) {
            return false;
        }

        return document.length() == lengthCPF || document.length() == lengthCNPJ;
    }
}
