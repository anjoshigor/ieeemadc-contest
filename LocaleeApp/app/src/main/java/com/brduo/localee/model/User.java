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

    public List<EventSimplified> eventsCreated;
    public Date updatedAt;
    public Date createdAt;
    public String photoUrl;

    public User(String _id, String email, String password, String name, List<EventSimplified> eventsCreated, Date updatedAt, Date createdAt, String photoUrl) {
        this._id = _id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.eventsCreated = eventsCreated;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.photoUrl = photoUrl;
    }
}
