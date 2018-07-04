package com.example.chapmac.rakkan.mqtt_app_test.Menu;

public class BottomMenuItem {

    private int type;
    private int imageResource;
    private String text1;
    private String text2;
    private int imageResource1;

    public BottomMenuItem(int type ,int imageResource, String text1, String text2, int imageResource1) {
        this.type = type;
        this.imageResource = imageResource;
        this.text1 = text1;
        this.text2 = text2;
        this.imageResource1 = imageResource1;
    }

    public BottomMenuItem() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

    public int getImageResource1() {
        return imageResource1;
    }

    public void setImageResource1(int imageResource1) {
        this.imageResource1 = imageResource1;
    }
}
