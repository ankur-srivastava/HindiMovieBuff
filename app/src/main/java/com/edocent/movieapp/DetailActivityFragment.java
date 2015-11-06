package com.edocent.movieapp;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.edocent.movieapp.database.MovieDBHelper;
import com.edocent.movieapp.model.Movie;
import com.edocent.movieapp.model.Trailer;
import com.edocent.movieapp.utilities.AppConstants;
import com.squareup.picasso.Picasso;
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
    TextView trailerView;

    ArrayList<Trailer> trailers;

    public DetailActivityFragment() {

    }

    public void setMovieDetailObject(Movie movieDetailObject) {
        this.movieDetailObject = movieDetailObject;
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
        trailerView = (TextView) view.findViewById(R.id.trailerTitleId);
        favoriteIconId = (ImageView) view.findViewById(R.id.favoriteIconId);

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
                    MovieDBHelper movieDBHelper = new MovieDBHelper(getActivity());
                    new MovieDBHelper.UpdateMovieAsync().execute(movieDBHelper, movieDetailObject, getActivity());

                    Toast.makeText(getActivity(), "Your choice has been updated !!", Toast.LENGTH_SHORT).show();
                }
            });

            String imageURL = movieDetailObject.getPosterPath();
            Picasso.with(getActivity()).load(imageURL).into(movieDetailImage);

            movieDetailTitle.setText(movieDetailObject.getTitle());
            movieDetailYear.setText(movieDetailObject.getReleaseDate());
            movieDetailRating.setText(movieDetailObject.getVoteAverage());
            movieDetailOverview.setText(movieDetailObject.getOverview());
            movieDetailLength.setText(movieDetailObject.getRuntime()+" minutes");

            if(movieDetailObject != null && movieDetailObject.getTrailerLink() != null && !movieDetailObject.getTrailerLink().equals("")){
                trailerView.setText("Click to view Trailer");
                trailerView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String trailerURL = movieDetailObject.getTrailerLink();
                        if(trailerURL != null){
                            Uri uri = null;
                            try {
                                uri = Uri.parse(trailerURL).buildUpon().build();
                            }catch (Exception e){
                                Log.e(TAG, e.getMessage());
                            }
                            if(uri != null){
                                Log.v(TAG, "Video URI " + uri.toString());
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
                });
            }else{
                trailerView.setText("Sorry, no trailers are available");
            }
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
            //String youtubeURL = AppConstants.MOVIE_YOUTUBE_URL+"/"+tempTrailer.getTrailerKey();
            String youtubeURL = tempTrailer.getTrailerKey();
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
}
