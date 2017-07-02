package com.brduo.evt.controller;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brduo.evt.R;
import com.brduo.evt.model.Event;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;

/**
 * Created by anjoshigor on 25/06/17.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        CardView eventCard;
        TextView eventName, eventAddress, eventDate;
        ImageView eventImage;
        TextView distance;


        EventViewHolder(View itemView) {
            super(itemView);

            eventCard = (CardView) itemView.findViewById(R.id.event_card_view);
            eventName = (TextView) itemView.findViewById(R.id.event_name);
            eventAddress = (TextView) itemView.findViewById(R.id.event_address);
            eventDate = (TextView) itemView.findViewById(R.id.event_date);
            distance = (TextView) itemView.findViewById(R.id.event_distance);
            eventImage = (ImageView) itemView.findViewById(R.id.event_image);

        }
    }

    List<Event> events;

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
        holder.eventName.setText(shortenText(events.get(position).getName(), charSize+15));
        holder.eventAddress.setText(shortenText(events.get(position).getAddress(), charSize));
        holder.eventDate.setText(date);
        holder.distance.setText("undefined");
    }

    private String shortenText(String text, int size){
        if(text.length() > size){
            Log.d("EventAdapter","Shorten the text: "+ text);
            text = text.substring(0,size);
            text = text.concat("...");
        }

        return text;
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
