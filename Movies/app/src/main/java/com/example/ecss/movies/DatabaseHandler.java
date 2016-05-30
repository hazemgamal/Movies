package com.example.ecss.movies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by ecss on 30/04/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private final String LOG_TAG = DatabaseHandler.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;


    private static final String DATABASE_NAME = "favoritesDB";


    private static final String TABLE_FAVORITES = "favorites";

    private static final String KEY_ID = "id";
    private static final String KEY_POSTER_PATH = "poster_path";
    private static final String KEY_TITLE = "title";
    private static final String KEY_VOTE_AVERAGE = "vote_average";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_KEY = "dbKey";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FAVORITES_TABLE = "CREATE TABLE " + TABLE_FAVORITES + "("
                + KEY_KEY + " INTEGER PRIMARY KEY," + KEY_POSTER_PATH + " TEXT," + KEY_VOTE_AVERAGE + " TEXT," + KEY_RELEASE_DATE + " TEXT," + KEY_OVERVIEW + " TEXT," + KEY_TITLE + " TEXT,"
                + KEY_ID + " TEXT" + ")";
        db.execSQL(CREATE_FAVORITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);


        // Create tables again
        onCreate(db);
    }

    public void addMovie(MovieObj movie) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_POSTER_PATH, movie.getPoster_path());
        values.put(KEY_VOTE_AVERAGE, movie.getVote_average());
        values.put(KEY_RELEASE_DATE, movie.getRelease_date());
        values.put(KEY_OVERVIEW, movie.getOverview());
        values.put(KEY_TITLE, movie.getTitle());
        values.put(KEY_ID, movie.getId());

        db.insert(TABLE_FAVORITES, null, values);
        db.close();
    }


    public ArrayList<MovieObj> getAllMovies() {
        ArrayList<MovieObj> movieList = new ArrayList<MovieObj>();

        String selectQuery = "SELECT  * FROM " + TABLE_FAVORITES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {

                MovieObj movie = new MovieObj();

                movie.setDbKey(Integer.parseInt(cursor.getString(0)));
                movie.setPoster_path(cursor.getString(1));
                movie.setVote_average(cursor.getString(2));
                movie.setRelease_date(cursor.getString(3));
                movie.setOverview(cursor.getString(4));
                movie.setTitle(cursor.getString(5));
                movie.setId(cursor.getString(6));

                movieList.add(movie);
            } while (cursor.moveToNext());
        }

        return movieList;
    }
}
