package com.rdm.android.learningwithnationalparks.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.StreetViewPanoramaOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.Marker;
import com.rdm.android.learningwithnationalparks.R;
import com.rdm.android.learningwithnationalparks.activities.ParkSearchActivity;
import com.rdm.android.learningwithnationalparks.adapters.NationalInfoWindowAdapter;
import com.rdm.android.learningwithnationalparks.networkMaps.GetNationalParksData;

import static com.rdm.android.learningwithnationalparks.R.id.national_map;
import butterknife.BindView;

import static android.content.ContentValues.TAG;


/**
 * Created by Rebecca McBath
 * on 2/18/19.
 */
public class NationalParkSearchFragment
	extends Fragment implements OnMapReadyCallback,
	GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
		private static final String LOG_TAG = NationalParkSearchFragment.class.getSimpleName();

		private GoogleMap mMap;
		private Marker marker;
		double latitude;
		double longitude;
		GoogleApiClient mGoogleApiClient;
		Location mLastLocation;
		Marker mCurrLocationMarker;
		LocationRequest mLocationRequest;
		public static final int REQUEST_LOCATION = 199;

		@BindView(R.id.fragment_nat_park_search_layout)
		CoordinatorLayout coordinatorLayout;

    public NationalParkSearchFragment() {
	}

		public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_national_park_search, container, false);
		setHasOptionsMenu(true);

		getActivity().setTitle("US National Parks");

		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			checkLocationPermission();
		}

		//Check if Google Play Services Available or not
		if (!CheckGooglePlayServices()) {
			Log.i(LOG_TAG, "onCreateView: Google Play Services are not available");
			getActivity().finish();
		} else {
			Log.i(LOG_TAG, "onCreateView: Google Play Services available");
		}
		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		SupportMapFragment mapFragment = ((SupportMapFragment)
				this.getChildFragmentManager().findFragmentById(national_map));
		if (mapFragment != null) {
			mapFragment.getMapAsync(this);
		}

		return rootView;
	}

		private boolean CheckGooglePlayServices() {
		GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
		int result = googleAPI.isGooglePlayServicesAvailable(getContext());
		if (result != ConnectionResult.SUCCESS) {
			if (googleAPI.isUserResolvableError(result)) {
				googleAPI.getErrorDialog(getActivity(), result,
						0).show();
			}
			return false;
		}
		return true;
	}

		/**
		 * Manipulates the map once available.
		 * This callback is triggered when the map is ready to be used.
		 * If Google Play services is not installed on the device, the user will be prompted to install
		 * it inside the SupportMapFragment. This method will only be triggered once the user has
		 * installed Google Play services and returned to the app.
		 */
		@Override
		public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

		//Initialize Google Play Services
		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (ContextCompat.checkSelfPermission(getContext(),
					Manifest.permission.ACCESS_FINE_LOCATION)
					== PackageManager.PERMISSION_GRANTED) {
				//Location Permission already granted
				buildGoogleApiClient();
				mMap.setMyLocationEnabled(true);
				Log.i(LOG_TAG, "buildGoogleApiClient Method Called");
			}
		} else {
			//Request Location Permission
			checkLocationPermission();
		}

		setInfoWindowClickToPanorama(mMap);

		showNationalView();

			// Set custom info window adapter for the google map
			googleMap.setInfoWindowAdapter(new NationalInfoWindowAdapter(getLayoutInflater()));
	}

		protected synchronized void buildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(getContext())
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API)
				.build();
		mGoogleApiClient.connect();
	}

		@Override
		public void onConnected(Bundle bundle) {
		mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(1000);
		mLocationRequest.setFastestInterval(1000);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		if (ContextCompat.checkSelfPermission(getContext(),
				Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
		}
	}

	private void showNationalView(){
		Log.d(LOG_TAG, "showNationalView Method Called");

		Window window = getActivity().getWindow();
		Snackbar.make(window.getDecorView().getRootView(), R.string.pick_nat_park,
				Snackbar.LENGTH_LONG).show();

		String url = getNatParkSearchUrl();
		Object[] DataTransfer = new Object[2];
		DataTransfer[0] = mMap;
		DataTransfer[1] = url;
		Log.d(LOG_TAG, "showNationalView() called: " + url);
		GetNationalParksData getNationalParksData = new GetNationalParksData();
		getNationalParksData.execute(DataTransfer);
	}

		private String getNatParkSearchUrl() {

		StringBuilder googletextInputPlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json?");
		googletextInputPlacesUrl.append("query=National+Parks+in+United+States");
		googletextInputPlacesUrl.append("&key=").append(getString(R.string.api_key_maps));
		Log.d(LOG_TAG, "googletextInputPlacesUrl is " + googletextInputPlacesUrl.toString());
		return (googletextInputPlacesUrl.toString());
	}

	@Override
		public void onConnectionSuspended(int i) {
	}

		@Override
		public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
	}

		public void checkLocationPermission() {
		if (ContextCompat.checkSelfPermission(getContext(),
				Manifest.permission.ACCESS_FINE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED) {

			// Asking user if explanation is needed
			if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
					Manifest.permission.ACCESS_FINE_LOCATION)) {

				//Show an explanation to the user *asynchronously* -- don't block this thread
				// waiting for the user's response! After the user sees the explanation, try again
				// to request the permission. Prompt the user once explanation has been shown
				new AlertDialog.Builder(getContext())
						.setTitle(R.string.title_location_permission)
						.setMessage(R.string.text_location_permission)
						.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								//Prompt the user once explanation has been shown
								ActivityCompat.requestPermissions(getActivity(),
										new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
										REQUEST_LOCATION);
							}
						})
						.create()
						.show();

			} else {
				// No explanation needed, we can request the permission.
				ActivityCompat.requestPermissions(getActivity(),
						new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
						REQUEST_LOCATION);
			}
		}
	}

		@Override
		public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
		@NonNull int[] grantResults) {
		Log.d(TAG, "onRequestPermissionsResult()");

		switch (requestCode) {
			case REQUEST_LOCATION: {

				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {

					// permission was granted.
					if (ContextCompat.checkSelfPermission(getContext(),
							Manifest.permission.ACCESS_FINE_LOCATION)
							== PackageManager.PERMISSION_GRANTED) {

						if (mGoogleApiClient == null) {
							buildGoogleApiClient();
						}
						mMap.setMyLocationEnabled(true);
					}

				} else {
					// Permission denied, Disable the functionality that depends on this permission
					Window window = getActivity().getWindow();
					Snackbar.make(window.getDecorView().getRootView(), R.string.permission_denied,
							Snackbar.LENGTH_LONG).show();
				}
				break;
			}
		}
	}

		@Override
		public void onPause() {
		super.onPause();
	}

	/**
	 * Starts a Street View panorama when an info window is clicked.
	 * @param map The GoogleMap to set the listener to.
	 */
	private void setInfoWindowClickToPanorama(GoogleMap map) {
		map.setOnInfoWindowClickListener(
				new GoogleMap.OnInfoWindowClickListener() {
					@Override
					public void onInfoWindowClick(Marker marker) {
							// Set the position to the position of the marker
							StreetViewPanoramaOptions options =
									new StreetViewPanoramaOptions().position(
											marker.getPosition());

							SupportStreetViewPanoramaFragment streetViewFragment
									= SupportStreetViewPanoramaFragment
									.newInstance(options);

							// Replace the fragment and add it to the backstack
							getChildFragmentManager().beginTransaction()
									.replace(R.id.national_map,
											streetViewFragment)
									.addToBackStack(null).commit();
						}
				});
	}
}
