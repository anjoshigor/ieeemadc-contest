package com.brduo.localee.controller;

import com.brduo.localee.model.UploadResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by anjoshigor on 13/07/17.
 */

public interface UploadsAPI {
    String UPLOADS_BASE_URL = "http://uploads.im/";

    @Multipart
    @POST("api")
    Call<UploadResponse> uploadFile(
            @Part("upload") RequestBody description,
            @Part MultipartBody.Part file
    );
}
