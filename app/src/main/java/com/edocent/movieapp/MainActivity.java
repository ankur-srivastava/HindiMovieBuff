package com.edocent.movieapp;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.edocent.movieapp.utilities.AppUtility;

public class MainActivity extends Activity {

    ConnectivityManager connMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isOnline = AppUtility.isOnline(connMgr);

        if(isOnline) {
            loadMainFragment();
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

    private void loadMainFragment() {
        MainActivityFragment mainActivityFragment = new MainActivityFragment();

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.sectionOneFragmentId, mainActivityFragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        */
        if(id == R.id.favoriteMoviesId){
            Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
