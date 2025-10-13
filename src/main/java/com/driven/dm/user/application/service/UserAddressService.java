package com.driven.dm.user.application.service;

import com.driven.dm.global.entity.Address;
import com.driven.dm.global.exception.AppException;
import com.driven.dm.user.application.exception.UserErrorCode;
import com.driven.dm.user.domain.entity.User;
import com.driven.dm.user.domain.entity.UserAddress;
import com.driven.dm.user.infrastructure.repository.UserAddressRepository;
import com.driven.dm.user.infrastructure.repository.UserRepository;
import com.driven.dm.user.presentation.dto.request.UserAddressCreateRequest;
import com.driven.dm.user.presentation.dto.request.UserAddressUpdateRequest;
import com.driven.dm.user.presentation.dto.response.UserAddressResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAddressService {

    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;

    private final static int MAX_USER_ADDRESS_COUNT = 10;

    @Transactional
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

    @Transactional(readOnly = true)
    public List<UserAddressResponse> getAddresses(UUID userId) {
        List<UserAddress> addresses =
            userAddressRepository.findAllByUserIdAndDeletedAtIsNullOrderByIsDefaultDescCreatedAtDesc(
                userId);

        return addresses.stream()
            .map(UserAddressResponse::from)
            .toList();
    }

    @Transactional
    public UserAddressResponse updateAddress(UUID userId, UUID addressId,
        UserAddressUpdateRequest request) {
        UserAddress userAddress = userAddressRepository.findByIdAndUser_IdAndDeletedAtIsNull(
                addressId, userId)
            .orElseThrow(() -> AppException.of(UserErrorCode.USER_ADDRESS_NOT_FOUND));

        userAddress.updateAddress(
            request.zipCode(),
            request.primaryAddress(),
            request.detailAddress()
        );

        if (Boolean.TRUE.equals(request.isDefault()) && !userAddress.isDefault()) {
            userAddress.markAsDefault();
            userAddressRepository.clearDefaultOfUserExcept(userId, addressId);
        }

        return UserAddressResponse.from(userAddress);
    }

    private void assertUserAddressLimitNotExceeded(UUID userId) {
        long count = userAddressRepository.countByUser_IdAndDeletedAtIsNull(userId);
        if (count >= MAX_USER_ADDRESS_COUNT) {
            throw new AppException(UserErrorCode.MAX_ADDRESS_REACHED);
        }
    }
}
