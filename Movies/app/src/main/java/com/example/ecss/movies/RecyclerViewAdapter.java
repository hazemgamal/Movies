package com.example.ecss.movies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ecss on 29/04/2016.
 */


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private List<String> moviesList;
    private int id1, id2;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView trailer;

        public MyViewHolder(View view) {
            super(view);
            trailer = (TextView) view.findViewById(id1);
        }
    }


    public RecyclerViewAdapter(int id1, int id2, List<String> moviesList) {
        this.moviesList = moviesList;
        this.id1 = id1;
        this.id2 = id2;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(id2, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.trailer.setText(moviesList.get(position));
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}