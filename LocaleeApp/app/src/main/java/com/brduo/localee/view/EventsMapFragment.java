package com.brduo.localee.view;


import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brduo.localee.Manifest;
import com.brduo.localee.R;
import com.brduo.localee.controller.EventsController;
import com.brduo.localee.model.Event;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsMapFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks {

    GoogleMap eventsMap;
    EventsController controller;

    public static final int DEFAULT_ZOOM = 14;

    private GoogleApiClient mGoogleApiClient;

    private Location mLastKnownLocation;

    private boolean mLocationPermissionGranted;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        controller.getInstance();
        return inflater.inflate(R.layout.fragment_events_map, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
            return;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        eventsMap = googleMap;

        updateLocationUI();
        //getDeviceLocation();
        Drawable circleDrawable = getResources().getDrawable(R.drawable.dot_marker);
        BitmapDescriptor icon = getMarkerIconFromDrawable(circleDrawable);


        for (Event event : controller.getCurrentEvents()) {
            Log.i("EventsMapFragment", "adding Markers..");
            eventsMap.addMarker(new MarkerOptions()
                    .position(new LatLng(event.lat, event.lng))
                    .title(event.name)
                    .snippet(event.address)
                    .icon(icon));
        }

    }

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void updateLocationUI() {
        if (eventsMap == null) return;
        if (ActivityCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            eventsMap.setMyLocationEnabled(true);
            eventsMap.getUiSettings().setMyLocationButtonEnabled(true);
            Log.i("updateLocation", "Permisson Granted..");
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
      /*  SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    */
    }


    @Override
    public void onConnectionSuspended(int i) {

    }


    private void getDeviceLocation() {

        if (ActivityCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
            mLastKnownLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
            Log.i("getDeviceLocation", "Permisson Granted..");
        }

        // Set the map's camera position to the current location of the device.
        eventsMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(mLastKnownLocation.getLatitude(),
                        mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));


    }

    @Override
    public void onResume() {
        super.onResume();

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(LocationServices.API) //The main entry point for location services integration.
                    .addApi(Places.PLACE_DETECTION_API) //provides quick access to the device's current place...
                    .addApi(Places.GEO_DATA_API) //provides access to Google's database of local place and business information...
                    .build();
        }

        if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.connect();
            Log.i("onresume", "Googgle Client Connected..");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }


}
