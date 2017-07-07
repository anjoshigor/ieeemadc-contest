package com.brduo.localee.util;

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
           case ANIME:
               color = R.color.purple_alpha87;
               break;
           default:
               color = R.color.white_alpha87;
       }
       textView.setBackgroundColor(textView.getResources().getColor(color));
    }
}
