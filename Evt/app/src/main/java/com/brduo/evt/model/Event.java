package com.brduo.evt.model;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by anjoshigor on 25/06/17.
 */

public class Event {
    private String name, address, imagePath;
    private Calendar date;
    private String shareLink;

    public Event(String name, String address, String imagePath, Calendar date, String shareLink) {
        this.name = name;
        this.address = address;
        this.imagePath = imagePath;
        this.date = date;
        this.shareLink = shareLink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public String getShareLink() {
        return shareLink;
    }

    public void setShareLink(String shareLink) {
        this.shareLink = shareLink;
    }

}
