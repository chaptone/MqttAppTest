package com.example.chapmac.rakkan.mqtt_app_test.detail;

import com.google.firebase.firestore.Exclude;

// DetailItem object.
public class DetailItem {

    private String documentId;
    private String message;
    private String time;

    public DetailItem() {
    }

    public DetailItem(String message, String time) {
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
