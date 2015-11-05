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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Ankur on 11/2/2015.
 */
public class AppUtility {

    private static final String TAG = AppUtility.class.getSimpleName();

    public static String getReleaseDate(String date){
        StringBuilder dateString = new StringBuilder();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date releaseDate = null;
        try {
            releaseDate = formatter.parse(date);
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }

        if(releaseDate != null){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(releaseDate);
            calendar.get(Calendar.YEAR);
            dateString.append(Calendar.DAY_OF_MONTH).append(" ").append(Calendar.MONTH).append(" ").append(Calendar.YEAR);
        }

        Log.v(TAG, "Date is "+dateString.toString());
        return dateString.toString();
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
                    //.appendQueryParameter(AppConstants.PAGE_NO, String.valueOf(pageNo))
                    //.appendQueryParameter(AppConstants.SORT_BY, sortBy)
                    .appendQueryParameter(AppConstants.API_KEY, AppConstants.MOVIE_API_KEY)
                    .build();

            Log.v(TAG, "URI - "+uri.toString());
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
            tempMovie.setHindiMovieId(tempObject.getString("Id"));
            tempMovie.setImdbId(tempObject.getString("ImdbId"));
            tempMovie.setTitle(tempObject.getString("Title"));
            if(tempObject.getString("ReleaseDate") != null && !tempObject.getString("ReleaseDate").equals("")){
                tempMovie.setReleaseDate(AppUtility.getReleaseDate(tempObject.getString("ReleaseDate")));
            }
            tempMovie.setRuntime(tempObject.getString("Runtime"));
            tempMovie.setPosterPath(tempObject.getString("PosterPath"));
            tempMovie.setOverview(tempObject.getString("Description"));
            tempMovie.setMovieLength(tempObject.getString("Runtime"));
            tempMovie.setVoteCount(tempObject.getString("RatingCount"));
            tempMovie.setVoteAverage(tempObject.getString("Rating"));

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return tempMovie;
    }
}
