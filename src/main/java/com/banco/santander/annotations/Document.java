package com.banco.santander.annotations;


import com.banco.santander.annotations.impl.DocumentValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DocumentValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Document {

    String message() default "Invalid Document";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
