package com.rdm.android.learningwithnationalparks.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.rdm.android.learningwithnationalparks.adapters.Sound;
import com.rdm.android.learningwithnationalparks.adapters.SoundAdapter;
import com.rdm.android.learningwithnationalparks.R;
import java.util.ArrayList;
import java.util.List;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SoundsListFragment extends Fragment {
    private static final String LOG_TAG = SoundsListFragment.class.getSimpleName();

    @BindView(R.id.play_audio)
    @Nullable
    ImageView playAudioImage;
    @BindView(R.id.sound_recycler_view)
    RecyclerView mSoundRecyclerView;

    private Sound sound;
    private List<Sound> sounds = new ArrayList<>();
    private SoundAdapter mSoundAdapter;
    private int mSoundResourceID;
    private int mImageResourceID;
    private int mPlayAudioImageId;
    private String mTitle;
    private Unbinder unbinder;
    private LinearLayoutManager mLayoutManager;
    private static final String LIST_IMPORT = "sound_list";
    private Parcelable mListState;

    public SoundsListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onViewCreated(container, savedInstanceState);

        if (savedInstanceState != null) {
            sounds = savedInstanceState.getParcelableArrayList(LIST_IMPORT);
        }

        View rootView = inflater.inflate(R.layout.fragment_sounds, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        // Create a list of sounds and images: title, description, sound file, image file,
        // boolean value "isVisible" of true
        final ArrayList<Sound> sounds = new ArrayList<Sound>();
        sounds.add(new Sound(getString(R.string.anemone), getString(R.string.anemonedes), R.raw.anemonegeyser,
                R.drawable.geyser, true));
        sounds.add(new Sound(getString(R.string.paintpots), getString(R.string.paintpotdes), R.raw.artistpaintpots,
                R.drawable.paintpots, true));
        sounds.add(new Sound(getString(R.string.eagle), getString(R.string.eagledes), R.raw.baldeagle,
                R.drawable.bird, true));
        sounds.add(new Sound(getString(R.string.bison), getString(R.string.bisondes), R.raw.bison,
                R.drawable.bison, true));
        sounds.add(new Sound(getString(R.string.coyotes), getString(R.string.coyotedes), R.raw.coyotes,
                R.drawable.coyote, true));
        sounds.add(new Sound(getString(R.string.elk), getString(R.string.elkdes),
                R.raw.elk, R.drawable.elk, true));
        sounds.add(new Sound(getString(R.string.grizzly), getString(R.string.grizzlydes),
                R.raw.grizzlybear, R.drawable.bear, true));
        sounds.add(new Sound(getString(R.string.fire), getString(R.string.firedes),
                R.raw.maplefire, R.drawable.maplefire, true));
        sounds.add(new Sound(getString(R.string.faithful), getString(R.string.faithfuldes),
                R.raw.oldfaithful, R.drawable.oldfaithful, true));
        sounds.add(new Sound(getString(R.string.fox), getString(R.string.foxdes),
                R.raw.redfox, R.drawable.redfox, true));
        sounds.add(new Sound(getString(R.string.redsquirrel), getString(R.string.redsquirreldes),
                R.raw.redsquirrel, R.drawable.redsquirrel, true));
        sounds.add(new Sound(getString(R.string.sandhillcranes), getString(R.string.cranedes),
                R.raw.sandhillcranes, R.drawable.sandhillcranes, true));
        sounds.add(new Sound(getString(R.string.toad), getString(R.string.toaddes), R.raw.spadefoottoad,
                R.drawable.frog, true));
        sounds.add(new Sound(getString(R.string.thunder), getString(R.string.thunderdes), R.raw.thunder,
                R.drawable.thunderstorm, true));
        sounds.add(new Sound(getString(R.string.groundsquirrel), getString(R.string.groundsquirreldes),
                R.raw.uintagroundsquirrel, R.drawable.groundsquirrel, true));
        sounds.add(new Sound(getString(R.string.wolves), (getString(R.string.wolfdes)), R.raw.wolves,
                R.drawable.wolves, true));
        sounds.add(new Sound(getString(R.string.lake), getString(R.string.lakedes), R.raw.yellowstonesinginglake,
                R.drawable.singinglake, true));

            mLayoutManager = new LinearLayoutManager(getActivity());
            mLayoutManager.setOrientation(RecyclerView.VERTICAL);
            mSoundRecyclerView.setLayoutManager(mLayoutManager);
            SoundAdapter mSoundAdapter = new SoundAdapter(getActivity(), sound, sounds, mSoundResourceID,
                    mImageResourceID);
            mSoundRecyclerView.setAdapter(mSoundAdapter);

        return rootView;
    }

    public void setSound(Sound sound) {
        this.sound = sound;
    }

    public void setSounds(List<Sound> sounds) {
        this.sounds = sounds;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(LIST_IMPORT, (ArrayList<? extends Parcelable>) sounds);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mSoundAdapter != null) {
            mSoundAdapter.notifyDataSetChanged();
        }
    }
}