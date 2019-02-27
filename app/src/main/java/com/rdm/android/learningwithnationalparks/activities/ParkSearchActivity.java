package com.rdm.android.learningwithnationalparks.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.rdm.android.learningwithnationalparks.fragments.LocalParkSearchFragment;
import com.rdm.android.learningwithnationalparks.R;
import com.rdm.android.learningwithnationalparks.fragments.NationalParkSearchFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ParkSearchActivity extends AppCompatActivity {
    private static final String LOG_TAG = ParkSearchActivity.class.getSimpleName();

    LocalParkSearchFragment mLocalParkSearchFragment;
	NationalParkSearchFragment mNatParkSearchFragment;
    @BindView(R.id.park_search_linear_layout)
    LinearLayout linearLayout;
	private static final int VIEW_NATIONAL = 0;
	private static final int VIEW_LOCAL = 1;
	private static String VIEW_OPTION = "view_option";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_park_search);
	    ButterKnife.bind(this);

	    Toolbar toolbar = findViewById(R.id.tool_bar);
	    setSupportActionBar(toolbar);
	    getSupportActionBar().setTitle(R.string.park_search_label);
	    ActionBar ab = getSupportActionBar();
	    ab.setDisplayHomeAsUpEnabled(true);

	    //set default preference when the activity starts (national map view)
	    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

	    int pref = sharedPrefs.getInt(VIEW_OPTION, MODE_PRIVATE);

	    if (pref == 0) {
		    mNatParkSearchFragment = new NationalParkSearchFragment();
		    getSupportFragmentManager().beginTransaction()
				    .add(R.id.park_search_container, mNatParkSearchFragment)
				    .commit();

	    } else {
		    mNatParkSearchFragment = new NationalParkSearchFragment();
		    getSupportFragmentManager().beginTransaction()
				    .add(R.id.park_search_container, mNatParkSearchFragment)
				    .commit();
	    }
    }

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.maps_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

		switch (item.getItemId()) {
			case R.id.action_local_view:

				sharedPrefs.edit().putInt(VIEW_OPTION, VIEW_LOCAL).apply();

				mLocalParkSearchFragment = new LocalParkSearchFragment();
				getSupportFragmentManager().beginTransaction()
						.replace(R.id.park_search_container, mLocalParkSearchFragment)
						.commit();

				return true;

			case R.id.action_national_view:

				sharedPrefs.edit().putInt(VIEW_OPTION, VIEW_NATIONAL).apply();

				mNatParkSearchFragment = new NationalParkSearchFragment();
				getSupportFragmentManager().beginTransaction()
						.replace(R.id.park_search_container, mNatParkSearchFragment)
						.commit();

				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        if (requestCode == LocalParkSearchFragment.REQUEST_LOCATION) {
            mLocalParkSearchFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
	        mNatParkSearchFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i(LOG_TAG, "ParkSearchActivity: onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "ParkSearchActivity: onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}



