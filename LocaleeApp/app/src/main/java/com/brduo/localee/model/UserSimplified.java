package com.brduo.localee.model;

/**
 * Created by anjoshigor on 05/07/17.
 */

public class UserSimplified {
    public int _id;
    public String name;
    public String email;
    public String photoUrl;

    public UserSimplified(int _id, String name, String email, String photoUrl) {
        this._id = _id;
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
    }

}