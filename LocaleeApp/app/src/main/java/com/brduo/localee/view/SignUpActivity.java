package com.brduo.localee.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.brduo.localee.R;
import com.brduo.localee.model.User;
import com.brduo.localee.util.UserProcessManager;

import java.io.File;
import java.util.ArrayList;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;

public class SignUpActivity extends AppCompatActivity {

    private EditText mInputName;
    private EditText mInputEmail;
    private EditText mInputPassword;
    private TextView alreadyMember;
    private Button bSignUp;
    private ImageView bImage;
    private User user;
    private ArrayList<String> filePaths;
    private File userPhoto;
    private ProgressBar loadingBar;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        user = new User();
        this.mInputName = (EditText) findViewById(R.id.input_name);
        this.mInputEmail = (EditText) findViewById(R.id.input_email);
        this.mInputPassword = (EditText) findViewById(R.id.input_password);
        this.bSignUp = (Button) findViewById(R.id.btn_signup);
        this.alreadyMember = (TextView) findViewById(R.id.link_login);
        this.bImage = (ImageView) findViewById(R.id.user_photo);
        this.loadingBar = (ProgressBar) findViewById(R.id.progress_bar);
        snackbar = Snackbar.make(loadingBar, "Info", Snackbar.LENGTH_LONG);

        alreadyMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

        bSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.email = mInputEmail.getText().toString();
                user.password = mInputPassword.getText().toString();
                user.name = mInputName.getText().toString();
                Log.i("USER on click", user.toString());
                UserProcessManager manager = new UserProcessManager(v.getContext(), user, userPhoto, loadingBar, snackbar);
                manager.uploadToService();
            }
        });

        bImage.setOnClickListener(pictureListener);
    }

    ImageView.OnClickListener pictureListener = new ImageView.OnClickListener() {
        @Override
        public void onClick(View v) {
            showFileChooser();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FilePickerConst.REQUEST_CODE_PHOTO:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    filePaths = new ArrayList<>();
                    filePaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
                }
                break;
        }

        if (filePaths.size() > 0) {
            Log.i("Image:", filePaths.get(0));
            userPhoto = new File(filePaths.get(0));
            bImage.setImageURI(Uri.fromFile(userPhoto));
            filePaths.clear();
        } else {
            Snackbar.make(alreadyMember, "Error", Snackbar.LENGTH_LONG).show();
        }
    }

    private void showFileChooser() {
        FilePickerBuilder.getInstance().setMaxCount(1)
                .setSelectedFiles(filePaths)
                .setActivityTheme(R.style.AppTheme)
                .pickPhoto(this);
    }
}
