package com.sample.anujain.myapplication;

import android.app.IntentService;
import android.app.WallpaperManager;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.util.Random;

/**
 * Created by anujain on 9/10/2016.
 */
public class autoWallpaper extends IntentService implements BasicImageDownloader.OnImageLoaderListener {
    final String url = "http://walle.mod.bz";

    public autoWallpaper(String name) {
        super(name);
    }

    public autoWallpaper() {
        super("hello");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //use image downloader download and set the wallpaper
        RequestQueue queue = Volley.newRequestQueue(this);
        final BasicImageDownloader downloader = new BasicImageDownloader(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("@@@@@@", response);
                        String[] listing = response.toString().split(",");
                        process(listing, downloader);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // mTextView.setText("That didnt work!");
                if (error != null) {
                    Log.d(error.toString(), "network connection failed");
                } else {
                    Log.d("@@@@@", "network connection failed");
                }
            }
        });
        queue.add(stringRequest);
    }

    private void process(String[] listing, BasicImageDownloader downloader) {
        Random r = new Random();
        downloader.download(url + "/files/" + listing[r.nextInt(listing.length)], false);
    }

    @Override
    public void onError(BasicImageDownloader.ImageError error) {

    }

    @Override
    public void onProgressChange(int percent) {

    }

    @Override
    public void onComplete(GridItems result) {
        WallpaperManager wallManager = WallpaperManager.getInstance(getApplicationContext());
        try {
            wallManager.clear();
            wallManager.setBitmap(result.bitmapImage);
        } catch (IOException ex) {

        }
    }
}
