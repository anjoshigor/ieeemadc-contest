package com.brduo.localee.util;

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
    private static final String PREF_NAME = "localee-pref";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String USER_EMAIL = "email";
    private static final String USER_ID = "id";
    private static final String USER_PHOTO_URL = "url";
    private static final String USER_NAME = "name";

    public PreferenceManager(Context c) {
        context = c;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public void setUserInfo(String id, String email, String photoUrl, String name) {
        editor.putString(USER_EMAIL, email);
        editor.putString(USER_ID, id);
        editor.putString(USER_PHOTO_URL, photoUrl);
        editor.putString(USER_NAME, name);
        editor.commit();
    }

    public boolean isLogged() {
        if (this.getUserEmail() == null)
            return false;
        if (this.getUserId() == null)
            return false;
        if (this.getUserName() == null)
            return false;
        if (this.getUserPhotoUrl() == null)
            return false;

        return true;
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public String getUserEmail() {
        return pref.getString(USER_EMAIL, null);
    }

    public String getUserId() {
        return pref.getString(USER_ID, null);
    }

    public String getUserPhotoUrl() {
        return pref.getString(USER_PHOTO_URL, null);
    }

    public String getUserName() {
        return pref.getString(USER_NAME, null);
    }
}
