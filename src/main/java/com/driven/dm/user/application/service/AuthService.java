package com.driven.dm.user.application.service;

import com.driven.dm.global.exception.AppException;
import com.driven.dm.user.application.exception.UserErrorCode;
import com.driven.dm.user.domain.entity.User;
import com.driven.dm.user.infrastructure.repository.UserRepository;
import com.driven.dm.user.presentation.controller.dto.request.UserCreateRequest;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UUID signUp(UserCreateRequest request) {
        validateDuplicateUsernameAndNickname(request.username(), request.nickname());

        User savedUser = userRepository.save(
            User.of(
                request.username(),
                passwordEncoder.encode(request.password()),
                request.nickname())
        );
        return savedUser.getId();
    }

    private void validateDuplicateUsernameAndNickname(String username, String nickname) {
        List<User> users = userRepository.findAllByUsernameOrNickname(username, nickname);

        boolean usernameDuplicate = users.stream()
            .anyMatch(user -> user.getUsername().equals(username));
        if (usernameDuplicate) {
            throw AppException.of(UserErrorCode.DUPLICATE_USER_NAME);
        }

        boolean nicknameDuplicate = users.stream()
            .anyMatch(user -> user.getNickname().equals(nickname));
        if (nicknameDuplicate) {
            throw AppException.of(UserErrorCode.DUPLICATE_NICK_NAME);
        }
    }
}
