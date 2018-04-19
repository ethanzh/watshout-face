package com.watshout.face;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity { //EasyLocationAppCompatActivity {

    private static final int ACCESS_FINE_LOCATION = 1;

    private static final int READ_PHONE_STATE = 2;

    public static TextView text;

    @SuppressLint("HardwareIds")
    private String getID() {
        return Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        LocationListener locationListener;

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        text = findViewById(R.id.text);
        Button button = findViewById(R.id.button);

        final int FINE_LOCATION_PERMISSION = 1;

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        FINE_LOCATION_PERMISSION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }

        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

        locationListener = new MyLocationListener();

        assert locationManager != null;
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);


    }

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

class MyLocationListener implements LocationListener {


    private String parseGPSData(Location location) {
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        double time = location.getTime();

        MainActivity.text.setText(lat + ", " + lon);

        return "{\"lat\": " + lat + ", \"long\": " + lon + ", \"time\": " + time + "}";
    }

    public void onLocationChanged(Location location) {
        String message = String.format(
                "New Location \n Longitude: %1$s \n Latitude: %2$s",
                location.getLongitude(), location.getLatitude()
        );

        String data = parseGPSData(location);

        PostData post = new PostData();

        post.execute(data);

        Log.v("GPSDATA", message);
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
                            /* This is called when the GPS status alters */
        switch (status) {
            case LocationProvider.OUT_OF_SERVICE:
                Log.v("GPSDATA", "OUT_OF_SERVICE");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.v("GPSDATA", "TEMPORARILY_UNAVAILABLE");
                break;
            case LocationProvider.AVAILABLE:
                Log.v("GPSDATA", "AVAILABLE");
                break;
        }
    }

    public void onProviderDisabled(String provider) {
        Log.v("GPSDATA", "Disabled");
    }

    public void onProviderEnabled(String provider) {
        Log.v("GPSDATA", "Enabled");
    }


}
