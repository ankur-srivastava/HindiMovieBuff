package com.edocent.movieapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.edocent.movieapp.R;
import com.edocent.movieapp.model.Movie;
import com.edocent.movieapp.utilities.AppConstants;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by SRIVASTAVAA on 10/20/2015.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {

    static final String TAG = MovieAdapter.class.getSimpleName();

    Context mContext;
    int resource;
    List<Movie> mMovieList;

    public MovieAdapter(Context context, int resource, List<Movie> movieList) {
        super(context, resource, movieList);
        this.mContext = context;
        this.resource = resource;
        this.mMovieList = movieList;
    }


    //Sizes - "w92", "w154", "w185", "w342", "w500", "w780", or "original"
    //Sample image URL - http://image.tmdb.org/t/p/w75/5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup){
        ViewHolderItem viewHolderItem;

        Movie movie = mMovieList.get(position);

        if(convertView == null){
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            convertView = inflater.inflate(resource, viewGroup, false);
            viewHolderItem = new ViewHolderItem();
            viewHolderItem.movieIconItem = (ImageView) convertView.findViewById(R.id.movieIconId);
            convertView.setTag(viewHolderItem);
        }else{
            viewHolderItem = (ViewHolderItem) convertView.getTag();
        }

        //ImageView movieIcon = (ImageView) convertView.findViewById(R.id.movieIconId);
        String imageURL = AppConstants.MOVIE_URL+movie.getPosterPath();

        try {
            if(imageURL != null && !imageURL.equals("")){
                Picasso.with(getContext()).load(Uri.parse(imageURL)).into(viewHolderItem.movieIconItem);
            }
        }catch (Exception ex){
            Log.e(TAG, "Problem with the URL "+ex.getMessage());
        }

        return convertView;
    }

    /*Added to use ViewHolder pattern*/
    static class ViewHolderItem{
        ImageView movieIconItem;
    }
}
