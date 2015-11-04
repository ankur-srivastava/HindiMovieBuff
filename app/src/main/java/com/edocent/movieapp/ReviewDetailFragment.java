package com.edocent.movieapp;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edocent.movieapp.model.Review;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewDetailFragment extends Fragment {

    Review review;
    TextView reviewDetailAuthorId;
    TextView reviewDetailContentId;

    public ReviewDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_detail, container, false);

        reviewDetailAuthorId = (TextView) view.findViewById(R.id.reviewDetailAuthorId);
        reviewDetailContentId = (TextView) view.findViewById(R.id.reviewDetailContentId);

        reviewDetailAuthorId.setText("By "+review.getAuthor());
        reviewDetailContentId.setText(review.getContent());

        return view;
    }

    public void setReview(Review review) {
        this.review = review;
    }
}
