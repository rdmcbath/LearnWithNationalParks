package com.rdm.android.learningwithnationalparks.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ProgressBar;
import com.rdm.android.learningwithnationalparks.fragments.SavedLessonDetailFragment;
import com.rdm.android.learningwithnationalparks.networkLessons.Datum;
import com.rdm.android.learningwithnationalparks.networkLessons.LessonPlan;
import com.rdm.android.learningwithnationalparks.R;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedLessonDetailActivity extends AppCompatActivity {
    private static final String LOG_TAG = SavedLessonDetailActivity.class.getSimpleName();

    public LessonPlan lessonPlan;
    public Datum datum;
    public boolean mDualPane;
    private String STATE_KEY = "list_state";
    private Parcelable mListState;
    private LinearLayoutManager mLayoutManager;
    private int position;
    private Context mContext;
    @BindView(R.id.loading_spinner)
    @Nullable
    ProgressBar spinnerProgress;
    public static final String KEY_LESSON_PLAN = "lesson_plan";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_lesson_plan_detail);
        ButterKnife.bind(this);

        Bundle data = getIntent().getExtras();
        datum = data.getParcelable(KEY_LESSON_PLAN);

        Bundle args = new Bundle();
        args.putParcelable(KEY_LESSON_PLAN, datum);

        SavedLessonDetailFragment savedLessonDetailFragment = new SavedLessonDetailFragment();
        savedLessonDetailFragment.setArguments(args);
        savedLessonDetailFragment.setDatum(datum);
        savedLessonDetailFragment.setLessonPlan(lessonPlan);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.saved_lessons_detail_container, savedLessonDetailFragment)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        Log.i(LOG_TAG, "LessonDetailActivity :onSaveInstanceState");
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

