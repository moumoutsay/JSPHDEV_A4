package com.example.moumoutsay.smsmessagerwithgeo.model;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by moumoutsay on 4/13/15.
 */
public class GPSInfo extends Service implements LocationListener {
    private final String LOG_TAG = GPSInfo.class.getSimpleName();
    private final Context context;
    private final long MIN_DIS_FOR_UPDATE = 10;
    private final long PERIOD_TO_REFREAH_GPS = 1000 * 60 ;

    private boolean isGPSEnable = false;
    private boolean isNetworkEnable = false;
    private boolean canGetLocation = false;

    private double latitude;
    private double longtitude;
    private LocationManager locationManager;
    private Location location;

    public GPSInfo(Context context) {
        this.context = context;
        getLocation();
    }

    private Location getLocation() {
        try {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

            if (locationManager == null) {
                throw new Exception("Can not get locationMananger");
            }

            isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if ( !isNetworkEnable && !isGPSEnable) {
                canGetLocation = false;
                throw new Exception("Neither network nor GPS service is enable");
            } else {
                canGetLocation = true;

                if (isNetworkEnable) { // get from network
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            PERIOD_TO_REFREAH_GPS,
                            MIN_DIS_FOR_UPDATE,
                            this
                    );
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }

                if (isGPSEnable) { // get from GPS
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            PERIOD_TO_REFREAH_GPS,
                            MIN_DIS_FOR_UPDATE,
                            this
                    );
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }

                if (location != null) {
                    latitude = location.getLatitude();
                    longtitude = location.getLongitude();
                }
            }

        } catch (Exception e) {
            Log.d(LOG_TAG, "Can not get Location" + e);
            return null;
        }
        return location;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
