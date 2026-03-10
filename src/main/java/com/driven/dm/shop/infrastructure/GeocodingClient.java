package com.driven.dm.shop.infrastructure;

public interface GeocodingClient {

    record GeoPoint(double latitude, double longitude) {


    }

    GeoPoint convert(String address);

}
