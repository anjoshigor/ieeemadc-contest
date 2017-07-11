package com.brduo.localee.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.brduo.localee.R;
import com.brduo.localee.controller.LocaleeAPI;
import com.brduo.localee.model.Event;
import com.brduo.localee.util.AlphaBackgroundCategory;
import com.brduo.localee.util.EmailClickListener;
import com.brduo.localee.util.StringsFormatter;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EventActivity extends AppCompatActivity implements OnMapReadyCallback {
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    ImageView eventImage, userImage;
    TextView categoryTv, addressTv, addressMapTv, descriptionTv, startDateTv;
    TextView userNameTv, contactTv;
    Intent intent;
    String id;
    boolean renderedFlag = false;
    Event actualEvent;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        eventImage = (ImageView) findViewById(R.id.image_toolbar);
        categoryTv = (TextView) findViewById(R.id.category);
        addressTv = (TextView) findViewById(R.id.address);
        addressMapTv = (TextView) findViewById(R.id.mapAddress);
        descriptionTv = (TextView) findViewById(R.id.descriptionText);
        startDateTv = (TextView) findViewById(R.id.start_date);
        userNameTv = (TextView) findViewById(R.id.user_name);
        userImage = (ImageView) findViewById(R.id.user_photo);
        contactTv = (TextView) findViewById(R.id.contact);
        setSupportActionBar(toolbar);

        //Consumir servico do evento especifico
        intent = getIntent();
        id = intent.getStringExtra("id");
        String imageUrl = intent.getStringExtra("imageUrl");

        if (id == null) {
            Log.e("EventActivity", "Error when moving event id fto EventActivity through extra");
        } else {
            Log.i("EventActivity", "Consuming event details");
            getEvent(id);
        }

        //if already has the url, no need to render after
        if (imageUrl != null) {
            Picasso.with(this)
                    .load(imageUrl)
                    .resize(1920, 1080)
                    .onlyScaleDown()
                    .centerCrop()
                    .into(eventImage);

            renderedFlag = true;
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void getEvent(String id) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(LocaleeAPI.LOCALEE_BASE_URL)
                .build();

        LocaleeAPI api = retrofit.create(LocaleeAPI.class);

        Call<Event> call = api.getEvent(id);
        call.enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                if (response.isSuccessful()) {
                    Log.i("RETROFIT", "Sucesso na obtenção de um evento");
                    actualEvent = response.body();
                    updateView(actualEvent);
                } else {
                    Log.e("RETROFIT", "Erro na obtenção de um evento");
                }
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                t.printStackTrace();
                Log.e("RETROFIT", t.getMessage());
            }
        });
    }

    private void updateView(Event event) {
        if (!renderedFlag) {
            Picasso.with(this)
                    .load(event.photoUrl)
                    .placeholder(R.drawable.ic_curves)
                    .resize(1920, 1080)
                    .onlyScaleDown()
                    .centerCrop()
                    .into(eventImage);
        }
        collapsingToolbarLayout.setTitle(event.name);
        addressTv.setText(event.address);
        addressMapTv.setText(event.address);
        categoryTv.setText(event.category);
        AlphaBackgroundCategory.set(categoryTv, event.category);
        descriptionTv.setText(event.description);
        startDateTv.setText(StringsFormatter.formatDate(event.date));
        userNameTv.setText(event.createdBy.name);

        Picasso.with(this)
                .load(event.createdBy.photoUrl)
                .placeholder(R.drawable.ic_user)
                .into(userImage);

        contactTv.setOnClickListener(new EmailClickListener(event.createdBy.email, event.name));

        //map
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapFragment);

        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        LatLng position = new LatLng(-7.1622870,-34.8172080);

        map.addMarker( new MarkerOptions().position(position).title(actualEvent.name));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(position).zoom(14.0f).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        map.moveCamera(cameraUpdate);
    }
}
