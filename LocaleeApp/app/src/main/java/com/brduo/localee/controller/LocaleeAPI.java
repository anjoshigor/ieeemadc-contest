package com.brduo.localee.controller;

import com.brduo.localee.model.Event;
import com.brduo.localee.model.EventResponse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by anjoshigor on 05/07/17.
 */

public interface LocaleeAPI {

    String LOCALEE_BASE_URL = "https://localee-api.herokuapp.com/";

    @GET("events?$sort=endDate&$limit=50")
    Call<EventResponse> getEvents(@Query("startDate[$gte]") String dateGte);

    @GET("events/{id}")
    Call<Event> getEvent(@Path("id") String id);
}
