package com.driven.dm.global.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class Address {
    private String zipCode;
    private String primaryAddress;
    private String detailAddress;

    public Address(String zipCode, String primaryAddress, String detailAddress) {
        this.zipCode = zipCode;
        this.primaryAddress = primaryAddress;
        this.detailAddress = detailAddress;
    }

    public static Address create(String zipCode, String primaryAddress, String detailAddress) {
        return new Address(zipCode, primaryAddress, detailAddress);
    }
}
