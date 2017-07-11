package com.brduo.localee.controller;

import android.util.Log;

import com.brduo.localee.model.Event;
import com.brduo.localee.model.EventResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
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

public class EventsController implements Callback<EventResponse> {


    public void getAllEvents() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(LocaleeAPI.LOCALEE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        LocaleeAPI localeeApi = retrofit.create(LocaleeAPI.class);
        String dateString = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new Date());
        Log.i("GETALLEVENTS", dateString);
        Call<EventResponse> call = localeeApi.getEvents(dateString);
        call.enqueue(this);

    }

    @Override
    public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
        if (response.isSuccessful()) {
            List<Event> eventList = response.body().data;
            for (Event event : eventList) {
                Log.i("RESPOSTA", event.name);
            }
        } else {
            System.out.println(response.errorBody());
        }
    }

    @Override
    public void onFailure(Call<EventResponse> call, Throwable t) {
        Log.e("RESPOSTA", t.getMessage());
        t.printStackTrace();
    }
}
