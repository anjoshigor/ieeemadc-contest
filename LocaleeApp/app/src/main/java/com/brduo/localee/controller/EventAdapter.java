package com.brduo.localee.controller;


import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.content.ContextCompat;
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
import com.brduo.localee.util.AlphaBackgroundCategory;
import com.brduo.localee.view.EventActivity;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by anjoshigor on 25/06/17.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    protected Calendar auxCalendar = new GregorianCalendar();

    public List<Event> events;
    private Location userLocation;
    private Geocoder geoCoder;

    public static class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected CardView eventCard;
        protected TextView eventName, eventAddress, eventDate;
        protected ImageView eventImage;
        protected TextView distance, txtViewNearYou;
        protected Intent eventIntent;

        //To be passed to event activity
        protected String Id;
        protected String ImageUrl;


        EventViewHolder(View itemView) {
            super(itemView);

//            eventCard = (CardView) itemView.findViewById(R.id.event_card_view);
            eventName = (TextView) itemView.findViewById(R.id.event_name);
            //eventAddress = (TextView) itemView.findViewById(R.id.event_address);
            eventDate = (TextView) itemView.findViewById(R.id.event_date);
            distance = (TextView) itemView.findViewById(R.id.event_distance);
            eventImage = (ImageView) itemView.findViewById(R.id.event_image);

            txtViewNearYou = (TextView) itemView.findViewById(R.id.id_near_you);

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


    public EventAdapter(List<Event> events, Location location, Context context) {
        this.events = events;
        this.userLocation = location;
        geoCoder = new Geocoder(context, Locale.getDefault());
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card, parent, false);
        EventViewHolder evh = new EventViewHolder(v);
        return evh;
    }


    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        String month, address, period;
        int day, hour;
        float distance;
        String distanceString;

        Date date = events.get(position).date;
        Context imageContext = holder.eventImage.getContext();
        int charSize = imageContext.getResources().getInteger(R.integer.max_characters);

        //date
        auxCalendar.setTime(date);
        month = auxCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
        day = auxCalendar.get(Calendar.DAY_OF_MONTH);
        hour = auxCalendar.get(Calendar.HOUR);
        period = auxCalendar.getDisplayName(Calendar.AM_PM, Calendar.SHORT, Locale.getDefault());
        //distance
        address = events.get(position).address;
        Address addr;
        try {
            List<Address> addrList = geoCoder.getFromLocationName(address, 1);
            if (addrList.size() == 0)
                throw new IOException("Empty list from getFromLocationName Method");

            addr = addrList.get(0);
            Location aux = new Location("");
            aux.setLatitude(addr.getLatitude());
            aux.setLongitude(addr.getLongitude());
            distance = userLocation.distanceTo(aux);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("EventAdapter", "Erro ao obter geolocation from address", e);
            distance = -1;
        }


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
        }
        //binding
        Picasso.with(imageContext) // Specify the application context
                .load(events.get(position).photoUrl)// Image url to load from
                .placeholder(R.drawable.ic_curves)
                .resize(1920,1080)
                .onlyScaleDown()
                .centerCrop()
                .into(holder.eventImage); // ImageView to display image
        holder.eventName.setText(shortenText(events.get(position).name, charSize + 15));
        //holder.eventAddress.setText(shortenText(events.get(position).getAddress(), charSize));
        holder.eventDate.setText(day + " " + month + ", " + hour + period);
        holder.distance.setText(distanceString);
        AlphaBackgroundCategory.set(holder.distance, events.get(position).category);
        AlphaBackgroundCategory.set(holder.txtViewNearYou, events.get(position).category);

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
