package com.teralser.weatherapp.manager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
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
import com.teralser.weatherapp.model.Coordinates;
import com.teralser.weatherapp.model.LocationItem;
import com.teralser.weatherapp.utils.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    private boolean mRequestingLocationUpdates = false;

    private Context appContext;
    private Activity activity;
    private LocationListener listener;
    private GeocodeRunnable geocodeRunnable;

    private ArrayList<LocationItem> locations;

    public GPSManager(Context context) {
        appContext = context;
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
                    stopLocationUpdates();
                    updateMyLocationInList(mCurrentLocation);
                }
            }
        };
    }

    private void updateMyLocationInList(Location mCurrentLocation) {
        getAddressFrom(mCurrentLocation, name -> {
            geocodeRunnable.shutdown();

            locations.get(0).setCoordinates(Coordinates.fromLocation(mCurrentLocation));
            if (!TextUtils.isEmpty(name)) {
                locations.get(0).setName(name);
            }
            listener.locationObtained(mCurrentLocation);
        });
    }

    public static class GeocodeRunnable implements Runnable {

        private volatile boolean done = false;
        private Geocoder geocoder;
        private Location location;
        private GeocodeResultCallback callback;

        public GeocodeRunnable(Context appContext, Location location, GeocodeResultCallback callback) {
            this.location = location;
            this.callback = callback;
            geocoder = new Geocoder(appContext, Locale.ENGLISH);
        }

        @Override
        public void run() {
            while (!done) {
                String result = "";
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
                            location.getLongitude(), 1);
                    if (addresses != null && !addresses.isEmpty()) {
                        result = addresses.get(0).getLocality() + ", " + addresses.get(0).getCountryName();
                    }
                    Logger.loge(TAG, "Addresses --> " + result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (callback != null) callback.onResultReady(result);
            }
        }

        public void shutdown() {
            done = true;
            callback = null;
            geocoder = null;
        }

        public interface GeocodeResultCallback {
            void onResultReady(String name);
        }
    }

    private void getAddressFrom(Location location, GeocodeRunnable.GeocodeResultCallback callback) {
        if (geocodeRunnable != null) {
            geocodeRunnable.shutdown();
            geocodeRunnable = null;
        }

        geocodeRunnable = new GeocodeRunnable(appContext, location, callback);
        new Thread(geocodeRunnable).start();
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
                            Logger.logd(TAG, "LocationItem settings are not satisfied. Attempting to upgrade " +
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
        appContext = null;
        listener = null;
        activity = null;
        mFusedLocationClient = null;
        mSettingsClient = null;
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

    public ArrayList<LocationItem> getLocations() {
        if (locations == null) {
            locations = new ArrayList<>();
            locations.add(new LocationItem(activity.getString(R.string.cur_location), null));
            locations.add(new LocationItem(activity.getString(R.string.london),
                    new Coordinates(51.50, -0.12)));
            locations.add(new LocationItem(activity.getString(R.string.new_york),
                    new Coordinates(40.71, -74.005)));
            locations.add(new LocationItem(activity.getString(R.string.tokyo),
                    new Coordinates(35.68, 139.69)));
        }
        return locations;
    }

    public interface LocationListener {
        void locationObtained(@NonNull Location location);

        void locationAccessGranted(boolean isGranted);
    }
}
