package com.edocent.movieapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by SRIVASTAVAA on 10/20/2015.
 */
public class Movie implements Parcelable{

    int id;

    String hindiMovieId;
    String imdbId;
    String runtime;

    String title;
    String overview;
    String releaseDate;
    String posterPath;
    String voteCount;
    String movieLength;
    String voteAverage;
    String favorite;
    String trailerLink;

    ArrayList<Trailer> trailersList;

    public Movie() {
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", hindiMovieId='" + hindiMovieId + '\'' +
                ", imdbId='" + imdbId + '\'' +
                ", title='" + title + '\'' +
                ", overview='" + overview + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", posterPath='" + posterPath + '\'' +
                '}';
    }

    public Movie(Parcel in) {
        setId(in.readInt());
        setHindiMovieId(in.readString());
        setImdbId(in.readString());
        setTitle(in.readString());
        setOverview(in.readString());
        setReleaseDate(in.readString());
        setPosterPath(in.readString());
        setVoteCount(in.readString());
        setMovieLength(in.readString());
        setVoteAverage(in.readString());
        setFavorite(in.readString());
        setTrailerLink(in.readString());
        setRuntime(in.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeString(getHindiMovieId());
        dest.writeString(getImdbId());
        dest.writeString(getTitle());
        dest.writeString(getOverview());
        dest.writeString(getReleaseDate());
        dest.writeString(getPosterPath());
        dest.writeString(getVoteCount());
        dest.writeString(getMovieLength());
        dest.writeString(getVoteAverage());
        dest.writeString(getFavorite());
        dest.writeString(getTrailerLink());
        dest.writeString(getRuntime());
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(String voteCount) {
        this.voteCount = voteCount;
    }

    public String getMovieLength() {
        return movieLength;
    }

    public void setMovieLength(String movieLength) {
        this.movieLength = movieLength;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public ArrayList<Trailer> getTrailersList() {
        return trailersList;
    }

    public void setTrailersList(ArrayList<Trailer> trailersList) {
        this.trailersList = trailersList;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHindiMovieId() {
        return hindiMovieId;
    }

    public void setHindiMovieId(String hindiMovieId) {
        this.hindiMovieId = hindiMovieId;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getTrailerLink() {
        return trailerLink;
    }

    public void setTrailerLink(String trailerLink) {
        this.trailerLink = trailerLink;
    }
}