package com.hackernews.macintoshuser.hackernews.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by macintoshuser on 8/15/16.
 */
public class MySqliteHelper extends SQLiteOpenHelper {
    public static final String TABLE_STORIES = "stories";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_BY = "by";
    public static final String COLUMN_DESCENDSNTS = "descendants";
    public static final String COLUMN_KIDS = "kids";
    public static final String COLUMN_SCORE  = "score";
    public static final String COLUMN_VIEWED = "viewed";


    private static final String DATABASE_NAME = "stories.sqlite";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String CREATE_TABLE_STORIES = "create table "
            + TABLE_STORIES + "(" + COLUMN_ID
            + " text primary key not null, "
            + COLUMN_VIEWED + " integer not null default(0));";


    public MySqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_STORIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySqliteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        onCreate(db);
    }
}
