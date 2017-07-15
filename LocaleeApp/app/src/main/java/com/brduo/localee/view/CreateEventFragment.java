package com.brduo.localee.view;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.brduo.localee.R;
import com.brduo.localee.controller.LocaleeAPI;
import com.brduo.localee.controller.UploadsAPI;
import com.brduo.localee.model.Event;
import com.brduo.localee.model.UploadResponse;
import com.brduo.localee.model.User;
import com.brduo.localee.model.UserSimplified;
import com.brduo.localee.util.PreferenceManager;
import com.brduo.localee.util.StringsFormatter;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.vision.text.Text;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
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
    private TextView startDateButton, endDateButton;
    private TextView imageButton, addressResult;
    private EditText eventEditText, descriptionEditText;
    private Button submitButton;
    private ProgressBar loadingBar;

    File eventPhoto;
    Calendar startCalendar, endCalendar;
    View rootView;
    Event event;
    UserSimplified user;
    private PreferenceManager preferenceManager;

    public CreateEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_create_event, container, false);

        //test if user is logged
        preferenceManager = new PreferenceManager(getActivity());

        if (!preferenceManager.isLogged()) {
            launchLogin();
        }


        startDateButton = (TextView) rootView.findViewById(R.id.startDateButton);
        endDateButton = (TextView) rootView.findViewById(R.id.endDateButton);
        imageButton = (TextView) rootView.findViewById(R.id.imageButton);
        eventEditText = (EditText) rootView.findViewById(R.id.eventEditText);
        descriptionEditText = (EditText) rootView.findViewById(R.id.descriptionEditText);
        submitButton = (Button) rootView.findViewById(R.id.submit);
        addressResult = (TextView) rootView.findViewById(R.id.address_result);

        setCurrentDate();

        startDateButton.setOnClickListener(startButtonClickListener);
        endDateButton.setOnClickListener(endButtonClickListener);
        imageButton.setOnClickListener(imageButtonOnClickListener);
        submitButton.setOnClickListener(submitOnClickListener);
        loadingBar = (ProgressBar) rootView.findViewById(R.id.progressBar);


        filePaths = new ArrayList<>();

        event = new Event();
        user = new UserSimplified();

        final SupportPlaceAutocompleteFragment autocompleteFragment = (SupportPlaceAutocompleteFragment)
                getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: obter informações sobre o local selecionado.
                Log.i("PLACES API", "Place: " + place.getName());
                event.address = place.getName() + ", " + place.getAddress().toString();
                LatLng coords = place.getLatLng();
                event.lat = coords.latitude;
                event.lng = coords.longitude;
                setCityAndCountry(place);
                Log.i("EVENT", event.toString());

                addressResult.setText(event.address);
                addressResult.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pin_drop_black_24dp, 0, 0, 0);
            }

            @Override
            public void onError(Status status) {
                // TODO: Solucionar o erro.
                Log.i("PLACES API", "Ocorreu um erro: " + status);
            }
        });

        return rootView;

    }

    private boolean validate() {

        if (eventEditText.getText().equals("") || eventEditText.getText()
                .equals(R.string.event_name)) {
            return false;
        }
        if (descriptionEditText.getText().equals("") || descriptionEditText.getText()
                .equals(R.string.event_description)) {
            return false;
        }
        if (eventPhoto == null) {
            return false;
        }
        if (endCalendar.before(startCalendar)) {
            return false;
        }
        if (addressResult.getText().equals("")) {
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
            imageButton.setText(R.string.photo_uploaded);
        } else {
            imageButton.setText(R.string.photo_not_uploaded);
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

    private TextView.OnClickListener imageButtonOnClickListener = new TextView.OnClickListener() {

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

    private void setCityAndCountry(Place place) {
        String city = "";
        String country = "";
        if (place.getLocale() == null) {
            LatLng coordinates = place.getLatLng(); // Get the coordinates from your place
            Geocoder geocoder = new Geocoder(rootView.getContext(), Locale.getDefault());

            try {
                List<Address> addresses = geocoder.getFromLocation(
                        coordinates.latitude,
                        coordinates.longitude,
                        1); // Only retrieve 1 address
                Address address = addresses.get(0);
                country = address.getCountryName();
                city = address.getSubLocality();
            } catch (IOException ioex) {
                ioex.printStackTrace();
                Log.e("GEOCODER", ioex.getMessage());
            }
        } else {
            Log.i("SET COUNTRY", "NOT FROM GEOCODER");
            country = place.getLocale().getDisplayCountry();
            city = place.getLocale().getDisplayName();
        }

        event.city = city;
        event.setCountry(country);
    }

    private void launchLogin() {
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }

}
