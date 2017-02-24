package com.mobilehealth.locationexercise;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;

public class ActivityRecognitionActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,ResultCallback<Status> {

    protected ActivityDetectionBroadcastReceiver mBroadcastReceiver;
    protected Button btnRequestActivity;
    protected Button btnRemoveActivity;
    protected TextView txtStatus;
    protected GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognition);

        mBroadcastReceiver = new ActivityDetectionBroadcastReceiver();;
        initialiseView();
        buildGoogleApiClient();


    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, new IntentFilter(Constants.BROADCAST_ACTION));
    }

    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    private void initialiseView() {

        btnRequestActivity = (Button)findViewById(R.id.btnRequestActivityUpdate);
        btnRemoveActivity = (Button)findViewById(R.id.btnRemoveActivityUpdate);
        txtStatus = (TextView)findViewById(R.id.txtStatus);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("activity","mGoogleApi connected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("activity","mGoogleApi suspended");
         mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("activity","mGoogleApi failed:"+connectionResult.getErrorMessage());
    }


    public void  requestActivityUpdatesButtonHandler(View view){
        if(!mGoogleApiClient.isConnected()){
            Toast.makeText(this,getString(R.string.not_connected),Toast.LENGTH_SHORT).show();
            return;
        }

            ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(
                    mGoogleApiClient,
                    Constants.DETECTION_INTERVAL_IN_MILISECONDS,
                    getAcivityDetectionPendingIntent()
            ).setResultCallback(this);

            btnRequestActivity.setEnabled(false);
            btnRemoveActivity.setEnabled(true);




    }

    public void removeActivityUpdatesButtonHandler(View view){
        if(!mGoogleApiClient.isConnected()){
            Toast.makeText(this,getString(R.string.not_connected),Toast.LENGTH_SHORT).show();
            return;
        }



            ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(
                    mGoogleApiClient,
                    getAcivityDetectionPendingIntent()
            ).setResultCallback(this);

            btnRequestActivity.setEnabled(true);
            btnRemoveActivity.setEnabled(false);


    }


    public PendingIntent getAcivityDetectionPendingIntent(){
        Intent intent = new Intent(this, DetectedActivitiesIntentService.class);
        return PendingIntent.getService(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onResult(@NonNull Status status) {
        String TAG = "callback";

        if(status.isSuccess()){
           Log.e(TAG,"Successfully added activity detection.");
        }else{
            Log.e(TAG,"Error adding or removing activity detection");
        }
    }


    public class ActivityDetectionBroadcastReceiver extends BroadcastReceiver{

       protected static final String TAG = "receiver";

       ArrayList<DetectedActivity> detectedActivityList;

       @Override
       public void onReceive(Context context, Intent intent) {
           detectedActivityList = intent.getParcelableArrayListExtra(Constants.ACTIVITY_EXTRA);

           String strStatus = "";

           for(DetectedActivity da :detectedActivityList ){
              Log.i(TAG,"ACTIVITY DETECTION type:"+da.getType() + "  "+da.getConfidence()+"%");
               strStatus += Constants.getActivityString(da.getType(),ActivityRecognitionActivity.this) +" "+ da.getConfidence()+ "%\n";
           }

           txtStatus.setText(strStatus);
       }
   }


}
