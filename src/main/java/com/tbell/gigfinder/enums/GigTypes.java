package com.tbell.gigfinder.enums;

public enum GigTypes {
    STREET_DANCE("Street Dance"), BIRTHDAY("Birthday"), WEDDING("Wedding"), REUNION("Reunion"), ANNIVERSARY("Anniversary"), CORPORATE("Corporate"), OPENING("Opening Slot"), MULTI_BAND("Multi-Band Festival"),
    BAR("Bar"), CASINO("Casino"), CRUISE_SHIP("Cruise Ship"), LIVE_RADIO("Live Radio"), NATIONAL_TOUR("National Tour"),
    INTERNATIONAL_TOUR("International Tour"), OTHER("Other");

    private String type;

    GigTypes(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
