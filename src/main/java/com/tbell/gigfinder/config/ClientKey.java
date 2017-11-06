package com.tbell.gigfinder.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.ResourceBundle;

@Configuration
@PropertySource(value = {"classpath:apikey.properties"})
public class ClientKey {

    ResourceBundle bundle = ResourceBundle.getBundle("apikey");
    private final String STATIC_API_KEY = bundle.getString("STATIC_MAP_KEY");

    public String getSTATIC_API_KEY() {
        return STATIC_API_KEY;
    }
}
