package com.rdm.android.learningwithnationalparks.activities;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import com.rdm.android.learningwithnationalparks.adapters.Sound;
import com.rdm.android.learningwithnationalparks.fragments.SoundsListFragment;
import com.rdm.android.learningwithnationalparks.R;
import com.rdm.android.learningwithnationalparks.utils.AnalyticsUtils;
import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import butterknife.ButterKnife;

public class SoundsListActivity extends AppCompatActivity {
    private static final String LOG_TAG = SoundsListActivity.class.getSimpleName();

    private Sound sound;
    List<Sound> sounds = new ArrayList<>();
    private int audioResourceID;
    //Handles playback of all the sound files
    private MediaPlayer mediaPlayer;
    //Handles audio focus when playing a sound file
    private AudioManager audioManager;
    private LinearLayoutManager mLayoutManager;
    private boolean mDualPane;
    private String STATE_KEY = "list_state";
    private Parcelable mListState;
	private AnalyticsUtils analytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sounds);
        ButterKnife.bind(this);

        if (sounds != null) {

            // Create and setup the {@link AudioManager} to request audio focus
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

            Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(R.string.sounds_toolbar_title);
            ActionBar ab = getSupportActionBar();
            ab.setDisplayHomeAsUpEnabled(true);

            SoundsListFragment soundsListFragment = new SoundsListFragment();
            soundsListFragment.setSounds(sounds);
            soundsListFragment.setSound(sound);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.sounds_list_container, soundsListFragment)
                    .commit();
        }

	    analytics().reportEventFB(getApplicationContext(), getString(R.string.sound_activity_analytics));
    }

    public void handleSoundClick(Sound currentSound) {

        // Release the media player if it currently exists because we are about to
        // play a different sound file
        releaseMediaPlayer();

        // Request audio focus so in order to play the audio file. The app needs to play a
        // short audio file, so we will request audio focus with a short amount of time
        // with AUDIOFOCUS_GAIN_TRANSIENT.
        int result = audioManager.requestAudioFocus(onAudioFocusChangeListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            // We have audio focus now.
            // Create and setup the {@link MediaPlayer} for the audio resource associated
            // with the current sound
            mediaPlayer = MediaPlayer.create(this, currentSound.getAudioResourceId());

            // Start the audio file
            mediaPlayer.start();

            // Setup a listener on the media player, so that we can stop and release the
            // media player once the sound has finished playing.
            mediaPlayer.setOnCompletionListener(mCompletionListener);
        }
    }

    //This listener gets triggered whenever the audio focus changes i.e., we gain or lose audio focus
    // because of another app or device).
    private AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                // The AUDIOFOCUS_LOSS_TRANSIENT case means that we've lost audio focus for a
                // short amount of time. The AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means that
                // our app is allowed to continue playing sound but at a lower volume. We'll treat
                // both cases the same way because our app is playing short sound files.

                // Pause playback and reset player to the start of the file. That way, we can
                // play the word from the beginning when we resume playback.
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                mediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // The AUDIOFOCUS_LOSS case means we've lost audio focus and
                // Stop playback and clean up resources
                releaseMediaPlayer();
            }
        }
    };

    //This listener gets triggered when the {@link MediaPlayer} has completed playing the audio file.
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            // Now that the sound file has finished playing, release the media player resources.
            releaseMediaPlayer();
        }
    };

    //Clean up the media player by releasing its resources.
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mediaPlayer = null;

            // Regardless of whether or not we were granted audio focus, abandon it. This also
            // unregisters the AudioFocusChangeListener so we don't get anymore callbacks.
            audioManager.abandonAudioFocus(onAudioFocusChangeListener);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mLayoutManager = new LinearLayoutManager(this);
        Log.i(LOG_TAG, "SoundsListActivity: onSaveInstanceState");
        super.onSaveInstanceState(outState);
        mListState = mLayoutManager.onSaveInstanceState();
        outState.putParcelable(STATE_KEY, mListState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mLayoutManager = new LinearLayoutManager(this);
        Log.i(LOG_TAG, "SoundsListActivity: onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mListState = savedInstanceState.getParcelable(STATE_KEY);
        }
    }

    @Override
    protected void onResume() {
        mLayoutManager = new LinearLayoutManager(this);
        Log.i(LOG_TAG, "SoundsListActivity: onResume");
        super.onResume();
        if (mListState != null) {
            mLayoutManager.onRestoreInstanceState(mListState);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
        finish();
    }

    @Override
    public void onBackPressed() {
        releaseMediaPlayer();
        finish();
    }

    public AnalyticsUtils analytics() {
        if (analytics == null) analytics = new AnalyticsUtils(this);
        return analytics;
    }
}


