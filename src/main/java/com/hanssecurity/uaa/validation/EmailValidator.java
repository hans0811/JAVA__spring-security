package com.hanssecurity.uaa.validation;

import com.hanssecurity.uaa.annotaion.ValidEmail;
import lombok.val;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @author hans
 */
public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    private final static String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Override
    public void initialize(ValidEmail constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return validateEmail(value);
    }

    private boolean validateEmail(final String email) {
        val pattern = Pattern.compile(EMAIL_PATTERN);
        val matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
