package com.rdm.android.learningwithnationalparks.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.TextUtils;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedLessonAdapter extends RecyclerView.Adapter<SavedLessonAdapter.ViewHolder> {
    private static final String LOG_TAG = SavedLessonAdapter.class.getSimpleName();

    private Cursor cursor = null;
    public Datum datum;
    private Context context;
    @BindView(R.id.empty_view)
    @Nullable
    TextView mEmptyView;
    private static final String KEY_LESSON_PLAN = "lesson_plan";

    public SavedLessonAdapter(List<Datum> data, Context context) {
        this.context = context;

        cursor = context.getContentResolver()
                .query(LessonContract.SavedEntry.CONTENT_URI, null, null, null, null);

        setCursor(cursor);
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.saved_lesson_card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        this.cursor.moveToPosition(position);
        holder.bindModel(this.cursor);
    }

    @Override
    public int getItemCount() {
        if (cursor == null) {
            return 0;
        } else {
            return cursor.getCount();
        }
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

        void bindModel(Cursor cursor) {
            savedLessonTitle.setText(cursor.getString(cursor.getColumnIndex
                    (LessonContract.SavedEntry.COLUMN_TITLE)));
            savedLessonObjective.setText(cursor.getString(cursor.getColumnIndex
                    (LessonContract.SavedEntry.COLUMN_OBJECTIVE)));
            savedLessonGradeLevel.setText(cursor.getString(cursor.getColumnIndex
                    (LessonContract.SavedEntry.COLUMN_GRADE_LEVEL)));
            savedLessonSubject.setText(cursor.getString(cursor.getColumnIndex
                    (LessonContract.SavedEntry.COLUMN_SUBJECT)));
            savedLessonDuration.setText(cursor.getString(cursor.getColumnIndex
                    (LessonContract.SavedEntry.COLUMN_DURATION)));
        }


    @Override
        public void onClick(View view) {
            Log.i(LOG_TAG, "onClick in SavedLessonAdapter Called");

            // Start the LessonDetail activity
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
