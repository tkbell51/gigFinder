package com.tbell.gigfinder.googleAPI;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleGeoResult {

    private GoogleGeoAddressComponent [] address_components;
    private String formatted_address;
    private GoogleGeoGeometry geometry;
    private Boolean partial_match;
    private String place_id;
    private String [] types;

    public GoogleGeoResult() {}

    public GoogleGeoAddressComponent[] getAddress_components() {
        return address_components;
    }

    public void setAddress_components(GoogleGeoAddressComponent[] address_components) {
        this.address_components = address_components;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public GoogleGeoGeometry getGeometry() {
        return geometry;
    }

    public void setGeometry(GoogleGeoGeometry geometry) {
        this.geometry = geometry;
    }

    public Boolean getPartial_match() {
        return partial_match;
    }

    public void setPartial_match(Boolean partial_match) {
        this.partial_match = partial_match;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return "GoogleGeoResult{" +
                "address_components=" + Arrays.toString(address_components) +
                ", formatted_address='" + formatted_address + '\'' +
                ", geometry=" + geometry +
                ", partial_match=" + partial_match +
                ", place_id='" + place_id + '\'' +
                ", types=" + Arrays.toString(types) +
                '}';
    }
}
