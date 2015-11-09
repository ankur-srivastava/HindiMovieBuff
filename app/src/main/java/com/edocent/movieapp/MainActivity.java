package com.edocent.movieapp;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.edocent.movieapp.utilities.AppUtility;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    ConnectivityManager connMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isOnline = AppUtility.isOnline(connMgr);

        if(isOnline) {
            loadMainFragment(null);
        }else{
            loadNoInternetFragment();
        }
        AppUtility.setupBannerIcon(getActionBar(), (ImageView)findViewById(android.R.id.home));
    }

    private void loadNoInternetFragment() {
        NoInternetFragment noInternetFragment = new NoInternetFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.sectionOneFragmentId, noInternetFragment);
        fragmentTransaction.commit();
    }

    private void loadMainFragment(String query) {
        MainActivityFragment mainActivityFragment = new MainActivityFragment();

        if(query != null){
            mainActivityFragment.setQuery(query);
        }

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.sectionOneFragmentId, mainActivityFragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        getMenuInflater().inflate(R.menu.movie_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.favoriteMoviesId){
            Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
            startActivity(intent);
        }else if(id == R.id.menu_item_search){
            onSearchRequested();
            return true;
        }else if(id == R.id.menu_item_clear){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNewIntent(Intent intent){
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.v(TAG, "Received a query "+query);
            loadMainFragment(query);
        }
    }
}