package com.watshout.face;

import android.os.NetworkOnMainThreadException;
import android.util.Log;

import java.io.IOException;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


class PostExample {
    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client = new OkHttpClient();

    String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)  // Changing this from put to post changes behavior
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (NetworkOnMainThreadException e) {
            Log.e("error", e.toString());
            return "null";
        }

    }

    String bowlingJson() {

        long unixTime = System.currentTimeMillis();

        String time = Long.toString(unixTime);

        // what is our range?
        int max = 100;

        // create instance of Random class
        Random randomNum = new Random();

        int _lat = -29 + randomNum.nextInt(max) / 10;
        int _long = 131 + randomNum.nextInt(max) / 7;

        // return "{\"" + time + "\": {\"lat\": 120.3, \"long\": 90.4}}";

        return "{\"lat\": " + _lat + ", \"long\": " + _long + "}";

    }

}