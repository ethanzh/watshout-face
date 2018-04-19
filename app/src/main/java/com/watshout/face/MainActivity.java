package com.watshout.face;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.akhgupta.easylocation.EasyLocationAppCompatActivity;
import com.akhgupta.easylocation.EasyLocationRequest;
import com.akhgupta.easylocation.EasyLocationRequestBuilder;
import com.google.android.gms.location.LocationRequest;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity { //EasyLocationAppCompatActivity {

    private static final int ACCESS_FINE_LOCATION = 1;

    private static final int READ_PHONE_STATE = 2;

    @SuppressLint("HardwareIds")
    private String getID() {
        return Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID);
    }

    private String parseGPSData(Location location) {
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        double time = location.getTime();

        String toPost = "{\"lat\": " + lat + ", \"long\": " + lon + ", \"time\": " + time + "}";

        return toPost;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        String data = parseGPSData(location);

        Log.wtf("REAL", data);

        PostData post = new PostData();
        post.execute(data);


        /*
        int locationPermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (locationPermissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION);
        }

        int phoneStatePermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (phoneStatePermissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE);
        }
        */


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView text = findViewById(R.id.text);
        Button button = findViewById(R.id.button);

        /*

        @SuppressLint("RestrictedApi")
        LocationRequest locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(2000);
         //       .setFastestInterval(1000);

        EasyLocationRequest easyLocationRequest = new EasyLocationRequestBuilder()
                .setLocationRequest(locationRequest)
        //        .setFallBackToLastLocationTime(3000)
                .build();

        requestLocationUpdates(easyLocationRequest);

        */

    }

    /*
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ACCESS_FINE_LOCATION: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Permission enabled

                } else {

                    // Permission disabled

                }
                return;
            }

            case READ_PHONE_STATE: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Permission enabled

                } else {

                    // Permission disabled

                }
                return;
            }
        }
    }



    @Override
    public void onLocationPermissionGranted() {
        Log.wtf("GPS", "Permission Granted");
    }

    @Override
    public void onLocationPermissionDenied() {
        Log.wtf("GPS", "Permission Denied");
    }

    @Override
    public void onLocationReceived(Location location) {

        double lat = location.getLatitude();
        double lon = location.getLongitude();

        String toPost = "{\"lat\": " + lat + ", \"long\": " + lon + "}";

        double time = location.getTime();

        Log.wtf("GPS", Double.toString(time));

        PostData post = new PostData();
        post.execute(toPost);

    }

    @Override
    public void onLocationProviderEnabled() {
        Log.wtf("GPS", "Enabled");
    }

    @Override
    public void onLocationProviderDisabled() {
        Log.wtf("GPS", "Disabled");
    }

    */

}

class PostData extends AsyncTask<String, Void, Void> {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();

    protected void onPreExecute() {
        //display progress dialog.

    }

    protected Void doInBackground(String... strings) {

        String jsonData = strings[0];

        RequestBody body = RequestBody.create(JSON, jsonData);
        Request request = new Request.Builder()
                .url("https://personalsite-backend.firebaseio.com/coords.json")
                .post(body)  // Changing this from put to post changes behavior
                .build();

        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected void onPostExecute(Void result) {

    }

}
