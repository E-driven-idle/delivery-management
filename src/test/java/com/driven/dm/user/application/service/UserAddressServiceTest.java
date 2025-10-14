package com.driven.dm.user.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import com.driven.dm.global.entity.Address;
import com.driven.dm.global.exception.AppException;
import com.driven.dm.user.application.exception.UserErrorCode;
import com.driven.dm.user.domain.entity.User;
import com.driven.dm.user.domain.entity.UserAddress;
import com.driven.dm.user.infrastructure.repository.UserAddressRepository;
import com.driven.dm.user.infrastructure.repository.UserRepository;
import com.driven.dm.user.presentation.dto.request.UserAddressCreateRequest;
import com.driven.dm.user.presentation.dto.response.UserAddressResponse;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserAddressServiceTest {

    @InjectMocks
    UserAddressService userAddressService;

    @Mock
    UserRepository userRepository;

    @Mock
    UserAddressRepository userAddressRepository;

    @Test
    @DisplayName("유저가 주소 생성 요청을 보내면 기본주소로 저장된다")
    void createUserAddressTest() {
        UUID userId = UUID.randomUUID();
        String zipCode = "00000";
        String primaryAddress = "서울특별시 동작구";
        String detailAddress = "우리집";

        UserAddressCreateRequest request = new UserAddressCreateRequest(zipCode, primaryAddress,
            detailAddress);

        User mockUser = mock(User.class);
        given(mockUser.getId()).willReturn(userId);

        UserAddress mockUserAddress = mock(UserAddress.class);
        ArgumentCaptor<UserAddress> userAddressCaptor = ArgumentCaptor.forClass(UserAddress.class);
        given(userAddressRepository.countByUser_IdAndDeletedAtIsNull(userId)).willReturn(0L);
        given(userRepository.findById(userId)).willReturn(Optional.of(mockUser));
        given(userAddressRepository.save(userAddressCaptor.capture())).willReturn(mockUserAddress);

        UUID result = userAddressService.createUserAddress(userId, request);

        UserAddress savedUserAddress = userAddressCaptor.getValue();

        assertThat(result).isEqualTo(savedUserAddress.getId());
        assertThat(savedUserAddress.getUser().getId()).isEqualTo(userId);
        assertThat(savedUserAddress.getAddress().getZipCode()).isEqualTo(zipCode);
        assertThat(savedUserAddress.getAddress().getPrimaryAddress()).isEqualTo(primaryAddress);
        assertThat(savedUserAddress.getAddress().getDetailAddress()).isEqualTo(detailAddress);
        assertThat(savedUserAddress.isDefault()).isTrue();

        then(userAddressRepository).should().countByUser_IdAndDeletedAtIsNull(userId);
        then(userRepository).should().findById(userId);
        then(userAddressRepository).should(times(1)).save(any(UserAddress.class));
    }

    @Test
    @DisplayName("주소가 10개 이상이면 추가 생성은 실패한다")
    void shouldFailToCreate_whenHas10Addresses() {
        UUID userId = UUID.randomUUID();
        String zipCode = "00000";
        String primaryAddress = "서울특별시 동작구";
        String detailAddress = "우리집";

        UserAddressCreateRequest request = new UserAddressCreateRequest(zipCode, primaryAddress,
            detailAddress);

        User mockUser = mock(User.class);
        given(userRepository.findById(userId)).willReturn(Optional.of(mockUser));

        given(userAddressRepository.countByUser_IdAndDeletedAtIsNull(userId)).willReturn(10L);

        assertThatThrownBy(() -> {
            userAddressService.createUserAddress(userId, request);
        }).isInstanceOf(AppException.class)
            .hasMessage(UserErrorCode.MAX_ADDRESS_REACHED.getMessage());

        then(userAddressRepository).should(never()).save(any(UserAddress.class));
    }

    @Test
    @DisplayName("주소를 조회한다")
    void shouldReturnList_whenAddressesExist() {
        UUID userId = UUID.randomUUID();
        UserAddress address = UserAddress.createDefault(
            mock(User.class),
            Address.create("00000", "서울시 강남구", "우리집")
        );

        given(
            userAddressRepository.findAllByUserIdAndDeletedAtIsNullOrderByIsDefaultDescCreatedAtDesc(
                userId))
            .willReturn(List.of(address));

        List<UserAddressResponse> result = userAddressService.getAddresses(userId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).isDefault()).isTrue();
        assertThat(result.get(0).primaryAddress()).isEqualTo("서울시 강남구");

        then(userAddressRepository).should()
            .findAllByUserIdAndDeletedAtIsNullOrderByIsDefaultDescCreatedAtDesc(userId);
    }

    @Test
    @DisplayName("주소가 없으면 빈 리스트 반환한다")
    void shouldReturnEmptyList_whenNoAddresses() {
        UUID userId = UUID.randomUUID();

        given(
            userAddressRepository.findAllByUserIdAndDeletedAtIsNullOrderByIsDefaultDescCreatedAtDesc(
                userId))
            .willReturn(List.of());

        List<UserAddressResponse> result = userAddressService.getAddresses(userId);

        assertThat(result).isEmpty();
        then(userAddressRepository).should()
            .findAllByUserIdAndDeletedAtIsNullOrderByIsDefaultDescCreatedAtDesc(userId);
    }
}