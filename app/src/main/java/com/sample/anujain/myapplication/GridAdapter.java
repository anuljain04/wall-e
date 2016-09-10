package com.sample.anujain.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.sample.anujain.myapplication.R.layout.grid_item_layout;

/**
 * Created by anujain on 9/4/2016.
 */
public class GridAdapter extends BaseAdapter {
    ArrayList<GridItems> items;

    public GridAdapter(Context context, int resource, List objects) {
        items = (ArrayList) objects;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewholder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(grid_item_layout, parent, false);
            holder = new viewholder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (viewholder) convertView.getTag();
        }
        GridItems item = items.get(position);
        holder.image.setImageBitmap(item.bitmapImage);
        holder.text.setText(item.label);
        return convertView;
    }

    class viewholder {
        ImageView image;
        TextView text;

        public viewholder(View v) {
            image = (ImageView) v.findViewById(R.id.imageView);
            text = (TextView) v.findViewById(R.id.textView);
        }
    }
}
