package com.tbell.gigfinder.config;



public class ClientKey {

    private final String STATIC_API_KEY = System.getenv("googleKey");


    public ClientKey() {
    }

    public  String getSTATIC_API_KEY() {
        return STATIC_API_KEY;
    }


}
