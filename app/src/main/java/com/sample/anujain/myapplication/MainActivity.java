package com.sample.anujain.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BasicImageDownloader.OnImageLoaderListener, AdapterView.OnItemClickListener, View.OnClickListener {

    private static final int PROGRESS = 0x1;
    final ArrayList<Bitmap> images = new ArrayList<Bitmap>();
    final ArrayList<String> imagelist = new ArrayList<String>();
    final String url = "http://walle.mod.bz";
    final ArrayList<GridItems> items = new ArrayList<GridItems>();
    GridView mygrid;
    int i = 0;
    GridAdapter adapter = null;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    private ProgressBar mProgress;
    private int mProgressStatus = 0;
    private Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        adapter = new GridAdapter(getApplicationContext(), R.id.gridview, items);
        mygrid = (GridView) findViewById(R.id.gridview);
        mygrid.setAdapter(adapter);
        mProgress = (ProgressBar) findViewById(R.id.progress);
        RequestQueue queue = Volley.newRequestQueue(this);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);
        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
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
        mProgress.setProgress(mProgress.getMax());
        mProgress.setVisibility(View.GONE);
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
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mProgress.setProgress(mProgressStatus += 20);
            }
        });
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab:
                animateFAB();
                break;
            case R.id.fab1:

                Log.d("Raj", "Fab 1");
                break;
            case R.id.fab2:

                Log.d("Raj", "Fab 2");
                break;
        }
    }


    public void animateFAB() {

        if (isFabOpen) {

            fab.startAnimation(rotate_backward);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;
            Log.d("Raj", "close");

        } else {

            fab.startAnimation(rotate_forward);
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;
            Log.d("Raj", "open");

        }
    }
}
