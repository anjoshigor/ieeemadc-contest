package com.brduo.localee.view;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.brduo.localee.R;
import com.brduo.localee.util.PreferenceManager;

public class LoginActivity extends AppCompatActivity {

    private EditText mPasswordView;
    private EditText mEmailView;
    private Button mLoginView;
    private View mProgressView;
    private View mLoginFormView;
    private PreferenceManager preferenceManager;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferenceManager = new PreferenceManager(this);

        this.mEmailView = (EditText) findViewById(R.id.input_email);
        this.mPasswordView = (EditText) findViewById(R.id.input_password);
        this.mLoginView = (Button) findViewById(R.id.btn_login);
        this.coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinator_layout);



        mLoginView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Welcome to AndroidHive", Snackbar.LENGTH_LONG);
                snackbar.show();
                if(!isEmailValid(mEmailView.getText().toString())) {

                } else if(!isPasswordValid(mPasswordView.getText().toString())) {

                } else {

                }
            }
        });
    }


    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }
}

