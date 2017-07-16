package com.brduo.localee.controller;

import com.brduo.localee.model.Event;
import com.brduo.localee.model.EventResponse;
import com.brduo.localee.model.User;
import com.brduo.localee.model.UserResponse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by anjoshigor on 05/07/17.
 */

public interface LocaleeAPI {

    String LOCALEE_BASE_URL = "https://localee-api.herokuapp.com/";

    @GET("events?$sort=endDate&$limit=50")
    Call<EventResponse> getEvents(@Query("startDate[$gte]") String dateGte);

    @GET("events?$sort=endDate&$limit=50")
    Call<EventResponse> getEventsBySingleCategory(@Query("category") String category);

    @GET("events?$sort=endDate&$limit=50")
    Call<EventResponse> getEventsByCategoryAndDate(@Query("category") String category, @Query("endDate[$gte]") String dateFrom, @Query("endDate[$lte]") String dateTo);

    @GET("events?$sort=endDate&$limit=50")
    Call<EventResponse> getEventsByDateBetween(@Query("endDate[$gte]") String dateFrom, @Query("endDate[$lte]") String dateTo);

    @GET("events/{id}")
    Call<Event> getEvent(@Path("id") String id);

    @GET("users/{id}")
    Call<User> getUser(@Path("id") String id);

    @POST("users/")
    Call<User> postUser(@Body User user);

    @POST("events/")
    Call<Event> postEvent(@Body Event event);
}
