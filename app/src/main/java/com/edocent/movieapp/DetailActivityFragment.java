package com.edocent.movieapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.edocent.movieapp.adapters.TrailerAdapter;
import com.edocent.movieapp.database.MovieDBHelper;
import com.edocent.movieapp.model.Movie;
import com.edocent.movieapp.model.Trailer;
import com.edocent.movieapp.utilities.AppConstants;
import com.squareup.picasso.Picasso;

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
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final String TAG = "DetailActivityFragment";

    Movie movieDetailObject;
    ImageView movieDetailImage;
    ImageView favoriteIconId;
    TextView movieDetailTitle;
    TextView movieDetailYear;
    TextView movieDetailLength;
    TextView movieDetailRating;
    TextView movieDetailOverview;
    TextView viewReviewsId;
    TrailerAdapter mTrailerAdapter;
    ListView trailerListView;
    ReviewScreen mReviewScreen;

    ArrayList<Trailer> trailers;

    public DetailActivityFragment() {

    }

    public void setMovieDetailObject(Movie movieDetailObject) {
        this.movieDetailObject = movieDetailObject;
    }

    static interface ReviewScreen{
        void displayReviews(long movieId);
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        mReviewScreen = (ReviewScreen) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        movieDetailImage = (ImageView) view.findViewById(R.id.movieDetailImageId);
        movieDetailTitle = (TextView) view.findViewById(R.id.movieDetailTitleId);
        movieDetailYear = (TextView) view.findViewById(R.id.movieDetailYearId);
        movieDetailLength = (TextView) view.findViewById(R.id.movieDetailLengthId);
        movieDetailRating = (TextView) view.findViewById(R.id.movieDetailRatingId);
        movieDetailOverview = (TextView) view.findViewById(R.id.movieDetailOverviewId);
        viewReviewsId = (TextView) view.findViewById(R.id.viewReviewsId);
        trailerListView = (ListView) view.findViewById(R.id.trailersListId);
        favoriteIconId = (ImageView) view.findViewById(R.id.favoriteIconId);


        trailerListView.setOnItemClickListener(this);
        viewReviewsId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Call Review Fragment
                Log.v(TAG, "Reviews clicked ..");
                mReviewScreen.displayReviews(movieDetailObject.getMovieId());
            }
        });

        if(savedInstanceState == null || !savedInstanceState.containsKey(AppConstants.MOVIE_LIST_FROM_BUNDLE_KEY)){
            if(movieDetailObject == null) {
                if (getActivity().getIntent() != null) {
                    movieDetailObject = (Movie) getActivity().getIntent().getExtras().get(AppConstants.DETAIL_MOVIE_OBJECT);
                }
            }
        }else{
            movieDetailObject = savedInstanceState.getParcelable(AppConstants.MOVIE_DTL_FROM_BUNDLE_KEY);
        }

        if(movieDetailObject != null){
            if(movieDetailObject.getFavorite() == null || movieDetailObject.getFavorite().equals("") || movieDetailObject.getFavorite().equals(AppConstants.NOT_FAVORITE_MOVIE)){
                //favoriteIconId.setImageResource(R.drawable.abc_btn_rating_star_off_mtrl_alpha);
                favoriteIconId.setImageResource(android.R.drawable.star_big_off);
            }else{
                favoriteIconId.setImageResource(android.R.drawable.star_big_on);
            }

            favoriteIconId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Check with the Database - if a row exists for this movie then update the flag
                    Log.v(TAG, "Fav Icon clicked");
                    MovieDBHelper movieDBHelper = new MovieDBHelper(getActivity());
                    Log.v(TAG, "Movie id is "+movieDetailObject.getId()+" and movie id is "+movieDetailObject.getMovieId());
                    new MovieDBHelper.UpdateMovieAsync().execute(movieDBHelper, movieDetailObject, getActivity());

                    Toast.makeText(getActivity(), "Your choice has been updated !!", Toast.LENGTH_SHORT).show();
                }
            });

            String imageURL = AppConstants.MOVIE_URL+movieDetailObject.getPosterPath();
            //Log.v(TAG, "Image URL " + imageURL);
            Picasso.with(getActivity()).load(imageURL).into(movieDetailImage);
            movieDetailTitle.setText(movieDetailObject.getTitle());
            movieDetailYear.setText(movieDetailObject.getReleaseDate());
            //movieDetailLength.setText(movieDetailObject.getMovieLength());
            movieDetailRating.setText(movieDetailObject.getVoteAverage());
            movieDetailOverview.setText(movieDetailObject.getOverview());

            //For Trailers
            new VideoURLService().execute(movieDetailObject.getMovieId());

            // For Review open a new fragment with a list to display all the reviews. Pass movie id to this fragment
            // /movie/{id}/reviews
            // MovieReviewsFragment
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
        //Log.v(TAG, "Device orientation changed in detailed view");
        if(movieDetailObject != null){
            bundle.putParcelable(AppConstants.MOVIE_DTL_FROM_BUNDLE_KEY, movieDetailObject);
            //Log.v(TAG, "Object saved in Bundle");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Trailer tempTrailer = null;
        if(trailers != null && trailers.size() > 0){
            tempTrailer = trailers.get(position);
        }
        if(tempTrailer != null){
            Log.v(TAG, "Following trailer was invoked " + tempTrailer.getTrailerName());
            //Invoke youtube
            String youtubeURL = AppConstants.MOVIE_YOUTUBE_URL+"/"+tempTrailer.getTrailerKey();

            Uri uri = null;
            try {
                uri = Uri.parse(youtubeURL).buildUpon().build();
            }catch (Exception e){
                Log.e(TAG, e.getMessage());
            }
            if(uri != null){
                //Log.v(TAG, "Video URI " + uri.toString());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                //intent.setDataAndType(uri, "video/*");
                intent.setData(uri);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }

            }else{
                Toast.makeText(getActivity(), "Some problem with the video", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class VideoURLService extends AsyncTask<Long, Void, String> {

        private ProgressDialog dialog =
                new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            dialog.setMessage("Getting the trailers !!");
            dialog.show();
        }

        @Override
        protected String doInBackground(Long... params) {
            long movieId = 0;

            if(params[0] != null){
                movieId = params[0];
                if(movieId != 0){
                    return getTrailersJSONString(movieId);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            if(result != null && !result.equals("")){
                JSONObject jsonObject = null;
                JSONArray jsonArray = null;
                trailers = new ArrayList<>();

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
                            Trailer trailer = new Trailer();

                            try {
                                trailer.setTrailerKey(tempJsonObject.getString("key"));
                                trailer.setTrailerName(tempJsonObject.getString("name"));
                            } catch (JSONException e) {
                                Log.e(TAG, e.getMessage());
                            }
                            trailers.add(trailer);
                        }
                    }
                }
                if(trailers != null && trailers.size() > 0){
                    Log.v(TAG, "Trailers List "+trailers.size());
                    movieDetailObject.setTrailersList(trailers);
                    mTrailerAdapter = new TrailerAdapter(getActivity(), R.layout.list_item_trailer, trailers);
                    trailerListView.setAdapter(mTrailerAdapter);
                }
                dialog.dismiss();
            }
        }
    }

    /*
    Get data from the service
    This code has been borrowed from https://gist.github.com/udacityandroid/d6a7bb21904046a91695
    * */
    public String getTrailersJSONString(long movieId){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String movieTrailerJsonStr = null;

        try {
            String base_url = AppConstants.TRAILERS_BASE_URL+"/"+movieId+"/videos";
            Uri uri= Uri.parse(base_url).buildUpon()
                    .appendQueryParameter(AppConstants.API_KEY, AppConstants.MOVIE_API_KEY)
                    .build();

            Log.v(TAG, "Trailers URI - "+uri.toString());
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
            movieTrailerJsonStr = buffer.toString();
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

        return movieTrailerJsonStr;
    }
}
