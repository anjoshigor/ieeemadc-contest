package com.brduo.localee.controller;

import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.app.AppCompatDelegate;
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
import com.brduo.localee.model.EventSimplified;
import com.brduo.localee.util.AlphaBackgroundCategory;
import com.brduo.localee.util.StringsFormatter;
import com.brduo.localee.view.EventActivity;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by alvesmarcos on 7/15/17.
 */

public class EventSimplifiedAdapter  extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    protected Calendar auxCalendar = new GregorianCalendar();

    public List<EventSimplified> events;


    public static class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected CardView eventCard;
        protected TextView eventName, eventAddress, eventDate;
        protected ImageView eventImage;
        protected TextView eventCategory;
        protected Intent eventIntent;

        //To be passed to event activity
        protected String Id;
        protected String ImageUrl;


        EventViewHolder(View itemView) {
            super(itemView);

            eventCard = (CardView) itemView.findViewById(R.id.event_card_view);
            eventName = (TextView) itemView.findViewById(R.id.event_name);
            eventDate = (TextView) itemView.findViewById(R.id.event_date);
            eventImage = (ImageView) itemView.findViewById(R.id.event_image);
            eventCategory = (TextView) itemView.findViewById(R.id.category);


            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {


            eventIntent = new Intent(v.getContext(), EventActivity.class);
            eventIntent.putExtra("id", this.Id);
            eventIntent.putExtra("imageUrl", this.ImageUrl);
            v.getContext().startActivity(eventIntent);
        }
    }


    public EventSimplifiedAdapter(List<EventSimplified> events, Context context) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        this.events = events;
    }

    @Override
    public EventAdapter.EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_event_card, parent, false);
        EventAdapter.EventViewHolder evh = new EventAdapter.EventViewHolder(v);
        return evh;
    }


    @Override
    public void onBindViewHolder(EventAdapter.EventViewHolder holder, int position) {

        float distance;
        String distanceString;

        Date date = events.get(position).startDate;
        Context imageContext = holder.eventImage.getContext();

        //distance
       /* Location eventLocation = new Location("");
        eventLocation.setLatitude(events.get(position).lat);
        eventLocation.setLatitude(events.get(position).lng);
        distance = userLocation.distanceTo(eventLocation);


        if (distance == -1) {
            distanceString = events.get(position).category;
            holder.distance.setBackgroundColor(ContextCompat.getColor(imageContext, R.color.colorAccent));
        } else {
            if (distance > 1000) {
                distance = distance / 1000.0f;
                distanceString = String.format("%.1f", distance) + "km";
            } else {
                distanceString = (int) distance + "m";
            }
        }*/
        //binding
        Picasso.with(imageContext) // Specify the application context
                .load(events.get(position).photoUrl)// Image url to load from
                .into(holder.eventImage); // ImageView to display image
        holder.eventName.setText(events.get(position).name);
        //holder.eventAddress.setText(shortenText(events.get(position).getAddress(), charSize));
        holder.eventDate.setText(StringsFormatter.formatDate(events.get(position).startDate) + "\n" +
                StringsFormatter.formatDate(events.get(position).endDate));
        // holder.distance.setText(distanceString);
        AlphaBackgroundCategory.set(holder.eventCategory, events.get(position).category);
        holder.eventCategory.setText(events.get(position).category);
        holder.Id = events.get(position)._id;
        holder.ImageUrl = events.get(position).photoUrl;
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
