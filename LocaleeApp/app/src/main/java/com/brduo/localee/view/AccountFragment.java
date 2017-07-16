package com.brduo.localee.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brduo.localee.R;
import com.brduo.localee.controller.LocaleeAPI;
import com.brduo.localee.model.EventResponse;
import com.brduo.localee.model.EventSimplified;
import com.brduo.localee.model.User;
import com.brduo.localee.util.PreferenceManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AccountFragment extends Fragment {
    PreferenceManager preferenceManager;

    private TextView mUserName;
    private TextView mEmail;
    private TextView mYourEvents;
    private ImageView mImage;
    private List<EventSimplified> events;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);
        preferenceManager = new PreferenceManager(getActivity());

        mUserName = (TextView) rootView.findViewById(R.id.user_name);
        mEmail = (TextView) rootView.findViewById(R.id.email);
        mYourEvents = (TextView) rootView.findViewById(R.id.yourevents);
        mImage = (ImageView) rootView.findViewById(R.id.user_photo);

        if (!preferenceManager.isLogged()) {
            launchLogin();
        } else {
            mUserName.setText(preferenceManager.getUserName());
            mEmail.setText(preferenceManager.getUserEmail());
            Picasso.with(rootView.getContext())
                    .load(preferenceManager.getUserPhotoUrl())
                    .into(mImage);

            getUserInfo(preferenceManager.getUserId());
        }

        return rootView;
    }

    private void launchLogin() {
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
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
                    Log.i("Events Created", events.toString());
                    if(events.size() == 0){
                        mYourEvents.setText(R.string.no_events);
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
