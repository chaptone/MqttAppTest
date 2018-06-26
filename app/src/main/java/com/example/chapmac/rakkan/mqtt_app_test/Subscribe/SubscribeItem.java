package com.example.chapmac.rakkan.mqtt_app_test.Subscribe;

public class SubscribeItem {

    private int image;
    private String topic;
    private String description;

    public SubscribeItem(int image, String topic, String description) {

        this.image = image;
        this.topic = topic;
        this.description = description;
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
