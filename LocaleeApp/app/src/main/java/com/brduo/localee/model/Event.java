package com.brduo.localee.model;

import android.util.Pair;

import java.util.Calendar;

/**
 * Created by anjoshigor on 25/06/17.
 */

public class Event {

    public String _id;
    public String name;
    public String address;
    public String description;
    public String category;
    public Pair<Integer, Integer> coords;
    public String photoUrl;
    public Calendar date;
    public UserSimplified createdBy;
    public Calendar updatedAt;
    public Calendar createdAt;

    public Event(String _id, String name, String address, String description, String category, Pair<Integer, Integer> coords, String photoUrl, Calendar date, UserSimplified ceratedBy, Calendar updatedAt, Calendar createdAt) {
        this._id = _id;
        this.name = name;
        this.address = address;
        this.description = description;
        this.category = category;
        this.coords = coords;
        this.photoUrl = photoUrl;
        this.date = date;
        this.createdBy = ceratedBy;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }
}
