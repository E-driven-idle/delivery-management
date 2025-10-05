package com.driven.dm.user.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

import com.driven.dm.global.exception.AppException;
import com.driven.dm.user.application.exception.UserErrorCode;
import com.driven.dm.user.domain.entity.User;
import com.driven.dm.user.domain.entity.UserRole;
import com.driven.dm.user.infrastructure.repository.UserRepository;
import com.driven.dm.user.presentation.dto.request.UserCreateRequest;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    AuthService authService;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Nested
    class signUp {

        private UUID userId;
        private String username;
        private String password;
        private String nickname;
        private String encodedPassword;

        private UserCreateRequest request;

        @BeforeEach
        void setUp() {
            userId = UUID.randomUUID();
            username = "sejun";
            password = "password12";
            nickname = "sejunO";
            encodedPassword = "encodedpassword12";

            request = UserCreateRequest.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .build();
        }

        @Test
        @DisplayName("회원가입 성공시 인코딩된 비밀번호로 저장된다.")
        void signUp_saveWithEncodedPassword() {
            given(passwordEncoder.encode(password)).willReturn(encodedPassword);
            given(userRepository.findAllByUsernameOrNickname(username, nickname)).willReturn(
                List.of());

            User userMock = mock(User.class);
            given(userMock.getId()).willReturn(userId);

            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
            given(userRepository.save(userCaptor.capture())).willReturn(userMock);

            UUID result = authService.signUp(request);

            assertThat(result).isEqualTo(userId);

            User savedUser = userCaptor.getValue();
            assertThat(savedUser.getUsername()).isEqualTo(username);
            assertThat(savedUser.getNickname()).isEqualTo(nickname);
            assertThat(savedUser.getPassword()).isEqualTo(encodedPassword);
            assertThat(savedUser.getRole()).isEqualTo(UserRole.CUSTOMER);

            then(passwordEncoder).should().encode(password);
            then(userRepository).should().findAllByUsernameOrNickname(username, nickname);
            then(userRepository).should().save(any(User.class));
        }

        @Test
        @DisplayName("중복된 ID를 사용하면 회원가입에 실패한다")
        void signUp_fails_withDuplicateId() {
            User duplicateIdUser = mock(User.class);
            given(duplicateIdUser.getUsername()).willReturn(username);
            given(userRepository.findAllByUsernameOrNickname(username, nickname)).willReturn(
                List.of(duplicateIdUser));

            assertThatThrownBy(() -> {
                authService.signUp(request);
            }).isInstanceOf(AppException.class)
                .hasMessage(UserErrorCode.DUPLICATE_USER_NAME.getMessage());

            then(userRepository).should(never()).save(any(User.class));
        }

        @Test
        @DisplayName("중복된 닉네임을 사용하면 회원가입에 실패한다.")
        void signUp_fails_withDuplicateNickname() {
            User duplicateNicknameUser = mock(User.class);
            given(duplicateNicknameUser.getUsername()).willReturn("");
            given(duplicateNicknameUser.getNickname()).willReturn(nickname);
            given(userRepository.findAllByUsernameOrNickname(username, nickname)).willReturn(
                List.of(duplicateNicknameUser));

            assertThatThrownBy(() -> {
                authService.signUp(request);
            }).isInstanceOf(AppException.class)
                .hasMessage(UserErrorCode.DUPLICATE_NICK_NAME.getMessage());

            then(userRepository).should(never()).save(any(User.class));
        }
    }
}