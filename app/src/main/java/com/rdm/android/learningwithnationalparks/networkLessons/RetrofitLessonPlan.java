package com.rdm.android.learningwithnationalparks.networkLessons;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitLessonPlan {

    //This call executes the lesson plan search, sorting by subject, and retrieves them from the
    //National Park Service API. Here is what the query looks like:
    //https://developer.nps.gov/api/v1/lessonplans?sort=subject&api_key=(MYAPIKEYHERE)

    @GET("lessonplans?sort=subject&api_key=ykqtU0ep0jqNDHp2TcNfXDzO7pKQMznBZsJk0kEu")
    Call<LessonPlan> getLessonPlanSubject();

    @GET("lessonplans?sort=title&api_key=ykqtU0ep0jqNDHp2TcNfXDzO7pKQMznBZsJk0kEu")
    Call<LessonPlan> getLessonPlanTitle();
}

