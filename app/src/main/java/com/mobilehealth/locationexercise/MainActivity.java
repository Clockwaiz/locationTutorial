package com.mobilehealth.locationexercise;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener{

   protected static int UPDATE_INTERVAL = 1000;

   protected Button btnDetectActivity;
   protected Button btnGeofence;
   protected TextView txtOutput;
   protected TextView txtLatitude;
   protected TextView txtLongitude;
   protected GoogleApiClient mGoogleApiClient;
   protected LocationRequest mLocationRequest;
   protected Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialiseView();
        buildGoogleApiClient();

        btnDetectActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ActivityRecognitionActivity.class);
                startActivity(i);

            }
        });

        btnGeofence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, GeoFenceActivity.class);
                startActivity(i);
            }
        });

    }

    protected void initialiseView(){
        txtOutput = (TextView)findViewById(R.id.txtOutput);
        txtLatitude = (TextView)findViewById(R.id.txtLatitude);
        txtLongitude = (TextView)findViewById(R.id.txtLongitude);

        btnDetectActivity = (Button)findViewById(R.id.btnDetectActivity);
        btnGeofence = (Button)findViewById(R.id.btnGeofence);

    }

    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mGoogleApiClient.isConnected())
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);


        if (ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ){

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);
        }


        //Single update call to get the location
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLastLocation != null){
            txtLongitude.setText(String.valueOf(mLastLocation.getLongitude()));
            txtLatitude.setText((String.valueOf(mLastLocation.getLatitude())));
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        txtOutput.setText(location.toString());

        txtLongitude.setText(String.valueOf(location.getLongitude()));
        txtLatitude.setText((String.valueOf(location.getLatitude())));

    }
}
