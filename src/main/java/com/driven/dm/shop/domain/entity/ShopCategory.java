package com.driven.dm.shop.domain.entity;

public enum ShopCategory {

    NONE("카테고리를 선택해주세요"),
    KOREAN("한식"),
    CHINESE("중식"),
    SNACK("분식"),
    CHICKEN("치킨"),
    PIZZA("피자");

    private final String displayName;

    ShopCategory(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName(){
        return this.displayName;
    }

}
