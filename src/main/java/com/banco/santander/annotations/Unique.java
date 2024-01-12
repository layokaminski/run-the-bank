package com.banco.santander.annotations;

import com.banco.santander.annotations.impl.UniqueValidator;
import com.banco.santander.services.CustomerService;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = UniqueValidator.class)
@Documented
public @interface Unique {
    String message() default "Unique field already exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    Class<CustomerService> service();
    String fieldName();
}
