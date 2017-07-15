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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.brduo.localee.R;
import com.brduo.localee.util.PreferenceManager;

public class LoginActivity extends AppCompatActivity {

    private EditText mPasswordView;
    private EditText mEmailView;
    private Button mLoginView;
    private ProgressBar mProgressView;
    private TextView mCreateAccount;
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
        this.mCreateAccount = (TextView) findViewById(R.id.link_signup);
       // this.mProgressView = (ProgressBar) findViewById(R.id.progress_bar);

        mCreateAccount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                finish();
            }
        });

        mLoginView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isEmailValid(mEmailView.getText().toString())) {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, R.string.error_invalid_email, Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if(!isPasswordValid(mPasswordView.getText().toString())) {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, R.string.error_invalid_password, Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    // TODO GET USER BY EMAIL and PASSWORD or GET ALL USERS
                    preferenceManager.setUserInfo("sms", "me", "y", "alvemarcos");
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
                finish();
            }
        });
    }


    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 3;
    }
}

