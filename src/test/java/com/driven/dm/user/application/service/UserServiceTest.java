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
import com.driven.dm.user.presentation.controller.dto.request.UserUpdateRequest;
import com.driven.dm.user.presentation.controller.dto.response.UserResponse;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
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
        }).isInstanceOf(AppException.class).hasMessage(UserErrorCode.USER_NOT_FOUND.getMessage());

        then(userRepository).should().findByIdAndStatus(notActiveUserId, UserStatus.ACTIVE);
    }

    @Test
    @DisplayName("새로운 닉네임과 패스워드로 요청시 업데이트한다.")
    void updateUser_updatesNicknameAndEncodesPassword_whenBothProvided() {
        UUID userId = UUID.randomUUID();
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
        UUID userId = UUID.randomUUID();
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
        UUID userId = UUID.randomUUID();
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
        UUID userId = UUID.randomUUID();
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
        UUID userId = UUID.randomUUID();
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
}
