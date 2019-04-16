package com.rdm.android.learningwithnationalparks.networkMaps;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetNearbyParksData extends AsyncTask<Object, String, String> {

    private String googlePlacesData;
    private GoogleMap mMap;

	@Override
    protected String doInBackground(Object... params) {
        try {
            Log.d("GetNearbyParksData", "doInBackground");
            mMap = (GoogleMap) params[0];
	        String url = (String) params[1];
            DownloadUrl downloadUrl = new DownloadUrl();
            googlePlacesData = downloadUrl.readUrl(url);
        } catch (Exception e) {
            Log.d("GooglePlacesReadTask", e.toString());
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("GetNearbyParksData", "onPostExecute - parse Json result");
        List<HashMap<String, String>> nearbyPlacesList;
        JsonParserNearbyPlaces jsonParser = new JsonParserNearbyPlaces();
        nearbyPlacesList = jsonParser.parse(result);
        ShowNearbyPlaces(nearbyPlacesList);
    }

    private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {
        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            Log.d("GetNearbyParksData", "showNearbyPlaces");
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName + ": " + vicinity);
	        mMap.addMarker(markerOptions);
	        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
	        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        }
    }
}
