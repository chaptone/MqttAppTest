package com.example.chapmac.rakkan.mqtt_app_test.subscribe;

import com.google.firebase.firestore.Exclude;

// SubscribeItem object.
public class SubscribeItem {

    private String documentId;
    private String topic;
    private String time;

    public SubscribeItem() {
    }

    public SubscribeItem(String topic, String time) {
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

    public String getTopic() {
        return topic;
    }

    public String getTime() {
        return time;
    }
}
