package com.lennonwoo.maxim.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lennonwoo.maxim.model.Maxim;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MaximDB {

    private static MaximDB INSTANCE;

    Context context;

    MaximDBHelper dbHelper;

    public MaximDB(Context context) {
        this.context = context;
        dbHelper = new MaximDBHelper(context);
    }

    public static MaximDB getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new MaximDB(context);
        }
        return INSTANCE;
    }

    public void saveMaxim(String maxim) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MaximDBContact.MaximTable.COLUMN_NAME_CONTENT, maxim);
        db.insert(MaximDBContact.MaximTable.TABLE_NAME, null, values);
        db.close();
    }

    public void deleteMaxim(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(MaximDBContact.MaximTable.TABLE_NAME, MaximDBContact.MaximTable._ID + "= ?",
                new String[]{"" + id});
    }

    public void updateMaxim(int id, String s) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MaximDBContact.MaximTable.COLUMN_NAME_CONTENT, s);
        db.update(MaximDBContact.MaximTable.TABLE_NAME, values, MaximDBContact.MaximTable._ID + "= ?",
                new String[]{"" + id});
    }

    public String getRandomMaxim() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor =
                db.query(MaximDBContact.MaximTable.TABLE_NAME, null, null, null, null, null, null);
        int totalNum = cursor.getCount();
        Random rand = new Random();
        cursor.moveToPosition(rand.nextInt(totalNum));
        String result = cursor.getString(cursor.getColumnIndex(MaximDBContact.MaximTable.COLUMN_NAME_CONTENT));
        cursor.close();
        return result;
    }

    public List<Maxim> getAllMaxim() {
        List<Maxim> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor =
                db.query(MaximDBContact.MaximTable.TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String s = cursor.getString(cursor.getColumnIndex(
                        MaximDBContact.MaximTable.COLUMN_NAME_CONTENT
                ));
                int id = cursor.getInt(cursor.getColumnIndex(
                        MaximDBContact.MaximTable._ID
                ));
                Maxim maxim = new Maxim(s, id);
                list.add(maxim);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

}
