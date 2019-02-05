package com.rdm.android.learningwithnationalparks.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import java.util.Arrays;

public class LessonProvider extends ContentProvider {
    public static final String LOG_TAG = LessonProvider.class.getSimpleName();

    //favorite_lesson.lesson_id = ?
    public static final String savedLessonID =
            LessonContract.SavedEntry.COLUMN_LESSON_ID + " = ? ";

    //database helper object
    private LessonDbHelper mDbHelper;

    //Codes for the UriMatcher
    static final int SAVED = 100;
    static final int SAVED_WITH_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.
        sUriMatcher.addURI(LessonContract.CONTENT_AUTHORITY, LessonContract.PATH_SAVED, SAVED);
        sUriMatcher.addURI(LessonContract.CONTENT_AUTHORITY, LessonContract.PATH_SAVED + "/#", SAVED_WITH_ID);
    }

    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        Log.d(LOG_TAG, "onCreate: DB Helper initialized");
        mDbHelper = new LessonDbHelper(getContext());
        return true;
    }

    private Cursor getAllSavedLessons() {

        Log.d(LOG_TAG, "getAllSavedLessons: Returning AllSavedLessons Cursor");
        return mDbHelper.getReadableDatabase().query(
                LessonContract.SavedEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    private Cursor getSavedLessonByID(Uri uri, String[] columns) {
        String selection = savedLessonID;
        //Select all rows that belong to a particular category
        String[] selectionArgs = new String[]{LessonContract.SavedEntry.getLessonCategoryOrIDFromUri(uri)};

        Log.d(LOG_TAG, "getSavedLessonByID: Selection: " + selection + "\n Selection Args: " + Arrays.toString(selectionArgs));

        return mDbHelper.getReadableDatabase().query(
                LessonContract.SavedEntry.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case SAVED:
                // For the SAVED code, query the saved table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the saved table.
                cursor = database.query(LessonContract.SavedEntry.TABLE_NAME, projection,
                        selection, selectionArgs,
                        null, null, sortOrder);
                break;

            case SAVED_WITH_ID:
                // For the SAVED_WITH_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.saved/saved/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = LessonContract.SavedEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                // This will perform a query on the saved table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(LessonContract.SavedEntry.TABLE_NAME, projection,
                        selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        //set notification URI on the cursor, so we know what content URI the cursor was created for
        //If the data at this URI changes, then we know we need to update the cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case SAVED:
                return insertSavedLesson(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert a saved lesson into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertSavedLesson(Uri uri, ContentValues values) {
        Log.d(LOG_TAG, "insert: Started");

        // Check that the grade level is not null
        String gradeLevel = values.getAsString(LessonContract.SavedEntry.COLUMN_GRADE_LEVEL);
        if (gradeLevel == null) {
            throw new IllegalArgumentException("Lesson requires a gradeLevel");
        }
        // Check that the objective is not null
        String objective = values.getAsString(LessonContract.SavedEntry.COLUMN_OBJECTIVE);
        if (objective == null) {
            throw new IllegalArgumentException("Lesson requires an objective");
        }
        // Check that the subject is not null
        String subject = values.getAsString(LessonContract.SavedEntry.COLUMN_SUBJECT);
        if (subject == null) {
            throw new IllegalArgumentException("Lesson requires a subject");
        }
        // Check that the title is not null
        String title = values.getAsString(LessonContract.SavedEntry.COLUMN_TITLE);
        if (title == null) {
            throw new IllegalArgumentException("Lesson requires a title");
        }
        // Check that the duration is not null
        String duration = values.getAsString(LessonContract.SavedEntry.COLUMN_DURATION);
        if (duration == null) {
            throw new IllegalArgumentException("Lesson requires a duration");
        }
        // Check that the duration is not null
        String url = values.getAsString(LessonContract.SavedEntry.COLUMN_URL);
        if (url == null) {
            throw new IllegalArgumentException("Lesson requires a url");
        }
        // Get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        // Insert the new saved lesson with the given values
        long id = database.insert(LessonContract.SavedEntry.TABLE_NAME, null, values);

        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        //Notify all listeners that the saved data has changed, for the saved content URI.
        //uri: content://com.example.android.saved/saved
        getContext().getContentResolver().notifyChange(uri, null);

        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SAVED:
                return updateSaved(uri, contentValues, selection, selectionArgs);
            case SAVED_WITH_ID:
                // For the SAVED_WITH_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = LessonContract.SavedEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateSaved(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Update saved lessons in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more lessons).
     * Return the number of rows that were successfully updated.
     */
    private int updateSaved(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        // If the {@link SavedEntry#COLUMN_GRADE_LEVEL} key is present,
        // check that the grade level value is not null.
        if (values.containsKey(LessonContract.SavedEntry.COLUMN_GRADE_LEVEL)) {
            String gradeLevel = values.getAsString(LessonContract.SavedEntry.COLUMN_GRADE_LEVEL);
            if (gradeLevel == null) {
                throw new IllegalArgumentException("Lesson requires a grade level");
            }
        }
        // If the {@link SavedEntry#COLUMN_OBJECTIVE} key is present,
        // check that the objective value is not null.
        if (values.containsKey(LessonContract.SavedEntry.COLUMN_OBJECTIVE)) {
            String objective = values.getAsString(LessonContract.SavedEntry.COLUMN_OBJECTIVE);
            if (objective == null) {
                throw new IllegalArgumentException("Lesson requires an objective");
            }
        }
        // If the {@link SavedEntry#COLUMN_SUBJECT} key is present,
        // check that the subject value is not null.
        if (values.containsKey(LessonContract.SavedEntry.COLUMN_SUBJECT)) {
            String subject = values.getAsString(LessonContract.SavedEntry.COLUMN_SUBJECT);
            if (subject == null) {
                throw new IllegalArgumentException("Lesson requires a subject");
            }
        }
        // If the {@link SavedEntry#COLUMN_TITLE} key is present,
        // check that the title value is not null.
        if (values.containsKey(LessonContract.SavedEntry.COLUMN_TITLE)) {
            String title = values.getAsString(LessonContract.SavedEntry.COLUMN_TITLE);
            if (title == null) {
                throw new IllegalArgumentException("Lesson requires a title");
            }
        }
        // If the {@link SavedEntry#COLUMN_DURATION} key is present,
        // check that the duration value is not null.
        if (values.containsKey(LessonContract.SavedEntry.COLUMN_DURATION)) {
            String duration = values.getAsString(LessonContract.SavedEntry.COLUMN_DURATION);
            if (duration == null) {
                throw new IllegalArgumentException("Lesson requires a duration");
            }
        }
        // If the {@link SavedEntry#COLUMN_URL} key is present,
        // check that the url value is not null.
        if (values.containsKey(LessonContract.SavedEntry.COLUMN_URL)) {
            String url = values.getAsString(LessonContract.SavedEntry.COLUMN_URL);
            if (url == null) {
                throw new IllegalArgumentException("Lesson requires a url");
            }
        }

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }
        // Get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(LessonContract.SavedEntry.TABLE_NAME,
                values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rowsUpdated;
    }

    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        // Get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        //Notify all listeners that the saved data has changed, for the saved content URI.
        //uri: content://com.example.android.saved/saved
        getContext().getContentResolver().notifyChange(uri, null);

        // Track the number of rows that were deleted
        int rowsDeleted = 0;

        final int match = sUriMatcher.match(uri);

        int deleteRows;

        switch (match) {
            case SAVED:
                // Delete all rows that match the selection and selection args
                deleteRows = database.delete(LessonContract.SavedEntry.TABLE_NAME,
                        selection, selectionArgs);
                break;

            case SAVED_WITH_ID:
                // Delete a single row given by the ID in the URI
                selection = LessonContract.SavedEntry._ID + "=?";

                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                // Delete a single row given by the ID in the URI
                deleteRows = database.delete(LessonContract.SavedEntry.TABLE_NAME,
                        selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (deleteRows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deleteRows;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {

        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SAVED:
                // allows for multiple transactions
                db.beginTransaction();
                // keep track of successful inserts
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(LessonContract.SavedEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);

                return returnCount;
            default:
                return super.bulkInsert(uri, values);

        }
    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SAVED:
                return LessonContract.SavedEntry.CONTENT_LIST_TYPE;
            case SAVED_WITH_ID:
                return LessonContract.SavedEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}

