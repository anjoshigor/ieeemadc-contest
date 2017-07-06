package com.brduo.localee.model;

import java.util.List;

/**
 * Created by anjoshigor on 06/07/17.
 */

public class EventResponse {
    public int total;
    public int limit;
    public int skip;

    public List<Event> data;

    public EventResponse(int total, int limit, int skip, List<Event> data) {
        this.total = total;
        this.limit = limit;
        this.skip = skip;
        this.data = data;
    }
}
