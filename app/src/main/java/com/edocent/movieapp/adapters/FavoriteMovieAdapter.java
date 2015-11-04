package com.edocent.movieapp.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.edocent.movieapp.R;
import com.edocent.movieapp.utilities.AppConstants;
import com.squareup.picasso.Picasso;

/**
 * Created by ankursrivastava on 11/1/15.
 */
public class FavoriteMovieAdapter extends CursorAdapter {

    private static final String TAG = "FavoriteMovieAdapter";
    private LayoutInflater cursorInflater;

    public FavoriteMovieAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return cursorInflater.inflate(R.layout.list_item_movie, parent, false);
    }

    @Override
    public void bindView(View convertView, Context context, Cursor cursor) {
        ImageView imageView = (ImageView) convertView.findViewById(R.id.movieIconId);

        //ImageView movieIcon = (ImageView) convertView.findViewById(R.id.movieIconId);
        String imageURL = AppConstants.MOVIE_URL+cursor.getString(2);

        try {
            if(imageURL != null && !imageURL.equals("")){
                Picasso.with(context).load(Uri.parse(imageURL)).into(imageView);
            }
        }catch (Exception ex){
            Log.e(TAG, "Problem with the URL " + ex.getMessage());
        }
    }
}
