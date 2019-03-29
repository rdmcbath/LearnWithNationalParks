package com.rdm.android.learningwithnationalparks.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.maps.model.StreetViewPanoramaLocation;
import com.rdm.android.learningwithnationalparks.adapters.LocalInfoWindowAdapter;
import com.rdm.android.learningwithnationalparks.fragments.LocalParkSearchFragment;
import com.rdm.android.learningwithnationalparks.R;
import com.rdm.android.learningwithnationalparks.fragments.NationalParkSearchFragment;
import com.rdm.android.learningwithnationalparks.networkMaps.GetNationalParksData;
import com.rdm.android.learningwithnationalparks.networkMaps.GetNearbyParksData;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

public class ParkSearchActivity extends AppCompatActivity implements OnMapReadyCallback,
		GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
	private static final String LOG_TAG = ParkSearchActivity.class.getSimpleName();

	LocalParkSearchFragment mLocalParkSearchFragment;
	NationalParkSearchFragment mNatParkSearchFragment;
	@BindView(R.id.park_search_linear_layout)
	LinearLayout linearLayout;
	public static final int REQUEST_LOCATION = 199;
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private long UPDATE_INTERVAL = 15000;  /* 15 secs */
	private long FASTEST_INTERVAL = 5000; /* 5 secs */
	private static final int VIEW_NATIONAL = 0;
	private static final int VIEW_LOCAL = 1;
	private static final String PREFS_VIEW_OPTION = "com.rdm.android.learningwithnationalparks.view_option";
	private static final String PREFS_LOCATE_MAP_TYPE = "com.rdm.android.learningwithnationalparks.map_type";
	private SharedPreferences sharedPrefs;
	private GoogleMap mMap;
	private Marker marker;
	double latitude;
	double longitude;
	GoogleApiClient mGoogleApiClient;
	Location mLastLocation;
	Marker mCurrLocationMarker;
	LocationRequest mLocationRequest;
	SupportMapFragment mapFragment;
	Activity activity;
	SupportStreetViewPanoramaFragment streetViewFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_park_search);
		ButterKnife.bind(this);

		Toolbar toolbar = findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);
		ActionBar ab = getSupportActionBar();
		if (ab != null) {
			ab.setDisplayHomeAsUpEnabled(true);
		}

		//Check if Google Play Services is available or not
		if (!checkGooglePlayServices()) {
			Log.i(LOG_TAG, "onCreateView: Google Play Services are not available");
			Snackbar.make(getWindow().getDecorView().getRootView(), "Google Play Services are not available", Snackbar.LENGTH_LONG).show();
			finish();
		} else {
			Log.i(LOG_TAG, "onCreateView: Google Play Services available");
		}

		// Obtain the SupportMapFragment and get notified when the map is ready
		// to be used.
		mapFragment = SupportMapFragment.newInstance();
		getSupportFragmentManager().beginTransaction().add(R.id.park_search_container, mapFragment).commit();
		mapFragment.getMapAsync(this);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.map_options, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

//		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
//
//		switch (item.getItemId()) {
//			case R.id.action_local_view:
//
//				sharedPrefs.edit().putInt(VIEW_OPTION, VIEW_LOCAL).apply();
//				getSupportActionBar().setTitle("Parks Near You");
//
//				return true;
//
//			case R.id.action_national_view:
//
//				sharedPrefs.edit().putInt(VIEW_OPTION, VIEW_NATIONAL).apply();
//				getSupportActionBar().setTitle("National Parks");
//
//				return true;
//
//			case android.R.id.home:
//				onBackPressed();
//				return true;
//
//			default:
//				return super.onOptionsItemSelected(item);
//		}

		// Change the map type based on the user's selection.
		switch (item.getItemId()) {
			case R.id.normal_map:
				mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				sharedPrefs.edit().putInt(PREFS_LOCATE_MAP_TYPE, GoogleMap.MAP_TYPE_NORMAL).apply();
				return true;
			case R.id.hybrid_map:
				mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
				sharedPrefs.edit().putInt(PREFS_LOCATE_MAP_TYPE, GoogleMap.MAP_TYPE_HYBRID).apply();
				return true;
			case R.id.satellite_map:
				mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
				sharedPrefs.edit().putInt(PREFS_LOCATE_MAP_TYPE, GoogleMap.MAP_TYPE_SATELLITE).apply();
				return true;
			case R.id.terrain_map:
				mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
				sharedPrefs.edit().putInt(PREFS_LOCATE_MAP_TYPE, GoogleMap.MAP_TYPE_TERRAIN).apply();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		this.mMap = googleMap;

		setMapStyle(mMap); // load the map style from json raw file
		setMapLongClick(mMap); // Set a long click listener for the map;
		setPoiClick(mMap); // Set a click listener for points of interest.

		//Enable Location which includes building the Google API Client
		enableMyLocation(mMap);

		// Get shared prefs for view option and map type
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		int pref = sharedPrefs.getInt(PREFS_VIEW_OPTION, VIEW_NATIONAL); // default is the national view
		int mapType = sharedPrefs.getInt(PREFS_LOCATE_MAP_TYPE, GoogleMap.MAP_TYPE_NORMAL); // default is the normal map type
		mMap.setMapType(mapType);

		// Set custom info window adapter for the google map
		mMap.setInfoWindowAdapter(new LocalInfoWindowAdapter(getLayoutInflater(), this));

		setInfoWindowClickToPanorama(mMap);


		if (pref == VIEW_NATIONAL) {
			showNationalView();
		} else if (pref == VIEW_LOCAL){
			showLocalView();
		}
	}

	/**
	 * Checks for location permissions, and requests them if they are missing.
	 * Otherwise, enables the location layer.
	 */
	private void enableMyLocation(GoogleMap map) {
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
			buildGoogleApiClient();
			map.setMyLocationEnabled(true);
		} else {
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
		}
	}


	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		// Check if location permissions are granted and if so enable the
		// location data layer.
		switch (requestCode) {
			case REQUEST_LOCATION:
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

					enableMyLocation(mMap);
					break;
				}
		}
	}

	protected synchronized void buildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API)
				.build();
		mGoogleApiClient.connect();
	}

//	public void checkLocationPermission() {
//		if (ContextCompat.checkSelfPermission(this,
//				Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//			// Asking user if explanation is needed
//			if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
//
//				//Show an explanation to the user *asynchronously* -- don't block this thread
//				// waiting for the user's response! After the user sees the explanation, try again
//				// to request the permission. Prompt the user once explanation has been shown
//				new AlertDialog.Builder(this)
//						.setTitle(R.string.title_location_permission)
//						.setMessage(R.string.text_location_permission)
//						.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
//							@Override
//							public void onClick(DialogInterface dialogInterface, int i) {
//								//Prompt the user once explanation has been shown
//								ActivityCompat.requestPermissions(activity,
//										new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
//							}
//						})
//						.create()
//						.show();
//
//			} else {
//				// No explanation needed, we can request the permission.
//				ActivityCompat.requestPermissions(this,
//						new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
//			}
//		}
//	}

//	@Override
//	public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
//		Log.d(TAG, "onRequestPermissionsResult()");
//
//		switch (requestCode) {
//			case REQUEST_LOCATION: {
//
//				// If request is cancelled, the result arrays are empty
//				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//					// permission was granted
//					if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//							== PackageManager.PERMISSION_GRANTED) {
//
//						buildGoogleApiClient();
//						mMap.setMyLocationEnabled(true);
//					}
//
//				} else {
//					// Permission denied, Disable the functionality that depends on this permission
//					Snackbar.make(getWindow().getDecorView().getRootView(), R.string.permission_denied, Snackbar.LENGTH_LONG).show();
//				}
//				break;
//			}
//		}
//	}

	/**
	 * Adds a blue marker to the map when the user long clicks on it.
	 *
	 * @param map The GoogleMap to attach the listener to.
	 */
	private void setMapLongClick(final GoogleMap map) {

		// Add a blue marker to the map when the user performs a long click.
		map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
			@Override
			public void onMapLongClick(LatLng latLng) {
				String snippet = String.format(Locale.getDefault(),
						getString(R.string.lat_long_snippet),
						latLng.latitude,
						latLng.longitude);

				map.addMarker(new MarkerOptions()
						.position(latLng)
						.title(getString(R.string.dropped_pin))
						.snippet(snippet)
						.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
			}
		});
	}

	/**
	 * Adds a marker when a place of interest (POI) is clicked with the name of
	 * the POI and immediately shows the info window.
	 *
	 * @param map The GoogleMap to attach the listener to.
	 */
	private void setPoiClick(final GoogleMap map) {
		map.setOnPoiClickListener(new GoogleMap.OnPoiClickListener() {
			@Override
			public void onPoiClick(PointOfInterest poi) {
				Marker poiMarker = map.addMarker(new MarkerOptions()
						.position(poi.latLng)
						.title(poi.name));
				poiMarker.showInfoWindow();
				poiMarker.setTag(getString(R.string.poi));
			}
		});
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.i(LOG_TAG, "LocalParkSearchFragment onLocationChanged");

		mLastLocation = location;
		if (mCurrLocationMarker != null) {
			mCurrLocationMarker.remove();
		}
		//Place current location marker
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		LatLng latLng = new LatLng(latitude, longitude);
		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.position(latLng);
		markerOptions.title(getString(R.string.current_position));
		markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
		mCurrLocationMarker = mMap.addMarker(markerOptions);

		//move map camera
		mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 9));

        Snackbar.make(getWindow().getDecorView().getRootView(), R.string.current_location, Snackbar.LENGTH_LONG).show();

		Log.i(LOG_TAG, "onLocationChanged: " + String.format("latitude:%.3f longitude:%.3f", latitude, longitude));

		//stop location updates
		if (mGoogleApiClient != null) {
			LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
			Log.i(LOG_TAG, "onLocationChanged: Removing Location Updates");
		}
		Log.i(LOG_TAG, "LocalParkSearchFragment: onLocationChanged: Exit");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.i(LOG_TAG, "onSaveInstanceState");
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.i(LOG_TAG, "onRestoreInstanceState");
		super.onRestoreInstanceState(savedInstanceState);
	}

	private void showNationalView() {

		if (haveNetworkConnection()) {

			String url = getNatParkSearchUrl();
			Object[] DataTransfer = new Object[2];
			DataTransfer[0] = mMap;
			DataTransfer[1] = url;
			Log.d(LOG_TAG, "showNationalView() called: " + url);
			GetNationalParksData getNationalParksData = new GetNationalParksData();
			getNationalParksData.execute(DataTransfer);
			Snackbar.make(getWindow().getDecorView().getRootView(), R.string.pick_nat_park, Snackbar.LENGTH_LONG).show();
		} else {
			// no connection available, show message
			Snackbar.make(getWindow().getDecorView().getRootView(), R.string.no_network, Snackbar.LENGTH_LONG).show();
		}
	}

	private String getNatParkSearchUrl() {

		StringBuilder googletextInputPlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json?");
		googletextInputPlacesUrl.append("query=National+Parks+in+United+States");
		googletextInputPlacesUrl.append("&key=").append(getString(R.string.api_key_maps));
		Log.d(LOG_TAG, "googletextInputPlacesUrl is " + googletextInputPlacesUrl.toString());
		return (googletextInputPlacesUrl.toString());
	}

	private void showLocalView() {

		if (haveNetworkConnection()) {

			String url = getLocalParkSearchUrl(latitude, longitude);
			Object[] DataTransfer = new Object[2];
			DataTransfer[0] = mMap;
			DataTransfer[1] = url;
			GetNearbyParksData getNearbyParksData = new GetNearbyParksData();
			getNearbyParksData.execute(DataTransfer);
			Log.d(LOG_TAG, "showLocalView Method Called: " + url);
		} else {
			// no connection available, show message
			Snackbar.make(getWindow().getDecorView().getRootView(), R.string.no_network, Snackbar.LENGTH_LONG).show();
		}
	}

	private String getLocalParkSearchUrl(double latitude, double longitude) {

		String types = "park|campground|rv_park|zoo";
		int PROXIMITY_RADIUS = 50000;

		StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
		googlePlacesUrl.append("location=").append(latitude).append(",").append(longitude);
		googlePlacesUrl.append("&radius=").append(PROXIMITY_RADIUS);
		googlePlacesUrl.append("&type=").append(types);
		googlePlacesUrl.append("&sensor=true");
		googlePlacesUrl.append("&key=").append(getString(R.string.api_key_maps));
		Log.d("getUrl", googlePlacesUrl.toString());
		Log.d(LOG_TAG, "nearby parks url endpoint: " + googlePlacesUrl.toString());

		return (googlePlacesUrl.toString());
	}

	/**
	 * Starts a Street View panorama when an info window is clicked.
	 *
	 * @param map The GoogleMap to set the listener to.
	 */
	private void setInfoWindowClickToPanorama(GoogleMap map) {
		map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

			@Override
			public void onInfoWindowClick(Marker marker) {

				// Set the position to the position of the marker
				StreetViewPanoramaOptions options = new StreetViewPanoramaOptions().position(marker.getPosition());
				streetViewFragment = SupportStreetViewPanoramaFragment.newInstance(options);

				// Replace the fragment and add it to the backstack
				getSupportFragmentManager().beginTransaction()
						.replace(R.id.park_search_container, streetViewFragment, "panoramaFragment")
						.addToBackStack(null)
						.commit();

				streetViewFragment.getStreetViewPanoramaAsync(new OnStreetViewPanoramaReadyCallback() {
					@Override
					public void onStreetViewPanoramaReady(StreetViewPanorama panorama) {

						panorama.setOnStreetViewPanoramaChangeListener(new StreetViewPanorama.OnStreetViewPanoramaChangeListener() {
							@Override
							public void onStreetViewPanoramaChange(StreetViewPanoramaLocation streetViewPanoramaLocation) {
								if (streetViewPanoramaLocation != null && streetViewPanoramaLocation.links != null) {
									// panorama view is present
								} else {
									// panorama view not available
									Toast.makeText(ParkSearchActivity.this, "Panorama view not available at this marker " + ("\ud83d\ude16"), Toast.LENGTH_LONG).show();
									onBackPressed();
								}
							}
						});
					}

				});
			}
		});
	}

	/**
	 * Loads a style from the map_style.json file to style the Google Map. Log
	 * the errors if the loading fails.
	 *
	 * @param map The GoogleMap object to style.
	 */
	private void setMapStyle(GoogleMap map) {
		try {
			// Customise the styling of the base map using a JSON object defined
			// in a raw resource file.
			boolean success = map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));

			if (!success) {
				Log.e(TAG, "Style parsing failed.");
			}
		} catch (Resources.NotFoundException e) {
			Log.e(TAG, "Can't find style. Error: ", e);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (mGoogleApiClient != null) {
			mGoogleApiClient.connect();
		}
	}

	@Override
	public void onStop(){
		stopLocationUpdates();
		super.onStop();
	}

	@Override
	public void onPause() {
		super.onPause();
		//stop location updates when Activity is no longer active
		stopLocationUpdates();
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (!checkGooglePlayServices()) {
			Toast.makeText(getApplicationContext(),"Please install google play services",Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onConnected(@Nullable Bundle bundle) {

		mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(UPDATE_INTERVAL);
		mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
			LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
		}
	}

	@Override
	public void onConnectionSuspended(int i) {
		Log.i(TAG, "Connection suspended");
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
		Log.i(TAG, "connection failed");
	}

	private boolean checkGooglePlayServices() {
		GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
		int result = googleAPI.isGooglePlayServicesAvailable(this);
		if (result != ConnectionResult.SUCCESS) {
			if (googleAPI.isUserResolvableError(result)) {
				googleAPI.getErrorDialog(this, result,
						0).show();
			}
			return false;
		}
		return true;
	}


	private void startLocationUpdates() {

	}

	public void stopLocationUpdates() {
		if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
			LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
			mGoogleApiClient.disconnect();
		}
	}

	private boolean haveNetworkConnection() {
		boolean haveConnectedWifi = false;
		boolean haveConnectedMobile = false;

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		assert cm != null;
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					haveConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					haveConnectedMobile = true;
		}
		return haveConnectedWifi || haveConnectedMobile;
	}
}




