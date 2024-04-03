package com.sr.electronic.store.Electronic_Store.validate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ImageNameValidater.class)
@Configuration
public @interface ImageNameValid {
    //Error Message
    String message() default "Invalid Image Name";
    //represent group of constraints
    Class<?>[] groups() default {};
    //additional information about annotation
    Class<? extends Payload>[] payload() default {};
}
