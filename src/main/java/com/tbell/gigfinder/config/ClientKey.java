package com.tbell.gigfinder.config;

public class ClientKey {


    private final String API_KEY = System.getenv("googleKey");

    public ClientKey() {}

    public String getAPI_KEY() {
        return API_KEY;
    }

//    System.getenv("string evn name of key")
}
