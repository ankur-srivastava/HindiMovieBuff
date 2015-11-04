package com.edocent.movieapp;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.GridView;

import com.edocent.movieapp.adapters.FavoriteMovieAdapter;
import com.edocent.movieapp.adapters.MovieAdapter;
import com.edocent.movieapp.database.MovieDBHelper;
import com.edocent.movieapp.model.Movie;
import com.edocent.movieapp.utilities.AppConstants;
import com.edocent.movieapp.utilities.AppUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

    GridView moviesListView;
    static String TAG = "MainActivityFragment";
    ArrayList<Movie> moviesListFromJSON;
    ArrayList<Movie> allMoviesList;
    MovieAdapter adapter;

    int pageNo = 1;
    int tempFirstVisibleItem;
    boolean refreshEnabled = true;

    Bundle tempBundle;

    public MainActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragment_main, container, false);
        View view = inflater.inflate(R.layout.fragment_main_horizontal, container, false);

        moviesListView = (GridView)view.findViewById(R.id.moviesListViewId);
        moviesListView.setOnItemClickListener(this);
        moviesListView.setOnScrollListener(this);

        tempBundle = savedInstanceState;

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(tempBundle == null || !tempBundle.containsKey(AppConstants.MOVIE_LIST_FROM_BUNDLE_KEY)){
            if(refreshEnabled){
                getMovieList();
            }
        }else{
            moviesListFromJSON = tempBundle.getParcelableArrayList(AppConstants.MOVIE_LIST_FROM_BUNDLE_KEY);
            if(allMoviesList == null){
                allMoviesList = new ArrayList<>();
            }
            allMoviesList.addAll(moviesListFromJSON);
            setAdapter();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.v(TAG, "Reset Data");
        moviesListFromJSON = null;
        allMoviesList = null;
        adapter = null;
        pageNo = 1;
        refreshEnabled = true;
    }

    public String getSortOrderPref(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortOrderPref = sharedPreferences.getString(getString(R.string.pref_rating_sort), "1");
        return sortOrderPref;
    }

    public void getMovieList(){
        MovieService service = new MovieService();
        if(getSortOrderPref().equals("3")) {
            setCursorAdapter();
        }else if(getSortOrderPref().equals("2")) {
            service.execute(AppConstants.RATING);
        }else{
            service.execute(AppConstants.POPULARITY);
        }
    }
    /*In this method position parameter denotes position of item in list and id denotes the primary key*/
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        View largeSectionTwoFragment = view.findViewById(R.id.sectionTwoFragmentId);
        Movie detailMovieObj = null;

        if(allMoviesList != null && allMoviesList.get(position) != null){
            detailMovieObj = allMoviesList.get(position);
            if(detailMovieObj != null && detailMovieObj.getHindiMovieId() != null && !detailMovieObj.getHindiMovieId().equals("")){
                //get detail object from db based on movieid
                MovieDBHelper movieDBHelper = new MovieDBHelper(getActivity());
                Movie tempMovie = MovieDBHelper.getMovie(movieDBHelper.getReadableDatabase(), detailMovieObj.getHindiMovieId());
                if(tempMovie != null){
                    Log.v(TAG, "Got movie from DB... ");
                    detailMovieObj = tempMovie;
                }
            }
        }

        /*If the user clicks a favorite movie*/
        if(detailMovieObj == null){
            int _id = (int)id;
            MovieDBHelper movieDBHelper = new MovieDBHelper(getActivity());
            detailMovieObj = MovieDBHelper.getMovieUsingId(movieDBHelper, _id);
        }

        if(largeSectionTwoFragment != null){
            loadDetailFragment(detailMovieObj);
        }else{
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra(AppConstants.DETAIL_MOVIE_OBJECT, detailMovieObj);
            startActivity(intent);
        }
    }

    private void loadDetailFragment(Movie movieObject) {
        //We have two fragments
        DetailActivityFragment detailActivityFragment = new DetailActivityFragment();
        detailActivityFragment.setMovieDetailObject(movieObject);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.sectionTwoFragmentId, detailActivityFragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) { }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(refreshEnabled && (tempFirstVisibleItem != firstVisibleItem) && ((totalItemCount - firstVisibleItem) <= AppConstants.FETCH_LIMIT)){
            tempFirstVisibleItem = firstVisibleItem;
            getMovieList();
        }
    }

    public class MovieService extends AsyncTask<String, Void, String>{

        private ProgressDialog dialog =
                new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            dialog.setMessage("We are almost done !!");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            refreshEnabled = false;
            //Log.v(TAG, "In doInBackground with param "+params[0]);
            return AppUtility.getMovieJSONString(params[0], pageNo);
        }

        @Override
        protected void onPostExecute(String result){
            /*Populate the Movie object with the data from the service call*/
            JSONObject jsonObject = null;
            JSONArray jsonArray = null;
            //Log.v(TAG, "Got the following result "+result);
            try {
                jsonArray = jsonObject.getJSONArray(result);
            } catch (JSONException e) {
                Log.e(TAG, "Error "+e.getMessage());
            }
            if(jsonArray != null){
                moviesListFromJSON = new ArrayList<Movie>();
                for(int i=0;i<jsonArray.length();i++){
                    try {
                        JSONObject tempObject = jsonArray.getJSONObject(i);
                        if(tempObject != null){
                            moviesListFromJSON.add(AppUtility.mapMovieData(tempObject));
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "Error " + e.getMessage());
                    }

                }
            }
            if(moviesListFromJSON != null) {
                //Log.v(TAG, "moviesListFromJSON size is " + moviesListFromJSON.size());
                setAdapter();
                pageNo++;
                if(allMoviesList == null){
                    allMoviesList = new ArrayList<>();
                }
                allMoviesList.addAll(moviesListFromJSON);
            }
            dialog.dismiss();
            refreshEnabled = true;
        }
    }

    public void setAdapter(){
        if(adapter == null) {
            adapter = new MovieAdapter(getActivity(), R.layout.list_item_movie, moviesListFromJSON);
            moviesListView.setAdapter(adapter);
        }else{
            adapter.addAll(moviesListFromJSON);
            adapter.notifyDataSetChanged();
        }
    }

    public void setCursorAdapter(){
        Log.v(TAG, "Trying to start cursor !!");
        ProgressDialog tempDialog =
                new ProgressDialog(getActivity());
        tempDialog.setMessage("Getting your Favorites !!");
        tempDialog.show();

        MovieDBHelper helper = new MovieDBHelper(getActivity());
        CursorAdapter ca = new FavoriteMovieAdapter(getActivity(), MovieDBHelper.getFavoriteMoviesCursor(helper), 0);
        moviesListView.setAdapter(ca);

        tempDialog.dismiss();
    }

    @Override
    public void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
        if(moviesListFromJSON != null){
            bundle.putParcelableArrayList(AppConstants.MOVIE_LIST_FROM_BUNDLE_KEY, moviesListFromJSON);
        }
    }
}