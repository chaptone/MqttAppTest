package com.example.chapmac.rakkan.mqtt_app_test.Home;

import com.google.firebase.firestore.Exclude;

public class HomeItem {

    private String documentId;
    private String topic;
    private String message;
    private String time;

    public HomeItem() {
    }

    public HomeItem(String topic, String message, String time) {
        this.topic = topic;
        this.message = message;
        this.time = time;
    }

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
