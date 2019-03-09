package com.rdm.android.learningwithnationalparks.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
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

import java.util.List;

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
				    .setPrimaryNavigationFragment(mNatParkSearchFragment)
				    .add(R.id.park_search_container, mNatParkSearchFragment)
				    .addToBackStack("NationalParkSearchFragment")
				    .commit();

	    } else {
		    mLocalParkSearchFragment = new LocalParkSearchFragment();
		    getSupportFragmentManager().beginTransaction()
				    .setPrimaryNavigationFragment(mLocalParkSearchFragment)
				    .add(R.id.park_search_container, mLocalParkSearchFragment)
				    .addToBackStack("LocalParkSearchFragment")
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
						.setPrimaryNavigationFragment(mLocalParkSearchFragment)
						.replace(R.id.park_search_container, mLocalParkSearchFragment)
						.addToBackStack("LocalParkSearchFragment")
						.commit();

				return true;

			case R.id.action_national_view:

				sharedPrefs.edit().putInt(VIEW_OPTION, VIEW_NATIONAL).apply();

				mNatParkSearchFragment = new NationalParkSearchFragment();
				getSupportFragmentManager().beginTransaction()
						.setPrimaryNavigationFragment(mLocalParkSearchFragment)
						.replace(R.id.park_search_container, mNatParkSearchFragment)
						.addToBackStack("NationalParkSearchFragment")
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

	@RequiresApi(api = Build.VERSION_CODES.O)
	@Override
	public void onBackPressed() {

		if (!recursivePopBackStack(getFragmentManager())) {
			super.onBackPressed();
		}
	}

	/**
	 * Recursively look through nested fragments for a backstack entry to pop
	 * @return: true if a pop was performed
	 */
	@RequiresApi(api = Build.VERSION_CODES.O)
	public static boolean recursivePopBackStack(FragmentManager fragmentManager) {
		if (fragmentManager.getFragments() != null) {
			for (Fragment fragment : fragmentManager.getFragments()) {
				if (fragment != null && fragment.isVisible()) {
					boolean popped = recursivePopBackStack(fragment.getChildFragmentManager());
					if (popped) {
						return true;
					}
				}
			}
		}

		if (fragmentManager.getBackStackEntryCount() > 0) {
			fragmentManager.popBackStack();
			return true;
		}

		return false;
	}
}



