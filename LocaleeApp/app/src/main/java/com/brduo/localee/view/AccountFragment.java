package com.brduo.localee.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brduo.localee.R;
import com.brduo.localee.util.PreferenceManager;
import com.google.android.gms.internal.fi;
import com.squareup.picasso.Picasso;


public class AccountFragment extends Fragment {
    PreferenceManager preferenceManager;

    private TextView mUserName;
    private TextView mEmail;
    private TextView mYourEvents;
    private ImageView mImage;

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

        }
        return rootView;

    }

    private void launchLogin() {
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }
}
