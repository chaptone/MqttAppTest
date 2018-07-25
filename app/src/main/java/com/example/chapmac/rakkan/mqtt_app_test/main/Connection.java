package com.example.chapmac.rakkan.mqtt_app_test.main;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

// This class for connection object.
public class Connection {

     private String id;
     private String name;
     private String host;
     private String port;
     private String user;
     private String pass;
     private int color;
     @ServerTimestamp Date time;

    public Connection() {
    }

    public Connection(String name, String host, String port, String user, String pass) {
        this.name = name;
        this.host = host;
        this.port = port;
        this.user = user;
        this.pass = pass;
        this.color = ColorGenerator.MATERIAL.getRandomColor();
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getColor() {
        return color;
    }
}
