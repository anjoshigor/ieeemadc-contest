package com.brduo.localee.view;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.ScaleAnimation;

import com.brduo.localee.controller.EventAdapter;
import com.brduo.localee.R;
import com.brduo.localee.controller.EventsController;
import com.brduo.localee.controller.LocaleeAPI;
import com.brduo.localee.model.Event;
import com.brduo.localee.util.EventCategory;
import com.brduo.localee.model.EventResponse;
import com.brduo.localee.util.LocationTracker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EventsListFragment extends Fragment {

    private RecyclerView eventListRecycler;
    private EventAdapter adapter;
    private LocationTracker locationTracker;
    private EventsController controller;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controller = EventsController.getInstance();
        Log.i("EventsListFragment", "Events size: "+controller.getCurrentEvents().size());
        // setContentView(R.layout.fragment_events_list);
        final View rootView = inflater.inflate(R.layout.fragment_events_list, viewGroup, false);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.color_title_toolbar));
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.search_bar_placeholder);
        setHasOptionsMenu(true);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(rootView.getContext());
                View mView = getActivity().getLayoutInflater().inflate(R.layout.dialog_search_event, null);

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });

        locationTracker = new LocationTracker(getContext());

        if (!locationTracker.hasLocation()) {
            locationTracker.showGPSActivation();
        }


        eventListRecycler = (RecyclerView) rootView.findViewById(R.id.event_list_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(rootView.getContext());

        eventListRecycler.setHasFixedSize(true);
        eventListRecycler.setLayoutManager(layoutManager);
        adapter = new EventAdapter(controller.getCurrentEvents(), locationTracker.getLocation(), getContext());
        eventListRecycler.setAdapter(adapter);


        getAllEvents();


        return rootView;
    }

    void getAllEvents() {
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
        Log.i("GETALLEVENTS",dateString);
        Call<EventResponse> call = api.getEvents(dateString);
        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                if (response.isSuccessful()) {
                    controller.setCurrentEvents(response.body().data);
                    adapter.events = controller.getCurrentEvents();
                    eventListRecycler.setAdapter(adapter);
                } else {
                    Log.e("RETROFIT", "Erro na listagem de eventos");
                }
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {
                t.printStackTrace();
                Log.e("RETROFIT", t.getMessage());
            }
        });
    }
}

