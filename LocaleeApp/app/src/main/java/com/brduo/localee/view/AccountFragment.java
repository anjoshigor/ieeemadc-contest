package com.brduo.localee.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brduo.localee.R;
import com.brduo.localee.util.PreferenceManager;


public class AccountFragment extends Fragment {
    PreferenceManager preferenceManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);
        preferenceManager = new PreferenceManager(getActivity());

        if (!preferenceManager.isLogged()) {
            launchLogin();
        }

        return rootView;

    }

    private void launchLogin() {
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }
}
