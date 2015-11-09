package com.edocent.movieapp;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

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
 * @author Ankur Srivastava
 */
public class MainActivityFragment extends Fragment implements AdapterView.OnItemClickListener{

    String TAG = "MainActivityFragment";
    String query;
    GridView moviesListView;
    GridView nextReleaseView;
    ArrayList<Movie> moviesListFromJSON;
    ArrayList<Movie> allMoviesList;
    MovieAdapter adapter;
    ArrayList<Movie> nextReleaseListFromJSON;
    MovieAdapter nextReleaseAdapter;
    Bundle tempBundle;

    public MainActivityFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        moviesListView = (GridView)view.findViewById(R.id.moviesListViewId);
        moviesListView.setOnItemClickListener(this);

        nextReleaseView = (GridView) view.findViewById(R.id.nextReleaseListViewId);
        nextReleaseView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                callListenerCode(nextReleaseListFromJSON, view, position, id);
            }
        });

        tempBundle = savedInstanceState;

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(tempBundle == null || !tempBundle.containsKey(AppConstants.MOVIE_LIST_FROM_BUNDLE_KEY)){
            getMovieList();
        }else{
            moviesListFromJSON = tempBundle.getParcelableArrayList(AppConstants.MOVIE_LIST_FROM_BUNDLE_KEY);
            if(allMoviesList == null){
                allMoviesList = new ArrayList<>();
            }
            if(moviesListFromJSON != null){
                allMoviesList.addAll(moviesListFromJSON);
            }
            setAdapter();

            nextReleaseListFromJSON = tempBundle.getParcelableArrayList(AppConstants.NEXT_MOVIE_LIST_FROM_BUNDLE_KEY);
            setNextReleaseAdapter();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        moviesListFromJSON = null;
        allMoviesList = null;
        adapter = null;
    }

    public void getMovieList(){
        MovieService service = new MovieService();
        service.execute();
    }

    public void getNextReleaseList(){
        NextReleaseService service = new NextReleaseService();
        service.execute();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        callListenerCode(allMoviesList, view, position, id);
    }

    private void loadDetailFragment(Movie movieObject) {
        DetailActivityFragment detailActivityFragment = new DetailActivityFragment();
        detailActivityFragment.setMovieDetailObject(movieObject);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.sectionTwoFragmentId, detailActivityFragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    public class MovieService extends AsyncTask<String, Void, String>{

        private ProgressDialog dialog =
                new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            dialog.setMessage("We are almost done !!");
            dialog.show();
            cancelTask(dialog);
        }

        @Override
        protected String doInBackground(String... params) {
            if(query != null && !query.equals("")){
                return AppUtility.getMovieJSONString(AppConstants.MOVIE_BY_TITLE, query);
            }
            return AppUtility.getMovieJSONString(AppConstants.BASE_URL, null);
        }

        @Override
        protected void onPostExecute(String result){
            JSONArray jsonArray = null;
            //Log.v(TAG, "Got the following result "+result);
            if(isCancelled()){
                Log.v(TAG, "Task got cancelled");
            }else{
                try {
                    jsonArray = new JSONArray(result);
                } catch (JSONException e) {
                    Log.e(TAG, "Error "+e.getMessage());
                }
                if(jsonArray != null){
                    moviesListFromJSON = new ArrayList<>();
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
                    if(allMoviesList == null){
                        allMoviesList = new ArrayList<>();
                    }
                    allMoviesList.addAll(moviesListFromJSON);
                }
                getNextReleaseList();
                dialog.dismiss();
            }
        }

        void cancelTask(final ProgressDialog pd) {
            //Define a thread to cancel Progress Bar after Xsec
            Runnable progressThread = new Runnable() {
                @Override
                public void run() {
                    try {
                        //Log.v(TAG, "Task Status is "+getStatus());
                        if(getStatus() != Status.FINISHED){
                            pd.dismiss();
                            if (getActivity() != null) {
                                Toast.makeText(getActivity(), "Weak Internet Connection", Toast.LENGTH_SHORT).show();
                            }
                            cancel(true);
                        }
                    }catch (Exception e){
                        Log.e(TAG, "Check error "+e.getMessage());
                    }
                }
            };

            Handler progressHandler = new Handler();
            progressHandler.postDelayed(progressThread, AppConstants.PROGRESS_DIALOG_TIME);
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

    public void setNextReleaseAdapter(){
        if(nextReleaseAdapter == null) {
            nextReleaseAdapter = new MovieAdapter(getActivity(), R.layout.list_item_movie, nextReleaseListFromJSON);
            nextReleaseView.setAdapter(nextReleaseAdapter);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
        if(moviesListFromJSON != null){
            bundle.putParcelableArrayList(AppConstants.MOVIE_LIST_FROM_BUNDLE_KEY, moviesListFromJSON);
        }
        if(nextReleaseListFromJSON != null){
            bundle.putParcelableArrayList(AppConstants.NEXT_MOVIE_LIST_FROM_BUNDLE_KEY, nextReleaseListFromJSON);
        }
    }

    public class NextReleaseService extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            cancelTask();
        }

        @Override
        protected String doInBackground(String... params) {
            return AppUtility.getMovieJSONString(AppConstants.NEXT_RELEASE_URL, null);
        }

        @Override
        protected void onPostExecute(String result){
            JSONArray jsonArray = null;
            //Log.v(TAG, "Got the following result "+result);
            try {
                jsonArray = new JSONArray(result);
            } catch (JSONException e) {
                Log.e(TAG, "Error "+e.getMessage());
            }
            if(jsonArray != null){
                nextReleaseListFromJSON = new ArrayList<>();
                for(int i=0;i<jsonArray.length();i++){
                    try {
                        JSONObject tempObject = jsonArray.getJSONObject(i);
                        if(tempObject != null){
                            nextReleaseListFromJSON.add(AppUtility.mapMovieData(tempObject));
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "Error " + e.getMessage());
                    }

                }
            }
            if(nextReleaseListFromJSON != null) {
                //Log.v(TAG, "moviesListFromJSON size is " + moviesListFromJSON.size());
                setNextReleaseAdapter();
            }
        }

        void cancelTask() {
            Runnable progressThread = new Runnable() {
                @Override
                public void run() {
                    try {
                        //Log.v(TAG, "Task Status is " + getStatus());
                        if(getStatus() != Status.FINISHED){
                            cancel(true);
                        }
                    }catch (Exception e){
                        Log.e(TAG, "Check error "+e.getMessage());
                    }
                }
            };

            Handler progressHandler = new Handler();
            progressHandler.postDelayed(progressThread, AppConstants.PROGRESS_DIALOG_TIME);
        }
    }

    private void callListenerCode(ArrayList<Movie> movieListParam, View v, int position, long id) {
        Movie detailMovieObj = null;
        View largeSectionTwoFragment = v.findViewById(R.id.sectionTwoFragmentId);
        if(movieListParam != null && movieListParam.get(position) != null){
            detailMovieObj = movieListParam.get(position);
            if(detailMovieObj != null && detailMovieObj.getHindiMovieId() != null
                    && !detailMovieObj.getHindiMovieId().equals("")){
                MovieDBHelper movieDBHelper = new MovieDBHelper(getActivity());
                Movie tempMovie = MovieDBHelper.getMovie(movieDBHelper.getReadableDatabase(), detailMovieObj.getHindiMovieId());
                if(tempMovie != null){
                    //Log.v(TAG, "Got movie from DB... ");
                    detailMovieObj = tempMovie;
                }
            }
        }

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

    public void setQuery(String query) {
        this.query = query;
    }
}