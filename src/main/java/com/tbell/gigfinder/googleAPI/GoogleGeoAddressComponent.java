package com.tbell.gigfinder.googleAPI;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleGeoAddressComponent {
    private String long_name;
    private String short_name;
    private String [] types;

    public GoogleGeoAddressComponent() {
    }

    public String getLong_name() {
        return long_name;
    }

    public void setLong_name(String long_name) {
        this.long_name = long_name;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return "GoogleGeoAddressComponent{" +
                "long_name='" + long_name + '\'' +
                ", short_name='" + short_name + '\'' +
                ", types=" + Arrays.toString(types) +
                '}';
    }
}
