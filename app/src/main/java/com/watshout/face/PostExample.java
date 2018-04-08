package com.watshout.face;

import android.os.NetworkOnMainThreadException;
import android.util.Log;

import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostExample {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (NetworkOnMainThreadException e) {
            Log.e("error", e.toString());
            return "null";
        }

    }

    String bowlingJson(String player1, String player2) {
        return "{\"9999\": {\"number\": \"12345\", \"username\":\"ethan\"}}";
    }

    public static void main(String[] args) throws IOException {
        PostExample example = new PostExample();
        String json = example.bowlingJson("Jesse", "Jake");
        String response = example.post("http://www.roundsapp.com/post", json);
        System.out.println(response);
    }
}