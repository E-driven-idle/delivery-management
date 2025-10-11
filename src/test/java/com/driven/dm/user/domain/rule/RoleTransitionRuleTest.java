package com.driven.dm.user.domain.rule;


import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.driven.dm.global.exception.AppException;
import com.driven.dm.user.domain.entity.UserRole;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class RoleTransitionRuleTest {

    private static final UserIdAndRole MASTER =
        UserIdAndRole.from(UUID.randomUUID(), UserRole.MASTER);
    private static final UserIdAndRole MANAGER =
        UserIdAndRole.from(UUID.randomUUID(), UserRole.MANAGER);
    private static final UserIdAndRole OWNER =
        UserIdAndRole.from(UUID.randomUUID(), UserRole.OWNER);
    private static final UserIdAndRole CUSTOMER =
        UserIdAndRole.from(UUID.randomUUID(), UserRole.CUSTOMER);


    @ParameterizedTest
    @MethodSource("masterAndManager")
    @DisplayName("MANAGER, MASTER 는 권한을 업데이트할 수 있다")
    void shouldRoleUpdate_whenActorIsMasterAndManager(UserIdAndRole actor) {
        UserIdAndRole target = CUSTOMER;
        assertThatNoException().isThrownBy(() -> {
            RoleTransitionRule.authorize(actor, target, UserRole.CUSTOMER);
        });
    }

    static Stream<UserIdAndRole> masterAndManager() {
        return Stream.of(MASTER, MANAGER);
    }

    @ParameterizedTest
    @MethodSource("ownerAndCustomer")
    @DisplayName("CUSTOMER, OWNER 는 권한을 업데이트할 수 없다")
    void shouldFailRoleUpdate_whenActorIsCustomerAndOwner(UserIdAndRole actor) {
        UserIdAndRole target = CUSTOMER;
        assertThatThrownBy(() -> {
            RoleTransitionRule.authorize(actor, target, UserRole.CUSTOMER);
        }).isInstanceOf(AppException.class);
    }

    static Stream<UserIdAndRole> ownerAndCustomer() {
        return Stream.of(OWNER, CUSTOMER);
    }

    @ParameterizedTest(name = "[{index}] MASTER -> {0} 허용")
    @MethodSource("allTargetRolesExceptMaster")
    @DisplayName("MASTER 는 MASTER 를 제외한 모든 권한으로 업데이트할 수 있다")
    void shouldAllowAllTargetRolesExcludeMaster_whenActorIsMaster(UserRole targetRole) {
        UserIdAndRole target = UserIdAndRole.from(UUID.randomUUID(), targetRole);

        assertThatNoException().isThrownBy(() -> {
            RoleTransitionRule.authorize(MASTER, target, targetRole);
        });
    }

    static Stream<UserRole> allTargetRolesExceptMaster() {
        return Stream.of(UserRole.values()).filter(role -> role != UserRole.MASTER);
    }

    @ParameterizedTest(name = "[{index}] MANAGER -> {0} 허용")
    @MethodSource("targetRolesCustomerAndOwner")
    @DisplayName("MANAGER 는 CUSTOMER, OWNER 로 권한을 업데이트할 수 있다")
    void shouldAllowCustomerAndOwner_whenActorIsManager(UserRole targetRole) {
        UserIdAndRole target = UserIdAndRole.from(UUID.randomUUID(), targetRole);

        assertThatNoException().isThrownBy(() -> {
            RoleTransitionRule.authorize(MANAGER, target, targetRole);
        });
    }

    static Stream<UserRole> targetRolesCustomerAndOwner() {
        return Stream.of(UserRole.values()).filter(role -> {
            return role == UserRole.CUSTOMER || role == UserRole.OWNER;
        });
    }

    @ParameterizedTest
    @MethodSource("usersOfEachRole")
    @DisplayName("본인의 권한을 업데이트할 수 없다")
    void shouldFailUpdateRole_whenOwn(UserIdAndRole actor) {
        assertThatThrownBy(() -> {
            RoleTransitionRule.authorize(actor, actor, UserRole.CUSTOMER);
        }).isInstanceOf(AppException.class);
    }

    static Stream<UserIdAndRole> usersOfEachRole() {
        return Stream.of(MASTER, MANAGER, OWNER, CUSTOMER);
    }
}