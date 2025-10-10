package com.driven.dm.user.application.service;

import com.driven.dm.global.entity.Address;
import com.driven.dm.global.exception.AppException;
import com.driven.dm.user.application.exception.UserErrorCode;
import com.driven.dm.user.domain.entity.User;
import com.driven.dm.user.domain.entity.UserAddress;
import com.driven.dm.user.infrastructure.repository.UserAddressRepository;
import com.driven.dm.user.infrastructure.repository.UserRepository;
import com.driven.dm.user.presentation.controller.dto.request.UserAddressCreateRequest;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAddressService {

    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;

    private final static int MAX_USER_ADDRESS_COUNT = 10;

    public UUID createUserAddress(UUID userId, UserAddressCreateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw AppException.of(UserErrorCode.USER_NOT_FOUND);
        });

        assertUserAddressLimitNotExceeded(userId);

        userAddressRepository.clearDefaultByUserId(userId);

        UserAddress userAddress = UserAddress.createDefault(
            user,
            Address.create(
                request.zipCode(),
                request.primaryAddress(),
                request.detailAddress()
            )
        );

        userAddressRepository.save(userAddress);
        return userAddress.getId();
    }

    private void assertUserAddressLimitNotExceeded(UUID userId) {
        long count = userAddressRepository.countByUser_IdAndDeletedAtIsNull(userId);
        if (count >= MAX_USER_ADDRESS_COUNT) {
            throw new AppException(UserErrorCode.MAX_ADDRESS_REACHED);
        }
    }
}
