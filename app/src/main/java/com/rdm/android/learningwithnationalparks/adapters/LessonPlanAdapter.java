package com.rdm.android.learningwithnationalparks.adapters;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.rdm.android.learningwithnationalparks.activities.LessonListActivity;
import com.rdm.android.learningwithnationalparks.networkLessons.Datum;
import com.rdm.android.learningwithnationalparks.networkLessons.LessonPlan;
import com.rdm.android.learningwithnationalparks.R;
import java.util.List;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LessonPlanAdapter extends RecyclerView.Adapter<LessonPlanAdapter.ViewHolder> {
    private static final String LOG_TAG = LessonPlanAdapter.class.getSimpleName();

    public LessonPlan lessonPlan;
    private LessonListActivity lessonListActivity;
    public List<Datum> data;
    public Datum datum;
    private Context context;
    private static final String KEY_LESSON_PLAN = "lesson_plan";

    public LessonPlanAdapter(LessonPlan lessonPlan, List<Datum> data, Context context) {
        this.lessonPlan = lessonPlan;
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lesson_card_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonPlanAdapter.ViewHolder holder, int position) {
        holder.lessonTitle.setText(data.get(position).getTitle());
        holder.lessonObjective.setText(data.get(position).getQuestionObjective());
        holder.lessonGradeLevel.setText(data.get(position).getGradeLevel());
        holder.lessonSubject.setText(data.get(position).getSubject());
        holder.lessonDuration.setText(data.get(position).getDuration());
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.lesson_title)
        TextView lessonTitle;
        @BindView(R.id.lesson_objective)
        TextView lessonObjective;
        @BindView(R.id.lesson_grade_level)
        TextView lessonGradeLevel;
        @BindView(R.id.lesson_subject)
        TextView lessonSubject;
        @BindView(R.id.lesson_duration)
        TextView lessonDuration;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.i(LOG_TAG, "onClick in LessonPlanAdapter Called");

                LessonListActivity lessonListActivity = (LessonListActivity) context;
                Datum datum = lessonPlan.getData().get(getAdapterPosition());
                Bundle data = new Bundle();
                data.putParcelable(KEY_LESSON_PLAN, datum);
                lessonListActivity.handleClickLessonDetail(datum);

                Fragment fragmentGet = new Fragment();
                Bundle args = new Bundle();
                args.putParcelable(KEY_LESSON_PLAN, datum);
                fragmentGet.setArguments(args);
            }
        }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
