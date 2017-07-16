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
import com.brduo.localee.controller.EventSimplifiedAdapter;
import com.brduo.localee.controller.EventsController;
import com.brduo.localee.controller.LocaleeAPI;
import com.brduo.localee.model.EventResponse;
import com.brduo.localee.model.EventSimplified;
import com.brduo.localee.model.User;
import com.brduo.localee.util.LocationTracker;
import com.brduo.localee.util.PreferenceManager;
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

public class YourEventsActivity extends AppCompatActivity {
    private RecyclerView eventListRecycler;
    private EventSimplifiedAdapter adapter;
    private EventsController controller;
    private ProgressBar progressBar;
    private List<EventSimplified> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_events);

        eventListRecycler = (RecyclerView) findViewById(R.id.event_list_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        progressBar = (ProgressBar) findViewById(R.id.list_progress);
        eventListRecycler.setHasFixedSize(true);
        eventListRecycler.setLayoutManager(layoutManager);
        adapter = new EventSimplifiedAdapter(controller.getCurrentEventsSimpfified(), this);
        eventListRecycler.setAdapter(adapter);


        getUserInfo(new PreferenceManager(this).getUserId());

    }

    private void getUserInfo(String id) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(LocaleeAPI.LOCALEE_BASE_URL)
                .build();

        LocaleeAPI api = retrofit.create(LocaleeAPI.class);


        Call<User> call = api.getUser(id);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    events = response.body().eventsCreated;
//                    controller.setCurrentEventsSimpfified(events);
//                    adapter.events = controller.getCurrentEventsSimpfified();
//                    eventListRecycler.setAdapter(adapter);
                    Log.i("Events Created", events.toString());
                    if(events.size() == 0){
                        Snackbar snackbar = Snackbar
                                .make(progressBar, R.string.no_events, Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                } else {
                    Log.e("RETROFIT", "Erro na listagem de eventos");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
                Log.e("RETROFIT", t.getMessage());
            }
        });
    }
}
