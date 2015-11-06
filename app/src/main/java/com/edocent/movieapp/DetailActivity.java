package com.edocent.movieapp;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.edocent.movieapp.model.Review;
import com.edocent.movieapp.utilities.AppUtility;

public class DetailActivity extends Activity implements MovieReviewsFragment.ReviewDetail{

    private static final String TAG = "DetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_detail);

        setupDetailFragment();
        AppUtility.setupBannerIcon(getActionBar(), (ImageView) findViewById(android.R.id.home));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.favoriteMoviesId){
            Intent intent = new Intent(DetailActivity.this, FavoritesActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void setupDetailFragment(){
        DetailActivityFragment detailActivityFragment = new DetailActivityFragment();

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.movieDetailFragmentId, detailActivityFragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void displayReviewDetail(Review review) {
        Log.v(TAG, "Reviews clicked in Activity .. "+review.getAuthor());
        ReviewDetailFragment reviewDetailFragment = new ReviewDetailFragment();
        reviewDetailFragment.setReview(review);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.movieDetailFragmentId, reviewDetailFragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}