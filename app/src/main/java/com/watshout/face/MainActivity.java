package com.watshout.face;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.provider.Settings.Secure;

import com.google.android.gms.location.LocationServices;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends AppCompatActivity {

    int enabled = 0;
    boolean everStarted = false;

    private boolean hasGps() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView text = findViewById(R.id.text);
        Button button = findViewById(R.id.button);

        @SuppressLint("HardwareIds") String android_id =
                Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID);

        Log.wtf("android_id", android_id);

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.

                int longitude = (int) location.getLongitude();

                Log.wtf("LOCATION", Integer.toString(longitude));

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.wtf("LOCATION", "status");
            }

            public void onProviderEnabled(String provider) {
                Log.wtf("LOCATION", "enabled");
            }

            public void onProviderDisabled(String provider) {
                Log.wtf("LOCATION", "disabled");
            }
        };

        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        assert locationManager != null;
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.wtf("ENABLED", Integer.toString(enabled));

                if (enabled == 0){

                    enabled = 1;

                    for(int i = 0; i < 1000; i++){
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        new PutData().execute();
                    }
                } else {

                    Log.wtf("ENABLED", "I'm running");
                    enabled = 1;

                }
            }
        });
    }

}

class PutData extends AsyncTask<Void,Void,Void> {
    protected void onPreExecute() {
        //display progress dialog.

    }
    protected Void doInBackground(Void... params) {
        PostExample example = new PostExample();
        String json = example.bowlingJson();
        String response = null;
        try {

            //String url = "https://personalsite-backend.firebaseio.com/coords/" + time + ".json";

            String url = "https://personalsite-backend.firebaseio.com/coords.json";

            response = example.post(url, json);


        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(response);

        return null;
    }

    protected void onPostExecute(Void result){

    }

}
