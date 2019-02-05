package com.rdm.android.learningwithnationalparks.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.rdm.android.learningwithnationalparks.fragments.ImageGridFragment;
import com.rdm.android.learningwithnationalparks.networkFlickr.FlickrPhoto;
import com.rdm.android.learningwithnationalparks.networkFlickr.FlickrResponse;
import com.rdm.android.learningwithnationalparks.networkFlickr.Photos;
import com.rdm.android.learningwithnationalparks.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageGridActivity extends AppCompatActivity {
    private static final String LOG_TAG = ImageGridActivity.class.getSimpleName();

    @BindView(R.id.empty_view)
    @Nullable
    TextView mEmptyView;
    private String STATE_KEY = "list_state";
    private Parcelable mListState;
    private FlickrResponse flickrResponse;
    private Photos photos;
    private List<FlickrPhoto> photoItems = new ArrayList<>();
    private Context context;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        ButterKnife.bind(this);

        ImageGridFragment imageGridFragment = new ImageGridFragment();
        imageGridFragment.setPhotos(photos);
        imageGridFragment.setFlickrPhoto(photoItems);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.image_grid_container, imageGridFragment)
                .commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.image_grid_toolbar_title);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        Log.i(LOG_TAG, "ImageGridActivity :onSaveInstanceState");
        super.onSaveInstanceState(state);
        mLayoutManager = new LinearLayoutManager(this);
        mListState = mLayoutManager.onSaveInstanceState();
        state.putParcelable(STATE_KEY, mListState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        mLayoutManager = new LinearLayoutManager(this);
        Log.i(LOG_TAG, "ImageGridActivity :onRestoreInstanceState");
        if (state != null) {
            mListState = state.getParcelable(STATE_KEY);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "ImageGridActivity :onResumeInstanceState");
        if (mListState != null) {
            mLayoutManager.onRestoreInstanceState(mListState);
        }
    }
}