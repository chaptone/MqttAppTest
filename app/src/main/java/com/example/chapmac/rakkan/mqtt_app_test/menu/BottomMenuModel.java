package com.example.chapmac.rakkan.mqtt_app_test.menu;

public class BottomMenuModel {
    public static final int LARGE_TYPE=0;
    public static final int NORMAL_TYPE=1;
    public static final int QUIT_TYPE=2;

    public int type;
    public int data;
    public String text;

    public BottomMenuModel(int type, String text, int data)
    {
        this.type=type;
        this.data=data;
        this.text=text;
    }
}
