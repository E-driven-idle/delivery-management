package com.driven.dm.user.infrastructure.repository;

import com.driven.dm.user.domain.entity.UserAddress;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserAddressRepository extends JpaRepository<UserAddress, UUID> {

    long countByUser_IdAndDeletedAtIsNull(UUID userId);

    @Query("""
          update UserAddress ua
             set ua.isDefault = false
           where ua.user.id = :userId
             and ua.isDefault = true
             and ua.deletedAt is null
        """)
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    int clearDefaultByUserId(@Param("userId") UUID userId);


    @Query("""
            update UserAddress ua
               set ua.isDefault = false
             where ua.user.id = :userId
               and ua.id <> :keepId
               and ua.deletedAt is null
        """)
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    int clearDefaultOfUserExcept(@Param("userId") UUID userId, @Param("keepId") UUID keepId);

    List<UserAddress> findAllByUserIdAndDeletedAtIsNullOrderByIsDefaultDescCreatedAtDesc(
        UUID userId);

    Optional<UserAddress> findByIdAndUser_IdAndDeletedAtIsNull(UUID addressId, UUID userId);
}
