package com.sample.anujain.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BasicImageDownloader.OnImageLoaderListener, AdapterView.OnItemClickListener {

    final ArrayList<Bitmap> images = new ArrayList<Bitmap>();
    final ArrayList<String> imagelist = new ArrayList<String>();
    final String url = "http://walle.mod.bz";
    final ArrayList<GridItems> items = new ArrayList<GridItems>();
    GridView mygrid;
    int i = 0;
    GridAdapter adapter = null;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        adapter = new GridAdapter(getApplicationContext(), R.id.gridview, items);
        mygrid = (GridView) findViewById(R.id.gridview);
        mygrid.setAdapter(adapter);
        RequestQueue queue = Volley.newRequestQueue(this);
        final BasicImageDownloader downloader = new BasicImageDownloader(this);
        float density = getResources().getDisplayMetrics().density;
        // return 0.75 if it's LDPI
        // return 1.0 if it's MDPI
        // return 1.5 if it's HDPI
        // return 2.0 if it's XHDPI
        // return 3.0 if it's XXHDPI
        // return 4.0 if it's XXXHDPI
        //we are planning to support LDPI,MDPI and HDPI
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("@@@@@@", response);
                        String[] listing = response.toString().split(",");
                        for (String x : listing) imagelist.add(x);
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
        mygrid.setOnItemClickListener(this);
    }

    private void process(String[] images, BasicImageDownloader downloader) {
        for (String imageLink : images) {
            downloader.download(imageLink, false);
        }
    }


    @Override
    public void onError(BasicImageDownloader.ImageError error) {

    }

    @Override
    public void onProgressChange(int percent) {

    }

    @Override
    public void onComplete(GridItems result) {
        Log.d("@@@@@@", "completed bitmap download");
        items.add(result);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
        GridItems item = (GridItems) parent.getItemAtPosition(position);
        //Create intent
        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
        Global.img = item.ImageId;
        intent.putExtra("title", item.label);
        //Start details activity
        startActivity(intent);
    }
}
