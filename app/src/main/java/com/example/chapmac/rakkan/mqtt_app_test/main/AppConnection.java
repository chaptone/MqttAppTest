package com.example.chapmac.rakkan.mqtt_app_test.main;

import io.t28.shade.annotation.Preferences;
import io.t28.shade.annotation.Property;

@Preferences("app.connection")
public abstract class AppConnection {
    @Property(key = "key_connection", converter = ConnectionConverter.class)
    public abstract Connection connection();
}