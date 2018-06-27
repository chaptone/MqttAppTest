package com.example.chapmac.rakkan.mqtt_app_test.Subscribe;

import com.google.firebase.firestore.Exclude;

public class SubscribeItem {

    private String documentId;
    private int image;
    private String topic;
    private String time;

    public SubscribeItem() {
    }

    public SubscribeItem(int image, String topic, String time) {

        this.image = image;
        this.topic = topic;
        this.time = time;
    }

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public void changeText1(String text){
        time = text;
    }

    public int getImage() {
        return image;
    }

    public String getTopic() {
        return topic;
    }

    public String getTime() {
        return time;
    }
}
