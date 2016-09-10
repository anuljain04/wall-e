package com.sample.anujain.myapplication;

import android.graphics.Bitmap;

/**
 * Created by anujain on 9/4/2016.
 */
public class GridItems {
    Bitmap bitmapImage;
    String label;

    public GridItems(Bitmap id, String text) {
        bitmapImage = id;
        label = text;
    }
}
