package com.example.chapmac.rakkan.mqtt_app_test.main;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import io.t28.shade.converter.Converter;

public class ConnectionConverter implements Converter<Connection,String>{
    @NonNull
    @Override
    public Connection toConverted(@Nullable String s) {
        return new Gson().fromJson(s, Connection.class);
    }

    @NonNull
    @Override
    public String toSupported(@Nullable Connection connection) {
        return new Gson().toJson(connection, Connection.class);
    }
}
