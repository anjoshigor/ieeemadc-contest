package com.brduo.evt.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.brduo.evt.R;
import com.brduo.evt.controller.EventAdapter;

public class EventActivity extends AppCompatActivity {
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    ImageView eventImage;
    Intent intent;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        eventImage = (ImageView) findViewById(R.id.image_toolbar);
        setSupportActionBar(toolbar);

        //Consumir servico do evento especifico
        intent = getIntent();
        id = intent.getIntExtra("id", -1);
        String eventName = intent.getStringExtra("name");

        if(id == -1){
            Log.e("EventActivity","Error when moving event id fto EventActivity through extra");
            Log.i("EventActivity", "Consuming full details for event");
            //Consumir servico com id
        } else {
            Log.i("EventActivity", "Consuming only leftovers details");
            //Consumir apenas os detalhes restantes
        }


        collapsingToolbarLayout.setTitle(eventName);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
