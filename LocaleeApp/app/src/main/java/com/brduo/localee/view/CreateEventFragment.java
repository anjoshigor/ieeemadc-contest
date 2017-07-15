package com.brduo.localee.view;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.brduo.localee.R;
import com.brduo.localee.controller.LocaleeAPI;
import com.brduo.localee.controller.UploadsAPI;
import com.brduo.localee.model.Event;
import com.brduo.localee.model.UploadResponse;
import com.brduo.localee.util.StringsFormatter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateEventFragment extends Fragment {

    private ArrayList<String> filePaths;
    private DatePickerDialog startdatePicker, enddatePicker;
    private TimePickerDialog startTimePicker, endTimePicker;
    private TextView startDateButton, endDateButton, uploadMessage;
    private FloatingActionButton imageButton;
    private EditText eventEditText, descriptionEditText;
    private Button submitButton;
    private ProgressBar loadingBar;

    File eventPhoto;
    Calendar startCalendar, endCalendar;
    View rootView;
    Event event;

    public CreateEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_create_event, container, false);


        startDateButton = (TextView) rootView.findViewById(R.id.startDateButton);
        uploadMessage = (TextView) rootView.findViewById(R.id.uploadMessage);
        endDateButton = (TextView) rootView.findViewById(R.id.endDateButton);
        imageButton = (FloatingActionButton) rootView.findViewById(R.id.imageButton);
        eventEditText = (EditText) rootView.findViewById(R.id.eventEditText);
        descriptionEditText = (EditText) rootView.findViewById(R.id.descriptionEditText);
        submitButton = (Button) rootView.findViewById(R.id.submit);
        setCurrentDate();

        startDateButton.setOnClickListener(startButtonClickListener);
        endDateButton.setOnClickListener(endButtonClickListener);
        imageButton.setOnClickListener(imageButtonOnClickListener);
        submitButton.setOnClickListener(submitOnClickListener);
        loadingBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        filePaths = new ArrayList<>();

        event = new Event();
        return rootView;

    }

    private boolean validate() {

        if (eventEditText.getText().equals("")) {
            return false;
        }
        if (descriptionEditText.getText().equals("")) {
            return false;
        }
        if (eventPhoto == null) {
            return false;
        }
        if (endCalendar.before(startCalendar)) {
            return false;
        }

        return true;


    }

    private void uploadPhoto(File file) {
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
                        file
                );

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", file.getName(), requestFile);

        // add another part within the multipart request
        String descriptionString = "hello, this is description speaking";
        RequestBody description =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, descriptionString);

        // finally, execute the request
        Call<UploadResponse> call = service.uploadFile(description, body);
        call.enqueue(new Callback<UploadResponse>() {
            @Override
            public void onResponse(Call<UploadResponse> call,
                                   Response<UploadResponse> response) {
                Log.v("Upload", "success");
                UploadResponse resp = response.body();
                Log.v("Upload response", resp.data.img_url);

                event.photoUrl = resp.data.img_url;
                event.name = eventEditText.getText().toString();
                event.startDate = startCalendar.getTime();
                event.endDate = endCalendar.getTime();
                event.description = descriptionEditText.getText().toString();

                loadingBar.setIndeterminate(false);

            }

            @Override
            public void onFailure(Call<UploadResponse> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }

    private void setCurrentDate() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        startCalendar = c;
        endCalendar = c;


        String date = StringsFormatter.formatDate(c);
        // set current date into textview
        startDateButton.setText(date);
        endDateButton.setText(date);

        // set current date into datepicker
        startdatePicker = new DatePickerDialog(rootView.getContext(), startDatePickerListener, year, month, day);
        enddatePicker = new DatePickerDialog(rootView.getContext(), endDatePickerListener, year, month, day);

        // set current date into TimePicker
        startTimePicker = new TimePickerDialog(rootView.getContext(), startTimePickerListener, hour, minute, false);
        endTimePicker = new TimePickerDialog(rootView.getContext(), endTimePickerListener, hour, minute, false);

    }

    private void showFileChooser() {
        FilePickerBuilder.getInstance().setMaxCount(1)
                .setSelectedFiles(filePaths)
                .setActivityTheme(R.style.AppTheme)
                .pickPhoto(this);
    }

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
            eventPhoto = new File(filePaths.get(0));
            uploadMessage.setText(R.string.photo_uploaded);
        } else {
            uploadMessage.setText(R.string.photo_not_uploaded);
        }
    }

    private DatePickerDialog.OnDateSetListener startDatePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            int year = selectedYear;
            int month = selectedMonth;
            int day = selectedDay;

            startCalendar.set(Calendar.YEAR, year);
            startCalendar.set(Calendar.MONTH, month);
            startCalendar.set(Calendar.DAY_OF_MONTH, day);

            // set selected date into datepicker also
            startdatePicker.updateDate(year, month, day);

            openStartTimer();

        }
    };

    private DatePickerDialog.OnDateSetListener endDatePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            int year = selectedYear;
            int month = selectedMonth;
            int day = selectedDay;


            endCalendar.set(Calendar.YEAR, year);
            endCalendar.set(Calendar.MONTH, month);
            endCalendar.set(Calendar.DAY_OF_MONTH, day);

            // set selected date into datepicker also
            enddatePicker.updateDate(year, month, day);

            openEndTimer();

        }
    };

    private TimePickerDialog.OnTimeSetListener startTimePickerListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            startCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            startCalendar.set(Calendar.MINUTE, minute);

            startDateButton.setText(StringsFormatter.formatDate(startCalendar));
        }
    };

    private TimePickerDialog.OnTimeSetListener endTimePickerListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            endCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            endCalendar.set(Calendar.MINUTE, minute);

            endDateButton.setText(StringsFormatter.formatDate(endCalendar));
        }
    };


    private void openEndTimer() {
        endTimePicker.show();
    }

    private void openStartTimer() {
        startTimePicker.show();
    }

    private TextView.OnClickListener startButtonClickListener = new TextView.OnClickListener() {

        @Override
        public void onClick(View v) {
            startdatePicker.show();
        }
    };


    private TextView.OnClickListener endButtonClickListener = new TextView.OnClickListener() {

        @Override
        public void onClick(View v) {
            enddatePicker.show();
        }
    };

    private FloatingActionButton.OnClickListener imageButtonOnClickListener = new FloatingActionButton.OnClickListener() {

        @Override
        public void onClick(View v) {
            showFileChooser();
        }
    };

    private Button.OnClickListener submitOnClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (validate()) {
                loadingBar.setIndeterminate(true);
                uploadPhoto(eventPhoto);

            } else {
                Snackbar snackbar = Snackbar
                        .make(rootView, R.string.validation_error, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
    };
}
