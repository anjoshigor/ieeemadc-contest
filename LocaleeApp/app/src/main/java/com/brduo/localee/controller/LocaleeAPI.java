package com.brduo.localee.controller;

import com.brduo.localee.model.Event;
import com.brduo.localee.model.EventResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by anjoshigor on 05/07/17.
 */

public interface LocaleeAPI {

    String LOCALEE_BASE_URL = "https://localee-api.herokuapp.com/";

    @GET("events/")
    Call<EventResponse> getEvents();
}
