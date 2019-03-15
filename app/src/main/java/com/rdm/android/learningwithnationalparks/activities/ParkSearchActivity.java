package com.rdm.android.learningwithnationalparks.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import static com.rdm.android.learningwithnationalparks.fragments.NationalParkSearchFragment.REQUEST_LOCATION;

public class ParkSearchActivity extends AppCompatActivity {
	private static final String LOG_TAG = ParkSearchActivity.class.getSimpleName();

	LocalParkSearchFragment mLocalParkSearchFragment;
	NationalParkSearchFragment mNatParkSearchFragment;
	@BindView(R.id.park_search_linear_layout)
	LinearLayout linearLayout;
	private static final int VIEW_NATIONAL = 0;
	private static final int VIEW_LOCAL = 1;
	private static String VIEW_OPTION = "view_option";
	private SharedPreferences sharedPrefs;

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

		//set default preference when the activity starts (national map view)
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

		int pref = sharedPrefs.getInt(VIEW_OPTION, MODE_PRIVATE);

		if (pref == 0) {
			getSupportActionBar().setTitle("National Parks");
			mNatParkSearchFragment = new NationalParkSearchFragment();
			getSupportFragmentManager().beginTransaction()
					.setPrimaryNavigationFragment(mNatParkSearchFragment)
					.add(R.id.park_search_container, mNatParkSearchFragment, "NationalParkSearchFragment")
					.commit();
		} else {
			getSupportActionBar().setTitle("Parks Near You");
			mLocalParkSearchFragment = new LocalParkSearchFragment();
			getSupportFragmentManager().beginTransaction()
					.setPrimaryNavigationFragment(mLocalParkSearchFragment)
					.add(R.id.park_search_container, mLocalParkSearchFragment, "LocalParkSearchFragment")
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

		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

		switch (item.getItemId()) {
			case R.id.action_local_view:

				sharedPrefs.edit().putInt(VIEW_OPTION, VIEW_LOCAL).apply();
				getSupportActionBar().setTitle("Parks Near You");

				mLocalParkSearchFragment = new LocalParkSearchFragment();
				getSupportFragmentManager().beginTransaction()
						.setPrimaryNavigationFragment(mLocalParkSearchFragment)
						.replace(R.id.park_search_container, mLocalParkSearchFragment, "LocalParkSearchFragment")
						.commit();
				return true;

			case R.id.action_national_view:

				sharedPrefs.edit().putInt(VIEW_OPTION, VIEW_NATIONAL).apply();
				getSupportActionBar().setTitle("National Parks");

				mNatParkSearchFragment = new NationalParkSearchFragment();
				getSupportFragmentManager().beginTransaction()
						.setPrimaryNavigationFragment(mLocalParkSearchFragment)
						.replace(R.id.park_search_container, mNatParkSearchFragment, "NationalParkSearchFragment")
						.commit();
				return true;

			case android.R.id.home:
				onBackPressed();
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
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

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		Fragment currentFragment = this.getFragmentManager().findFragmentByTag("panoramaFragment");

		if (currentFragment != null && currentFragment.isVisible()) {
			FragmentManager childFm = currentFragment.getChildFragmentManager();
			childFm.popBackStack();
		} else {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
		super.onBackPressed();
	}
}




