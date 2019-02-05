package com.rdm.android.learningwithnationalparks.networkLessons;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LessonPlanClient {

    public static final String BASE_URL_NPS = "https://developer.nps.gov/api/v1/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL_NPS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
