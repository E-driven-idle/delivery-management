package com.driven.dm.user.domain.entity;

import com.driven.dm.global.entity.Address;
import com.driven.dm.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@Table(name = "p_user_address")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAddress extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Embedded
    private Address address;

    @Column(nullable = false)
    private boolean isDefault;

    private UserAddress(User user, Address address, boolean isDefault) {
        this.user = user;
        this.address = address;
        this.isDefault = isDefault;
    }

    public static UserAddress createDefault(User user, Address address) {
        return new UserAddress(user, address, true);
    }
}
