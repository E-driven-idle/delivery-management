package com.driven.dm.user.application.service;

import com.driven.dm.global.exception.AppException;
import com.driven.dm.user.application.exception.UserErrorCode;
import com.driven.dm.user.domain.entity.User;
import com.driven.dm.user.domain.entity.UserStatus;
import com.driven.dm.user.infrastructure.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserReader {

    private final UserRepository userRepository;

    public User findActiveUser(UUID id) {

        User findUser = userRepository.findByIdAndStatus(id, UserStatus.ACTIVE)
            .orElseThrow(() -> AppException.of(UserErrorCode.USER_NOT_FOUND));

        return findUser;
    }
}
