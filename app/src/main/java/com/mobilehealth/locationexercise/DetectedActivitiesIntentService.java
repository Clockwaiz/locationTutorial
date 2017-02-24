package com.mobilehealth.locationexercise;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;

/**
 * Created by LengKwokWai on 23/2/2017.
 */

public class DetectedActivitiesIntentService extends IntentService{

    protected  static final String TAG = "detection_is";

    public DetectedActivitiesIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);

        Intent localIntent = new Intent(Constants.BROADCAST_ACTION);

        //Get the list of the probable activities associated with the current stated device
        //Each activity is associated with a confidence level, which is an 0 and 100
        ArrayList<DetectedActivity> detectedActivities = (ArrayList)result.getProbableActivities();

        Log.i(TAG,"activities detected");

        //Broadcast the list of detected activities
        localIntent.putExtra(Constants.ACTIVITY_EXTRA,detectedActivities);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }
}
