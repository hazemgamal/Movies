package com.example.ecss.movies;

import java.io.Serializable;

/**
 * Created by ecss on 17/04/2016.
 */
public class MovieObj implements Serializable {
    public String poster_path, title, vote_average, release_date, overview, id;
    public int dbKey;

    public MovieObj() {

    }

    public MovieObj(String poster_path, String title, String vote_average, String release_date, String overview, String id) {
        this.poster_path = poster_path;
        this.title = title;
        this.vote_average = vote_average;
        this.release_date = release_date;
        this.overview = overview;
        this.id = id;
    }

    public int getDbKey() {
        return dbKey;
    }

    public void setDbKey(int dbKey) {
        this.dbKey = dbKey;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
