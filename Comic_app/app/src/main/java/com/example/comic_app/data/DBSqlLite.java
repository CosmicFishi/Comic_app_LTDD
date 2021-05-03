package com.example.comic_app.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBSqlLite extends SQLiteOpenHelper {
    public DBSqlLite(Context context) {
        super(context, "User.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table User(name TEXT primary key, sdt TEXT, id TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Boolean insertUser(String name, String sdt, String id){
        SQLiteDatabase BD = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("sdt", sdt);
        contentValues.put("id", id);
        long rs = BD.insert("User", null, contentValues);

        if (rs == -1){
            return false;
        }
        return true;
    }

    public Cursor getUser(){
        SQLiteDatabase BD = this.getWritableDatabase();
        Cursor cursor = BD.rawQuery("Select * from User", null);
        return cursor;
    }
}
