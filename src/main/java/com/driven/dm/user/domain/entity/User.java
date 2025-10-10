package com.driven.dm.user.domain.entity;

import com.driven.dm.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@Table(name = "p_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    public User(String username, String nickname, String password, UserRole role,
        UserStatus status) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    public static User of(String username, String password, String nickname) {
        return new User(username, nickname, password, UserRole.CUSTOMER, UserStatus.ACTIVE);
    }

    public boolean isNickNameChanged(String nickname) {
        return !this.nickname.equals(nickname);
    }

    public void updateNickName(String nickname) {
        this.nickname = nickname;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void deactivate() {
        super.delete(this.id);
        this.status = UserStatus.DELETED;
    }

    public void changeRole(UserRole role) {
        this.role = role;
    }
}