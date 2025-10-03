package com.driven.dm.user.application.service;

import com.driven.dm.global.exception.AppException;
import com.driven.dm.user.application.exception.UserErrorCode;
import com.driven.dm.user.domain.entity.User;
import com.driven.dm.user.domain.entity.UserStatus;
import com.driven.dm.user.infrastructure.repository.UserRepository;
import com.driven.dm.user.presentation.controller.dto.request.UserUpdateRequest;
import com.driven.dm.user.presentation.controller.dto.response.UserResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserResponse getUser(UUID id) {
        return UserResponse.from(findActiveUser(id));
    }

    @Transactional
    public UserResponse updateUser(UUID id, UserUpdateRequest request) {
        User findUser = findActiveUser(id);

        String nickname = request.nickname();
        String password = request.password();

        if (validateNickName(findUser, nickname)) {
            findUser.updateNickName(nickname);
        }

        if (validatePassword(findUser, password)) {
            findUser.updatePassword(passwordEncoder.encode(password));
        }

        return UserResponse.from(findUser);
    }

    private boolean validatePassword(User user, String password) {
        if (!StringUtils.hasText(password) || passwordEncoder.matches(password,
            user.getPassword())) {
            return false;
        }
        return true;
    }

    private boolean validateNickName(User user, String nickname) {
        if (!StringUtils.hasText(nickname) || !user.isNickNameChanged(nickname)) {
            return false;
        }
        checkDuplicateNickName(nickname);
        return true;
    }

    private User findActiveUser(UUID id) {
        User findUser = userRepository.findByIdAndStatus(id, UserStatus.ACTIVE)
            .orElseThrow(() -> AppException.of(UserErrorCode.USER_NOT_FOUND));
        return findUser;
    }

    private void checkDuplicateNickName(String nickName) {
        if (userRepository.existsByNickname(nickName)) {
            throw AppException.of(UserErrorCode.DUPLICATE_NICK_NAME);
        }
    }


}
