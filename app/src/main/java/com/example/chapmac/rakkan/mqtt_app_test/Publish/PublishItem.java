package com.example.chapmac.rakkan.mqtt_app_test.Publish;

import com.google.firebase.firestore.Exclude;

public class PublishItem {

    private String documentId;
    private int image;
    private String topic;
    private String message;
    private String time;

    public PublishItem() {

    }

    public PublishItem(int image, String topic, String message, String time) {
        this.image = image;
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

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
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
