package com.hanssecurity.uaa.validation;

import com.hanssecurity.uaa.annotaion.PasswordMatch;
import com.hanssecurity.uaa.annotaion.ValidPassword;
import com.hanssecurity.uaa.domain.dto.UserDto;
import lombok.val;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author hans
 */
public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, UserDto> {


    @Override
    public void initialize(PasswordMatch constraintAnnotation) {

    }

    @Override
    public boolean isValid(UserDto userDto, ConstraintValidatorContext context) {

        return userDto.getPassword().equals(userDto.getMatchingPassword());
    }
}
