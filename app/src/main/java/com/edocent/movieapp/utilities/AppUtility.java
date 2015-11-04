package com.edocent.movieapp.utilities;

import android.net.Uri;
import android.util.Log;

import com.edocent.movieapp.model.Movie;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by SRIVASTAVAA on 11/2/2015.
 */
public class AppUtility {

    private static final String TAG = AppUtility.class.getSimpleName();

    public static String getYearFromJSON(String date){
        String year = "";
        if(date != null && date.length() > 0 && date.indexOf("-") > 0) {
            year = date.substring(0, date.indexOf("-"));
        }else{
            year = date;
        }
        return year;
    }

    /*
    Get data from the service
    This code has been borrowed from https://gist.github.com/udacityandroid/d6a7bb21904046a91695
    Used in MainActivityFragment
    * */
    public static String getMovieJSONString(String sortBy, int pageNo){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String movieJsonStr = null;

        try {
            Uri uri= Uri.parse(AppConstants.BASE_URL).buildUpon()
                    .appendQueryParameter(AppConstants.PAGE_NO, String.valueOf(pageNo))
                    .appendQueryParameter(AppConstants.SORT_BY, sortBy)
                    .appendQueryParameter(AppConstants.API_KEY, AppConstants.MOVIE_API_KEY)
                    .build();

            //Log.v(TAG, "URI - "+uri.toString());
            URL url = new URL(uri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            StringBuffer buffer = new StringBuffer();

            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            movieJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e(TAG, "Error ", e);
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error closing stream", e);
                }
            }
        }
        return movieJsonStr;
    }

    /*Map JSON to Movie object*/
    public static Movie mapMovieData(JSONObject tempObject){
        if(tempObject == null){
            return null;
        }
        Movie tempMovie = new Movie();
        try {
            tempMovie.setTitle(tempObject.getString("title"));
            tempMovie.setMovieId(tempObject.getLong("id"));
            if(tempObject.getString("release_date") != null && !tempObject.getString("release_date").equals("")){
                tempMovie.setReleaseDate(AppUtility.getYearFromJSON(tempObject.getString("release_date")));
            }
            tempMovie.setPosterPath(tempObject.getString("poster_path"));
            tempMovie.setOverview(tempObject.getString("overview"));
            tempMovie.setMovieLength(tempObject.getString("overview"));
            tempMovie.setVoteCount(tempObject.getString("vote_count"));
            tempMovie.setVoteAverage(tempObject.getString("vote_average")+"/10");

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return tempMovie;
    }
}
