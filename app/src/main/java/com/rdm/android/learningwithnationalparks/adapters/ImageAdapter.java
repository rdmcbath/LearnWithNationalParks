package com.rdm.android.learningwithnationalparks.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.rdm.android.learningwithnationalparks.activities.ImageDetailActivity;
import com.rdm.android.learningwithnationalparks.networkFlickr.Description;
import com.rdm.android.learningwithnationalparks.networkFlickr.FlickrPhoto;
import com.rdm.android.learningwithnationalparks.networkFlickr.FlickrResponse;
import com.rdm.android.learningwithnationalparks.networkFlickr.Photos;
import com.rdm.android.learningwithnationalparks.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private static final String LOG_TAG = ImageAdapter.class.getSimpleName();

    private FlickrResponse flickrResponse;
    private Photos photos;
    private List<FlickrPhoto> photoItems = new ArrayList<>();
    private Context context;

    public ImageAdapter(List<FlickrPhoto> photoItems, Description description, Context context) {
        this.photoItems = photoItems;
        Description description1 = description;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_grid_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ViewHolder holder, int position) {

        String imageUrl = photoItems.get(position).getUrlO();

        Glide.with(context)
                .load(imageUrl)
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                .into(holder.flickrImage);

        holder.cardTitleTextView.setText(photoItems.get(position).getTitle());
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.flickr_image)
        ImageView flickrImage;
        @BindView(R.id.card_title)
        TextView cardTitleTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.i(LOG_TAG, "onClick in ImageAdapter Called");
            // Start the ImageDetail activity
            FlickrPhoto data = photoItems.get(getAdapterPosition());
            Intent imageDetailIntent = new Intent(context, ImageDetailActivity.class);
            imageDetailIntent.putExtra("IMAGE", data);
            context.startActivity(imageDetailIntent);
        }
    }

    @Override
    public int getItemCount() {
        return photoItems.size();
    }
}
