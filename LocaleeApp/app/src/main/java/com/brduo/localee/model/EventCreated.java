package com.brduo.localee.model;

import java.util.List;

/**
 * Created by anjoshigor on 15/07/17.
 */

public class EventCreated {
    public String _id;
    public EventSimplified event;

    public EventCreated(String _id, EventSimplified event) {
        this._id = _id;
        this.event = event;
    }

    @Override
    public String toString() {
        return "EventCreated{" +
                "_id='" + _id + '\'' +
                ", event=" + event +
                '}';
    }
}
