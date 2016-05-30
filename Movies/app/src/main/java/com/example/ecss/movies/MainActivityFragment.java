package com.example.ecss.movies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class MainActivityFragment extends Fragment {
    private ImageAdapter adapter;
    private GridView gridview;
    private View rootView;
    private ArrayList<MovieObj> result = null;
    private final String LOG_TAG = MainActivity.class.getSimpleName();

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        gridview = (GridView) rootView.findViewById(R.id.gridview_movies);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                if (rootView.findViewById(R.id.fragment_detail) == null) {
                    //handset case
                    Intent intent = new Intent(getActivity(), DetailActivity.class).putExtra("MovieObj", adapter.getItem(position));
                    startActivity(intent);
                } else {
                    //tablet case
                    ((Callback) getActivity()).onItemSelected(adapter.getItem(position));
                }
            }
        });
        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sorting = prefs.getString("sort", "popular");
        if (sorting.equals("popular")) {

            NetworkConnection task = new NetworkConnection();

            try {
                result = getMoviesDataFromJson(task.execute("http://api.themoviedb.org/3/movie/popular?").get());
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            if (result == null) {
                Toast.makeText(getActivity(), "Popular Movies list is empty", Toast.LENGTH_LONG).show();
            }

        } else if (sorting.equals("toprated")) {

            NetworkConnection task = new NetworkConnection();
            try {
                result = getMoviesDataFromJson(task.execute("http://api.themoviedb.org/3/movie/top_rated?").get());
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            if (result == null) {
                Toast.makeText(getActivity(), "Top Rated Movies list is empty", Toast.LENGTH_LONG).show();
            }

        } else {
            DatabaseHandler db = new DatabaseHandler(getActivity());
            result = db.getAllMovies();
            if (result.size() == 0) {
                Toast.makeText(getActivity(), "Favorite Movies list is empty", Toast.LENGTH_LONG).show();
            }
        }

        if (result != null) {
            adapter = new ImageAdapter(getActivity(), result);
            gridview.setAdapter(adapter);
        }
    }

    private ArrayList<MovieObj> getMoviesDataFromJson(String moviesJsonStr) throws JSONException {

        if (moviesJsonStr == null) return null;
        JSONObject jsonObject = new JSONObject(moviesJsonStr);
        JSONArray results = jsonObject.getJSONArray("results");

        ArrayList<MovieObj> items = new ArrayList<MovieObj>();


        try {
            for (int i = 0; i < results.length(); i++) {

                JSONObject temp = results.getJSONObject(i);

                items.add(new MovieObj(temp.getString("poster_path"), temp.getString("title"), temp.getString("vote_average"), temp.getString("release_date"), temp.getString("overview"), temp.getString("id")));
            }

            return items;
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

    public interface Callback {
        public void onItemSelected(MovieObj movie);
    }
}
