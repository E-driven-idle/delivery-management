package com.driven.dm.user.presentation.validation;


import com.driven.dm.user.domain.entity.UserRole;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = UserRoleSubsetValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserRoleSubset {
    UserRole[] anyOf();
    String message() default "수정할 수 없는 권한입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
