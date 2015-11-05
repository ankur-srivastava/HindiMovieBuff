package com.edocent.movieapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.GridView;

import com.edocent.movieapp.adapters.FavoriteMovieAdapter;
import com.edocent.movieapp.database.MovieDBHelper;
import com.edocent.movieapp.model.Movie;
import com.edocent.movieapp.utilities.AppConstants;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteMoviesFragment extends Fragment {

    private static final String TAG = "FavoriteMoviesFragment";
    GridView favoriteMoviesGridId;

    public FavoriteMoviesFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_movies, container, false);

        favoriteMoviesGridId = (GridView) view.findViewById(R.id.favoriteMoviesGridId);

        favoriteMoviesGridId.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View largeSectionTwoFragment = view.findViewById(R.id.sectionTwoFragmentId);
                Movie detailMovieObj = null;


                int _id = (int)id;
                MovieDBHelper movieDBHelper = new MovieDBHelper(getActivity());
                detailMovieObj = MovieDBHelper.getMovieUsingId(movieDBHelper, _id);


                if(largeSectionTwoFragment != null){
                    //loadDetailFragment(detailMovieObj);
                }else{
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    intent.putExtra(AppConstants.DETAIL_MOVIE_OBJECT, detailMovieObj);
                    startActivity(intent);
                }
            }
        });

        setCursorAdapter();
        return view;
    }


    public void setCursorAdapter() {
        MovieDBHelper helper = new MovieDBHelper(getActivity());
        CursorAdapter ca = new FavoriteMovieAdapter(getActivity(), MovieDBHelper.getFavoriteMoviesCursor(helper), 0);
        favoriteMoviesGridId.setAdapter(ca);
    }
}
