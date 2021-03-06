package com.rdm.android.learningwithnationalparks.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;
import com.rdm.android.learningwithnationalparks.fragments.ImageGridFragment;
import com.rdm.android.learningwithnationalparks.networkFlickr.FlickrPhoto;
import com.rdm.android.learningwithnationalparks.networkFlickr.FlickrResponse;
import com.rdm.android.learningwithnationalparks.networkFlickr.Photos;
import com.rdm.android.learningwithnationalparks.R;
import com.rdm.android.learningwithnationalparks.utils.AnalyticsUtils;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageGridActivity extends AppCompatActivity {
    private static final String LOG_TAG = ImageGridActivity.class.getSimpleName();

    @BindView(R.id.empty_view)
    @Nullable
    TextView mEmptyView;
    @BindView(R.id.toolbar_top)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    private String STATE_KEY = "list_state";
    private Parcelable mListState;
    private FlickrResponse flickrResponse;
    private Photos photos;
    private List<FlickrPhoto> photoItems = new ArrayList<>();
    private Context context;
    private LinearLayoutManager mLayoutManager;
    private AnalyticsUtils analytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbarTitle.setText(R.string.image_grid_toolbar_title);

        Toolbar toolbar = findViewById(R.id.toolbar_top);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ImageGridFragment imageGridFragment = new ImageGridFragment();
        imageGridFragment.setPhotos(photos);
        imageGridFragment.setFlickrPhoto(photoItems);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.image_grid_container, imageGridFragment)
                .commit();

	    analytics().reportEventFB(getApplicationContext(), getString(R.string.image_activity_analytics));
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

    public AnalyticsUtils analytics() {
        if (analytics == null) analytics = new AnalyticsUtils(this);
        return analytics;
    }
}