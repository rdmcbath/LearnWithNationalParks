package com.rdm.android.learningwithnationalparks.networkMaps;

import android.util.Log;
import android.widget.Toast;

import com.rdm.android.learningwithnationalparks.activities.ParkSearchActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Rebecca McBath
 * on 2/21/19.
 */
public class JsonParserTextInputPlaces {

	List<HashMap<String, String>> parse(String jsonData) {
		JSONArray jsonArray = null;
		JSONObject jsonObject;

		try {
			Log.d("TextInputPlaces", "parse");
			jsonObject = new JSONObject(jsonData);
			jsonArray = jsonObject.getJSONArray("results");
		} catch (JSONException e) {
			Log.d("TextInputPlaces", "parse error");
			e.printStackTrace();
		}
		return getTextInputPlaces(jsonArray);
	}


	private List<HashMap<String, String>> getTextInputPlaces(JSONArray jsonArray) {

		List<HashMap<String, String>> txtInputPlacesList = null;
		if (jsonArray != null) {
			int placesCount = jsonArray.length();

			txtInputPlacesList = new ArrayList<>();
			HashMap<String, String> txtInputPlaceMap;
			Log.d("TextInputPlaces", "getTextInputPlaces");

			for (int i = 0; i < placesCount; i++) {
				try {
					txtInputPlaceMap = getTextInputPlace((JSONObject) jsonArray.get(i));
					txtInputPlacesList.add(txtInputPlaceMap);
					Log.d("TextInputPlaces", "Adding textInputPlaces");

				} catch (JSONException e) {
					Log.d("TextInputPlaces", "Error in Adding textInputPlaces");
					e.printStackTrace();
				}
			}
		} else {
			Log.d("getTextInputPlaces", "Error getting jsonArray.length(), probably null due to no connection");
		}
		return txtInputPlacesList;
	}

	private HashMap<String, String> getTextInputPlace(JSONObject googlePlaceJson) {
		HashMap<String, String> googleTextInputPlaceMap = new HashMap<String, String>();
		String placeName = "-NA-";
		String formatted_address = "-NA-";
		String latitude = "";
		String longitude = "";
		String reference = "";

		Log.d("TextInputPlaces", "getTextInputPlace Entered");

		try {
			if (!googlePlaceJson.isNull("name")) {
				placeName = googlePlaceJson.getString("name");
			}
			if (!googlePlaceJson.isNull("formatted_address")) {
				formatted_address = googlePlaceJson.getString("formatted_address");
			}
			latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
			longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
			reference = googlePlaceJson.getString("reference");
			googleTextInputPlaceMap.put("place_name", placeName);
			googleTextInputPlaceMap.put("formatted_address", formatted_address);
			googleTextInputPlaceMap.put("lat", latitude);
			googleTextInputPlaceMap.put("lng", longitude);
			googleTextInputPlaceMap.put("reference", reference);
			Log.d("TextInputPlaces", "getTextInputPlace - Putting TextInputPlaces");
		} catch (JSONException e) {
			Log.d("TextInputPlaces", "getTextInputPlace - Error");
			e.printStackTrace();
		}
		return googleTextInputPlaceMap;
	}
}

