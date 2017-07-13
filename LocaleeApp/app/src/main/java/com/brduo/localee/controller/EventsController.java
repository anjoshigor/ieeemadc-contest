package com.brduo.localee.controller;

import android.util.Log;

import com.brduo.localee.model.Event;
import com.brduo.localee.model.EventResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by anjoshigor on 05/07/17.
 */

public class EventsController {
    private static EventsController instance;
    private static List<Event> currentEvents;

    public static EventsController getInstance() {
        if (instance == null) {
            instance = new EventsController();
        }

        return instance;
    }

    private EventsController() {
        currentEvents = new ArrayList<>();
    }

    public static List<Event> getCurrentEvents() {
        return currentEvents;
    }

    public static void setCurrentEvents(List<Event> events) {
        currentEvents = events;
    }

}
