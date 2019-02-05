package com.rdm.android.learningwithnationalparks.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

public class LessonContract {
    private static final String LOG_TAG = LessonContract.class.getSimpleName();

    public static final String CONTENT_AUTHORITY = "com.rdm.android.learnwithnationalparks";
    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    public static final String PATH_SAVED = "saved";

    private LessonContract() {
    }

    /**
     * Inner class that defines constant values for the saved database table.
     * Each entry in the table represents a single lesson.
     */
    public static final class SavedEntry implements BaseColumns {
        //inside each of the Entry classes in the contract, we create a full URI for the class as a
        //constant called CONTENT_URI. The Uri.withAppendedPath() method appends the BASE_CONTENT_URI
        //which contains the scheme and the content authority) to the path segment.
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_SAVED);

        public static final String TABLE_NAME = "saved";
        public static final String COLUMN_LESSON_ID = BaseColumns._ID;
        public static final String COLUMN_GRADE_LEVEL = "gradeLevel";
        public static final String COLUMN_OBJECTIVE = "questionObjective";
        public static final String COLUMN_SUBJECT = "subject";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DURATION = "duration";
        public static final String COLUMN_URL = "url";

        public static Uri buildSavedLessonUri(Long id) {
            Log.d(LOG_TAG, "buildSavedLessonUri: " + id);
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getLessonCategoryOrIDFromUri(Uri lessonUriWithCategory) {
            Log.d(LOG_TAG, "getLessonCategoryOrIDFromUri: " + lessonUriWithCategory);
            String category = lessonUriWithCategory.getPathSegments().get(1);
            Log.d(LOG_TAG, "getLessonCategoryOrIDFromUri: " + category);
            return category;
        }

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of saved lessons.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SAVED;
        /**
         * The MIME type of the {@link #CONTENT_URI} for a single saved lesson.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SAVED;

    }
}

