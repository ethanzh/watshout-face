package com.watshout.face;

import android.os.NetworkOnMainThreadException;
import android.util.Log;

import java.io.IOException;
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
                .put(body)  // Changing this from put to post changes behavior
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
        // return "{\"9999\": {\"number\": \"12345\", \"username\":\"ethan\"}}";


        return "{\"" + "test" + "\": \"teeest\"}";
    }

}