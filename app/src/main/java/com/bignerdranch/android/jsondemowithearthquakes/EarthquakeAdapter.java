package com.bignerdranch.android.jsondemowithearthquakes;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import androidx.recyclerview.widget.RecyclerView;

public class EarthquakeAdapter extends RecyclerView.Adapter<EarthquakeAdapter.EarthquakeViewHolder> {

    private List<Earthquake> mEarthquakes;

    public static class EarthquakeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private static final String LOCATION_SEPARATOR = " of ";
        private static final String DEFAULT_LOCATION_OFFSET = "Near the";
        // hello
        private TextView mMagnitudeTextView;
        private TextView mPrimaryLocationTextView;
        private TextView mLocationOffsetTextView;
        private TextView mDateTextView;
        private TextView mTimeTextView;
        private Earthquake mEarthquake;
        private Context mContext;

        public EarthquakeViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);

            mMagnitudeTextView = (TextView) itemView.findViewById(R.id.magnitudeTextView);
            mPrimaryLocationTextView = (TextView) itemView.findViewById(R.id.primaryLocationTextView);
            mLocationOffsetTextView = (TextView) itemView.findViewById(R.id.locationOffsetTextView);
            mDateTextView = (TextView) itemView.findViewById(R.id.dateTextView);
            mTimeTextView = (TextView) itemView.findViewById(R.id.timeTextView);

        }

        @Override
        public void onClick(View v) {
            Uri earthquakeUri = Uri.parse(mEarthquake.getUrl());
            Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
            mContext.startActivity(websiteIntent);
        }

        public void bindEarthQuake(Earthquake earthquake) {
            mEarthquake = earthquake;

            double magnitude = mEarthquake.getMagnitude();
            DecimalFormat formatter = new DecimalFormat("0.0");
            String magString = formatter.format(magnitude);
            mMagnitudeTextView.setText(magString);

            String fullLocation = mEarthquake.getLocation();
            String primaryLocationString;
            String locationOffsetString;
            if(fullLocation.contains(LOCATION_SEPARATOR)) {
                String parts[] = fullLocation.split(LOCATION_SEPARATOR);
                locationOffsetString = parts[0] + LOCATION_SEPARATOR;
                primaryLocationString = parts[1];
            }
            else {
                locationOffsetString = DEFAULT_LOCATION_OFFSET;
                primaryLocationString = fullLocation;
            }
            mPrimaryLocationTextView.setText(primaryLocationString);
            mLocationOffsetTextView.setText(locationOffsetString);

            Date dateObject = new Date(mEarthquake.getTimeInMilliseconds());
            String formattedDate = formatDate(dateObject);
            mDateTextView.setText(formattedDate);

            String formattedTime = formatTime(dateObject);
            mTimeTextView.setText(formattedTime);

        }

        private String formatDate(Date dateObject) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
            return dateFormat.format(dateObject);

        }

        private String formatTime(Date dateObject) {
            SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
            return timeFormat.format(dateObject);
        }
    }

    public EarthquakeAdapter(List<Earthquake> earthquakes) {
        mEarthquakes = earthquakes;
    }

    public void setData(List<Earthquake> earthquakes) {
        mEarthquakes = earthquakes;
    }

    @Override
    public EarthquakeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item, parent, false);
        return new EarthquakeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EarthquakeViewHolder holder, int position) {
        Earthquake earthquake = mEarthquakes.get(position);
        holder.bindEarthQuake(earthquake);
    }

    @Override
    public int getItemCount() {
        return mEarthquakes.size();
    }
}

