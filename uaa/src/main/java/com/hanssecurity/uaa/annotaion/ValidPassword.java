package com.hanssecurity.uaa.annotaion;

import com.hanssecurity.uaa.validation.PasswordConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author hans
 */
@Target( {ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordConstraintValidator.class)
@Documented
public @interface ValidPassword {
    String message() default "{ValidPassword.password}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
