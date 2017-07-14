package com.brduo.localee.model;


import java.util.Date;
import java.util.Date;

import com.brduo.localee.util.EventCategory;


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
    public String city, country;
    public Date startDate, endDate;
    public float lat, lng;
    public UserSimplified createdBy;
    public Date updatedAt;
    public Date createdAt;

    public Event(){

    }

    public Event(String _id, String name, String address, String description, String category,
                 String photoUrl, String city, String country, Date startDate, Date endDate,
                 float  lat, float lng, UserSimplified createdBy, Date updatedAt, Date createdAt) {
        this._id = _id;
        this.name = name;
        this.address = address;
        this.description = description;
        this.category = category;
        this.photoUrl = photoUrl;
        this.city = city;
        this.country = country;
        this.startDate = startDate;
        this.endDate = endDate;
        this.lat = lat;
        this.lng = lng;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Event{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", lat=" + lat +
                ", lng=" + lng +
                ", createdBy=" + createdBy +
                ", updatedAt=" + updatedAt +
                ", createdAt=" + createdAt +
                '}';
    }
}
