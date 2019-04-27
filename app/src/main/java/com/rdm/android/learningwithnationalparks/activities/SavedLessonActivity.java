package com.rdm.android.learningwithnationalparks.activities;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;
import com.rdm.android.learningwithnationalparks.fragments.SavedLessonFragment;
import com.rdm.android.learningwithnationalparks.networkLessons.Datum;
import com.rdm.android.learningwithnationalparks.networkLessons.LessonPlan;
import com.rdm.android.learningwithnationalparks.R;
import java.util.List;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedLessonActivity extends AppCompatActivity {
    private static final String LOG_TAG = SavedLessonActivity.class.getSimpleName();

    @BindView(R.id.empty_view)
    @Nullable
    TextView mEmptyView;
    public boolean mDualPane;
    private String STATE_KEY = "list_state";
    private Parcelable mListState;
    private LinearLayoutManager mLayoutManager;
    public LessonPlan lessonPlan;
    public List<Datum> data;
    public Datum datum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_lessons);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.saved_lesson_toolbar_title);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        SavedLessonFragment savedLessonFragment = new SavedLessonFragment();
        savedLessonFragment.setLessonPlan(lessonPlan);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.saved_lesson_list_container, savedLessonFragment)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        mLayoutManager = new LinearLayoutManager(this);
        Log.i(LOG_TAG, "SavedLessonActivity :onSaveInstanceState");
        super.onSaveInstanceState(state);
        mListState = mLayoutManager.onSaveInstanceState();
        state.putParcelable(STATE_KEY, mListState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        mLayoutManager = new LinearLayoutManager(this);
        Log.i(LOG_TAG, "SavedLessonActivity :onRestoreInstanceState");
        super.onRestoreInstanceState(state);
        if (state != null) {
            mListState = state.getParcelable(STATE_KEY);
        }
    }

    @Override
    protected void onResume() {
        mLayoutManager = new LinearLayoutManager(this);
        Log.i(LOG_TAG, "SavedLessonActivity :onResumeInstanceState");
        super.onResume();
        if (mListState != null) {
            mLayoutManager.onRestoreInstanceState(mListState);
        }
    }
}

