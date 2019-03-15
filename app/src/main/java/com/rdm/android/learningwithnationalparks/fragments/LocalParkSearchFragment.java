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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.StreetViewPanoramaOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rdm.android.learningwithnationalparks.activities.ParkSearchActivity;
import com.rdm.android.learningwithnationalparks.adapters.LocalInfoWindowAdapter;
import com.rdm.android.learningwithnationalparks.networkMaps.GetNearbyParksData;
import com.rdm.android.learningwithnationalparks.R;
import butterknife.BindView;

import static android.content.ContentValues.TAG;
import static com.rdm.android.learningwithnationalparks.R.id.local_map;

public class LocalParkSearchFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String LOG_TAG = LocalParkSearchFragment.class.getSimpleName();

    private GoogleMap mMap;
    double latitude;
    double longitude;
	GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    public static final int REQUEST_LOCATION = 199;
    public static final String Park = "park";

//    @BindView(R.id.fab)
//    FloatingActionButton fab;
    @BindView(R.id.fragment_local_park_search_layout)
    CoordinatorLayout coordinatorLayout;

    public LocalParkSearchFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_local_park_search, container, false);

	    getActivity().setTitle("Parks Near You");

	    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
		    checkLocationPermission();
	    }

	    //Check if Google Play Services Available or not
	    if (!CheckGooglePlayServices()) {
		    Log.i(LOG_TAG, "LocalParkSearchFragment onCreateView: " +
				    "Finishing test case since Google Play Services are not available");
		    getActivity().finish();
	    } else {
		    Log.i(LOG_TAG, "LocalParkSearchFragment onCreateView: Google Play Services available");
	    }
	    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
	    SupportMapFragment mapFragment = ((SupportMapFragment)
			    this.getChildFragmentManager().findFragmentById(local_map));
	    if (mapFragment != null) {
		    mapFragment.getMapAsync(this);
	    }

//	    FloatingActionButton fab = rootView.findViewById(R.id.fab);
//	    fab.setOnClickListener(new View.OnClickListener() {
//
//		    @Override
//		    public void onClick(View v) {
//			    Log.i(LOG_TAG, "ParkSearchFragment onClick: FAB is Clicked");
//			    String url = getLocalParkSearchUrl(latitude, longitude);
//			    Object[] DataTransfer = new Object[2];
//			    DataTransfer[0] = mMap;
//			    DataTransfer[1] = url;
//			    Log.d("onClick", url);
//			    GetNearbyParksData getNearbyParksData = new GetNearbyParksData();
//			    getNearbyParksData.execute(DataTransfer);
//			    Log.i(LOG_TAG, "getNearbyParksData Method Called");
//			    Window window = getActivity().getWindow();
//			    Snackbar.make(window.getDecorView().getRootView(), R.string.pick_local_park,
//					    Snackbar.LENGTH_LONG).show();
//		    }
//	    });

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
        this.mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
                Log.d(LOG_TAG, "buildGoogleApiClient Method Called");
            }

        } else {
            //Request Location Permission
            checkLocationPermission();
        }

	    setInfoWindowClickToPanorama(mMap);

        // Set custom info window adapter for the google map
	    googleMap.setInfoWindowAdapter(new LocalInfoWindowAdapter(getLayoutInflater(), getContext()));

	    // Add and show marker when the map is touched
//	    googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//		    @Override
//		    public void onMapClick(LatLng arg0) {
//			    mMap.clear();
//			    MarkerOptions markerOptions = new MarkerOptions();
//			    markerOptions.position(arg0);
//			    mMap.animateCamera(CameraUpdateFactory.newLatLng(arg0));
//			    Marker marker = mMap.addMarker(markerOptions);
//			    marker.showInfoWindow();
//		    }
//	    });
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
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
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest, this);
        }
    }

	private void showLocalView() {
		Log.d(LOG_TAG, "showLocalView called");

		String url = getLocalParkSearchUrl(latitude, longitude);
		Object[] DataTransfer = new Object[2];
		DataTransfer[0] = mMap;
		DataTransfer[1] = url;
		GetNearbyParksData getNearbyParksData = new GetNearbyParksData();
		getNearbyParksData.execute(DataTransfer);
		Log.d(LOG_TAG, "getNearbyParksData Method Called");
		Window window = getActivity().getWindow();
		Snackbar.make(window.getDecorView().getRootView(), R.string.pick_local_park,
				Snackbar.LENGTH_LONG).show();
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

    @Override
    public void onConnectionSuspended(int i) {
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

	    showLocalView();

        //move map camera
	    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

//        Window window = getActivity().getWindow();
//        Snackbar.make(window.getDecorView().getRootView(), R.string.current_location,
//                Snackbar.LENGTH_LONG).show();

        Log.i(LOG_TAG, "onLocationChanged: " +
                String.format("latitude:%.3f longitude:%.3f", latitude, longitude));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            Log.i(LOG_TAG, "onLocationChanged: Removing Location Updates");
        }
        Log.i(LOG_TAG, "LocalParkSearchFragment: onLocationChanged: Exit");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    public void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                //Show an explanation to the user *asynchronously* -- don't block this thread
                // waiting for the user's response! After the user sees the explanation, try again
                // to request the permission. Prompt the user once explanation has been shown
                new AlertDialog.Builder(getActivity())
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
        Log.d(TAG, "LocalParkSearchFragment: onRequestPermissionsResult()");

        switch (requestCode) {
            case REQUEST_LOCATION: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {
                    // Permission denied, Disable the functionality that depends on this permission
                    Snackbar.make(getActivity().getWindow().getDecorView().getRootView(), R.string.permission_denied, Snackbar.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
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

						SupportStreetViewPanoramaFragment streetViewFragment = SupportStreetViewPanoramaFragment.newInstance(options);

							// Replace the fragment and add it to the backstack
							getChildFragmentManager().beginTransaction()
									.replace(R.id.local_map, streetViewFragment, "panoramaFragment")
									.addToBackStack("panoramaFragment").commit();
					}
				});
	}

	public void getPhotos() {


		

	}
}
