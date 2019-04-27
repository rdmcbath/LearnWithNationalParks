package com.rdm.android.learningwithnationalparks.adapters;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.rdm.android.learningwithnationalparks.activities.SoundsListActivity;
import com.rdm.android.learningwithnationalparks.R;
import java.util.ArrayList;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class SoundAdapter extends RecyclerView.Adapter<SoundAdapter.ViewHolder> {
    private static final String LOG_TAG = SoundAdapter.class.getSimpleName();

    private Sound sound;
    private ArrayList<Sound> sounds = new ArrayList<Sound>();
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private int soundResourceId;
    private int imageResourceId;
    private Context context;
    private boolean isVisible;

    public SoundAdapter(Activity context, Sound sound, ArrayList<Sound> sounds,
                        int soundResourceId, int imageResoureId) {
        this.context = context;
        this.sound = sound;
        this.sounds = sounds;
        this.soundResourceId = soundResourceId;
        this.imageResourceId = imageResoureId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sounds_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SoundAdapter.ViewHolder holder, int position) {

        //get sound
        Sound currentSound = sounds.get(position);

        holder.soundDescription.setText(sounds.get(position).getDescription());
        holder.soundTitle.setText(sounds.get(position).getTitle());
        holder.soundItemImage.setImageResource(sounds.get(position).getImageResourceId());

        //handle visibility state of play audio image overlay
        if (currentSound.getIsVisible()) {
            holder.playAudioImage.setVisibility(VISIBLE);
        } else {
            holder.playAudioImage.setVisibility(GONE);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.sound_description)
        TextView soundDescription;
        @BindView(R.id.sound_item_image)
        ImageView soundItemImage;
        @BindView(R.id.sound_title)
        TextView soundTitle;
        @BindView(R.id.play_audio)
        ImageView playAudioImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View itemView) {
            Log.i(LOG_TAG, "Clicked on position:  " + getAdapterPosition());

            //get sound
            Sound currentSound = sounds.get(getAdapterPosition());

                SoundsListActivity soundsListActivity = (SoundsListActivity) context;
                currentSound.getAudioResourceId();
                soundsListActivity.handleSoundClick(currentSound);

                //handle visibility of play audio overlay image
                if (playAudioImage.getVisibility() == VISIBLE) {
                    playAudioImage.setVisibility(GONE);
                    currentSound.setIsVisible(false);
                }
            }
        }

    @Override
    public int getItemCount() {
        return sounds.size();
    }
}
