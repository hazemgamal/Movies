package com.example.ecss.movies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ecss on 25/03/2016.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<MovieObj> items = new ArrayList<MovieObj>();

    public ImageAdapter(Context mContext, ArrayList<MovieObj> items) {
        this.mContext = mContext;
        this.items = items;
    }

    public int getCount() {
        return items.size();
    }

    public MovieObj getItem(int position) {
        return items.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    class Holder {
        public ImageView img;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        if (convertView == null) {

            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item_movie, null);
            holder.img = (ImageView) convertView.findViewById(R.id.gridview_item_movie_imageview);
            convertView.setTag(holder);

        } else {
            holder = (Holder) convertView.getTag();
        }

        Picasso.with(mContext).load("http://image.tmdb.org/t/p/w500" + getItem(position).poster_path).into(holder.img);

        return convertView;
    }
}
