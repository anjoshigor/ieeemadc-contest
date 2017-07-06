package com.brduo.localee.model;

import java.util.Calendar;

/**
 * Created by anjoshigor on 06/07/17.
 */

public class EventSimplified {
    public String _id;
    public String name;
    public String photoUrl;
    public Calendar date;

    public EventSimplified(String _id, String name, String photoUrl, Calendar date) {
        this._id = _id;
        this.name = name;
        this.photoUrl = photoUrl;
        this.date = date;
    }
}