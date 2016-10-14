package com.hackernews.macintoshuser.hackernews.dto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.hackernews.macintoshuser.hackernews.database.MySqliteHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by macintoshuser on 8/15/16.
 */
public class StoryDAO {

    private SQLiteDatabase database;
    private MySqliteHelper dbHelper;

    public StoryDAO(Context context) {
        dbHelper = new MySqliteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long insertStory(StoryDTO story){
        ContentValues values = new ContentValues();
        long rowID = -1;

        values.put(MySqliteHelper.COLUMN_ID, story.getId());
        values.put(MySqliteHelper.COLUMN_VIEWED, 0);

            rowID = database.insert(MySqliteHelper.TABLE_STORIES, null, values);
        return rowID;
    }

    public void setStoryViewed(int id){

        ContentValues values = new ContentValues();

        values.put(MySqliteHelper.COLUMN_VIEWED, 1);

        database.update(MySqliteHelper.TABLE_STORIES, values, "id = ?",
                new String[]{String.valueOf(id)});
    }

    public int getViewStory(long id){
        int result = 0;

        String where = "id = ?";
        String args[] = new String[] { String.valueOf(id) };
        Cursor cursor = database.query(MySqliteHelper.TABLE_STORIES,
                null, where, args, null, null, null);

        if(cursor.getCount() == 1) {
            cursor.moveToFirst();
                result = cursor.getInt(cursor.getColumnIndex(MySqliteHelper.COLUMN_VIEWED));
        }
        cursor.close();

        return result;
    }

    public static String strSeparator = ",";
    public static String convertArrayToString(int[] array){
        String str = "";
        for (int i = 0;i<array.length; i++) {
            str = str+array[i];
            // Do not append comma at the end of last element
            if(i<array.length-1){
                str = str+strSeparator;
            }
        }
        return str;
    }
    public static String[] convertStringToArray(String str){
        String[] arr = str.split(strSeparator);
        return arr;
    }
}
