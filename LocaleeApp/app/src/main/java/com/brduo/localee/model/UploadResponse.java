package com.brduo.localee.model;

/**
 * Created by anjoshigor on 14/07/17.
 */

public class UploadResponse {
    public int status_code;
    public String status_txt;
    public ImageData data;

    public UploadResponse(int status_code, String status_txt, ImageData data) {
        this.status_code = status_code;
        this.status_txt = status_txt;
        this.data = data;
    }
}

