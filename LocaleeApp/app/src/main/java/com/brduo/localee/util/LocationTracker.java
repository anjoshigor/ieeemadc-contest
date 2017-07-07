package com.brduo.localee.util;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by anjoshigor on 06/07/17.
 */

//inspired by http://www.androidhive.info/2012/07/android-gps-location-manager-tutorial/
public class LocationTracker implements LocationListener {

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 100; // 100 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 5; // 5 minutes

    private final Context mContext;
    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude


    double longitude; // longitude

    // Declaring a Location Manager
    protected LocationManager locationManager;

    public LocationTracker(Context context) {
        this.mContext = context;
        getCurrentLocation();
    }

    public boolean hasLocation() {
        return this.canGetLocation;
    }

    private void getCurrentLocation() {
        try {
            //Creating Locatino Manager
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            //trying from GPS
            if (isGPSEnabled) {
                if (location == null) {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("LOCATION", "GPS Enabled");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            Log.d("LOCATION", "Location successfully obtained!");
                            canGetLocation = true;
                            return;
                        }
                    }
                }
            }
            //trying from NetWork
            if (isNetworkEnabled) {
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                Log.d("LOCATION", "Network Enabled");
                if (locationManager != null) {
                    location = locationManager
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        Log.d("LOCATION", "Location successfully obtained!");
                        canGetLocation = true;
                        return;
                    }
                }
            }

        } catch (IllegalArgumentException iaex) {
            iaex.printStackTrace();
            Log.e("LOCATION", "Provider is null or doesn't exist " + iaex.getMessage());

        } catch (SecurityException sex) {
            sex.printStackTrace();
            Log.e("LOCATION", "No suitable permission is present " + sex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("LOCATION", ex.getMessage());
        }
    }

    public void showGPSActivation() {
        mContext.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(LocationTracker.this);
        }
    }

    public Location getLocation() {
        return location;
    }


    public double getLatitude() {
        return latitude;
    }


    public double getLongitude() {
        return longitude;
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
