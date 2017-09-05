package com.tbell.gigfinder.googleAPI;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class GoogleGeoBounds {

    private GoogleGeoLatLng northeast;
    private GoogleGeoLatLng southwest;

    public GoogleGeoBounds() {
    }

    public GoogleGeoLatLng getNortheast() {
        return northeast;
    }

    public void setNortheast(GoogleGeoLatLng northeast) {
        this.northeast = northeast;
    }

    public GoogleGeoLatLng getSouthwest() {
        return southwest;
    }

    public void setSouthwest(GoogleGeoLatLng southwest) {
        this.southwest = southwest;
    }

    @Override
    public String toString() {
        return "GoogleGeoBounds{" +
                "northeast=" + northeast +
                ", southwest=" + southwest +
                '}';
    }
}
