package com.edocent.movieapp;


import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.edocent.movieapp.adapters.ReviewsAdapter;
import com.edocent.movieapp.model.Review;
import com.edocent.movieapp.utilities.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * This Fragment will be used to display Movie Reviews
 * Sample URL http://api.themoviedb.org/3/movie/177677/reviews?api_key=2488d2824d22372dac5e1c8f6e779c5f
 */
public class MovieReviewsFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final String TAG = "MovieReviewsFragment";
    long movieId;
    ListView reviewsListId;
    ArrayList<Review> reviews;
    ReviewsAdapter reviewsAdapter;
    ReviewDetail reviewDetail;

    public MovieReviewsFragment() {
        // Required empty public constructor
    }

    static interface ReviewDetail{
        void displayReviewDetail(Review review);
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        reviewDetail = (ReviewDetail) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.fragment_movie_reviews, container, false);

        reviewsListId = (ListView) view.findViewById(R.id.reviewsListId);
        reviewsListId.setOnItemClickListener(this);

        new ReviewsFetch().execute(movieId);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Review reviewObject = reviews.get(position);

        if(reviewObject != null){
            Log.v(TAG, "Got the  following review object "+reviewObject.getAuthor());
            if(reviewDetail != null){
                reviewDetail.displayReviewDetail(reviewObject);
            }
            
        }
    }

    public class ReviewsFetch extends AsyncTask<Long, Void, String>{

        private ProgressDialog dialog =
                new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            dialog.setMessage("Getting the Reviews !!");
            dialog.show();
        }

        @Override
        protected String doInBackground(Long[] params) {
            long movieId = 0;

            if(params[0] != null){
                movieId = params[0];
                if(movieId != 0){
                    return getReviewsJSONString(movieId);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            if(result != null && !result.equals("")){
                JSONObject jsonObject = null;
                JSONArray jsonArray = null;
                reviews = new ArrayList<>();

                try {
                    jsonObject = new JSONObject(result);
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }

                if(jsonObject != null){
                    try {
                        jsonArray = jsonObject.getJSONArray("results");
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }

                if(jsonArray != null && jsonArray.length() > 0){
                    for(int i=0; i<jsonArray.length(); i++){
                        JSONObject tempJsonObject = null;
                        try {
                            tempJsonObject = jsonArray.getJSONObject(i);
                        } catch (JSONException e) {
                            Log.e(TAG, e.getMessage());
                        }
                        if(tempJsonObject != null){
                            Review tempReview = new Review();

                            try {
                                //tempReview.setReviewId(tempJsonObject.getLong("id"));
                                tempReview.setAuthor(tempJsonObject.getString("author"));
                                tempReview.setContent(tempJsonObject.getString("content"));
                                tempReview.setUrl(tempJsonObject.getString("url"));
                            } catch (JSONException e) {
                                Log.e(TAG, e.getMessage());
                            }
                            reviews.add(tempReview);
                        }
                    }
                }
                if(reviews != null && reviews.size() > 0){
                    reviewsAdapter = new ReviewsAdapter(getActivity(), R.layout.list_item_review, reviews);
                    reviewsListId.setAdapter(reviewsAdapter);
                }
            }
            dialog.dismiss();
        }
    }

    /*
    * http://api.themoviedb.org/3/movie/movie_id/videos?api_key=2488d2824d22372dac5e1c8f6e779c5f
    * */
    private String getReviewsJSONString(long movieId) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String movieReviewJsonStr = null;

        try {
            String base_url = AppConstants.MOVIE_REVIEWS_URL+"/"+movieId+"/reviews";
            Uri uri= Uri.parse(base_url).buildUpon()
                    .appendQueryParameter(AppConstants.API_KEY, AppConstants.MOVIE_API_KEY)
                    .build();

            Log.v(TAG, "Reviews URI - "+uri.toString());
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
            movieReviewJsonStr = buffer.toString();
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

        return movieReviewJsonStr;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }
}
