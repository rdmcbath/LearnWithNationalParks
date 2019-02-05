package com.rdm.android.learningwithnationalparks.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LessonDbHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = LessonDbHelper.class.getSimpleName();

    private static LessonDbHelper instance;

    public static synchronized LessonDbHelper getInstance(Context context) {
        if (instance == null)
            instance = new LessonDbHelper(context);
        return instance;
    }

    // Database version - in the event the database schema is changed
    private static final int DATABASE_VERSION = 13;

    public static final String DATABASE_NAME = "lessons.db";

    //Construct a new instance of the {@link LessonDbHelper}
    //@param context of the app

    public LessonDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create a String that contains the SQL statement to create the favorites table
        final String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE " +
                LessonContract.SavedEntry.TABLE_NAME + " (" +
                LessonContract.SavedEntry.COLUMN_LESSON_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LessonContract.SavedEntry.COLUMN_GRADE_LEVEL + " TEXT NOT NULL, " +
                LessonContract.SavedEntry.COLUMN_OBJECTIVE + " TEXT NOT NULL, " +
                LessonContract.SavedEntry.COLUMN_SUBJECT + " TEXT NOT NULL, " +
                LessonContract.SavedEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                LessonContract.SavedEntry.COLUMN_DURATION + " TEXT NOT NULL, " +
                LessonContract.SavedEntry.COLUMN_URL + " TEXT NOT NULL);";

        // Execute the SQL statement
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_TABLE);
        Log.i(LOG_TAG, "SQL Statement String is: " + SQL_CREATE_FAVORITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + LessonContract.SavedEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}