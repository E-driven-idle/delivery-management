package com.driven.dm.user.infrastructure.repository;

import com.driven.dm.user.domain.entity.User;
import com.driven.dm.user.domain.entity.UserStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);

    boolean existsByNickname(String nickname);

    Optional<User> findByIdAndStatus(UUID id, UserStatus active);

    List<User> findAllByUsernameOrNickname(String username, String nickname);

    @Query(
        value = "select p_user.* "
            + "from ("
            + " select p_user.user_id, p_user.created_at "
            + " from p_user "
            + " order by p_user.created_at desc "
            + " limit :limit offset :offset"
            + ") t left join p_user on t.user_id = p_user.user_id",
        nativeQuery = true
    )
    List<User> findAll(
        @Param("offset") Long offset,
        @Param("limit") Long limit
    );
}