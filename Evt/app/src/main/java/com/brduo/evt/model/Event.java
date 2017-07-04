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
    private int Id;

    public Event(int id, String name, String address, String imagePath, Calendar date, String shareLink) {
        this.Id = id;
        this.name = name;
        this.address = address;
        this.imagePath = imagePath;
        this.date = date;
        this.shareLink = shareLink;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
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
