package com.rdm.android.learningwithnationalparks.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rdm.android.learningwithnationalparks.activities.LessonListActivity;
import com.rdm.android.learningwithnationalparks.activities.SavedLessonActivity;
import com.rdm.android.learningwithnationalparks.adapters.LessonPlanAdapter;
import com.rdm.android.learningwithnationalparks.networkLessons.Datum;
import com.rdm.android.learningwithnationalparks.networkLessons.LessonPlan;
import com.rdm.android.learningwithnationalparks.networkLessons.LessonPlanClient;
import com.rdm.android.learningwithnationalparks.networkLessons.RetrofitLessonPlan;
import com.rdm.android.learningwithnationalparks.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LessonListFragment extends Fragment {
    private static final String LOG_TAG = LessonListFragment.class.getSimpleName();
    @BindView(R.id.lesson_plan_recycler)
    RecyclerView mLessonPlanRecyclerView;
    @BindView(R.id.frame_layout)
    FrameLayout frameLayout;
    @BindView(R.id.empty_view)
    @Nullable
    TextView mEmptyView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    boolean mDualPane;
    private Datum datum;
    private static final String LIST_IMPORT = "lesson_list";
    private Parcelable mListState;
    private LinearLayoutManager mLayoutManager;
    private List<Datum> data = new ArrayList<>();
    private LessonPlanAdapter mLessonPlanAdapter;
    public LessonPlan lessonPlan;
    private static final int SORT_ORDER_SUBJECT = 0;
    private static final int SORT_ORDER_TITLE = 1;
    private static String SORT_ORDER_PREFS_KEY;
    private String sort_criteria;
    private String x = "title";
    private Unbinder unbinder;

    public LessonListFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onViewCreated(container, savedInstanceState);
        setHasOptionsMenu(true);

        if (savedInstanceState != null) {
            data = savedInstanceState.getParcelableArrayList(LIST_IMPORT);
        }

        View rootView = inflater.inflate(R.layout.fragment_lesson_plan_list, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        mLessonPlanRecyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        //set default preferences when the activity/fragment starts (order by title)
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        PreferenceManager.setDefaultValues(getContext(), R.xml.settings_lesson_list, true);
        String sortBy = sharedPrefs.getString(getString(R.string.settings_sort_by_default), getString(R.string.lesson_search_action_title));

        if (data != null) {
            mLayoutManager = new LinearLayoutManager(getActivity());
            mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mLessonPlanRecyclerView.setLayoutManager(mLayoutManager);
            mLessonPlanAdapter = new LessonPlanAdapter(lessonPlan, data, getActivity());
            mLessonPlanRecyclerView.setAdapter(mLessonPlanAdapter);

            // Get a reference to the ConnectivityManager to check state of network connectivity

            ConnectivityManager connMgr = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = null;
            if (connMgr != null) {
                networkInfo = connMgr.getActiveNetworkInfo();
            }
            // If there is a network connection, load lesson plan data
            if (networkInfo != null && networkInfo.isConnected()) {

	            int pref = sharedPrefs.getInt(SORT_ORDER_PREFS_KEY, SORT_ORDER_TITLE);

	            if (pref == SORT_ORDER_TITLE) {
	            	loadLessonPlanListByTitle();
		            Snackbar.make(getActivity().getWindow().getDecorView().getRootView(), R.string.sorted_by_title, Snackbar.LENGTH_LONG).show();
		            Log.i(LOG_TAG, "loadLessonPlanList Title Method Called");
	            } else if (pref == SORT_ORDER_SUBJECT) {
		            loadLessonPlanListBySubject();
		            Snackbar.make(getActivity().getWindow().getDecorView().getRootView(), R.string.sorted_by_subject, Snackbar.LENGTH_LONG).show();
		            Log.i(LOG_TAG, "loadLessonPlanList Subject Method Called");
	            }

            } else {
			// no network
	            if(getActivity() != null) {
	            	progressBar.setVisibility(View.GONE);
		            Snackbar.make(getActivity().getWindow().getDecorView().getRootView(), R.string.no_network, Snackbar.LENGTH_LONG).show();
	            }
            }
        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.lesson_search_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        if (id == R.id.action_subject_search) {
            sharedPrefs.edit().putInt(SORT_ORDER_PREFS_KEY, SORT_ORDER_SUBJECT).apply();
            loadLessonPlanListBySubject();
            // Show snackbar to verify sort order by subject
            Snackbar snackbar = Snackbar.make(frameLayout, R.string.sorted_by_subject, Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
        if (id == R.id.action_title_search) {
            sharedPrefs.edit().putInt(SORT_ORDER_PREFS_KEY, SORT_ORDER_TITLE).apply();
            loadLessonPlanListByTitle();
            // Show snackbar to verify sort order by title
            Snackbar snackbar = Snackbar.make(frameLayout, R.string.sorted_by_title, Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
        if (id == R.id.action_saved_search) {
            Intent intent = new Intent(this.getContext(), SavedLessonActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadLessonPlanListByTitle() {
        RetrofitLessonPlan retrofitLessonPlan = LessonPlanClient.getClient().create(RetrofitLessonPlan.class);
        Call<LessonPlan> call = retrofitLessonPlan.getLessonPlanTitle();
        Log.i(LOG_TAG, "RetrofitLessonPlanTitle - GetLessonPlan by Title Called");

        call.enqueue(new Callback<LessonPlan>() {
            @Override
            public void onResponse(Call<LessonPlan> call, Response<LessonPlan> response) {
                if (response.isSuccessful()) {
                    LessonPlan lessonPlan = response.body();
                    Log.i(LOG_TAG, "Response.Body Retrofit Called");
                    mLessonPlanAdapter = new LessonPlanAdapter(lessonPlan, lessonPlan.getData(), getContext());
                    mLessonPlanRecyclerView.setLayoutManager(mLayoutManager);
                    mLessonPlanRecyclerView.setAdapter(mLessonPlanAdapter);
                    mLayoutManager.onRestoreInstanceState(mListState);
                    progressBar.setVisibility(View.GONE);
                    mLessonPlanRecyclerView.setVisibility(View.VISIBLE);
                    mLessonPlanAdapter.notifyDataSetChanged();
                } else {
                    System.out.println(response.errorBody());
                    if (mEmptyView != null) {
                        mEmptyView.setText(R.string.no_lesson_plans_found);
                    }
                }
            }

            @Override
            public void onFailure(Call<LessonPlan> call, Throwable t) {

                t.printStackTrace();
                if (mEmptyView != null) {
                    mEmptyView.setText(R.string.no_network);
                }
            }
        });
    }

    private void loadLessonPlanListBySubject() {
        RetrofitLessonPlan retrofitLessonPlan = LessonPlanClient.getClient().create(RetrofitLessonPlan.class);
        Call<LessonPlan> call = retrofitLessonPlan.getLessonPlanSubject();
        Log.i(LOG_TAG, "RetrofitLessonPlan - GetLessonPlan by Subject Called");

        call.enqueue(new Callback<LessonPlan>() {
            @Override
            public void onResponse(Call<LessonPlan> call, Response<LessonPlan> response) {
                if (response.isSuccessful()) {
                    LessonPlan lessonPlan = response.body();
                    Log.i(LOG_TAG, "Response.Body Retrofit Called");
                    mLessonPlanAdapter = new LessonPlanAdapter(lessonPlan, lessonPlan.getData(), getContext());
                    mLessonPlanRecyclerView.setLayoutManager(mLayoutManager);
                    mLessonPlanRecyclerView.setAdapter(mLessonPlanAdapter);
                    mLayoutManager.onRestoreInstanceState(mListState);
                    progressBar.setVisibility(View.GONE);
                    mLessonPlanRecyclerView.setVisibility(View.VISIBLE);
                    mLessonPlanAdapter.notifyDataSetChanged();
                } else {
                    System.out.println(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<LessonPlan> call, Throwable t) {

                t.printStackTrace();
                if (mEmptyView != null) {
                    mEmptyView.setText(R.string.no_network);
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(LIST_IMPORT, (ArrayList<? extends Parcelable>) data);
    }

    @Override
    public void onResume() {
        super.onResume();
        mLessonPlanAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setDatum(Datum datum) {
        this.datum = datum;
    }

    public void setLessonPlan(LessonPlan lessonPlan) {
        this.lessonPlan = lessonPlan;
    }
}