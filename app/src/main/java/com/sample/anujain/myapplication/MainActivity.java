package com.sample.anujain.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Calendar;

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
    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        adapter = new GridAdapter(getApplicationContext(), R.id.gridview, items);
        mygrid = (GridView) findViewById(R.id.gridview);
        mygrid.setAdapter(adapter);
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                GridItems item = (GridItems) msg.obj;
                items.add(item);
                adapter.notifyDataSetChanged();
                mProgressStatus += 20;
                if (mProgressStatus == 100) {
                    mProgress.setVisibility(View.GONE);
                }
            }
        };
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
    }


    @Override
    public void onError(BasicImageDownloader.ImageError error) {

    }

    @Override
    public void onProgressChange(int percent) {
    }

    @Override
    public void onComplete(GridItems result) {
        Message msg = new Message();
        msg.obj = result;
        mHandler.handleMessage(msg);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
        GridItems item = (GridItems) parent.getItemAtPosition(position);
        //Create intent
        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
        Global.img = item.bitmapImage;
        intent.putExtra("title", item.label);
        //Start details activity
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent i = new Intent(this, autoWallpaper.class);
        PendingIntent pending = PendingIntent.getBroadcast(this, 0, i, 0);
        i.setAction("change");
        int id = v.getId();
        Snackbar snackbar = Snackbar.make(v, "AutoChange Enabled !!!", Snackbar.LENGTH_LONG);
        switch (id) {
            case R.id.fab:
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        animateFAB();
                    }
                });
                break;
            case R.id.fab1:
                SharedPreferences prefs = this.getSharedPreferences(
                        "com.sample.anujain.myapplication", Context.MODE_PRIVATE);
                String autochangeProp = "com.example.app.autochange";
                // use a default value using new Date()
                boolean autochange = prefs.getBoolean(autochangeProp, false);
                if (autochange) {
                    prefs.edit().putBoolean(autochangeProp, false).apply();
                    //make it off
                    if (alarmMgr != null) {
                        alarmMgr.cancel(pending);
                    }
                    // get snackbar view
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(getResources().getColor(R.color.grey));
// change snackbar text color
                    int snackbarTextId = android.support.design.R.id.snackbar_text;
                    TextView textView = (TextView) snackbarView.findViewById(snackbarTextId);
                    textView.setTextColor(getResources().getColor(R.color.colorAccent));
                    textView.setText("AutoChange disabled !!!!");
                    snackbar.show();
                    //stop the service
                    fab1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    //change the color of button to red
                } else {
                    prefs.edit().putBoolean(autochangeProp, true).apply();
                    //make it on
                    // Set the alarm to start at approximately 00:00 midnight.
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(Calendar.HOUR_OF_DAY, 24);
                    // With setInexactRepeating(), you have to use one of the AlarmManager interval
                    // constants--in this case, AlarmManager.INTERVAL_DAY.
                    alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                            AlarmManager.INTERVAL_DAY, pending);
                    //start the service
                    fab1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.liteGreen)));
                    //change the color of button to green
                    // get snackbar view
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(getResources().getColor(R.color.grey));
                    // change snackbar text color
                    int snackbarTextId = android.support.design.R.id.snackbar_text;
                    TextView textView = (TextView) snackbarView.findViewById(snackbarTextId);
                    textView.setText("AutoChange enabled !!!!");
                    textView.setTextColor(getResources().getColor(R.color.liteGreen));
                    snackbar.show();
                }
                break;
            case R.id.fab2:
                //display about us dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
                builder.setTitle(getString(R.string.dialog_title));
                builder.setMessage(getString(R.string.dialog_message));
                AlertDialog dialog = builder.create();
// display dialog
                dialog.show();
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
