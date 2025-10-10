package com.driven.dm.user.presentation.validation;

import com.driven.dm.user.domain.entity.UserRole;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Set;

public class UserRoleSubsetValidator implements ConstraintValidator<UserRoleSubset, UserRole> {

    private Set<UserRole> allowed;

    @Override
    public void initialize(UserRoleSubset constraintAnnotation) {
        allowed = Set.of(constraintAnnotation.anyOf());
    }

    @Override
    public boolean isValid(UserRole value, ConstraintValidatorContext context) {
        return value == null || allowed.contains(value);
    }
}