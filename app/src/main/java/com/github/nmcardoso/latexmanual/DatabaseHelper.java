package com.github.nmcardoso.latexmanual;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
    private static final String TABLE_HISTORIC = "historic";
    private static final String TABLE_FAVORITES = "favorites";

    // Documentations columns
    public static final String DOCUMENTATIONS_ID = "_id";
    public static final String DOCUMENTATIONS_FILE_NAME = "file_name";
    public static final String DOCUMENTATIONS_TITLE = "title";
    public static final String DOCUMENTATIONS_DATA = "data";

    // Historic columns
    private static final String HISTORIC_ID = "_id";
    private static final String HISTORIC_CREATED_AT = "created_at";
    private static final String HISTORIC_DOC_ID = "documentations_id";

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

    // Create table HISTORIC statement
    private static final String CREATE_HISTORIC = "CREATE TABLE "
            + TABLE_HISTORIC + " ("
            + HISTORIC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + HISTORIC_CREATED_AT + " TEXT, "
            + HISTORIC_DOC_ID + " INTEGER NOT NULL" + ")";

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
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORIC);
        sqLiteDatabase.execSQL(CREATE_DOCUMENTATIONS);
        sqLiteDatabase.execSQL(CREATE_HISTORIC);
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


    public Cursor search(String query) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {
                DOCUMENTATIONS_ID,
                DOCUMENTATIONS_TITLE,
                DOCUMENTATIONS_FILE_NAME,
                DOCUMENTATIONS_DATA
        };

        // db.query(tabela, campos, where, whereArgs, groupBy, having, orderBy);
        Cursor cursor = db.query(
                TABLE_DOCUMENTATIONS, columns,
                DOCUMENTATIONS_TITLE + " LIKE ? OR " + DOCUMENTATIONS_DATA + " LIKE ?",
                new String[] { "%" + query + "%", "%" + query + "%" },
                null, null, null, "15"
        );

        if (cursor != null) {
            cursor.moveToFirst();
        }

        db.close();

        return cursor;
    }


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


    public Documentation getDocumentationById(int id) {
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

        if (cursor != null && cursor.moveToNext()) {
            ret = new Documentation(
                    cursor.getInt(cursor.getColumnIndex(DOCUMENTATIONS_ID)),
                    cursor.getString(cursor.getColumnIndex(DOCUMENTATIONS_TITLE)),
                    cursor.getString(cursor.getColumnIndex(DOCUMENTATIONS_FILE_NAME)),
                    cursor.getString(cursor.getColumnIndex(DOCUMENTATIONS_DATA))
            );
        }

        return ret;
    }


    public boolean isFavorite(String fileName) {
        SQLiteDatabase db = getReadableDatabase();
        boolean isFavorite;

        final String query = "SELECT * FROM " + TABLE_DOCUMENTATIONS + " AS d "
                + " INNER JOIN " + TABLE_FAVORITES + " AS f "
                + " ON d." + DOCUMENTATIONS_ID + " = f." + FAVORITES_DOC_ID
                + " WHERE d." + DOCUMENTATIONS_FILE_NAME + " = ?";

        Cursor cursor = db.rawQuery(query, new String[] { fileName });

        isFavorite = cursor.getCount() > 0;

        cursor.close();
        //db.close();

        return isFavorite;
    }


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


    public List<Favorite> getFavorites(int limit, int offset) {
        ArrayList<Favorite> favList = new ArrayList<>();
        Documentation doc;
        Favorite fav;
        SQLiteDatabase db = getReadableDatabase();

        final String query = "SELECT * "
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


    public List<Favorite> getFavorites(int limit) {
        return getFavorites(limit, 0);
    }


    public int getFavoritesCount() {
        int count = -1;
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


    public void deleteFavorite(int docId) {
        SQLiteDatabase db = getReadableDatabase();
        db.delete(TABLE_FAVORITES, FAVORITES_DOC_ID + " = ?",
                new String[] { Integer.toString(docId) });

        //db.close();
    }


    public List<Historic> getHistoric(int limit, int offset) {
        ArrayList<Historic> histList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        final String query = "SELECT h.*, d.* "
                + " FROM " + TABLE_DOCUMENTATIONS + " AS d "
                + " INNER JOIN " + TABLE_HISTORIC + " AS h "
                + " ON d." + DOCUMENTATIONS_ID + " = h." + HISTORIC_DOC_ID
                + " ORDER BY h." + HISTORIC_ID + " DESC "
                + " LIMIT ? "
                + " OFFSET ? ";

        Cursor cursor = db.rawQuery(query, new String[] { String.valueOf(limit), String.valueOf(offset) });

        if (cursor != null && !cursor.isClosed() && cursor.moveToFirst()) {
            do {
                Documentation doc = new Documentation();
                doc.setId(cursor.getInt(cursor.getColumnIndex(HISTORIC_DOC_ID)));
                doc.setFileName(cursor.getString(cursor.getColumnIndex(DOCUMENTATIONS_FILE_NAME)));
                doc.setTitle(cursor.getString(cursor.getColumnIndex(DOCUMENTATIONS_TITLE)));
                doc.setData(cursor.getString(cursor.getColumnIndex(DOCUMENTATIONS_DATA)));

                Historic hist = new Historic();
                hist.setId(cursor.getInt(cursor.getColumnIndex(HISTORIC_ID)));
                hist.setCreatedAt(cursor.getString(cursor.getColumnIndex(HISTORIC_CREATED_AT)));
                hist.setDocumentation(doc);

                histList.add(hist);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return histList;
    }


    public List<Historic> getHistoric(int limit) {
        return getHistoric(limit, 0);
    }


    public void insertHistoric(int docId) {
        ContentValues values = new ContentValues();
        values.put(HISTORIC_DOC_ID, docId);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        String currentDate = sdf.format(new Date());
        values.put(HISTORIC_CREATED_AT, currentDate);

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_HISTORIC, null, values);
    }


    public int getHistoricCount() {
        int count = -1;
        final String query = "SELECT COUNT(*) AS count "
                + " FROM " + TABLE_HISTORIC;

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


    public void clearHistoric() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_HISTORIC, null, null);
    }


    public List<Historic> getMostViewed(int limit) {
        List<Historic> ret = new ArrayList<>();

        final String query = "SELECT d.*, COUNT(h." + HISTORIC_DOC_ID + ") AS count "
                + " FROM " + TABLE_DOCUMENTATIONS + " d "
                + " INNER JOIN " + TABLE_HISTORIC + " h "
                + " ON d." + DOCUMENTATIONS_ID + " = h." + HISTORIC_DOC_ID
                + " GROUP BY h." + HISTORIC_DOC_ID
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

                    Historic hist = new Historic();
                    hist.setDocumentation(doc);
                    hist.setViewCount(cursor.getInt(cursor.getColumnIndex("count")));

                    ret.add(hist);
                } while (cursor.moveToNext());
            }

            cursor.close();
        }

        return ret;
    }


    public String test2() {
        SQLiteDatabase db = getReadableDatabase();

        final String query = "SELECT * "
                + " FROM " + TABLE_DOCUMENTATIONS + " AS d "
                + " INNER JOIN " + TABLE_HISTORIC + " AS h "
                + " ON d." + DOCUMENTATIONS_ID + " = h." + HISTORIC_DOC_ID
                + " ORDER BY " + " h." + HISTORIC_ID + " DESC "
                + " LIMIT 5";

        Cursor cursor = db.rawQuery(query, null);

        String ret = "";
        if (cursor != null) {
            ret = String.valueOf(cursor.getCount());
        }

        cursor.close();

        return ret;
    }
}