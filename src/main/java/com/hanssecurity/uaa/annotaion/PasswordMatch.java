package com.hanssecurity.uaa.annotaion;

import com.hanssecurity.uaa.validation.EmailValidator;
import com.hanssecurity.uaa.validation.PasswordMatchValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author hans
 */
@Target( {ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchValidator.class)
@Documented
public @interface PasswordMatch {
    String message() default "{PasswordMatches.userDto}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
