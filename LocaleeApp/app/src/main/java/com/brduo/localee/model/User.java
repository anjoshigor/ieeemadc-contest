package com.brduo.localee.model;

import java.util.Date;
import java.util.List;

/**
 * Created by anjoshigor on 06/07/17.
 */

public class User {
    public String _id;
    public String email;
    public String password;
    public String name;

    public List<EventCreated> eventsCreated;
    public Date updatedAt;
    public Date createdAt;
    public String photoUrl;

    public User() {

    }

    public User(String email, String password, String name, String photoUrl) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.photoUrl = photoUrl;
    }

    public User(String _id, String email, String password, String name, List<EventCreated> eventsCreated, Date updatedAt, Date createdAt, String photoUrl) {
        this._id = _id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.eventsCreated = eventsCreated;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.photoUrl = photoUrl;
    }

    @Override
    public String toString() {
        return "User{" +
                "_id='" + _id + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", eventsCreated=" + eventsCreated +
                ", updatedAt=" + updatedAt +
                ", createdAt=" + createdAt +
                ", photoUrl='" + photoUrl + '\'' +
                '}';
    }
}
