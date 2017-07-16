package com.brduo.localee.view;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.brduo.localee.R;
import com.brduo.localee.controller.LocaleeAPI;
import com.brduo.localee.model.User;
import com.brduo.localee.util.PreferenceManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Query;

public class LoginActivity extends AppCompatActivity {

    private EditText mPasswordView;
    private EditText mEmailView;
    private Button mLoginView;
    private ProgressBar mProgressView;
    private TextView mCreateAccount;
    private PreferenceManager preferenceManager;
    private User usersModel;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferenceManager = new PreferenceManager(this);

        this.mEmailView = (EditText) findViewById(R.id.input_email);
        this.mPasswordView = (EditText) findViewById(R.id.input_password);
        this.mLoginView = (Button) findViewById(R.id.btn_login);
        this.mCreateAccount = (TextView) findViewById(R.id.link_signup);
        this.mProgressView = (ProgressBar) findViewById(R.id.progress_bar);

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
                mProgressView.setIndeterminate(true);
                if(!isEmailValid(mEmailView.getText().toString())) {
                    Snackbar snackbar = Snackbar.make(mCreateAccount, R.string.error_invalid_email, Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if(!isPasswordValid(mPasswordView.getText().toString())) {
                    Snackbar snackbar = Snackbar.make(mCreateAccount, R.string.error_invalid_password, Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    Gson gson = new GsonBuilder()
                            .setLenient()
                            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                            .create();

                    Retrofit retrofit = new Retrofit.Builder()
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .baseUrl(LocaleeAPI.LOCALEE_BASE_URL)
                            .build();
                    LocaleeAPI api = retrofit.create(LocaleeAPI.class);

                    Call<User> call = api.getUserByEmail(mEmailView.getText().toString());
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccessful()) {
                                Log.i("RETROFIT", "Sucesso na obtenção de um email");
                                usersModel = response.body();
                                if (usersModel.password.equals(mPasswordView.getText().toString())){
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        finish();
                                } else {
                                    Snackbar snackbar = Snackbar.make(mCreateAccount, R.string.error_incorrect_login, Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }

                            }
                        }
                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Log.e("RETROFIT", "Erro na obtenção de um evento");
                            Snackbar snackbar = Snackbar.make(mCreateAccount, R.string.error_incorrect_login, Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    });
                }
                mProgressView.setIndeterminate(false);

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

