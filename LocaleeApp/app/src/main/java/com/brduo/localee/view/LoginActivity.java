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
import com.brduo.localee.model.Event;
import com.brduo.localee.model.User;
import com.brduo.localee.model.Users;
import com.brduo.localee.util.PreferenceManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private EditText mPasswordView;
    private EditText mEmailView;
    private Button mLoginView;
    private ProgressBar mProgressView;
    private TextView mCreateAccount;
    private PreferenceManager preferenceManager;
    private CoordinatorLayout coordinatorLayout;
    private Users usersModel;

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
                    Gson gson = new GsonBuilder()
                            .setLenient()
                            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                            .create();

                    Retrofit retrofit = new Retrofit.Builder()
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .baseUrl(LocaleeAPI.LOCALEE_BASE_URL)
                            .build();
                    LocaleeAPI api = retrofit.create(LocaleeAPI.class);

                    Call<Users> call = api.getUsers();
                    call.enqueue(new Callback<Users>() {
                        @Override
                        public void onResponse(Call<Users> call, Response<Users> response) {
                            if (response.isSuccessful()) {
                                Log.i("RETROFIT", "Sucesso na obtenção de um evento");
                                usersModel = response.body();

                                for(User u: usersModel.users) {
                                    if (u.email.equals(mEmailView.getText()) && u.password.equals(mPasswordView.getText())){
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        finish();
                                } else {
                                        Snackbar snackbar = Snackbar.make(coordinatorLayout, R.string.error_incorrect_login, Snackbar.LENGTH_LONG);
                                        snackbar.show();
                                    }

                                }
                            } else {
                                Log.e("RETROFIT", "Erro na obtenção de um evento");
                            }
                        }

                        @Override
                        public void onFailure(Call<Users> call, Throwable t) {
                            t.printStackTrace();
                            Log.e("RETROFIT", t.getMessage());
                        }
                    });
                }
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

