package com.example.ecss.movies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    private View rootView;
    private MovieObj movie;
    private RecyclerViewAdapter adapter;
    private RecyclerView listview;
    private RecyclerView listview_review;
    private ArrayList<String> links;
    private final String LOG_TAG = DetailActivity.class.getSimpleName();

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private DetailActivityFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final DetailActivityFragment.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public DetailActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        listview = (RecyclerView) rootView.findViewById(R.id.listview_trailers);

        listview.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), listview, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + links.get(position)));
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        Button clickButton = (Button) rootView.findViewById(R.id.favoriteButton);
        clickButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatabaseHandler db = new DatabaseHandler(getActivity());
                ArrayList<MovieObj> movies = db.getAllMovies();

                for (int i = 0; i < movies.size(); i++) {

                    if (movies.get(i).id.equals(movie.id)) {
                        Toast.makeText(getActivity(), "Movie is already marked", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                db.addMovie(movie);
                Toast.makeText(getActivity(), "Movie is successfully marked", Toast.LENGTH_LONG).show();
            }
        });


        listview_review = (RecyclerView) rootView.findViewById(R.id.listview_reviews);


        if (((MovieObj) getActivity().getIntent().getSerializableExtra("MovieObj")) == null) {

            //tablet case
            Bundle arguments = getArguments();
            if (arguments != null) {
                movie = arguments.getParcelable("Movie");
            }

        } else {

            //handset case
            movie = (MovieObj) getActivity().getIntent().getSerializableExtra("MovieObj");
        }

        TextView tv = (TextView) rootView.findViewById(R.id.title);
        tv.setText(movie.title);


        ImageView img = (ImageView) rootView.findViewById(R.id.poster);
        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w500" + movie.poster_path).into(img);


        tv = (TextView) rootView.findViewById(R.id.overview);
        tv.setText(movie.overview);

        tv = (TextView) rootView.findViewById(R.id.vote_average);
        tv.setText(movie.vote_average + "/10");

        tv = (TextView) rootView.findViewById(R.id.release_date);
        tv.setText(movie.release_date);

        ArrayList<String> result = null;
        NetworkConnection task = new NetworkConnection();
        try {
            result = getMoviesDataFromJson(task.execute("http://api.themoviedb.org/3/movie/" + movie.id + "/videos").get());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        if (result != null) {

            links = result;

            ArrayList<String> temp2 = new ArrayList<String>();

            for (int i = 0; i < result.size(); i++) {
                temp2.add("Trailer " + (i + 1));
            }

            adapter = new RecyclerViewAdapter(R.id.listview_item_movie_textview, R.layout.list_item_movie, temp2);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            listview.setLayoutManager(mLayoutManager);
            listview.setItemAnimator(new DefaultItemAnimator());
            listview.setAdapter(adapter);
        }


        task = new NetworkConnection();

        try {
            result = getReviewsDataFromJson(task.execute("http://api.themoviedb.org/3/movie/" + movie.id + "/reviews").get());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        if (result != null) {
            adapter = new RecyclerViewAdapter(R.id.listview_item_review_textview, R.layout.list_item_review, result);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            listview_review.setLayoutManager(mLayoutManager);
            listview_review.setItemAnimator(new DefaultItemAnimator());
            listview_review.setAdapter(adapter);
        }

        return rootView;
    }

    private ArrayList<String> getMoviesDataFromJson(String moviesJsonStr) throws JSONException {
        if (moviesJsonStr == null) return null;

        JSONObject jsonObject = new JSONObject(moviesJsonStr);
        JSONArray results = jsonObject.getJSONArray("results");
        ArrayList<String> items = new ArrayList<String>();

        try {
            for (int i = 0; i < results.length(); i++) {

                JSONObject temp = results.getJSONObject(i);

                items.add(temp.getString("key"));
            }


            return items;
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<String> getReviewsDataFromJson(String moviesJsonStr) throws JSONException {
        if (moviesJsonStr == null) return null;

        JSONObject jsonObject = new JSONObject(moviesJsonStr);
        JSONArray results = jsonObject.getJSONArray("results");
        ArrayList<String> items = new ArrayList<String>();

        try {
            for (int i = 0; i < results.length(); i++) {

                JSONObject temp = results.getJSONObject(i);

                items.add(temp.getString("author") + ": \n\n" + temp.getString("content"));
            }


            return items;
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }
}
