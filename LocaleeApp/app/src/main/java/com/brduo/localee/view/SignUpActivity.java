package com.brduo.localee.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.brduo.localee.R;

public class SignUpActivity extends AppCompatActivity {

    private EditText mInputName;
    private EditText mInputEmail;
    private EditText mInputPassword;
    private TextView alreadyMember;
    private Button bSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        this.mInputName = (EditText) findViewById(R.id.input_name);
        this.mInputEmail = (EditText) findViewById(R.id.input_email);
        this.mInputPassword = (EditText) findViewById(R.id.input_password);
        this.bSignUp = (Button) findViewById(R.id.btn_signup);
        this.alreadyMember = (TextView) findViewById(R.id.link_login);

        alreadyMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

        bSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO CHECK IF EXIST
                // TODO POST
            }
        });
    }
}
