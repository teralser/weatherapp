package com.teralser.weatherapp.gps;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.teralser.weatherapp.R;
import com.teralser.weatherapp.utils.Logger;

public class GPSManager {

    private static final String TAG = GPSManager.class.getSimpleName();

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private boolean mRequestingLocationUpdates = true;

    private Activity activity;
    private LocationListener listener;

    public GPSManager() {
    }

    public void init(Activity activity, @NonNull LocationListener locationListener) {
        Logger.logd(TAG, "init");
        this.activity = activity;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        mSettingsClient = LocationServices.getSettingsClient(activity);
        listener = locationListener;

        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                Location mCurrentLocation = locationResult.getLastLocation();
                Logger.logd(TAG, "onLocationResult: " + mCurrentLocation);
                if (mCurrentLocation != null) {
                    listener.locationObtained(mCurrentLocation);
                    stopLocationUpdates();
                }
            }
        };
    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    private void startLocationUpdates() {
        Logger.logd(TAG, "startLocationUpdates");
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(activity, locationSettingsResponse -> {
                    Logger.logd(TAG, "All location settings are satisfied.");

                    //noinspection MissingPermission
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                            mLocationCallback, Looper.myLooper());
                })
                .addOnFailureListener(activity, e -> {
                    int statusCode = ((ApiException) e).getStatusCode();
                    switch (statusCode) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            Logger.logd(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                    "location settings ");
                            try {
                                // Show the dialog by calling startResolutionForResult(), and check the
                                // result in onActivityResult().
                                ResolvableApiException rae = (ResolvableApiException) e;
                                rae.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException sie) {
                                Logger.logd(TAG, "PendingIntent unable to execute request.");
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            String errorMessage = activity.getString(R.string.location_settings_error);
                            Logger.loge(TAG, errorMessage);
                            Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show();
                            mRequestingLocationUpdates = false;
                    }
                });
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(activity, task -> mRequestingLocationUpdates = false);
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        Logger.logd(TAG, "requestPermissions");
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    public void onResume() {
        Logger.logd(TAG, "onResume");
        if (mRequestingLocationUpdates) {
            if (checkPermissions()) {
                startLocationUpdates();
            } else {
                requestPermissions();
            }
        }
    }

    public void onPause() {
        // Remove location updates to save battery.
        stopLocationUpdates();
    }

    public void onDestroy() {
        listener = null;
        activity = null;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.logd(TAG, "onActivityResult");
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Logger.logd(TAG, "User agreed to make required location settings changes.");
                        listener.locationAccessGranted(true);
                        mRequestingLocationUpdates = true;
                        // Nothing to do. startLocationUpdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
                        listener.locationAccessGranted(false);
                        Logger.loge(TAG, "User chose not to make required location settings changes.");
                        break;
                }
                break;
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Logger.logd(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Logger.loge(TAG, "User interaction was cancelled.");
                mRequestingLocationUpdates = false;
                listener.locationAccessGranted(false);
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (mRequestingLocationUpdates) {
                    listener.locationAccessGranted(true);
                    startLocationUpdates();
                }
            } else {
                // Permission denied.
                Logger.loge(TAG, "Permission denied.");
                listener.locationAccessGranted(false);
            }
        }
    }

    public void askCurrentLocation() {
        mRequestingLocationUpdates = true;
        onResume();
    }

    public interface LocationListener {
        void locationObtained(@NonNull Location location);
        void locationAccessGranted(boolean isGranted);
    }
}
