package com.watshout.face;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.provider.Settings.Secure;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.akhgupta.easylocation.EasyLocationAppCompatActivity;
import com.akhgupta.easylocation.EasyLocationRequest;
import com.akhgupta.easylocation.EasyLocationRequestBuilder;

import com.google.android.gms.location.LocationRequest;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends EasyLocationAppCompatActivity {

    private static final int REQUEST_READ_PHONE_STATE = 1;

    public static final String OBSERVE_GRANT_REVOKE_PERMISSIONS="android.permission.OBSERVE_GRANT_REVOKE_PERMISSIONS";

    @SuppressLint("HardwareIds")
    private String getID() {
        return Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {



        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        }



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView text = findViewById(R.id.text);
        Button button = findViewById(R.id.button);

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

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
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
