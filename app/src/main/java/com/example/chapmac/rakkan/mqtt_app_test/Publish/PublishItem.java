package com.example.chapmac.rakkan.mqtt_app_test.Publish;

public class PublishItem {

    private String topic;
    private String message;

    public PublishItem() {
    }

    public PublishItem(String topic, String message) {
        this.topic = topic;
        this.message = message;
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
