package com.rdm.android.learningwithnationalparks.networkMaps;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rdm.android.learningwithnationalparks.adapters.CustomInfoWindowAdapter;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Rebecca McBath
 * on 2/22/19.
 */
public class GetNationalParksData extends AsyncTask<Object, String, String> {

	private String googlePlacesData;
	private GoogleMap mMap;

	@Override
	protected String doInBackground(Object... params) {
		try {
			Log.d("GetNationalParksData", "doInBackground");
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
		Log.d("GetNationalParksData", "onPostExecute - parse Json result");
		List<HashMap<String, String>> txtInputPlacesList;
		JsonParserTextInputPlaces jsonParser = new JsonParserTextInputPlaces();
		txtInputPlacesList = jsonParser.parse(result);
		ShowNationalPlaces(txtInputPlacesList);
	}

	private void ShowNationalPlaces(List<HashMap<String, String>> nationalPlacesList) {
		for (int i = 0; i < nationalPlacesList.size(); i++) {
			Log.d("GetNationalParksData", "showNationalPlaces");
			MarkerOptions markerOptions = new MarkerOptions();
			HashMap<String, String> googlePlace = nationalPlacesList.get(i);
			double lat = Double.parseDouble(googlePlace.get("lat"));
			double lng = Double.parseDouble(googlePlace.get("lng"));
			String placeName = googlePlace.get("place_name");
			String formatted_address = googlePlace.get("formatted_address");
			String id = googlePlace.get("place_id");
			LatLng latLng = new LatLng(lat, lng);
			markerOptions.position(latLng);
			markerOptions.title(placeName + ": " + formatted_address);
			mMap.addMarker(markerOptions);
			markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

			//move map camera to be centered over the United States to show as many markers as possible and animate the zoom
			mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(39.8097343, -98.5556199)));
			mMap.animateCamera(CameraUpdateFactory.zoomTo(3));
		}
	}
}
