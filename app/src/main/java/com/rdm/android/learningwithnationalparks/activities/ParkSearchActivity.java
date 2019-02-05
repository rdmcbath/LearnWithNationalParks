package com.rdm.android.learningwithnationalparks.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.LinearLayout;

import com.rdm.android.learningwithnationalparks.fragments.ParkSearchFragment;
import com.rdm.android.learningwithnationalparks.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ParkSearchActivity extends AppCompatActivity {
    private static final String LOG_TAG = ParkSearchActivity.class.getSimpleName();

    ParkSearchFragment mParkSearchFragment;
    @BindView(R.id.park_search_linear_layout)
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park_search);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.main_park_search_label);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        mParkSearchFragment = new ParkSearchFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.park_search_container, mParkSearchFragment)
                .commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        if (requestCode == ParkSearchFragment.REQUEST_LOCATION) {
            mParkSearchFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
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



