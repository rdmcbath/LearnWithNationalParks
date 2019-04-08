package com.rdm.android.learningwithnationalparks.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rdm.android.learningwithnationalparks.adapters.ImageAdapter;
import com.rdm.android.learningwithnationalparks.networkFlickr.Description;
import com.rdm.android.learningwithnationalparks.networkFlickr.FlickrClient;
import com.rdm.android.learningwithnationalparks.networkFlickr.FlickrPhoto;
import com.rdm.android.learningwithnationalparks.networkFlickr.FlickrResponse;
import com.rdm.android.learningwithnationalparks.networkFlickr.Photos;
import com.rdm.android.learningwithnationalparks.networkFlickr.RetrofitFlickr;
import com.rdm.android.learningwithnationalparks.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.rdm.android.learningwithnationalparks.R.drawable.custom_progress_spinner;

public class ImageGridFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
	private static final String LOG_TAG = ImageGridFragment.class.getSimpleName();

	@BindView(R.id.image_grid_recycler)
	RecyclerView mImageGridRecyclerView;
	@BindView(R.id.images_frame_layout)
	FrameLayout frameLayout;
	@BindView(R.id.progress_bar)
	ProgressBar progressBar;
	@BindView(R.id.empty_view)
	@Nullable
	TextView mEmptyView;
	@BindView(R.id.swipe_refresh_layout)
	SwipeRefreshLayout refreshLayout;

	private List<FlickrPhoto> photoItems = new ArrayList<>();
	private FlickrResponse flickrResponse;
	private Description description;
	private Context context;
	private static final String LIST_IMPORT = "image_grid";
	private Parcelable mListState;
	private LinearLayoutManager mLayoutManager;
	private ImageAdapter mImageAdapter;
	private View rootView;

	public ImageGridFragment() {
	}

	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onViewCreated(container, savedInstanceState);
		setHasOptionsMenu(true);

		if (savedInstanceState != null) {
			photoItems = savedInstanceState.getParcelableArrayList(LIST_IMPORT);
		}
		rootView = inflater.inflate(R.layout.fragment_images, container, false);
		ButterKnife.bind(this, rootView);
		mImageGridRecyclerView.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);

		refreshLayout.setOnRefreshListener(this);
		refreshLayout.setColorSchemeResources(R.color.category_images);

		boolean mDualPane = getResources().getBoolean(R.bool.is_tablet);

		if (mDualPane) {
			mLayoutManager = new GridLayoutManager(getContext(), 4);
		} else {
			mLayoutManager = new GridLayoutManager(getContext(), 2);
		}
		mImageGridRecyclerView.setLayoutManager(mLayoutManager);
		mImageAdapter = new ImageAdapter(photoItems, description, getActivity());
		mImageGridRecyclerView.setAdapter(mImageAdapter);

		// Get a reference to the ConnectivityManager to check state of network connectivity
		ConnectivityManager connMgr = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = null;
		if (connMgr != null) {
			networkInfo = connMgr.getActiveNetworkInfo();
		}
		// If there is a network connection, load lesson plan data
		if (networkInfo != null && networkInfo.isConnected()) {

			loadImageGrid();
			Log.d(LOG_TAG, "loadImageGrid Method Called");

		} else {
			progressBar.setVisibility(View.GONE);
			Snackbar snackbar = Snackbar
					.make(frameLayout, R.string.no_network, Snackbar.LENGTH_SHORT);
			snackbar.show();
		}
		return rootView;
	}

	private void loadImageGrid() {
		RetrofitFlickr retrofitFlickr = FlickrClient.getClient().create(RetrofitFlickr.class);
		Call<FlickrResponse> call = retrofitFlickr.getParkPlacesGroupPhotostream();
		Log.d(LOG_TAG, "RetrofitFlickr - GetPhotostream Called");

		call.enqueue(new Callback<FlickrResponse>() {

			@Override
			public void onResponse(Call<FlickrResponse> call, Response<FlickrResponse> response) {

				if (response.isSuccessful()) {
					FlickrResponse flickrResponse = response.body();
					photoItems = flickrResponse.getPhotos().getPhotoItems();
					Log.d(LOG_TAG, "Response.Body Retrofit Called");
					mImageGridRecyclerView = rootView.findViewById(R.id.image_grid_recycler);
					progressBar = rootView.findViewById(R.id.progress_bar);
					mImageAdapter = new ImageAdapter(photoItems, description, getContext());
					mImageGridRecyclerView.setAdapter(mImageAdapter);
					mLayoutManager.onRestoreInstanceState(mListState);
					progressBar.setVisibility(View.GONE);
					mImageGridRecyclerView.setVisibility(View.VISIBLE);
					mImageAdapter.notifyDataSetChanged();

				} else {
					System.out.println(response.errorBody());
					if (mEmptyView != null) {
						mEmptyView.setText(R.string.no_images_found);
					}
				}
			}

			@Override
			public void onFailure(Call<FlickrResponse> call, Throwable t) {
				t.printStackTrace();
				if (mEmptyView != null) {
					mEmptyView.setText(R.string.no_network);
				}
			}
		});
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelableArrayList(LIST_IMPORT, (ArrayList<? extends Parcelable>) photoItems);
	}

	@Override
	public void onResume() {
		super.onResume();
		mImageAdapter.notifyDataSetChanged();
	}

	@Override
	public void onRefresh() {
		loadImageGrid();
		refreshDone();
	}

	private void refreshDone() {
		refreshLayout.setRefreshing(false);
	}

	public void setFlickrPhoto(FlickrPhoto flickrPhoto) {
	}

	public void setPhotos(Photos photos) {
	}

	public void setFlickrPhoto(List<FlickrPhoto> photoItems) {
		this.photoItems = photoItems;
	}
}
