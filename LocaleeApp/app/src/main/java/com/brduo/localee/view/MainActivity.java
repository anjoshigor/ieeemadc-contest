package com.brduo.localee.view;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.brduo.localee.R;
import com.brduo.localee.controller.EventsController;
import com.brduo.localee.util.PreferenceManager;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    private PreferenceManager preferenceManager;
    private EventsController controller;
    private Fragment fragment;
    private  MenuItem mMenuItem;
    private FragmentManager fragmentManager;
    private BottomNavigationView bottomNav;
    private   FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferenceManager = new PreferenceManager(this);

        if (preferenceManager.isFirstTimeLaunch()) {
            launchTutorial();
        }

        controller = EventsController.getInstance();
        Log.i("EventsListFragment", "Events size: "+controller.getCurrentEvents().size());

        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();

        if(Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            } else {
                fragment = new EventsListFragment();
                transaction.add(R.id.fragmentContainer, fragment).commit();
            }
        } else {
            fragment = new EventsListFragment();
            transaction.add(R.id.fragmentContainer, fragment).commit();
        }


        bottomNav = (BottomNavigationView) findViewById(R.id.navigation);
        disableShiftMode(bottomNav);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_events:
                        fragment = new EventsListFragment();
                        break;
                    case R.id.navigation_map:
                        fragment = new EventsMapFragment();
                        break;
                    case R.id.navigation_add:
                        fragment = new CreateEventFragment();
                        break;
                    case R.id.navigation_user:
                        fragment = new AccountFragment();
                        break;
                    default:
                        break;
                }
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragmentContainer, fragment).commit();
                return true;
            }
        });

    }

    private void launchTutorial() {
        startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
        finish();
    }

    // thanks https://stackoverflow.com/questions/40176244/how-to-disable-bottomnavigationview-shift-mode
    private void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                //noinspection RestrictedApi
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                //noinspection RestrictedApi
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("BNVHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("BNVHelper", "Unable to change value of shift mode", e);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fragment = new EventsListFragment();
                    transaction.add(R.id.fragmentContainer, fragment).commit();

                } else {

                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
