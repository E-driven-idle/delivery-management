package com.driven.dm.user.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

import com.driven.dm.global.exception.AppException;
import com.driven.dm.user.application.exception.UserErrorCode;
import com.driven.dm.user.domain.entity.User;
import com.driven.dm.user.domain.entity.UserStatus;
import com.driven.dm.user.infrastructure.repository.UserRepository;
import com.driven.dm.user.presentation.dto.request.UserUpdateRequest;
import com.driven.dm.user.presentation.dto.response.UserPageResponse;
import com.driven.dm.user.presentation.dto.response.UserResponse;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Nested
    class getUser {

        @Test
        @DisplayName("유효한 ID로 조회시 유저정보를 반환한다")
        void getUser_withActiveUserId() {
            UUID activeUserId = UUID.randomUUID();

            User activeUser = mock(User.class);
            given(activeUser.getId()).willReturn(activeUserId);
            given(userRepository.findByIdAndStatus(activeUserId, UserStatus.ACTIVE)).willReturn(
                Optional.of(activeUser));

            UserResponse user = userService.getUser(activeUserId);

            assertThat(user.id()).isEqualTo(activeUserId);
            then(userRepository).should().findByIdAndStatus(activeUserId, UserStatus.ACTIVE);
        }

        @Test
        @DisplayName("ACTIVE 상태가 아닌 유저 조회시 예외가 발생한다")
        void getUser_throwsException_whenUserNotActive() {
            UUID notActiveUserId = UUID.randomUUID();

            given(userRepository.findByIdAndStatus(notActiveUserId, UserStatus.ACTIVE)).willReturn(
                Optional.empty());

            assertThatThrownBy(() -> {
                userService.getUser(notActiveUserId);
            }).isInstanceOf(AppException.class)
                .hasMessage(UserErrorCode.USER_NOT_FOUND.getMessage());

            then(userRepository).should().findByIdAndStatus(notActiveUserId, UserStatus.ACTIVE);
        }

        @Test
        @DisplayName("page, pageSize 로 페이징 조회")
        void getUsers_withPageAndPageSize() {
            long page = 1L;
            long pageSize = 10L;
            long offset = (page - 1) * pageSize;

            List<User> users = List.of(
                User.of("sejun1", "1234", "sejunO"),
                User.of("sejun2", "1234", "sejunO"),
                User.of("sejun3", "1234", "sejunO")
            );
            long count = 3;

            given(userRepository.findAll(offset, pageSize)).willReturn(users);
            given(userRepository.count()).willReturn(count);

            UserPageResponse response = userService.getUsers(page, pageSize);

            assertThat(response.count()).isEqualTo(count);
            assertThat(response.users()).hasSize(3);
            assertThat(response.users().get(0).username()).isEqualTo("sejun1");

            then(userRepository).should().findAll(offset, pageSize);
            then(userRepository).should().count();
        }
    }

    @Nested
    class updateUser {

        private UUID userId;

        @BeforeEach
        void setUp() {
            userId = UUID.randomUUID();
        }

        @Test
        @DisplayName("새로운 닉네임과 패스워드로 요청시 업데이트한다.")
        void updateUser_updatesNicknameAndEncodesPassword_whenBothProvided() {
            String newNickname = "newNickname";
            String newPassword = "newPassword";
            String encodedPassword = "encodedNewPassword";
            UserUpdateRequest request = UserUpdateRequest.builder()
                .nickname(newNickname)
                .password(newPassword)
                .build();

            User user = User.of("sejun", newPassword, newNickname);

            given(userRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)).willReturn(
                Optional.of(user));
            given(passwordEncoder.encode(newPassword)).willReturn(encodedPassword);

            userService.updateUser(userId, request);

            assertThat(user.getNickname()).isEqualTo(newNickname);
            assertThat(user.getPassword()).isEqualTo(encodedPassword);
        }

        @Test
        @DisplayName("새로운 닉네임으로 요청시 닉네임만 업데이트한다.")
        void updateUser_updateNickNameOnly() {
            String oldNickname = "oldNickname";
            String newNickname = "newNickname";
            String oldPassword = "oldPassword";

            UserUpdateRequest requestOfNicknameOnly = UserUpdateRequest.builder()
                .nickname(newNickname)
                .build();

            User user = User.of("sejun", oldPassword, oldNickname);
            given(userRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)).willReturn(
                Optional.of(user));

            userService.updateUser(userId, requestOfNicknameOnly);

            assertThat(user.getNickname()).isEqualTo(newNickname);
            assertThat(user.getPassword()).isEqualTo(oldPassword);
        }

        @Test
        @DisplayName("새로운 패스워드로 요청시 패스워드만 업데이트한다.")
        void updateUser_updatePasswordOnly() {
            String sameNickname = "sameNickname";
            String oldPassword = "oldPassword";
            String newPassword = "newPassword";
            String encodedPassword = "encodedNewPassword";

            UserUpdateRequest requestOfPasswordOnly = UserUpdateRequest.builder()
                .password(newPassword)
                .build();

            User user = User.of("sejun", oldPassword, sameNickname);
            given(userRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)).willReturn(
                Optional.of(user));
            given(passwordEncoder.encode(newPassword)).willReturn(encodedPassword);

            userService.updateUser(userId, requestOfPasswordOnly);

            assertThat(user.getNickname()).isEqualTo(sameNickname);
            assertThat(user.getPassword()).isEqualTo(encodedPassword);
        }

        @Test
        @DisplayName("같은 닉네임으로 요청시 업데이트하지 않는다")
        void updateUser_doNotUpdate_whenNicknameUnchanged() {
            String sameNickname = "sameNickname";

            UserUpdateRequest requestOfSameNickname = UserUpdateRequest.builder()
                .nickname(sameNickname)
                .build();

            User mockUser = mock(User.class);
            given(mockUser.getNickname()).willReturn(sameNickname);
            given(userRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)).willReturn(
                Optional.of(mockUser));

            userService.updateUser(userId, requestOfSameNickname);

            then(mockUser).should(never()).updateNickName(sameNickname);
        }

        @Test
        @DisplayName("같은 패스워드로 요청시 업데이트하지 않는다")
        void updateUser_doNotUpdate_whenPasswordUnchanged() {
            String samePassword = "samePassword";
            String encodedPassword = "encodedPassword";

            UserUpdateRequest requestOfSamePassword = UserUpdateRequest.builder()
                .password(samePassword)
                .build();

            User mockUser = mock(User.class);
            given(mockUser.getPassword()).willReturn(encodedPassword);
            given(userRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)).willReturn(
                Optional.of(mockUser));
            given(passwordEncoder.matches(samePassword, encodedPassword)).willReturn(true);

            userService.updateUser(userId, requestOfSamePassword);

            then(passwordEncoder).should(never()).encode(anyString());
            then(mockUser).should(never()).updatePassword(anyString());
        }

        @Test
        @DisplayName("MASTER/MANAGER 요청시 유저 권한을 CUSTOMER 로 변경한다")
        void shouldUpdateUserRole_whenRequestMasterOrManager() {
        }
    }

    @Nested
    class deleteUser {
        @Test
        @DisplayName("ACTIVE 유저를 삭제한다")
        void deleteUser_delete_whenActiveUser() {
            UUID userId = UUID.randomUUID();

            User user = User.of("sejun", "1234", "sejunO");
            given(userRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)).willReturn(
                Optional.of(user));

            userService.deleteUser(userId);

            assertThat(user.getStatus()).isEqualTo(UserStatus.DELETED);
            assertThat(user.getDeletedAt()).isNotNull();
        }

        @Test
        @DisplayName("존재하지 않거나 ACTIVE 상태가 아닌 유저는 삭제시 예외가 발생한다")
        void deleteUser_failsDelete_whenNotActiveUser() {
            UUID userId = UUID.randomUUID();

            given(userRepository.findByIdAndStatus(userId, UserStatus.ACTIVE)).willReturn(
                Optional.empty());

            assertThatThrownBy(() -> {
                userService.deleteUser(userId);
            }).isInstanceOf(AppException.class)
                .hasMessage(UserErrorCode.USER_NOT_FOUND.getMessage());
        }
    }
}
