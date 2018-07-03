package com.example.chapmac.rakkan.mqtt_app_test;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Connection {

     private String id;
     private String name;
     private String host;
     private String port;
     private String user;
     private String pass;
     private boolean connected;
     @ServerTimestamp Date time;

    public Connection() {
    }

    public Connection(String host, String port, String user, String pass) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.pass = pass;
        this.connected = false;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
