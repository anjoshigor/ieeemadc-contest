package com.brduo.localee.view;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ProgressBar;

import com.brduo.localee.R;
import com.brduo.localee.controller.EventAdapter;
import com.brduo.localee.controller.EventsController;
import com.brduo.localee.controller.LocaleeAPI;
import com.brduo.localee.model.EventResponse;
import com.brduo.localee.util.LocationTracker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class YourEventsActivity extends AppCompatActivity {
    private RecyclerView eventListRecycler;
    private EventAdapter adapter;
    private LocationTracker locationTracker;
    private EventsController controller;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_events);

        eventListRecycler = (RecyclerView) findViewById(R.id.event_list_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        progressBar = (ProgressBar) findViewById(R.id.list_progress);
        eventListRecycler.setHasFixedSize(true);
        eventListRecycler.setLayoutManager(layoutManager);
        adapter = new EventAdapter(controller.getCurrentEvents(), null, this);
        eventListRecycler.setAdapter(adapter);


        getAllEvents();

    }

    void getAllEvents() {
        progressBar.setIndeterminate(true);
        Gson gson = new GsonBuilder()
                .setLenient()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(LocaleeAPI.LOCALEE_BASE_URL)
                .build();

        LocaleeAPI api = retrofit.create(LocaleeAPI.class);

        String dateString = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new Date());
        Log.i("GETALLEVENTS", dateString);
        Call<EventResponse> call = api.getEvents(dateString);
        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                if (response.isSuccessful()) {
                    controller.setCurrentEvents(response.body().data);
                    adapter.events = controller.getCurrentEvents();
                    eventListRecycler.setAdapter(adapter);
                    progressBar.setIndeterminate(false);

                    if (controller.getCurrentEvents().size() == 0) {
                        Snackbar snackbar = Snackbar
                                .make(progressBar, R.string.no_events, Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                } else {
                    Log.e("RETROFIT", "Erro na listagem de eventos");
                    progressBar.setIndeterminate(false);
                }
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {
                t.printStackTrace();
                Log.e("RETROFIT", t.getMessage());
                progressBar.setIndeterminate(false);
            }
        });
    }


}
