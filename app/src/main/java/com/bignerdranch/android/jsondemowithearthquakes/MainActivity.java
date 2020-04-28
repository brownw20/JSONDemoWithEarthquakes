package com.bignerdranch.android.jsondemowithearthquakes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2014-01-01&endtime=2014-01-02";

    private RecyclerView mRecyclerView;
    private EarthquakeAdapter mAdapter;
    private TextView mEmptyStateTextView;
    private View mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new EarthquakeAdapter(new ArrayList<Earthquake>());
        mRecyclerView.setAdapter(mAdapter);

        mEmptyStateTextView = findViewById(R.id.empty_view);
        mProgressBar = findViewById(R.id.loading_spinner);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            EarthQuakeAsyncTask task = new EarthQuakeAsyncTask();
            task.execute(REQUEST_URL);
        } else{
            mProgressBar.setVisibility(View.GONE);
            mEmptyStateTextView.setText("No Internet Connection");
            mEmptyStateTextView.setVisibility(View.VISIBLE);
        }

        EarthQuakeAsyncTask task = new EarthQuakeAsyncTask();
        task.execute(REQUEST_URL);
    }
    private class EarthQuakeAsyncTask extends AsyncTask<String, Void, List<Earthquake>>{
        @Override
        public List<Earthquake> doInBackground(String... urls){
            if(urls != null && urls.length > 0){
                List<Earthquake> results = NetworkingUtilities.fetchEarthQuakeData(urls[0]);
                return results;
            }
            return null;
        }
        @Override
        public void onPostExecute(List<Earthquake> earthquakes){
            mProgressBar.setVisibility(View.GONE);
            if(earthquakes != null && earthquakes.size()>0){
                mEmptyStateTextView.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                mAdapter.setData(earthquakes);
                mAdapter.notifyDataSetChanged();
            } else{
                mRecyclerView.setVisibility(View.GONE);
                mEmptyStateTextView.setText("No earthquakes found");
                mEmptyStateTextView.setVisibility(View.VISIBLE);
                //hello
            }
        }
    }
}


