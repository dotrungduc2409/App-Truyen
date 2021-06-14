package com.example.da.BLL;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBManager extends SQLiteOpenHelper {
    public DBManager(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //Truy vấn không trả kết quả
    public void QueryData(String strSQL) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(strSQL);
    }
    //Truy vấn trả kết quả
    public Cursor getData(String strSQL){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(strSQL, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
