package com.brduo.localee.model;

import java.util.Date;

/**
 * Created by anjoshigor on 06/07/17.
 */

public class EventSimplified {
    public String _id;
    public String name;
    public String photoUrl;
    public Date startDate;
    public Date endDate;
    public String category;

    public EventSimplified(String _id, String name, String photoUrl, Date startDate, Date endDate,
                           String category) {
        this._id = _id;
        this.name = name;
        this.photoUrl = photoUrl;
        this.startDate = startDate;
        this.endDate = endDate;
        this.category = category;
    }
}