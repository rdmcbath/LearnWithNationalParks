package com.rdm.android.learningwithnationalparks.adapters;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.rdm.android.learningwithnationalparks.R;
import com.rdm.android.learningwithnationalparks.activities.MainActivity;
import com.rdm.android.learningwithnationalparks.activities.ParkSearchActivity;
import com.rdm.android.learningwithnationalparks.fragments.NationalParkSearchFragment;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Rebecca McBath
 * on 2/14/19.
 */
public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

	private Context context;
	private Marker marker;

//	public CustomInfoWindowAdapter(Context context) {
//		this.context = context;
//	}
//
//	@Override
//	public View getInfoWindow(final Marker marker) {
//
//		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//		View v = inflater.inflate(R.layout.maps_custom_info_window, null);
//
//		TextView title = v.findViewById(R.id.map_window_title);
//		TextView snippet = v.findViewById(R.id.map_window_snippet);
//		ImageView photo = v.findViewById(R.id.map_window_image);
//
////		String imageUrl = "imageURL";
////
////		Glide.with(context)
////				.load(imageUrl)
////				.apply(RequestOptions.centerCropTransform())
////				.apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
////				.into(photo);
//
//		title.setText(marker.getTitle());
//		snippet.setText(marker.getSnippet());
//
//		return v;
//	}

//	@Override
//	public View getInfoContents(Marker marker) {
//
//		return null;
//	}

	private LayoutInflater mInflater;

	public CustomInfoWindowAdapter(LayoutInflater i){
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
		View v = mInflater.inflate(R.layout.maps_custom_info_window, null);
		// Populate fields
		TextView title = v.findViewById(R.id.map_window_title);
		title.setText(marker.getTitle());

		TextView description = v.findViewById(R.id.map_window_snippet);
		description.setText(marker.getSnippet());
		// Return info window contents
		return v;
	}
}

