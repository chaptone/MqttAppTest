package com.example.chapmac.rakkan.mqtt_app_test;

public class SubscribeItem {

    private int mImageResource;
    private String mtext1;
    private String mtext2;

    public SubscribeItem(int mImageResource, String mtext1, String mtext2) {

        this.mImageResource = mImageResource;
        this.mtext1 = mtext1;
        this.mtext2 = mtext2;
    }

    public int getmImageResource() {
        return mImageResource;
    }

    public String getMtext1() {
        return mtext1;
    }

    public String getMtext2() {
        return mtext2;
    }
}
