package com.brduo.localee.model;

/**
 * Created by anjoshigor on 14/07/17.
 */

public class ImageData {

    public String img_name;
    public String img_url;
    public String img_view;
    public String thumb_url;

    public ImageData(String img_name, String img_url, String img_view, String thumb_url) {
        this.img_name = img_name;
        this.img_url = img_url;
        this.img_view = img_view;
        this.thumb_url = thumb_url;
    }

}
