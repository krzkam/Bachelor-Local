package com.example.krzkam.local;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by krzkam on 16.12.2017.
 */

public class GetDirectionsData extends AsyncTask<Object,String,String> {

    private String googleDirectionsData;
    private GoogleMap mMap;
    private String url;
    public LatLng latLng;

    @Override
    protected String doInBackground(Object... params) {
        try {
            Log.d("GetNearbyDirectionsData", "doInBackground entered");
            mMap = (GoogleMap) params[0];
            url = (String) params[1];
            latLng = (LatLng) params[2];
            DownloadUrl downloadUrl = new DownloadUrl();
            googleDirectionsData = downloadUrl.readUrl(url);
            Log.d("GooglePlacesReadTask", "doInBackground Exit");
        } catch (IOException e) {
            Log.d("GooglePlacesReadTask", e.toString());
        }
        return googleDirectionsData;
    }

    @Override
    protected void onPostExecute(String result) {
        String[] directionsList;
        DataParser parser = new DataParser();
        directionsList = parser.parseDirections(result);
        displayDirections(directionsList);
    }

    private void displayDirections(String[] directionsList)
    {
        int count = directionsList.length;
        for (int i=0; i<count; i++)
        {
            PatternItem DOT = new Dot();
            PatternItem GAP = new Gap(10);
            List<PatternItem> PATTERN_POLYLINE_DOTTED = Arrays.asList(GAP, DOT);
            PolylineOptions options = new PolylineOptions();
            options.color(Color.DKGRAY);
            options.color(Color.parseColor("#38afea"));
            options.width(20);

            options.addAll(PolyUtil.decode(directionsList[i]));
            mMap.addPolyline(options).setPattern(PATTERN_POLYLINE_DOTTED);
            mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
        }
    }
}