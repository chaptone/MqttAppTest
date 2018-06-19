package com.example.chapmac.rakkan.mqtt_app_test.Menu;

public class BottomMenuItem {

    private int imageResource;
    private String text1;

    public BottomMenuItem() {
    }

    public BottomMenuItem(int imageResource, String text1) {
        this.imageResource = imageResource;
        this.text1 = text1;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }
}
