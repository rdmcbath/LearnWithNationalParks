package com.rdm.android.learningwithnationalparks.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.rdm.android.learningwithnationalparks.fragments.LessonListFragment;

/**
 * Created by Rebecca McBath
 * on 4/2/19.
 */
public class AnalyticsUtils {

	private static final String LOG_TAG = LessonListFragment.class.getSimpleName();
	private FirebaseAnalytics mFirebaseAnalytics;
	private Context mContext;

	private static final String FIREBASE_USER_PROPERTY_EVENT = "event";

	public AnalyticsUtils(@NonNull final Context context) {
		mContext = context;
		this.mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
	}

	/**
	 * Firebase Analytics:
	 * Use setScreenName in onResume of each fragment or activity so that
	 * events on that page will have screen name set.
	 * If you don't set name then the activity class name will show.
	 *
	 * @param activity
	 * @param screenName
	 */
	public void setScreenName(@NonNull final Activity activity, final String screenName) {
		Log.d(LOG_TAG, "Firebase Analytics: setScreenName() /screenName = " + screenName);
		// TODO: 1/25/19 Improve the screen names to match what the pages are really called
		if (activity == null) return;
		if (this.mFirebaseAnalytics == null) {
			this.mFirebaseAnalytics = FirebaseAnalytics.getInstance(activity.getApplicationContext());
		}
		this.mFirebaseAnalytics.setCurrentScreen(activity, screenName, null);
	}

	public void reportEventFB(@NonNull final Context applicationContext, String category) {
		reportEventFB(applicationContext, category, null, null, null);
	}

	public void reportEventFB(@NonNull final Context applicationContext, String category, String action) {
		reportEventFB(applicationContext, category, action, null, null);
	}

	public void reportEventFB(@NonNull final Context applicationContext, String category, String action, String label) {
		reportEventFB(applicationContext, category, action, label, null);
	}

	public void reportEventFB(final Context applicationContext,
	                          @Nullable final String category,
	                          @Nullable final String action,
	                          @Nullable final String label,
	                          @Nullable final Long value) {

		Log.d(LOG_TAG, "reportEvent() /category=" + category + " /action=" + action + " /label=" + label + " /value=" + value);

		if (this.mFirebaseAnalytics == null) {
			this.mFirebaseAnalytics = FirebaseAnalytics.getInstance(applicationContext);
		}

		Bundle bundle = null;
		if (category != null || action != null || label != null || value != null) {
			bundle = new Bundle();
			if (category != null) bundle.putString("category", category);
			if (action != null) bundle.putString("action", action);
			if (label != null) bundle.putString("label", label);
			if (value != null) bundle.putLong("value", value);
		}
		this.mFirebaseAnalytics.logEvent(FIREBASE_USER_PROPERTY_EVENT, bundle);
	}
}
