package com.brduo.evt;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by alvesmarcos on 17/06/17.
 */

/**
 * Classe responsavel por salvar o estado do aplicativo. O editor guarda informacao se e ou nao a
 * primeira execucao do app.
 */

public class PreferenceManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    // File name shared preferences
    private static final String PREF_NAME = "evt-welcome";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public PreferenceManager(Context c) {
        context = c;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

}
