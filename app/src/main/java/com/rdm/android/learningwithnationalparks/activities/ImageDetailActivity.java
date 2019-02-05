package com.rdm.android.learningwithnationalparks.activities;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.rdm.android.learningwithnationalparks.fragments.ImageDetailFragment;
import com.rdm.android.learningwithnationalparks.networkFlickr.FlickrPhoto;
import com.rdm.android.learningwithnationalparks.networkFlickr.Photos;
import com.rdm.android.learningwithnationalparks.R;

import butterknife.ButterKnife;

public class ImageDetailActivity extends AppCompatActivity {
    private static final String LOG_TAG = ImageDetailActivity.class.getSimpleName();

    public boolean mDualPane;
    private String STATE_KEY = "list_state";
    private Parcelable mListState;
    private LinearLayoutManager mLayoutManager;
    private FlickrPhoto flickrPhoto;
    private Photos photos;
    public static final String KEY_IMAGE_DETAIL = "IMAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        ButterKnife.bind(this);

        flickrPhoto = getIntent().getParcelableExtra(KEY_IMAGE_DETAIL);

        ImageDetailFragment imageDetailFragment = new ImageDetailFragment();
        imageDetailFragment.setPhotos(photos);
        imageDetailFragment.setFlickrPhoto(flickrPhoto);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.image_detail_container, imageDetailFragment)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        Log.i(LOG_TAG, "ImageDetailActivity :onSaveInstanceState");
        super.onSaveInstanceState(state);
        state.putParcelable(STATE_KEY, mListState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        Log.i(LOG_TAG, "ImageDetailActivity :onRestoreInstanceState");
        super.onRestoreInstanceState(state);
        if (state != null) {
            mListState = state.getParcelable(STATE_KEY);
        }
    }

    @Override
    protected void onResume() {
        Log.i(LOG_TAG, "ImageDetailActivity :onResumeInstanceState");
        super.onResume();
        if (mListState != null) {
            mLayoutManager.onRestoreInstanceState(mListState);
        }
    }
}

