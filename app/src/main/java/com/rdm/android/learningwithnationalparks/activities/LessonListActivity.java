package com.rdm.android.learningwithnationalparks.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rdm.android.learningwithnationalparks.fragments.LessonDetailFragment;
import com.rdm.android.learningwithnationalparks.fragments.LessonListFragment;
import com.rdm.android.learningwithnationalparks.networkLessons.Datum;
import com.rdm.android.learningwithnationalparks.networkLessons.LessonPlan;
import com.rdm.android.learningwithnationalparks.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LessonListActivity extends AppCompatActivity {
    private static final String LOG_TAG = LessonListActivity.class.getSimpleName();
    @BindView(R.id.empty_view)
    @Nullable
    TextView mEmptyView;
    @BindView(R.id.loading_spinner)
    @Nullable
    ProgressBar spinnerProgress;
    public boolean mDualPane;
    private String STATE_KEY = "list_state";
    public static final String KEY_LESSON_PLAN = "lesson_plan";
    private Parcelable mListState;
    private LinearLayoutManager mLayoutManager;
    public LessonPlan lessonPlan;
    public List<Datum> data = new ArrayList<>();
    public Datum datum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lesson_list);
        ButterKnife.bind(this);

        LessonListFragment lessonListFragment = new LessonListFragment();
        lessonListFragment.setLessonPlan(lessonPlan);
        lessonListFragment.setDatum(datum);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.lesson_list_container, lessonListFragment)
                .commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.lesson_list_toolbar_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void handleClickLessonDetail(Datum datum) {
        mDualPane = getResources().getBoolean(R.bool.is_tablet);

        if (mDualPane) {
            //we are in dualPane mode (Tablet) so replace the intro layout w/ LessonPlanDetail once
            //a lesson plan is clicked from the left side. Attach object bundle to
            // Fragment as Argument
            getSupportActionBar().hide();
            Bundle args = new Bundle();
            args.putParcelable(KEY_LESSON_PLAN, datum);

            LessonDetailFragment lessonDetailFragment = new LessonDetailFragment();
            lessonDetailFragment.setArguments(args);
            lessonDetailFragment.setLessonPlan(lessonPlan);
            lessonDetailFragment.setDatum(datum);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_container, lessonDetailFragment)
                    .commit();

        } else {
            //we are in single pane mode (phone), so start the LessonDetailActivity
            Intent lessonDetailIntent = new Intent(getBaseContext(), LessonDetailActivity.class);
            lessonDetailIntent.putExtra(KEY_LESSON_PLAN, datum);
            startActivity(lessonDetailIntent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        mLayoutManager = new LinearLayoutManager(this);
        Log.i(LOG_TAG, "LessonListActivity :onSaveInstanceState");
        super.onSaveInstanceState(state);
        mListState = mLayoutManager.onSaveInstanceState();
        state.putParcelable(STATE_KEY, mListState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        mLayoutManager = new LinearLayoutManager(this);
        Log.i(LOG_TAG, "LessonListActivity :onRestoreInstanceState");
        super.onRestoreInstanceState(state);
        if (state != null) {
            mListState = state.getParcelable(STATE_KEY);
        }
    }
}


