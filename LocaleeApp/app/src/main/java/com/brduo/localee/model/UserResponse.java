package com.brduo.localee.model;

import java.util.List;

/**
 * Created by anjoshigor on 06/07/17.
 */

public class UserResponse {
    public int total;
    public int limit;
    public int skip;

    public List<User> data;

    public UserResponse(int total, int limit, int skip, List<User> data) {
        this.total = total;
        this.limit = limit;
        this.skip = skip;
        this.data = data;
    }
}
