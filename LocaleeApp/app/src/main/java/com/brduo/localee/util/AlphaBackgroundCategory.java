package com.brduo.localee.util;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.brduo.localee.R;

/**
 * Created by alvesmarcos on 06/07/17.
 */

public class AlphaBackgroundCategory {
    public static void set(TextView textView, EventCategory category) {
       int color = 0;
       switch (category){
           case PARTY:
               color = R.color.blue_alpha87;
               break;
           case FOOD:
               color = R.color.yellow_alpha87;
               break;
           case TECH:
               color = R.color.purple_alpha87;
               break;
           default:
               color = R.color.black_alpha50;
       }
       textView.setBackgroundColor(textView.getResources().getColor(color));
    }
    public static void set(TextView textView, String category) {
        int color = 0;
        switch (category){
            case "music":
                color = R.color.blue_alpha87;
                break;
            case "food":
                color = R.color.yellow_alpha87;
                break;
            case "technology":
                color = R.color.purple_alpha87;
                break;
            default:
                color = R.color.black_alpha50;
        }
        textView.setBackgroundColor(textView.getResources().getColor(color));
    }
}
