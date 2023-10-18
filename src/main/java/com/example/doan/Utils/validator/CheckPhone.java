package com.example.doan.Utils.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneValidator.class)
@Documented
public @interface CheckPhone {
    String message() default  "invalid data";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
