package com.example.dafitserov.racelogic;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dafitserov.racelogic.databinding.FragmentRaceLogicBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import static android.content.Context.LOCATION_SERVICE;

public class RaceLogicFragment extends Fragment {

    LocationManager locationManager;
    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationClient;

    private static final long INTERVAL = 100;
    private static final long FASTEST_INTERVAL = 80;
    private final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    long millisecondTime, startTime = 0L ;
    double seconds, Minutes, MilliSeconds ;
    boolean isTrueTimer = false;
    Handler handler;


    FragmentRaceLogicBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_race_logic, container,false);

        view = binding.getRoot();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        checkGps();
        onLocationRequest();



        return view;
    }

    private void onLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setInterval(INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {

            checkLocationPermission();
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        } else {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        }


    }

    LocationCallback locationCallback = new LocationCallback(){

        Long timeStart;
        Long timeFinish;
        Long timeDifference;
        Location locationLast;


        @Override
        public void onLocationResult(LocationResult locationResult) {

            int speed = getSpeed(locationResult.getLastLocation());
            Log.d("EEE", "speed " + speed + " km/hour");
            binding.speedometerTextView.setText(String.valueOf(speed));
            if(!isTrueTimer){
                startTimer();
            }
        }

        private int getSpeed(Location location){

            if(timeStart == null || locationLast == null){
                timeFinish = timeStart = System.currentTimeMillis();
                locationLast = location;
                Log.d("EEE", "Return 0");
                return 0;
            }
            timeFinish = System.currentTimeMillis();
            timeDifference = (timeFinish - timeStart);
         //   Log.d("EEE", "timeDifference " + timeDifference);
            timeStart = timeFinish;

            Float distance = locationLast.distanceTo(location);
            locationLast = location;
         //   Log.d("EEE", "distance " + distance);
            //  speed meter/millis second
            double speed = distance / timeDifference;
            speed = speed*3600;
         //   Log.d("EEE", "speed " + speed + " km/hour");

            return (int)Math.round(speed);
        }

    };

    private void startTimer(){
        Log.d("EEE", "startTimer 0");
        isTrueTimer = true;
        startTime = SystemClock.uptimeMillis();
        handler = new Handler() ;
        handler.postDelayed(runnable, 0);
    }

    public Runnable runnable = new Runnable() {

        StringBuilder hundredthsSecondString = new StringBuilder();
        StringBuilder twoPatrSecondsString = new StringBuilder();
        StringBuilder onePartSecondsString = new StringBuilder();

        @SuppressLint("SetTextI18n")
        public void run() {

            millisecondTime = SystemClock.uptimeMillis() - startTime;
            

            int hundredthsSeconds =  Math.round(millisecondTime /10);

            hundredthsSecondString.delete(0, hundredthsSecondString.length());
            twoPatrSecondsString.delete(0, twoPatrSecondsString.length());
            onePartSecondsString.delete(0, onePartSecondsString.length());
            hundredthsSecondString.append(String.valueOf(hundredthsSeconds));

            Log.d("EEE", "valueOf   " + hundredthsSecondString.toString());


            if(hundredthsSecondString.length() < 2){

                twoPatrSecondsString.append(hundredthsSecondString.substring(hundredthsSecondString.length() - 1,
                        hundredthsSecondString.length()));

                twoPatrSecondsString.append("0", 0, 1);
                Log.d("EEE", "if one  " + twoPatrSecondsString.toString());
            } else {
                twoPatrSecondsString.append(hundredthsSecondString.substring(
                        hundredthsSecondString.length() - 2, hundredthsSecondString.length()));
                onePartSecondsString.append(hundredthsSecondString.substring(
                        0, hundredthsSecondString.length() - 2));
                Log.d("EEE", "if two  " + twoPatrSecondsString.toString());
            }


            if(onePartSecondsString.length() == 0){
                onePartSecondsString.append("0");
            }
            if(twoPatrSecondsString.length() == 1){
                twoPatrSecondsString.append("0");
            }


            binding.timeTextView.setText(onePartSecondsString.toString() + '.'
                    + twoPatrSecondsString.toString());


            handler.postDelayed(this, 20);
        }

    };

    private void checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    void checkGps() {

        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showGPSDisabledAlertToUser();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage("Enable GPS to use application")
                .setCancelable(false)
                .setPositiveButton("Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


    @Override
    public void onPause() {
        if(handler!= null){
            handler.removeCallbacks(runnable);
        }
        if(fusedLocationClient != null){
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
        super.onPause();
    }
}
