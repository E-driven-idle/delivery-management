package com.driven.dm.user.domain.rule;

import com.driven.dm.global.exception.AppException;
import com.driven.dm.user.application.exception.UserErrorCode;
import com.driven.dm.user.domain.entity.UserRole;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public class RoleTransitionRule {

    private final static Map<UserRole, Set<UserRole>> TARGET_ROLES = Map.of(
        UserRole.MASTER, EnumSet.of(UserRole.MANAGER, UserRole.OWNER, UserRole.CUSTOMER),
        UserRole.MANAGER, EnumSet.of(UserRole.OWNER, UserRole.CUSTOMER)
    );

    public static void authorize(UserIdAndRole actor, UserIdAndRole target, UserRole newRole) {
        if (actor.userId().equals(target.userId())) {
            throw AppException.of(UserErrorCode.SELF_ROLE_UPDATE);
        }

        Set<UserRole> allowedTargets =
            TARGET_ROLES.getOrDefault(actor.role(), Collections.emptySet());
        if (allowedTargets.isEmpty()) {
            throw AppException.of(UserErrorCode.USER_FORBIDDEN);
        }

        if (!allowedTargets.contains(newRole)) {
            throw AppException.of(UserErrorCode.USER_FORBIDDEN);
        }
    }
}
