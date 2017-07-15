package com.brduo.localee.util;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.brduo.localee.controller.LocaleeAPI;
import com.brduo.localee.controller.UploadsAPI;
import com.brduo.localee.model.UploadResponse;
import com.brduo.localee.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by anjoshigor on 15/07/17.
 */

public class SubmitUserClickListener implements View.OnClickListener {

    String name, email, password;
    File photo;
    ProgressBar loadingBar;
    PreferenceManager preferenceManager;
    Context context;
    TextView textView;

    public SubmitUserClickListener(Context c, String name, String email, String password, File photo, ProgressBar loadingBar, TextView messageTextView) {
        preferenceManager = new PreferenceManager(c);
        this.context = c;
        this.name = name;
        this.email = email;
        this.password = password;
        this.photo = photo;
        this.loadingBar = loadingBar;
        this.textView = messageTextView;
    }

    @Override
    public void onClick(View v) {
        this.loadingBar.setIndeterminate(true);
        uploadToService();
    }

    public void uploadToService() {
        // create upload service client

        Gson gson = new GsonBuilder()
                .setLenient()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(UploadsAPI.UPLOADS_BASE_URL)
                .build();

        UploadsAPI service = retrofit.create(UploadsAPI.class);


        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("image/jpeg"),
                        photo
                );

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", photo.getName(), requestFile);

        // add another part within the multipart request
        String descriptionString = "hello, this is description speaking";
        RequestBody description =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, descriptionString);

        // finally, execute the request
        Call<UploadResponse> call = service.uploadFile(description, body);
        call.enqueue(imageCallback);
    }

    Callback<UploadResponse> imageCallback = new Callback<UploadResponse>() {
        @Override
        public void onResponse(Call<UploadResponse> call,
                               Response<UploadResponse> response) {
            Log.v("Upload", "success");
            UploadResponse resp = response.body();
            Log.v("Upload response", resp.data.img_url);
            String image_url = resp.data.img_url;

            User user = new User(email, password, name, image_url);
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(LocaleeAPI.LOCALEE_BASE_URL)
                    .build();

            LocaleeAPI service = retrofit.create(LocaleeAPI.class);

            Call<User> userCall = service.postUser(user);
            userCall.enqueue(userCallBack);

        }

        @Override
        public void onFailure(Call<UploadResponse> call, Throwable t) {
            Log.e("Upload error:", t.getMessage());
            textView.setText("Erro!");
        }
    };


    Callback<User> userCallBack = new Callback<User>() {
        @Override
        public void onResponse(Call<User> call, Response<User> response) {
            Log.v("Upload", "success");
            User resp = response.body();
            preferenceManager.setUserInfo(resp._id, resp.email, resp.photoUrl, resp.name);
            loadingBar.setIndeterminate(false);
            textView.setText("Sucesso!");

        }

        @Override
        public void onFailure(Call<User> call, Throwable t) {
            textView.setText("Erro!");
        }
    };


}
