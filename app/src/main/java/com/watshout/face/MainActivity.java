package com.watshout.face;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.provider.Settings.Secure;
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

    @SuppressLint("HardwareIds")
    private String getID() {
        return Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView text = findViewById(R.id.text);
        Button button = findViewById(R.id.button);


        @SuppressLint("RestrictedApi")
        LocationRequest locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(5000)
                .setFastestInterval(5000);

        EasyLocationRequest easyLocationRequest = new EasyLocationRequestBuilder()
                .setLocationRequest(locationRequest)
                .setFallBackToLastLocationTime(3000)
                .build();

        requestLocationUpdates(easyLocationRequest);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int i = 0; i < 1000; i++) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    new PutData().execute();
                }
            }
        });
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

        Log.wtf("GPS", toPost);

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

class PutData extends AsyncTask<Void, Void, Void> {
    protected void onPreExecute() {
        //display progress dialog.

    }

    protected Void doInBackground(Void... params) {
        PostExample example = new PostExample();
        String json = example.bowlingJson();
        String response = null;
        try {

            String url = "https://personalsite-backend.firebaseio.com/coords.json";

            response = example.post(url, json);


        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(response);

        return null;
    }

    protected void onPostExecute(Void result) {

    }

}

