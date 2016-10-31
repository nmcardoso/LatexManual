package com.github.nmcardoso.latexmanual;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Pair;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database version
    private static final int DATABASE_VERSION = 1;

    // Database name
    private static final String DATABASE_NAME = "latex_manual";

    // Table names
    private static final String TABLE_DOCUMENTATIONS = "documentations";
    private static final String TABLE_HISTORY = "history";
    private static final String TABLE_FAVORITES = "favorites";

    // Documentations columns
    public static final String DOCUMENTATIONS_ID = "_id";
    public static final String DOCUMENTATIONS_FILE_NAME = "file_name";
    public static final String DOCUMENTATIONS_TITLE = "title";
    public static final String DOCUMENTATIONS_DATA = "data";

    // History columns
    private static final String HISTORY_ID = "_id";
    private static final String HISTORY_CREATED_AT = "created_at";
    private static final String HISTORY_DOC_ID = "documentations_id";

    // Favorites columns
    private static final String FAVORITES_ID = "_id";
    private static final String FAVORITES_CREATED_AT = "created_at";
    private static final String FAVORITES_DOC_ID = "documentations_id";

    // Create table DOCUMENTATIONS statement
    private static final String CREATE_DOCUMENTATIONS = "CREATE TABLE "
            + TABLE_DOCUMENTATIONS + " ("
            + DOCUMENTATIONS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DOCUMENTATIONS_FILE_NAME + " TEXT NOT NULL, "
            + DOCUMENTATIONS_TITLE + " TEXT NOT NULL, "
            + DOCUMENTATIONS_DATA + " TEXT NOT NULL" + ")";

    // Create table HISTORY statement
    private static final String CREATE_HISTORY = "CREATE TABLE "
            + TABLE_HISTORY + " ("
            + HISTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + HISTORY_CREATED_AT + " TEXT, "
            + HISTORY_DOC_ID + " INTEGER NOT NULL" + ")";

    // Create table FAVORITES statement
    private static final String CREATE_FAVORITES = "CREATE TABLE "
            + TABLE_FAVORITES + " ("
            + FAVORITES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + FAVORITES_CREATED_AT + " TEXT, "
            + FAVORITES_DOC_ID + " INTEGER NOT NULL" + ")";

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ssZ";

    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCUMENTATIONS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        sqLiteDatabase.execSQL(CREATE_DOCUMENTATIONS);
        sqLiteDatabase.execSQL(CREATE_HISTORY);
        sqLiteDatabase.execSQL(CREATE_FAVORITES);
        insertDocumentations(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    private void insertDocumentations(SQLiteDatabase db) {
        try {
            InputStream inputStream = context.getResources().openRawResource(R.raw.data_sql);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String query = "";

                while ((query = bufferedReader.readLine()) != null) {
                    db.execSQL(query);
                }

                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
    }

    /**
     * This method search all documentations that match with query parameter
     * in title or content field.
     * @param query Search string
     * @param limit The max number of register
     * @return A {@link List} of {@link Documentation} if the search result >= 1,
     * or an empty {@link List} otherwise.
     */
    public List<Documentation> search(String query, int limit) {
        List<Documentation> docList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        final String sqlQuery = "SELECT d.*, 1 AS priority "
                + " FROM " + TABLE_DOCUMENTATIONS + " AS d "
                + " WHERE d." + DOCUMENTATIONS_TITLE + " LIKE ? "
                + " UNION "
                + " SELECT d.*, 2 AS priority "
                + " FROM " + TABLE_DOCUMENTATIONS + " AS d "
                + " WHERE d." + DOCUMENTATIONS_ID + " NOT IN "
                + "     (SELECT " + DOCUMENTATIONS_ID
                + "     FROM " + TABLE_DOCUMENTATIONS
                + "     WHERE " + DOCUMENTATIONS_TITLE + " LIKE ?)"
                + " AND d." + DOCUMENTATIONS_DATA + " LIKE ?"
                + " ORDER BY priority ASC"
                + " LIMIT ?";

        query =  "%" + query.replace(" ", "%") + "%";

        Cursor cursor = db.rawQuery(sqlQuery, new String[] {query, query, query,
                String.valueOf(limit)});

        if (cursor != null && !cursor.isClosed()) {
            if (cursor.moveToFirst()) {
                do {
                    Documentation doc = new Documentation();
                    doc.setId(cursor.getInt(cursor.getColumnIndex(DOCUMENTATIONS_ID)));
                    doc.setTitle(cursor.getString(cursor.getColumnIndex(DOCUMENTATIONS_TITLE)));
                    doc.setFileName(cursor.getString(cursor.getColumnIndex(DOCUMENTATIONS_FILE_NAME)));
                    doc.setData(cursor.getString(cursor.getColumnIndex(DOCUMENTATIONS_DATA)));
                    docList.add(doc);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        return docList;
    }

    public List<Documentation> searchWithChar(String startWith, String query, int limit, int offset) {
        List<Documentation> docList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        final String sqlQuery = "SELECT d.*, 1 AS priority "
                + " FROM " + TABLE_DOCUMENTATIONS + " AS d "
                + " WHERE d." + DOCUMENTATIONS_TITLE + " LIKE ? "
                + " UNION "
                + " SELECT d.*, 2 AS priority "
                + " FROM " + TABLE_DOCUMENTATIONS + " AS d "
                + " WHERE d." + DOCUMENTATIONS_ID + " NOT IN "
                + "     (SELECT " + DOCUMENTATIONS_ID
                + "     FROM " + TABLE_DOCUMENTATIONS
                + "     WHERE " + DOCUMENTATIONS_TITLE + " LIKE ?)"
                + " AND d." + DOCUMENTATIONS_DATA + " LIKE ?"
                + " ORDER BY priority ASC"
                + " LIMIT ?";

        query =  "%" + query.replace(" ", "%") + "%";

        Cursor cursor = db.rawQuery(sqlQuery, new String[] {query, query, query,
                String.valueOf(limit)});

        if (cursor != null && !cursor.isClosed()) {
            if (cursor.moveToFirst()) {
                do {
                    Documentation doc = new Documentation();
                    doc.setId(cursor.getInt(cursor.getColumnIndex(DOCUMENTATIONS_ID)));
                    doc.setTitle(cursor.getString(cursor.getColumnIndex(DOCUMENTATIONS_TITLE)));
                    doc.setFileName(cursor.getString(cursor.getColumnIndex(DOCUMENTATIONS_FILE_NAME)));
                    doc.setData(cursor.getString(cursor.getColumnIndex(DOCUMENTATIONS_DATA)));
                    docList.add(doc);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        return docList;
    }

    /**
     * Fetches all documentations in the database such that the title starts with
     * the specified character.
     * @param firstChar initial character in the title of documentation
     * @param limit max results returned
     * @param offset point to start search
     * @return A {@link List} of {@link Documentation} if documentations has been found
     * or a empty {@link List} otherwise.
     * @see #getAllFirstChars()
     */
    public List<Documentation> getDocsByFirstChar(String firstChar, int limit, int offset) {
        List<Documentation> docList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        final String query = "SELECT * "
                + " FROM " + TABLE_DOCUMENTATIONS
                + " WHERE " + DOCUMENTATIONS_TITLE + " LIKE ?%"
                + " ORDER BY " + DOCUMENTATIONS_TITLE
                + " LIMIT ? "
                + " OFFSET ? ";

        Cursor cursor = db.rawQuery(query, new String[] { firstChar, String.valueOf(limit),
                String.valueOf(offset) });

        if (cursor != null && !cursor.isClosed()) {
            if (cursor.moveToFirst()) {
                do {

                } while (cursor.moveToNext());
            }
        }

        return docList;
    }

    /**
     * Fetch all rows of documentation table and maps the first character
     * of all title fields.
     * @return A {@link List} of {@link String} with the 1st chars,
     * or an empty {@link List} otherwise.
     */
    public List<String> getAllFirstChars() {
        SQLiteDatabase db = getReadableDatabase();
        List<String> charList = new ArrayList<>();

        final String query = "SELECT substr(" + DOCUMENTATIONS_TITLE + ", 1) AS ch"
                + " FROM " + TABLE_DOCUMENTATIONS
                + " GROUP BY ch "
                + " ORDER BY ch ASC";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && !cursor.isClosed()) {
            if (cursor.moveToFirst()) {
                do {
                    charList.add(cursor.getString(cursor.getColumnIndex("ch")));
                } while (cursor.moveToNext());
            }

            cursor.close();
        }

        return charList;
    }

    /**
     * Returns the primary key of documentation that file name match with the parameter.
     * @param fileName The file name of documentation
     * @return The id (primary key) of the documentation.
     */
    public int getDocumentationId(String fileName) {
        SQLiteDatabase db = getReadableDatabase();
        int id = -1;

        final String query = "SELECT " + DOCUMENTATIONS_ID
                + " FROM " + TABLE_DOCUMENTATIONS
                + " WHERE " + DOCUMENTATIONS_FILE_NAME + " = ?";

        Cursor cursor = db.rawQuery(query, new String[] { fileName });

        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getInt(cursor.getColumnIndex(DOCUMENTATIONS_ID));
            cursor.close();
        }

        //db.close();

        return id;
    }

    /**
     * Fetch documentation that matches the provided primary key.
     * @param id Primary key of documentation
     * @return A {@link Documentation} object or null
     */
    @Nullable
    public Documentation getDocumentation(int id) {
        Documentation ret = null;
        SQLiteDatabase db = getReadableDatabase();

        final String[] columns = {
                DOCUMENTATIONS_ID,
                DOCUMENTATIONS_TITLE,
                DOCUMENTATIONS_FILE_NAME,
                DOCUMENTATIONS_DATA
        };

        Cursor cursor = db.query(TABLE_DOCUMENTATIONS,
                columns,
                DOCUMENTATIONS_ID + " = ?",
                new String[] {String.valueOf(id)},
                null, null, null);

        if (cursor != null && !cursor.isClosed() && cursor.moveToNext()) {
            ret = new Documentation(
                    cursor.getInt(cursor.getColumnIndex(DOCUMENTATIONS_ID)),
                    cursor.getString(cursor.getColumnIndex(DOCUMENTATIONS_TITLE)),
                    cursor.getString(cursor.getColumnIndex(DOCUMENTATIONS_FILE_NAME)),
                    cursor.getString(cursor.getColumnIndex(DOCUMENTATIONS_DATA))
            );

            cursor.close();
        }

        return ret;
    }

    public boolean existsDocumentation(int id) {
        return getDocumentation(id) != null;
    }

    /**
     * Calculate the number of documentation in the database
     * @return The amount of documentation or -1.
     */
    public int getDocumentationCount() {
        int count = -1;
        final String query = "SELECT COUNT(*) AS count "
                + " FROM " + TABLE_DOCUMENTATIONS;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && !cursor.isClosed()) {
            if (cursor.moveToFirst()) {
                count = cursor.getInt(cursor.getColumnIndex("count"));
            }

            cursor.close();
        }

        return count;
    }

    /**
     * Fetch a random documentation
     * @return A {@link Documentation} object
     */
    public Documentation getRandomDocumentation() {
        SQLiteDatabase db = getReadableDatabase();
        Documentation doc = null;

        final String mostViewedIdQuery = "SELECT * "
                + " FROM " + TABLE_DOCUMENTATIONS
                + " ORDER BY RANDOM() LIMIT 1";

        Cursor cursor = db.rawQuery(mostViewedIdQuery, null);

        if (cursor != null && !cursor.isClosed()) {
            if (cursor.moveToFirst()) {
                doc = new Documentation();
                doc.setId(cursor.getInt(cursor.getColumnIndex(DOCUMENTATIONS_ID)));
                doc.setTitle(cursor.getString(cursor.getColumnIndex(DOCUMENTATIONS_TITLE)));
                doc.setData(cursor.getString(cursor.getColumnIndex(DOCUMENTATIONS_DATA)));
                doc.setFileName(cursor.getString(cursor.getColumnIndex(DOCUMENTATIONS_FILE_NAME)));
            }

            cursor.close();
        }

        return doc;
    }

    /**
     * Verify if a documentation is assigned as favorite.
     * @param docId The primary key of documentation.
     * @return True if the documentation is a favorite, False otherwise.
     */
    public boolean isFavorite(int docId) {
        SQLiteDatabase db = getReadableDatabase();
        boolean isFavorite;

        final String query = "SELECT * FROM " + TABLE_FAVORITES
                + " WHERE " + FAVORITES_DOC_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[] { Integer.toString(docId) });

        isFavorite = cursor.getCount() > 0;

        cursor.close();
        //db.close();

        return isFavorite;
    }

    /**
     * Assign a documentation as favorite.
     * @param docId Primary key of the documentation.
     * @see #getDocumentationId(String)
     */
    public void insertFavorite(int docId) {
        ContentValues values = new ContentValues();
        values.put(FAVORITES_DOC_ID, docId);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        String currentDate = sdf.format(new Date());
        values.put(FAVORITES_CREATED_AT, currentDate);

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_FAVORITES, null, values);

        //db.close();
    }

    /**
     * Get a 'page' of favorites sorted by date (desc).
     * Use the limit and offset to paginate the results.
     * The search will start in 'offset' point and will fetch the value of 'limit' results
     * @param limit the max results per page
     * @param offset point to start the page
     * @return A {@link List} of {@link Favorite} or a empty {@link List} if no favorites found.
     */
    public List<Favorite> getFavorites(int limit, int offset) {
        ArrayList<Favorite> favList = new ArrayList<>();
        Documentation doc;
        Favorite fav;
        SQLiteDatabase db = getReadableDatabase();

        final String query = "SELECT f.*, d.* "
                + " FROM " + TABLE_DOCUMENTATIONS + " AS d "
                + " INNER JOIN " + TABLE_FAVORITES + " AS f "
                + " ON d." + DOCUMENTATIONS_ID + " = f." + FAVORITES_DOC_ID
                + " ORDER BY f." + FAVORITES_ID + " DESC"
                + " LIMIT ? "
                + " OFFSET ? ";

        Cursor cursor = db.rawQuery(query, new String[] { String.valueOf(limit),
                String.valueOf(offset) });

        if (cursor != null && !cursor.isClosed() && cursor.moveToFirst()) {
            do {
                doc = new Documentation();
                doc.setId(cursor.getInt(cursor.getColumnIndex(FAVORITES_DOC_ID)));
                doc.setFileName(cursor.getString(cursor.getColumnIndex(DOCUMENTATIONS_FILE_NAME)));
                doc.setTitle(cursor.getString(cursor.getColumnIndex(DOCUMENTATIONS_TITLE)));
                doc.setData(cursor.getString(cursor.getColumnIndex(DOCUMENTATIONS_DATA)));

                fav = new Favorite();
                fav.setId(cursor.getInt(cursor.getColumnIndex(FAVORITES_ID)));
                fav.setCreatedAt(cursor.getString(cursor.getColumnIndex(FAVORITES_CREATED_AT)));
                fav.setDocumentation(doc);

                favList.add(fav);
            } while (cursor.moveToNext());

            cursor.close();
        }

        //db.close();

        return favList;
    }

    /**
     * This method calls {@link #getFavorites(int, int)} with offset equals to 0
     * @param limit The max quantity of rows
     * @return A {@link List} of {@link Favorite} or a empty {@link List} if no favorites found.
     * @see #getFavorites(int, int)
     */
    public List<Favorite> getFavorites(int limit) {
        return getFavorites(limit, 0);
    }

    /**
     * The amount of documentation assigned as favorite.
     * @return The number of favorites or 0.
     */
    public int getFavoritesCount() {
        int count = 0;
        final String query = "SELECT COUNT(*) AS count "
                + " FROM " + TABLE_FAVORITES;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && !cursor.isClosed()) {
            if (cursor.moveToFirst()) {
                count = cursor.getInt(cursor.getColumnIndex("count"));
            }

            cursor.close();
        }

        return count;
    }

    /**
     * Remove a documentation from favorites
     * @param docId The primary key of a documentation in database
     * @return 1 if the favorite was deleted, 0 otherwise
     * @see #getDocumentationId(String)
     */
    public int deleteFavorite(int docId) {
        SQLiteDatabase db = getReadableDatabase();
        int deletedRows = db.delete(TABLE_FAVORITES, FAVORITES_DOC_ID + " = ?",
                new String[] { Integer.toString(docId) });

        return deletedRows;
    }

    /**
     * Delete all registers of table favorites.
     * @return The number of rows removed.
     */
    public int deleteAllFavorites() {
        SQLiteDatabase db = getReadableDatabase();
        return db.delete(TABLE_FAVORITES, "1", null);
    }

    /**
     * Get a 'page' of history sorted by date (desc).
     * Use the limit and offset to paginate the results.
     * The search will start in 'offset' point and will fetch the value of 'limit' results
     * @param limit the max results per page
     * @param offset point to start the page
     * @return A {@link List} of {@link History} or a empty {@link List} if no history found.
     */
    public List<History> getHistory(int limit, int offset) {
        ArrayList<History> histList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        final String query = "SELECT h.*, d.* "
                + " FROM " + TABLE_DOCUMENTATIONS + " AS d "
                + " INNER JOIN " + TABLE_HISTORY + " AS h "
                + " ON d." + DOCUMENTATIONS_ID + " = h." + HISTORY_DOC_ID
                + " ORDER BY h." + HISTORY_ID + " DESC "
                + " LIMIT ? "
                + " OFFSET ? ";

        Cursor cursor = db.rawQuery(query, new String[] { String.valueOf(limit), String.valueOf(offset) });

        if (cursor != null && !cursor.isClosed() && cursor.moveToFirst()) {
            do {
                Documentation doc = new Documentation();
                doc.setId(cursor.getInt(cursor.getColumnIndex(HISTORY_DOC_ID)));
                doc.setFileName(cursor.getString(cursor.getColumnIndex(DOCUMENTATIONS_FILE_NAME)));
                doc.setTitle(cursor.getString(cursor.getColumnIndex(DOCUMENTATIONS_TITLE)));
                doc.setData(cursor.getString(cursor.getColumnIndex(DOCUMENTATIONS_DATA)));

                History hist = new History();
                hist.setId(cursor.getInt(cursor.getColumnIndex(HISTORY_ID)));
                hist.setCreatedAt(cursor.getString(cursor.getColumnIndex(HISTORY_CREATED_AT)));
                hist.setDocumentation(doc);

                histList.add(hist);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return histList;
    }

    /**
     * This method calls {@link #getHistory(int, int)} with offset equals to 0
     * @param limit The max quantity of rows
     * @return A {@link List} of {@link History} or a empty {@link List} if no history found.
     * @see #getHistory(int, int)
     */
    public List<History> getHistory(int limit) {
        return getHistory(limit, 0);
    }

    /**
     * Assign an documentation as viewed.
     * The current time will be automatically generated by System
     * @param docId Primary key of the documentation in database
     */
    public void insertHistory(int docId) {
        ContentValues values = new ContentValues();
        values.put(HISTORY_DOC_ID, docId);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        String currentDate = sdf.format(new Date());
        values.put(HISTORY_CREATED_AT, currentDate);

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_HISTORY, null, values);
    }

    /**
     * Amount of documentations viewed in all time.
     * @return An integer with value of views or zero
     * @see #getUniqueHistoryCount()
     */
    public int getHistoryCount() {
        int count = 0;
        final String query = "SELECT COUNT(*) AS count "
                + " FROM " + TABLE_HISTORY;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && !cursor.isClosed()) {
            if (cursor.moveToFirst()) {
                count = cursor.getInt(cursor.getColumnIndex("count"));
            }

            cursor.close();
        }

        return count;
    }

    /**
     * Returns the amount of documentation first seen,
     * excluding other views of the same documentation
     * @return An integer representing the unique views
     */
    public int getUniqueHistoryCount() {
        int count = 0;
        final String query = "SELECT COUNT(*) AS count "
                + " FROM " + TABLE_HISTORY + " h "
                + " GROUP BY h." + HISTORY_DOC_ID;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && !cursor.isClosed()) {
            if (cursor.moveToFirst()) {
                count = cursor.getInt(cursor.getColumnIndex("count"));
            }

            cursor.close();
        }

        return count;
    }

    /**
     * Returns a history list without repeating documentations viewed more than once
     * @param limit max number of result
     * @return A {@link List} of {@link History} with size <= limit, or an empty {@link List}
     * @see #getHistory(int, int)
     */
    public List<History> getUniqueHistory(int limit) {
        List<History> histList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        final String query = "SELECT h.*, d.* "
                + " FROM " + TABLE_DOCUMENTATIONS + " AS d "
                + " INNER JOIN " + TABLE_HISTORY + " AS h "
                + " ON d." + DOCUMENTATIONS_ID + " = h." + HISTORY_DOC_ID
                + " GROUP BY h." + HISTORY_DOC_ID
                + " ORDER BY h." + HISTORY_ID + " DESC "
                + " LIMIT ? ";
        Cursor cursor = db.rawQuery(query, new String[] { String.valueOf(limit) });

        if (cursor != null && !cursor.isClosed()) {
            if (cursor.moveToFirst()) {
                do {
                    Documentation doc = new Documentation();
                    doc.setId(cursor.getInt(cursor.getColumnIndex(HISTORY_DOC_ID)));
                    doc.setFileName(cursor.getString(cursor.getColumnIndex(DOCUMENTATIONS_FILE_NAME)));
                    doc.setTitle(cursor.getString(cursor.getColumnIndex(DOCUMENTATIONS_TITLE)));
                    doc.setData(cursor.getString(cursor.getColumnIndex(DOCUMENTATIONS_DATA)));

                    History hist = new History();
                    hist.setId(cursor.getInt(cursor.getColumnIndex(HISTORY_ID)));
                    hist.setCreatedAt(cursor.getString(cursor.getColumnIndex(HISTORY_CREATED_AT)));
                    hist.setDocumentation(doc);

                    histList.add(hist);
                } while (cursor.moveToNext());
            }

            cursor.close();
        }

        return histList;
    }

    /**
     * Clear all registers of table history.
     * @return The number of row deleted
     */
    public int deleteAllHistory() {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_HISTORY, "1", null);
    }

    /**
     * Return a {@link List} of {@link Pair}.
     * The {@link Pair} contains a {@link Documentation} object as first value
     * and its count of views as second value.
     * @param limit The max number of registers
     * @return The most viewed {@link List} or an empty {@link List}
     */
    public List<Pair<Documentation, Integer>> getMostViewed(int limit) {
        List<Pair<Documentation, Integer>> mvList = new ArrayList<>();

        final String query = "SELECT d.*, COUNT(h." + HISTORY_DOC_ID + ") AS count "
                + " FROM " + TABLE_DOCUMENTATIONS + " d "
                + " INNER JOIN " + TABLE_HISTORY + " h "
                + " ON d." + DOCUMENTATIONS_ID + " = h." + HISTORY_DOC_ID
                + " GROUP BY d." + DOCUMENTATIONS_TITLE
                + " ORDER BY count DESC "
                + " LIMIT ? ";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[] { String.valueOf(limit) });

        if (cursor != null && !cursor.isClosed()) {
            if (cursor.moveToFirst()) {
                do {
                    Documentation doc = new Documentation();
                    doc.setId(cursor.getInt(cursor.getColumnIndex(DOCUMENTATIONS_ID)));
                    doc.setTitle(cursor.getString(cursor.getColumnIndex(DOCUMENTATIONS_TITLE)));
                    doc.setFileName(cursor.getString(cursor.getColumnIndex(DOCUMENTATIONS_FILE_NAME)));
                    doc.setData(cursor.getString(cursor.getColumnIndex(DOCUMENTATIONS_DATA)));

                    Integer count = cursor.getInt(cursor.getColumnIndex("count"));
                    mvList.add(new Pair<Documentation, Integer>(doc, count));
                } while (cursor.moveToNext());
            }

            cursor.close();
        }

        return mvList;
    }

    public void test2() {
        SQLiteDatabase db = getReadableDatabase();

        final String query = "SELECT * "
                + " FROM " + TABLE_DOCUMENTATIONS
                + " LIMIT 100";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String log = DOCUMENTATIONS_ID + ": " +
                        String.valueOf(cursor.getInt(cursor.getColumnIndex(DOCUMENTATIONS_ID)))
                        + "; " + DOCUMENTATIONS_FILE_NAME + ": "
                        + cursor.getString(cursor.getColumnIndex(DOCUMENTATIONS_FILE_NAME))
                        + "; " + DOCUMENTATIONS_TITLE + ": "
                        + cursor.getString(cursor.getColumnIndex(DOCUMENTATIONS_TITLE));
                Log.d("DOC_DUMP", log);
            } while (cursor.moveToNext());
        }

        cursor.close();
    }
}