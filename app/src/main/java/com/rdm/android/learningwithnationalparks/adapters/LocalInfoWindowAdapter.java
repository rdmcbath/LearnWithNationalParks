package com.rdm.android.learningwithnationalparks.adapters;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.rdm.android.learningwithnationalparks.R;

import java.util.HashMap;
import java.util.Hashtable;

/**
 * Created by Rebecca McBath
 * on 3/6/19.
 */
public class LocalInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

	private LayoutInflater mInflater;

	public LocalInfoWindowAdapter(LayoutInflater i, Context context) {
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
	public View getInfoWindow(final Marker marker) {
		// Getting view from the layout file
		View v = mInflater.inflate(R.layout.custom_info_window_local, null);
		// Populate fields
		TextView title = v.findViewById(R.id.map_window_title_local);
		title.setText(marker.getTitle());

		TextView description = v.findViewById(R.id.map_window_snippet_local);
		description.setText(marker.getSnippet());

		Button panorama = v.findViewById(R.id.map_window_extra_content);
		panorama.setText(R.string.info_window_extra_content);

//		ImageView icon = v.findViewById(R.id.map_window_image_local);
//		Uri imageUri = Uri.parse(marker.getId());
//
//		Glide.with(mContext)
//				.load(imageUri)
//				.apply(RequestOptions.errorOf(R.drawable.park_tower))
//				.apply(RequestOptions.centerCropTransform())
//				.apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
//				.into(icon);

		// Return info window contents
		return v;
	}
}