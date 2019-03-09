package com.rdm.android.learningwithnationalparks.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.rdm.android.learningwithnationalparks.R;

import java.util.HashMap;


/**
 * Created by Rebecca McBath
 * on 2/14/19.
 */
public class NationalInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

	private Context context;
	private Marker marker;
	private HashMap<String, Uri> images=null;
	private LayoutInflater mInflater;

	public NationalInfoWindowAdapter(LayoutInflater i) {
		mInflater = i;
	}

	// This defines the contents within the info window based on the marker
	@Override
	public View getInfoContents(Marker marker) {
		return null;
	}

	// This changes the frame of the info window; returning null uses the default frame.
	// This is just the border and arrow surrounding the contents specified above
	@Override
	public View getInfoWindow(Marker marker) {
		// Getting view from the layout file
		View v = mInflater.inflate(R.layout.custom_info_window_national, null);
		// Populate fields
		TextView title = v.findViewById(R.id.map_window_title);
		title.setText(marker.getTitle());

		TextView description = v.findViewById(R.id.map_window_snippet);
		description.setText(marker.getSnippet());

		TextView extraContent = v.findViewById(R.id.map_window_extra_content);
		extraContent.setText(R.string.info_window_panorama);

		// Return info window contents
		return v;
	}
}

