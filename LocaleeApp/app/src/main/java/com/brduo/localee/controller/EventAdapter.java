package com.brduo.localee.controller;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brduo.localee.R;
import com.brduo.localee.model.Event;
import com.brduo.localee.view.EventActivity;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;

/**
 * Created by anjoshigor on 25/06/17.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    public static class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected CardView eventCard;
        protected TextView eventName, eventAddress, eventDate;
        protected ImageView eventImage;
        protected TextView distance;
        protected Intent eventIntent;

        //To be passed to event activity
        protected int Id;
        protected String Name;

        EventViewHolder(View itemView) {
            super(itemView);

            eventCard = (CardView) itemView.findViewById(R.id.event_card_view);
            eventName = (TextView) itemView.findViewById(R.id.event_name);
            eventAddress = (TextView) itemView.findViewById(R.id.event_address);
            eventDate = (TextView) itemView.findViewById(R.id.event_date);
            distance = (TextView) itemView.findViewById(R.id.event_distance);
            eventImage = (ImageView) itemView.findViewById(R.id.event_image);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {


            eventIntent = new Intent(v.getContext(), EventActivity.class);
            eventIntent.putExtra("id", this.Id);
            eventIntent.putExtra("name", this.Name);
            v.getContext().startActivity(eventIntent);
        }
    }

    public List<Event> events;


    public EventAdapter(List<Event> events) {
        this.events = events;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card, parent, false);
        EventViewHolder evh = new EventViewHolder(v);
        return evh;
    }


    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        String date;
        Calendar cal = events.get(position).getDate();
        Context imageContext = holder.eventImage.getContext();
        int charSize = imageContext.getResources().getInteger(R.integer.max_characters);
        date = cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH);

        //binding
        Picasso.with(imageContext) // Specify the application context
                .load(events.get(position).getImagePath()) // Image url to load from
                .into(holder.eventImage); // ImageView to display image
        holder.eventName.setText(shortenText(events.get(position).getName(), charSize + 15));
        holder.eventAddress.setText(shortenText(events.get(position).getAddress(), charSize));
        holder.eventDate.setText(date);
        holder.distance.setText("undefined");

        holder.Id = events.get(position).getId();
        holder.Name = events.get(position).getName();
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    private String shortenText(String text, int size) {
        if (text.length() > size) {
            Log.d("EventAdapter", "Shorten the text: " + text);
            text = text.substring(0, size);
            text = text.concat("...");
        }

        return text;
    }

}
