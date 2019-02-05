package com.rdm.android.learningwithnationalparks.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rdm.android.learningwithnationalparks.activities.SavedLessonDetailActivity;
import com.rdm.android.learningwithnationalparks.data.LessonContract;
import com.rdm.android.learningwithnationalparks.networkLessons.Datum;
import com.rdm.android.learningwithnationalparks.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedLessonAdapter extends RecyclerViewCursorAdapter<SavedLessonAdapter.ViewHolder> {
    private static final String LOG_TAG = SavedLessonAdapter.class.getSimpleName();

    public Datum datum;
    private Context context;
    @BindView(R.id.empty_view)
    @Nullable
    TextView mEmptyView;
    public static final String KEY_LESSON_PLAN = "lesson_plan";

    public SavedLessonAdapter(List<Datum> data, Context context) {
        super(null);
        this.context = context;

        Cursor cursor = context.getContentResolver()
                .query(LessonContract.SavedEntry.CONTENT_URI, null, null, null, null);

        swapCursor(cursor);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.saved_lesson_card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, Cursor cursor) {

        holder.savedLessonTitle.setText(cursor.getString(cursor.getColumnIndex
                (LessonContract.SavedEntry.COLUMN_TITLE)));
        holder.savedLessonObjective.setText(cursor.getString(cursor.getColumnIndex
                (LessonContract.SavedEntry.COLUMN_OBJECTIVE)));
        holder.savedLessonGradeLevel.setText(cursor.getString(cursor.getColumnIndex
                (LessonContract.SavedEntry.COLUMN_GRADE_LEVEL)));
        holder.savedLessonSubject.setText(cursor.getString(cursor.getColumnIndex
                (LessonContract.SavedEntry.COLUMN_SUBJECT)));
        holder.savedLessonDuration.setText(cursor.getString(cursor.getColumnIndex
                (LessonContract.SavedEntry.COLUMN_DURATION)));
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.saved_lesson_title)
        TextView savedLessonTitle;
        @BindView(R.id.saved_lesson_objective)
        TextView savedLessonObjective;
        @BindView(R.id.saved_grade_level)
        TextView savedLessonGradeLevel;
        @BindView(R.id.saved_lesson_subject)
        TextView savedLessonSubject;
        @BindView(R.id.saved_lesson_duration)
        TextView savedLessonDuration;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.i(LOG_TAG, "onClick in SavedLessonAdapter Called");

            // Start the LessonDetail activity
            Cursor cursor = getCursor();
            cursor.moveToPosition(getAdapterPosition());
            String title = cursor.getString(cursor.getColumnIndex
                    (LessonContract.SavedEntry.COLUMN_TITLE));
            Long id = cursor.getLong(cursor.getColumnIndex
                    (LessonContract.SavedEntry.COLUMN_LESSON_ID));
            String gradeLevel = cursor.getString(cursor.getColumnIndex
                    (LessonContract.SavedEntry.COLUMN_GRADE_LEVEL));
            String questionObjective = cursor.getString(cursor.getColumnIndex
                    (LessonContract.SavedEntry.COLUMN_OBJECTIVE));
            String subject = cursor.getString(cursor.getColumnIndex
                    (LessonContract.SavedEntry.COLUMN_SUBJECT));
            String duration = cursor.getString(cursor.getColumnIndex
                    (LessonContract.SavedEntry.COLUMN_DURATION));
            String url = cursor.getString(cursor.getColumnIndex
                    (LessonContract.SavedEntry.COLUMN_URL));

            Datum currentSavedLesson = new Datum
                    (gradeLevel, questionObjective, subject, title, id, duration, url);
            Intent lessonDetailIntent = new Intent(context, SavedLessonDetailActivity.class);
            lessonDetailIntent.putExtra(KEY_LESSON_PLAN, currentSavedLesson);
            context.startActivity(lessonDetailIntent);
        }
    }
}
