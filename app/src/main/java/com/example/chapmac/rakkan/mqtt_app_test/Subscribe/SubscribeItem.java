package com.example.chapmac.rakkan.mqtt_app_test.Subscribe;

import com.google.firebase.firestore.Exclude;

public class SubscribeItem {

    private String documentId;
    private int image;
    private String topic;
    private String description;

    public SubscribeItem() {
    }

    public SubscribeItem(int image, String topic, String description) {

        this.image = image;
        this.topic = topic;
        this.description = description;
    }

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public void changeText1(String text){
        description = text;
    }

    public int getImage() {
        return image;
    }

    public String getTopic() {
        return topic;
    }

    public String getDescription() {
        return description;
    }
}
