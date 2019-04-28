package com.rdm.android.learningwithnationalparks.activities;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ProgressBar;
import com.rdm.android.learningwithnationalparks.fragments.LessonDetailFragment;
import com.rdm.android.learningwithnationalparks.networkLessons.Datum;
import com.rdm.android.learningwithnationalparks.networkLessons.LessonPlan;
import com.rdm.android.learningwithnationalparks.R;
import com.rdm.android.learningwithnationalparks.utils.AnalyticsUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An activity representing a single Item detail screen. This activity is only used with narrow
 * width devices (phones). On tablet-size devices, item details are presented side-by-side with
 * a list of items in a {@link LessonListActivity}.
 */
public class LessonDetailActivity extends AppCompatActivity {
    private static final String LOG_TAG = LessonDetailActivity.class.getSimpleName();

    private LessonPlan lessonPlan;
    private Datum datum;
    private String STATE_KEY = "list_state";
    private Parcelable mListState;
    private LinearLayoutManager mLayoutManager;
    @BindView(R.id.loading_spinner)
    @Nullable
    ProgressBar spinnerProgress;
    private static final String KEY_LESSON_PLAN = "lesson_plan";
    private AnalyticsUtils analytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_plan_detail);
        ButterKnife.bind(this);

        Bundle data = getIntent().getExtras();
        if (data != null) {
            datum = data.getParcelable(KEY_LESSON_PLAN);
        }

        Bundle args = new Bundle();
        args.putParcelable(KEY_LESSON_PLAN, datum);

        LessonDetailFragment lessonDetailFragment = new LessonDetailFragment();
        lessonDetailFragment.setArguments(args);
        lessonDetailFragment.setDatum(datum);
        lessonDetailFragment.setLessonPlan(lessonPlan);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.lesson_detail_container, lessonDetailFragment)
                .commit();

        analytics().reportEventFB(getApplicationContext(), getString(R.string.lesson_activity_analytics));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle state) {
        Log.i(LOG_TAG, "LessonDetailActivity :onSaveInstanceState");
        mLayoutManager = new LinearLayoutManager(this);
        super.onSaveInstanceState(state);
        mListState = mLayoutManager.onSaveInstanceState();
        state.putParcelable(STATE_KEY, mListState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        Log.i(LOG_TAG, "LessonDetailActivity :onRestoreInstanceState");
        mLayoutManager = new LinearLayoutManager(this);
        super.onRestoreInstanceState(state);
        if (state != null) {
            mListState = state.getParcelable(STATE_KEY);
        }
    }

    @Override
    protected void onResume() {
        Log.i(LOG_TAG, "LessonDetailActivity :onResumeInstanceState");
        super.onResume();
        if (mListState != null) {
            mLayoutManager.onRestoreInstanceState(mListState);
        }
    }

    public AnalyticsUtils analytics() {
        if (analytics == null) analytics = new AnalyticsUtils(this);
        return analytics;
    }
}

