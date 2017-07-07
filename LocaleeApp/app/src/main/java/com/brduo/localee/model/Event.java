package com.brduo.localee.model;

import android.util.Pair;

import java.util.Date;
import java.util.Date;

/**
 * Created by anjoshigor on 25/06/17.
 */

public class Event {

    public String _id;
    public String name;
    public String address;
    public String description;
    public String category;
    public String photoUrl;
    public Date date;
    public UserSimplified createdBy;
    public Date updatedAt;
    public Date createdAt;

    public Event(String _id, String name, String address, String description, String category, String photoUrl, Date date, UserSimplified createdBy, Date updatedAt, Date createdAt) {
        this._id = _id;
        this.name = name;
        this.address = address;
        this.description = description;
        this.category = category;
        this.photoUrl = photoUrl;
        this.date = date;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }
}
