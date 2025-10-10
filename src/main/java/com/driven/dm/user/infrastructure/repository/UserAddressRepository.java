package com.driven.dm.user.infrastructure.repository;

import com.driven.dm.user.domain.entity.UserAddress;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAddressRepository extends JpaRepository<UserAddress, UUID> {

    long countByUser_IdAndDeletedAtIsNull(UUID userId);

}
