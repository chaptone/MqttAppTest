package com.example.chapmac.rakkan.mqtt_app_test.Menu;

public class BottomMenuItem {

    private int imageResource;
    private String text1;
    private int imageResource1;

    public BottomMenuItem(int imageResource, String text1, int imageResource1) {
        this.imageResource = imageResource;
        this.text1 = text1;
        this.imageResource1 = imageResource1;
    }

    public BottomMenuItem() {
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

    public int getImageResource1() {
        return imageResource1;
    }

    public void setImageResource1(int imageResource1) {
        this.imageResource1 = imageResource1;
    }
}
