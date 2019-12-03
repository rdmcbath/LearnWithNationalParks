package com.rdm.android.learningwithnationalparks.activities;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.material.snackbar.Snackbar;
import com.rdm.android.learningwithnationalparks.fragments.LocalParkSearchFragment;
import com.rdm.android.learningwithnationalparks.R;
import com.rdm.android.learningwithnationalparks.utils.AnalyticsUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ResultCallback<LocationSettingsResult> {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.lesson_plan_text_view)
    TextView lessonPlanView;
    @BindView(R.id.sights_text_view)
    TextView flickrView;
    @BindView(R.id.sounds_text_view)
    TextView soundsView;
    @BindView(R.id.search_text_view)
    TextView searchView;
    @BindView(R.id.lesson_plan_image)
    ImageView lessonPlanImageView;
    @BindView(R.id.sights_image)
    ImageView flickrImageView;
    @BindView(R.id.sounds_image)
    ImageView soundsImageView;
    @BindView(R.id.park_search_image)
    ImageView searchImageView;
    @BindView(R.id.toolbar_top)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    private Parcelable mListState;
    private String STATE_KEY = "list_state";
    public LinearLayoutManager mLayoutManager;
    protected GoogleApiClient mGoogleApiClient;
    PendingResult<LocationSettingsResult> result;
    private static final int REQUEST_CHECK_SETTINGS = 1000;
    LocalParkSearchFragment mParkSearchFragment;
    protected LocationRequest locationRequest;
	private AnalyticsUtils analytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbarTitle.setText(R.string.main_toolbar_title);

        lessonPlanView.setOnClickListener(view -> {
            Log.i("LOG_TAG", "OnClick in MainActivity LessonPlanView called");
            Intent lessonListIntent = new Intent(MainActivity.this, LessonListActivity.class);
            startActivity(lessonListIntent);
        });

        lessonPlanImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("LOG_TAG", "OnClick in MainActivity LessonPlanImageView called");
                Intent lessonListIntent = new Intent(MainActivity.this, LessonListActivity.class);
                startActivity(lessonListIntent);
            }
        });

        flickrView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("LOG_TAG", "OnClick in MainActivity PlacesView called");
                Intent sightsGridIntent = new Intent(MainActivity.this, ImageGridActivity.class);
                startActivity(sightsGridIntent);
            }
        });

        flickrImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("LOG_TAG", "OnClick in MainActivity PlacesImageView called");
                Intent flickrGridIntent = new Intent(MainActivity.this, ImageGridActivity.class);
                startActivity(flickrGridIntent);
            }
        });

        soundsView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("LOG_TAG", "OnClick in MainActivity SoundsView called");
                Intent soundsIntent = new Intent(MainActivity.this, SoundsListActivity.class);
                startActivity(soundsIntent);
            }
        });

        soundsImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("LOG_TAG", "OnClick in MainActivity SoundsImageView called");
                Intent soundsIntent = new Intent(MainActivity.this, SoundsListActivity.class);
                startActivity(soundsIntent);
            }
        });

        searchView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("LOG_TAG", "OnClick in MainActivity SearchView called");
                Intent searchIntent = new Intent(MainActivity.this, ParkSearchActivity.class);
                startActivity(searchIntent);
            }
        });

        searchImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("LOG_TAG", "OnClick in MainActivity SearchImageView called");
                Intent searchIntent = new Intent(MainActivity.this, ParkSearchActivity.class);
                startActivity(searchIntent);
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);

        analytics().reportEventFB(getApplicationContext(), getString(R.string.main_activity_analytics));

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(LOG_TAG, "MAIN ACTIVITY :onSaveInstanceState");
        outState.putParcelable(STATE_KEY, mListState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(LOG_TAG, "MAIN ACTIVITY onRestoreInstanceState");
        if (savedInstanceState != null) {
            mListState = savedInstanceState.getParcelable(STATE_KEY);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        builder.build()
                );

        result.setResultCallback(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                // NO need to show the dialog;
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //  Location settings are not satisfied. Show the user a dialog
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    //failed to show dialog
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are unavailable so not possible to show any dialog now
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                Snackbar.make(getWindow().getDecorView().getRootView(), R.string.location_enabled, Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(getWindow().getDecorView().getRootView(), R.string.location_not_enabled, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public AnalyticsUtils analytics() {
        if (analytics == null) analytics = new AnalyticsUtils(this);
        return analytics;
    }
}

