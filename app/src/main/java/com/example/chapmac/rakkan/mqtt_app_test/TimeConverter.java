package com.example.chapmac.rakkan.mqtt_app_test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeConverter {

    private String nowDate;
    private String nowWeek;
    private String nowMonth;
    private String nowYear;

    public TimeConverter() {

        nowDate = new SimpleDateFormat("dd")
                .format(Calendar.getInstance().getTime());
        nowWeek = new SimpleDateFormat("W")
                .format(Calendar.getInstance().getTime());
        nowMonth = new SimpleDateFormat("MM")
                .format(Calendar.getInstance().getTime());
        nowYear = new SimpleDateFormat("yyyy")
                .format(Calendar.getInstance().getTime());
    }

    public String convertTimeFrom(String time){

        String[] part = time.split(" ");
        String[] tmp = part[0].split("/");
        String date = tmp[0];
        String week = part[3];
        String month = tmp[1];
        String year = tmp[2];

        String[] tmp1 = part[2].split(":");
        String nameDate = tmp1[0];
        String nameMonth = tmp1[1];

        if(!nowYear.equals(year)){
            //e.g. 25/06/2018
            return part[0];
        }
        if(!nowMonth.equals(month)){
            //e.g. 25 Jun
            return date + " " + nameMonth;
        }
        if(!nowWeek.equals(week)){
            //e.g. 25 Jun
            return date + " " + nameMonth;
        }
        if(!nowDate.equals(date)){
            //e.g. Wed
            return nameDate;
        }
        String[] tmp2 = part[1].split(":");
        String timeH = tmp2[0];
        String timeM = tmp2[1];

        // Same date e.g. 11:40
        return timeH+":"+timeM;
    }
}
