package com.rdm.android.learningwithnationalparks.fragments;

import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.rdm.android.learningwithnationalparks.adapters.SavedLessonAdapter;
import com.rdm.android.learningwithnationalparks.data.LessonContract;
import com.rdm.android.learningwithnationalparks.networkLessons.Datum;
import com.rdm.android.learningwithnationalparks.networkLessons.LessonPlan;
import com.rdm.android.learningwithnationalparks.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedLessonFragment extends Fragment implements LoaderManager
        .LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = SavedLessonFragment.class.getSimpleName();

    private Uri mCurrentSavedUri = LessonContract.SavedEntry.CONTENT_URI;
    private static final int SAVED_CURSOR_LOADER_ID = 1;
    private SavedLessonAdapter mCursorAdapter;
    private List<Datum> data = new ArrayList<>();
    public Datum datum;
    public LessonPlan lessonPlan;
    private static final String LIST_IMPORT = "lesson_list";
    private Parcelable mListState;
    private LinearLayoutManager mLayoutManager;
    private SavedLessonAdapter mSavedAdapter;
    @BindView(R.id.saved_recycler)
    RecyclerView mSavedRecyclerView;
    @BindView(R.id.frame_layout)
    FrameLayout frameLayout;
    @BindView(R.id.empty_view)
    @Nullable
    View mEmptyView;

    public SavedLessonFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onViewCreated(container, savedInstanceState);
        setHasOptionsMenu(true);

        if (savedInstanceState != null) {
            data = savedInstanceState.getParcelableArrayList(LIST_IMPORT);
        }

        View rootView = inflater.inflate(R.layout.fragment_saved_lessons, container, false);
        ButterKnife.bind(this, rootView);

        mCursorAdapter = new SavedLessonAdapter(data, getContext());
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mSavedRecyclerView.setLayoutManager(mLayoutManager);
        mSavedRecyclerView.setAdapter(mCursorAdapter);
        mEmptyView.setVisibility(View.GONE);

        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
        getLoaderManager().initLoader(SAVED_CURSOR_LOADER_ID, null, this);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(LIST_IMPORT, (ArrayList<? extends Parcelable>) data);
    }

    /**
     * Helper method to delete all saved lessons in the database.
     */
    private void deleteAllSavedLessons() {

        int rowsDeleted = getActivity().getContentResolver()
                .delete(LessonContract.SavedEntry.CONTENT_URI, null, null);
        Log.i("SavedLessonFragment", rowsDeleted + " rows deleted from saved");

        Snackbar snackbar = Snackbar
                .make(frameLayout, R.string.delete_all_saved_successful, Snackbar.LENGTH_SHORT);
        snackbar.show();

        mCursorAdapter.notifyDataSetChanged();
        getLoaderManager().restartLoader(SAVED_CURSOR_LOADER_ID, null, this);
        mEmptyView.setVisibility(View.VISIBLE);

        if (rowsDeleted == 0) {
            Snackbar snackbar1 = Snackbar
                    .make(frameLayout, R.string.nothing_to_delete, Snackbar.LENGTH_SHORT);
            snackbar1.show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "TEST: ONRESUME");
        getLoaderManager().restartLoader(SAVED_CURSOR_LOADER_ID, null, this);
    }

    // Define a projection that specifies which columns from the database
    // you will actually use after this query.
    final String[] projection = new String[]{
            LessonContract.SavedEntry._ID,
            LessonContract.SavedEntry.COLUMN_GRADE_LEVEL,
            LessonContract.SavedEntry.COLUMN_OBJECTIVE,
            LessonContract.SavedEntry.COLUMN_SUBJECT,
            LessonContract.SavedEntry.COLUMN_TITLE,
            LessonContract.SavedEntry.COLUMN_DURATION,
            LessonContract.SavedEntry.COLUMN_URL
    };

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        Log.i(LOG_TAG, "TEST: OnSTART LOADING");

        // create and return a CursorLoader that will take care of creating a Cursor
        // for the data being displayed.
        return new android.support.v4.content.CursorLoader(getContext(), mCurrentSavedUri,
                projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.i(LOG_TAG, "TEST:OnLOAD FINISHED");

        if (cursor != null && cursor.getCount() > 0) {
            mSavedRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        } else {
            mSavedRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        }
        // Swap the new cursor in. (The framework will take care of closing the old cursor
        // once we return.)
        mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.i(LOG_TAG, "TEST:OnLOAD RESET");
        // This is called when the last Cursor provided to onLoadFinished() above is about to be
        // closed.  We need to make sure we are no longer using it.
        mCursorAdapter.swapCursor(null);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.saved_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Delete" menu option
            case R.id.delete_all_saved_lessons:
                //Pop up confirmation dialog for deletion
                showWarningDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Prompt the user to confirm that they want to delete all saved lessons.
     */
    private void showWarningDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.warning_dialog_msg_all_saved);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Yes" button, so delete all the saved lessons.
                deleteAllSavedLessons();
            }
        });

        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "No" button, so dismiss the dialog
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void setDatum(Datum datum) {
        this.datum = datum;
    }

    public void setLessonPlan(LessonPlan lessonPlan) {
        this.lessonPlan = lessonPlan;
    }
}




