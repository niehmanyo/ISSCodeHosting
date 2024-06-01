package com.iss.auth.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Login3rdAdapterManager {

    private final Map<String, Login3rdAdapter> adapters;

    @Autowired
    public Login3rdAdapterManager(Map<String, Login3rdAdapter> adapters) {
        this.adapters = adapters;
    }

    public Login3rdAdapter getAdapter(String type) {
        return adapters.get(type);
    }
}
