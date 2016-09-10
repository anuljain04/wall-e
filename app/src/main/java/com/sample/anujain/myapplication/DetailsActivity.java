package com.sample.anujain.myapplication;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by anujain on 9/6/2016.
 */
public class DetailsActivity extends ActionBarActivity implements View.OnClickListener {


    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_details);

        String title = getIntent().getStringExtra("title");
        Bitmap bitmap = Global.img;

        TextView titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(title);

        ImageView imageView = (ImageView) findViewById(R.id.image);
        imageView.setImageBitmap(bitmap);
        fab = (FloatingActionButton) findViewById(R.id.setwall);
        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        WallpaperManager myWallpaperManager
                = WallpaperManager.getInstance(getApplicationContext());
        try {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int width = Global.img.getWidth();
            int height = Global.img.getHeight();
            float scaleWidth = metrics.scaledDensity;
            float scaleHeight = metrics.scaledDensity;
            // create a matrix for the manipulation
            Matrix matrix = new Matrix();
            // resize the bit map
            matrix.postScale(scaleWidth, scaleHeight);
            // recreate the new Bitmap
            Bitmap resizedBitmap = Bitmap.createBitmap(Global.img, 0, 0, width, height, matrix, true);
            myWallpaperManager.setBitmap(resizedBitmap);
            Snackbar snackbar = Snackbar.make(view, "WallPaper has been Set :D", Snackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(getResources().getColor(R.color.grey));
            // change snackbar text color
            int snackbarTextId = android.support.design.R.id.snackbar_text;
            TextView textView = (TextView) snackbarView.findViewById(snackbarTextId);
            textView.setTextColor(getResources().getColor(R.color.liteGreen));
            snackbar.show();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}