package com.watshout.face;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView text = findViewById(R.id.text);
        Button button = findViewById(R.id.button);
        Button end = findViewById(R.id.end);


        final Timer t = new Timer();

        t.schedule(new TimerTask() {
            @Override
            public void run() {

                new PutData().execute();

            }
        }, 0, 2000);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

   /* void makeGetRequest() {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://personalsite-backend.firebaseio.com/users.json";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        text.setText(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                text.setText("That didn't work!");
            }
        });

        queue.add(stringRequest);

    } */
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
