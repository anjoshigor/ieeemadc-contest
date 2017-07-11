package com.brduo.localee.util;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.brduo.localee.R;

/**
 * Created by anjoshigor on 10/07/17.
 */

public class EmailClickListener implements View.OnClickListener {

    private String emailTo, emailFrom;
    private String emailSubject;
    private String emailContent;

    public EmailClickListener(String emailTo, String emailSubject, String emailContent) {
        this.emailTo = emailTo;
        this.emailSubject = emailSubject;
        this.emailContent = emailContent;
        this.emailFrom = "";

    }

    public EmailClickListener(String emailTo, String emailFrom, String emailSubject, String emailContent) {
        this.emailTo = emailTo;
        this.emailFrom = emailFrom;
        this.emailSubject = emailSubject;
        this.emailContent = emailContent;
    }

    public EmailClickListener(String emailTo) {
        this.emailTo = emailTo;
        this.emailSubject = "";
        this.emailContent = "";
        this.emailFrom = "";
    }

    public EmailClickListener(String emailTo, String emailSubject) {
        this.emailTo = emailTo;
        this.emailSubject = emailSubject;
        this.emailContent = "";
        this.emailFrom = "";

    }

    @Override
    public void onClick(View v) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + emailTo));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailContent);


        try {
            v.getContext().startActivity(Intent.createChooser(emailIntent,v.getResources().getString(R.string.send_email)));
            Log.i("EMAIL","Sending email...");
        } catch (android.content.ActivityNotFoundException ex) {
            ex.printStackTrace();
            Log.e("EMAIL",ex.getMessage());
            Snackbar.make(v, R.string.email_app_error, Snackbar.LENGTH_LONG).show();
        }
    }
}
