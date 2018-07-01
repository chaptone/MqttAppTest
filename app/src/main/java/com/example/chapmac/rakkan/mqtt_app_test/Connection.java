package com.example.chapmac.rakkan.mqtt_app_test;

import com.google.firebase.firestore.Exclude;

import org.parceler.Parcel;

@Parcel
public class Connection {
    String id;
    String host;
    String port;
    String user;
    String pass;
    String status;

    public Connection() {
    }

    public Connection(String id, String host, String port, String user, String pass, String status) {
        this.id = id;
        this.host = host;
        this.port = port;
        this.user = user;
        this.pass = pass;
        this.status = status;
    }

    public Connection(String host, String port, String user, String pass) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.pass = pass;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
