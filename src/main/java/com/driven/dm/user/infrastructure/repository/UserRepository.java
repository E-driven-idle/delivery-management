package com.driven.dm.user.infrastructure.repository;

import com.driven.dm.user.domain.entity.User;
import com.driven.dm.user.domain.entity.UserStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);

    boolean existsByNickname(String nickname);

    Optional<User> findByIdAndStatus(UUID id, UserStatus active);

    List<User> findAllByUsernameOrNickname(String username, String nickname);
}