package com.driven.dm.user.presentation.dto.request;

import com.driven.dm.user.domain.entity.UserRole;
import com.driven.dm.user.presentation.validation.UserRoleSubset;
import jakarta.validation.constraints.NotNull;

public record UserRoleUpdateRequest(
    @NotNull
    @UserRoleSubset(anyOf = {UserRole.OWNER, UserRole.CUSTOMER})
    UserRole role
) {

}
