package com.edocent.movieapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.edocent.movieapp.R;
import com.edocent.movieapp.model.Trailer;

import java.util.List;

/**
 * Created by SRIVASTAVAA on 10/20/2015.
 */
public class TrailerAdapter extends ArrayAdapter<Trailer> {

    static final String TAG = TrailerAdapter.class.getSimpleName();

    Context mContext;
    int resource;
    List<Trailer> mTrailerList;

    public TrailerAdapter(Context context, int resource, List<Trailer> mTrailerList) {
        super(context, resource, mTrailerList);
        this.mContext = context;
        this.resource = resource;
        this.mTrailerList = mTrailerList;
    }


    /*
    * http://api.themoviedb.org/3/movie/movie_id/videos?api_key=2488d2824d22372dac5e1c8f6e779c5f
    * */
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup){
        ViewHolderItem viewHolderItem;

        Trailer trailer = mTrailerList.get(position);

        if(convertView == null){
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            convertView = inflater.inflate(resource, viewGroup, false);
            viewHolderItem = new ViewHolderItem();
            viewHolderItem.trailerTitle = (TextView) convertView.findViewById(R.id.trailerTitleId);
            convertView.setTag(viewHolderItem);
        }else{
            viewHolderItem = (ViewHolderItem) convertView.getTag();
        }

        viewHolderItem.trailerTitle.setText(trailer.getTrailerName());

        return convertView;
    }

    /*Added to use ViewHolder pattern*/
    static class ViewHolderItem{
        TextView trailerTitle;

    }
}
