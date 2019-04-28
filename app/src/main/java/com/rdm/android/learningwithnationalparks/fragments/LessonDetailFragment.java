package com.rdm.android.learningwithnationalparks.fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.rdm.android.learningwithnationalparks.data.LessonContract;
import com.rdm.android.learningwithnationalparks.networkLessons.Datum;
import com.rdm.android.learningwithnationalparks.networkLessons.LessonPlan;
import com.rdm.android.learningwithnationalparks.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.content.Context.WINDOW_SERVICE;

public class LessonDetailFragment extends Fragment implements View.OnClickListener {
    private static final String LOG_TAG = LessonDetailFragment.class.getSimpleName();

    private List<Datum> data = new ArrayList<>();
    private Datum datum;
    private static final String IMPORT = "lesson_detail";
    private static final String KEY_LESSON_PLAN = "lesson_plan";
    private Parcelable mListState;
    private LinearLayoutManager mLayoutManager;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.webview)
    WebView webView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.detail_toolbar)
    Toolbar toolbar;
    @BindView(R.id.coordinator_layout_lesson_plan_detail)
    CoordinatorLayout coordinatorLayout;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LessonDetailFragment() {
    }

    @SuppressLint("SetJavaScriptEnabled")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(LOG_TAG, "LessonDetailFragment onCreateView");

        Bundle data = this.getArguments();
        if (data != null) {
            datum = data.getParcelable(KEY_LESSON_PLAN);
        }

        if (savedInstanceState != null) {
            datum = savedInstanceState.getParcelable(IMPORT);
        }

        View rootView = inflater.inflate(R.layout.fragment_lesson_plan_detail, container, false);
        Unbinder unbinder = ButterKnife.bind(this, rootView);

        // Set the title of the chosen lesson plan and show the Up button in the action bar.
        collapsingToolbar.setTitle(datum.getTitle());
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        webView.setInitialScale(1);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        webView.loadUrl(datum.getUrl());
        Log.i(LOG_TAG, "WebView Url: " + datum.getUrl());

        isLessonSaved();
        fab.setOnClickListener(this);

        return rootView;
    }

    //If user wants to save lesson, he will press the fab, press again for delete from saved
    @Override
    public void onClick(View v) {
        Log.i(LOG_TAG, "onClick: FAB in LessonDetailFragment has been clicked");
        isLessonSaved();
        if (isLessonSaved()) {
            deleteLesson();
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark_border));
        } else {
            saveLesson();
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark_fill));
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(IMPORT, datum);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    // Helper method to assist in determining if the lesson is already in the saved table
    private boolean isLessonSaved() {
        String title = datum.getTitle();

        Long currentLessonId = datum.getId();

        Cursor savedCursor = getActivity().getContentResolver().query(LessonContract.SavedEntry
                .buildSavedLessonUri(currentLessonId), null, null, null, null);


        Log.i(LOG_TAG, "CheckIfLessonSaved method: Lesson Title is " + title);
        assert savedCursor != null;
        if (savedCursor.moveToNext()) {
            Log.d("LessonDetailFragment", "savedLessonPresent: Lesson already saved");
            savedCursor.close();
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark_fill));
            return true;
        } else {
            Log.d("LessonDetailFragment", "savedLessonPresent: Lesson not saved yet");
            savedCursor.close();
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark_border));
            return false;
        }
    }

    public void setLessonPlan(LessonPlan lessonPlan) {
    }

    public void setDatum(Datum datum) {
        this.datum = datum;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void saveLesson() {

        Long id = datum.getId();
        String gradeLevel = datum.getGradeLevel();
        String objective = datum.getQuestionObjective();
        String subject = datum.getSubject();
        String title = datum.getTitle();
        String duration = datum.getDuration();
        String url = datum.getUrl();

        ContentValues contentValues = new ContentValues();

        contentValues.put(LessonContract.SavedEntry.COLUMN_LESSON_ID, id);
        contentValues.put(LessonContract.SavedEntry.COLUMN_GRADE_LEVEL, gradeLevel);
        contentValues.put(LessonContract.SavedEntry.COLUMN_OBJECTIVE, objective);
        contentValues.put(LessonContract.SavedEntry.COLUMN_SUBJECT, subject);
        contentValues.put(LessonContract.SavedEntry.COLUMN_TITLE, title);
        contentValues.put(LessonContract.SavedEntry.COLUMN_DURATION, duration);
        contentValues.put(LessonContract.SavedEntry.COLUMN_URL, url);

        Uri newUri = getActivity().getContentResolver().insert(LessonContract.SavedEntry.CONTENT_URI,
                contentValues);
        Log.i("LessonDetailFragment", "saveLesson method, new row Uri is " + newUri);
        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark_fill));

        // insertion was successful and we can display a snackbar to confirm.
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, R.string.successfully_added, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    private void deleteLesson() {
        Uri mCurrentSavedUri = LessonContract.SavedEntry.CONTENT_URI;

        Long currentLessonId = datum.getId();
        if (mCurrentSavedUri != null) {
            //Call the ContentResolver to delete the lesson at the given content URI.
            //Pass in null for the selection and selection args because the mCurrentSavedUri
            //content URI already identifies the favorite that we want
            int rowsDeleted = getActivity().getContentResolver().delete(LessonContract.SavedEntry
                    .buildSavedLessonUri(currentLessonId), null, null);
            Log.i("LessonDetailFragment", "deleteLesson method: Deleted: " + rowsDeleted);

            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark_border));

            //Show snackbar depending on whether or not the delete was successful
            if (rowsDeleted == 0) {
                //If no rows deleted, then there was an error with the delete.
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, R.string.delete_lesson_failed, Snackbar.LENGTH_SHORT);
                snackbar.show();
            } else {
                //otherwise, the delete was successful and we can confirm
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, R.string.delete_lesson_successful, Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        }
    }
}


