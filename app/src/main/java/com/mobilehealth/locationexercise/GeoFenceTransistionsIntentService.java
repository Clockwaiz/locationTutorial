package com.mobilehealth.locationexercise;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LengKwokWai on 24/2/2017.
 */

public class GeoFenceTransistionsIntentService extends IntentService {

    protected  static final String TAG = "geofence_is";

    public GeoFenceTransistionsIntentService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
       GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if(geofencingEvent.hasError()){
            String errorMessage = Constants.getErrorString(this, geofencingEvent.getErrorCode());
            Log.e(TAG,errorMessage);
            return;
        }

        //Get transition type
        int geofenceTransition = geofencingEvent.getGeofenceTransition();


        if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER || geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT){

            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();
            String geofenceTransitionDetails = getGeofenceTransitionDetails(this,geofenceTransition,triggeringGeofences);


            sendNotification(geofenceTransitionDetails);
            Log.i(TAG,geofenceTransitionDetails);

        }else{

            Log.e(TAG, getString(R.string.geofence_transition_invalid_type,geofenceTransition));
        }


    }

    private String getGeofenceTransitionDetails(GeoFenceTransistionsIntentService geoFenceTransistionsIntentService, int geofenceTransition, List<Geofence> triggeringGeofences) {

        String geofenceTransitionString = Constants.getGeoFenceString(geoFenceTransistionsIntentService,geofenceTransition);

        ArrayList triggeringGeofencesIdsList = new ArrayList();
        for(Geofence geofence : triggeringGeofences){
            triggeringGeofencesIdsList.add(geofence.getRequestId());
        }

        String triggeringGeofencesIdsString = TextUtils.join(",",triggeringGeofencesIdsList);

        return geofenceTransitionString + ": " +triggeringGeofencesIdsString;
    }


    private void sendNotification(String notificationDetails){

        Intent notificationIntent = new Intent(getApplicationContext(),GeoFenceActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        stackBuilder.addParentStack(GeoFenceActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setSmallIcon(R.mipmap.ic_geo)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_geo))
                .setColor(Color.RED)
                .setContentTitle(notificationDetails)
                .setContentText("Click notification to return to app")
                .setContentIntent(notificationPendingIntent);

        builder.setAutoCancel(true);

        NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);



        mNotificationManager.notify(0,builder.build());


    }
}
