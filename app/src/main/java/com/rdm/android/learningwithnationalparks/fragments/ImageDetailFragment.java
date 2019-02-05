package com.rdm.android.learningwithnationalparks.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.rdm.android.learningwithnationalparks.networkFlickr.Description;
import com.rdm.android.learningwithnationalparks.networkFlickr.FlickrPhoto;
import com.rdm.android.learningwithnationalparks.networkFlickr.Photos;
import com.rdm.android.learningwithnationalparks.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageDetailFragment extends Fragment implements View.OnClickListener {
    private static final String LOG_TAG = ImageDetailFragment.class.getSimpleName();

    private Photos photos;
    private FlickrPhoto flickrPhoto;
    private Description description;
    private List<FlickrPhoto> photoItems = new ArrayList<>();
    private static final String IMAGE_IMPORT = "image_import";
    private Parcelable mListState;
    private LinearLayoutManager mLayoutManager;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.image_description)
    TextView descriptionTextView;
    @BindView(R.id.link_to_flickr_page)
    TextView linkTextView;
    @BindView(R.id.image_detail_photo)
    ImageView imageDetailPhoto;
    @BindView(R.id.fab_share)
    FloatingActionButton fab;
    @BindView(R.id.detail_toolbar)
    Toolbar toolbar;
    @BindView(R.id.coordinator_layout_image_detail)
    CoordinatorLayout coordinatorLayout;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(LOG_TAG, "ImageDetailFragment onCreateView");

        if (savedInstanceState != null) {
            photoItems = savedInstanceState.getParcelableArrayList(IMAGE_IMPORT);
        }

        View rootView = inflater.inflate(R.layout.fragment_image_detail, container, false);
        ButterKnife.bind(this, rootView);

        // Set the title of the chosen image and show the Up button in the action bar.
        collapsingToolbar.setTitle(flickrPhoto.getTitle());
        Log.i(LOG_TAG, "ImageTitle: " + flickrPhoto.getTitle());
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //Set the selected image onto the App Bar space
        String imageUrl = flickrPhoto.getUrlO();

        Glide.with(getActivity())
                .load(imageUrl)
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                .into(imageDetailPhoto);

        // Set the description of the chosen image and remove the html tags within it
        String description = flickrPhoto.getDescription().getContent();
        String newDescription = Html.fromHtml(description).toString();
        descriptionTextView.setText(newDescription);

        linkTextView.setClickable(true);
        String text = getString(R.string.link_to_flickr_page);
        linkTextView.setText(Html.fromHtml(text));

        fab.setOnClickListener(this);

        return rootView;
    }

    public void setFlickrPhoto(FlickrPhoto flickrPhoto) {
        this.flickrPhoto = flickrPhoto;
    }

    public void setPhotos(Photos photos) {
        this.photos = photos;
    }

    @Override
    public void onClick(View view) {
        Log.i(LOG_TAG, "Share FAB button clicked");

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,
                getString(R.string.extra_text_share) + "\n" + flickrPhoto.getTitle() + "\n" + flickrPhoto.getUrlO());
        startActivity(Intent.createChooser(intent, "Share"));
    }
}
