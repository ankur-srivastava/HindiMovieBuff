package com.edocent.movieapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridView;
import android.widget.SimpleCursorAdapter;

import com.edocent.movieapp.model.Movie;
import com.edocent.movieapp.utilities.AppConstants;

import java.util.ArrayList;

/**
 * Created by Ankur on 10/28/2015.
 */
public class MovieDBHelper extends SQLiteOpenHelper {

    private static final String TAG = "MovieDBHelper";

    static ContentValues movieContentValues;
    static Context context;
    static Cursor tempCursor;
    static GridView moviesListView;

    public MovieDBHelper(Context context) {
        super(context, AppConstants.DB_NAME, null, AppConstants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        onUpgrade(db, 0, AppConstants.DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.v(TAG, "on upgrade called");
        if(oldVersion < AppConstants.DB_VERSION){
            Log.v(TAG, "Going to add new table Movie");
            //Add Movie table
            try{
                String query = "CREATE TABLE MOVIE (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "HINDIMOVIEID TEXT,"
                        + "IMDBID TEXT,"
                        +  AppConstants.MOVIE_TITLE+" TEXT,"
                        + "OVERVIEW TEXT,"
                        + "RELEASEDATE TEXT,"
                        + "POSTERPATH TEXT,"
                        + "COUNT TEXT,"
                        + "LENGTH TEXT,"
                        + "AVERAGE TEXT,"
                        + "FAVORITE TEXT"
                        + ")";
                Log.v(TAG, "Query "+query);
                db.execSQL(query);

            }catch (Exception e){
                Log.e(TAG, e.getMessage());
            }
        }
    }

    public static class FavoriteMovies extends AsyncTask<Object, Void, SimpleCursorAdapter>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected SimpleCursorAdapter doInBackground(Object[] params) {

            SQLiteOpenHelper tempDBHelper = (SQLiteOpenHelper) params[0];

            context = (Context)params[1];
            moviesListView = (GridView) params[2];

            SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(context, android.R.layout.simple_list_item_1,
                    getFavoriteMoviesCursor(tempDBHelper), new String[]{AppConstants.MOVIE_TITLE}, new int[]{android.R.id.text1}, 0);
            return simpleCursorAdapter;
        }

        @Override
        protected void onPostExecute(SimpleCursorAdapter adapter){
            //Setup the cursor adapter using this list
            moviesListView.setAdapter(adapter);
            Log.v(TAG, "Adapter set");
        }
    }

    public static class UpdateMovieAsync extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] params) {
            SQLiteOpenHelper tempDBHelper = (SQLiteOpenHelper) params[0];
            Movie movie = (Movie) params[1];
            context = (Context) params[2];
            if(tempDBHelper != null && movie != null){
                Log.v(TAG, "Going to update movie "+movie);
                updateMovie(tempDBHelper, movie);
            }
            return null;
        }
    }

    public static void updateMovie(SQLiteOpenHelper helper, Movie movie){
        SQLiteDatabase db = helper.getWritableDatabase();
        Movie movieFromDB = getMovie(db, movie.getHindiMovieId());
        Log.v(TAG, "Movie from DB is "+movieFromDB);
        movieContentValues = new ContentValues();
        if(movieFromDB != null){
            /*
            if(movieFromDB.getFavorite() == null || movieFromDB.getFavorite().equals("") || movieFromDB.getFavorite().equals(AppConstants.NOT_FAVORITE_MOVIE)){
                movieContentValues.put("FAVORITE", AppConstants.FAVORITE_MOVIE);
            }else{
                movieContentValues.put("FAVORITE", AppConstants.NOT_FAVORITE_MOVIE);
            }
            */
            if(movieFromDB.getFavorite() == null || movieFromDB.getFavorite().equals("") || movieFromDB.getFavorite().equals(AppConstants.NOT_FAVORITE_MOVIE)){
                movieContentValues.put("FAVORITE", AppConstants.FAVORITE_MOVIE);
                updateMovieRecord(db, movieContentValues, movieFromDB.getId());
            }else{
                deleteMovieRecord(db, movieFromDB.getId());
            }
        }else{
            Log.v(TAG, "Going to add movie "+movie);
            movieContentValues.put("HINDIMOVIEID", movie.getHindiMovieId());
            movieContentValues.put("IMDBID", movie.getImdbId());
            movieContentValues.put(AppConstants.MOVIE_TITLE, movie.getTitle());
            movieContentValues.put("OVERVIEW", movie.getOverview());
            movieContentValues.put("RELEASEDATE", movie.getReleaseDate());
            movieContentValues.put("POSTERPATH", movie.getPosterPath());
            movieContentValues.put("COUNT", movie.getVoteCount());
            movieContentValues.put("LENGTH", movie.getMovieLength());
            movieContentValues.put("AVERAGE", movie.getVoteAverage());
            movieContentValues.put("FAVORITE", AppConstants.FAVORITE_MOVIE);
            addMovie(db, movieContentValues);
        }
    }

    public static boolean updateMovieRecord(SQLiteDatabase db, ContentValues cv, int id){
        Log.v(TAG, "Ready to update");
        try{
            db.update("MOVIE", cv, "_id = ?", new String[]{Integer.toString(id)});
            Log.v(TAG, "Movie Updated");
            /*
            if(context != null){
                Toast.makeText(context, "Your selection has been updated", Toast.LENGTH_SHORT).show();
            }
            */
            return true;
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
            return false;
        }
    }

    public static boolean deleteMovieRecord(SQLiteDatabase db, int id){
        Log.v(TAG, "Ready to delete");
        try{
            int numRowsDeleted = db.delete("MOVIE", "_id = ?", new String[]{Integer.toString(id)});
            Log.v(TAG, "Movie Deleted "+numRowsDeleted);
            /*
            if(context != null){
                Toast.makeText(context, "Your selection has been updated", Toast.LENGTH_SHORT).show();
            }
            */
            return true;
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
            return false;
        }
    }

    public static boolean addMovie(SQLiteDatabase db, ContentValues cv){
        Log.v(TAG, "Ready to Add");
        long _id;
        try{
            _id = db.insert("MOVIE", null, cv);
            Log.v(TAG, "Movie Added with ID "+_id);
            /*
            if(context != null){
                Toast.makeText(context, "Movie added to Favorites", Toast.LENGTH_SHORT).show();
            }
            */
            return true;
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
            return false;
        }
    }

    public static Movie getMovie(SQLiteDatabase db, String hindiMovieId){
        Movie movie = null;
        //SQLiteDatabase db = helper.getReadableDatabase();
        try {

            if (db != null) {
                Cursor c = db.query("MOVIE", new String[]{"_id", "HINDIMOVIEID", "IMDBID", AppConstants.MOVIE_TITLE, "OVERVIEW", "RELEASEDATE", "POSTERPATH", "COUNT", "LENGTH", "AVERAGE", "FAVORITE"},
                        "HINDIMOVIEID=?",
                        new String[]{hindiMovieId},
                        null, null, null);
                if (c.moveToFirst()) {
                    movie = new Movie();
                    movie.setId(c.getInt(0));
                    movie.setHindiMovieId(c.getString(1));
                    movie.setImdbId(c.getString(2));
                    movie.setTitle(c.getString(3));
                    movie.setOverview(c.getString(4));
                    movie.setReleaseDate(c.getString(5));
                    movie.setPosterPath(c.getString(6));
                    movie.setVoteCount(c.getString(7));
                    movie.setMovieLength(c.getString(8));
                    movie.setVoteAverage(c.getString(9));
                    movie.setFavorite(c.getString(10));
                }

                c.close();
            }
        }catch(SQLiteException ex){
            Log.e(TAG, ex.getMessage());
        }
        return movie;
    }

    public static ArrayList<Movie> getFavoriteMovies(SQLiteOpenHelper helper){
        ArrayList<Movie> movieList = null;
        SQLiteDatabase db = helper.getReadableDatabase();
        try {
            if (db != null) {
                Cursor c = db.query("MOVIE",
                            new String[]{"_id", AppConstants.MOVIE_TITLE, "OVERVIEW", "RELEASEDATE", "POSTERPATH", "COUNT", "LENGTH",
                                    "AVERAGE", "FAVORITE"},
                        null,null,null, null, null);

                movieList = new ArrayList<>();

                if(c.getCount() > 0){
                    while(c.moveToNext()){
                        Movie movie = new Movie();
                        movie.setId(c.getInt(0));
                        movie.setTitle(c.getString(1));
                        movie.setOverview(c.getString(2));
                        movie.setReleaseDate(c.getString(3));
                        movie.setPosterPath(c.getString(4));
                        movie.setVoteCount(c.getString(5));
                        movie.setMovieLength(c.getString(6));
                        movie.setVoteAverage(c.getString(7));
                        movie.setFavorite(c.getString(8));
                        movieList.add(movie);
                    }
                }

                c.close();
            }
        }catch(SQLiteException ex){
            Log.e(TAG, ex.getMessage());
        }finally{
            db.close();
        }
        return movieList;
    }

    public static Cursor getFavoriteMoviesCursor(SQLiteOpenHelper helper){
        SQLiteDatabase db = helper.getReadableDatabase();
        try {
            if (db != null) {
                tempCursor = db.query("MOVIE",
                        //new String[]{"_id", "TITLE", "OVERVIEW", "RELEASEDATE", "POSTERPATH", "COUNT", "LENGTH",
                                //"AVERAGE", "FAVORITE"},
                        new String[]{"_id",AppConstants.MOVIE_TITLE,"POSTERPATH"},
                        //"FAVORITE=?",
                        //new String[]{AppConstants.FAVORITE_MOVIE},
                        null, null,
                        null,null,null);
            }
        }catch(SQLiteException ex){
            Log.e(TAG, ex.getMessage());
        }
        return tempCursor;
    }

    public static Movie getMovieUsingId(MovieDBHelper helper, int ID){
        Movie movie = null;
        SQLiteDatabase db = helper.getReadableDatabase();
        try {

            if (db != null) {
                Cursor c = db.query("MOVIE", new String[]{"_id", "HINDIMOVIEID", "IMDBID", AppConstants.MOVIE_TITLE, "OVERVIEW", "RELEASEDATE", "POSTERPATH", "COUNT", "LENGTH", "AVERAGE", "FAVORITE"},
                        "_id=?",
                        new String[]{Integer.toString(ID)},
                        null, null, null);
                if (c.moveToFirst()) {
                    movie = new Movie();
                    movie.setId(c.getInt(0));
                    movie.setHindiMovieId(c.getString(1));
                    movie.setImdbId(c.getString(2));
                    movie.setTitle(c.getString(3));
                    movie.setOverview(c.getString(4));
                    movie.setReleaseDate(c.getString(5));
                    movie.setPosterPath(c.getString(6));
                    movie.setVoteCount(c.getString(7));
                    movie.setMovieLength(c.getString(8));
                    movie.setVoteAverage(c.getString(9));
                    movie.setFavorite(c.getString(10));
                }

                c.close();
            }
        }catch(SQLiteException ex){
            Log.e(TAG, ex.getMessage());
        }
        return movie;
    }
}