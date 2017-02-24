package com.mobilehealth.locationexercise;

import android.content.Context;
import android.content.res.Resources;

import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

/**
 * Created by LengKwokWai on 23/2/2017.
 */

public class Constants {


    public static final String PACKAGE_NAME = "com.mobilehealth.locationexercise";
    public static final String BROADCAST_ACTION = PACKAGE_NAME + ".BROADCAST_ACTION";
    public static final String ACTIVITY_EXTRA = PACKAGE_NAME + ".ACTIVITY_EXTRA";
    public static final String SHARED_PREFERENCES_NAME = PACKAGE_NAME + ".SHARED_PREFERENCE";
    public static final String ACTIVITY_UPDATES_REQUESTED_KEY = PACKAGE_NAME + ".ACTIVITY_UPDATES_REQUESTED";
    public static final String DETECTED_ACTIVITIES = PACKAGE_NAME + ".DETECTED_ACTIVITY";
    public static final long DETECTION_INTERVAL_IN_MILISECONDS = 0;
    public static final int[] MONITORED_ACTIVITIES = {
            DetectedActivity.STILL,
            DetectedActivity.ON_FOOT,
            DetectedActivity.WALKING,
            DetectedActivity.RUNNING,
            DetectedActivity.ON_BICYCLE,
            DetectedActivity.IN_VEHICLE,
            DetectedActivity.TILTING,
            DetectedActivity.UNKNOWN


    };

    public static final long GEOFENCE_EXPIRATION_IN_HOURS = 12;
    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS = GEOFENCE_EXPIRATION_IN_HOURS * 60 * 60 * 1000;

    public static final float GEOFENCE_RADIUS_IN_METERS = 100; // 1 mile= 1.6 km

    private Constants(){

    }




    public static final String getActivityString(int detectedActivityType,Context context){
        Resources resources = context.getResources();
        switch(detectedActivityType){
            case DetectedActivity.STILL:
                return resources.getString(R.string.still);
            case DetectedActivity.ON_FOOT:
                return resources.getString(R.string.on_foot);
            case DetectedActivity.WALKING:
                return resources.getString(R.string.walking);
            case DetectedActivity.RUNNING:
                return resources.getString(R.string.running);
            case DetectedActivity.ON_BICYCLE:
                return resources.getString(R.string.on_bicycle);
            case DetectedActivity.IN_VEHICLE:
                return resources.getString(R.string.in_vehicle);
            case DetectedActivity.TILTING:
                return resources.getString(R.string.tilting);
            case DetectedActivity.UNKNOWN:
                default:
                    return resources.getString(R.string.unidentifiable_activity);


        }

    }



    public static String getErrorString(Context context, int errorCode) {
        Resources mResources = context.getResources();
        switch (errorCode) {
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return mResources.getString(R.string.geofence_not_available);
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return mResources.getString(R.string.geofence_too_many_geofences);
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return mResources.getString(R.string.geofence_too_many_pending_intents);
            default:
                return mResources.getString(R.string.unknown_geofence_error);
        }
    }

    public static String getGeoFenceString(Context context, int id) {
        Resources mResources = context.getResources();
        switch (id) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return mResources.getString(R.string.geofence_enter);
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return mResources.getString(R.string.geofence_exit);
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                return mResources.getString(R.string.geofence_dwell);
            default:
                return mResources.getString(R.string.unknown_geofence_error);
        }
    }


    public static final HashMap<String,LatLng> AREA_LANDMARKS = new HashMap<>();
        static{

            AREA_LANDMARKS.put("HSC", new LatLng(3.160111, 101.722772));
            AREA_LANDMARKS.put("AMPANG_PARK", new LatLng(3.159825, 101.719000));


        }
}
