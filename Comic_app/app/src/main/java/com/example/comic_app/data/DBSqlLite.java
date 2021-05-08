package com.example.comic_app.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBSqlLite extends SQLiteOpenHelper {
    public DBSqlLite(Context context) {
        super(context, null, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table User(_id INTEGER PRIMARY KEY, phone TEXT, imageSrc TEXT)");
        db.execSQL("create Table FavouriteComic(_id INTEGER PRIMARY KEY, slugg TEXT)");
        db.execSQL("create Table ComicHistory(_id INTEGER PRIMARY KEY, slugg TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists User");
        db.execSQL("drop Table if exists FavouriteComic");
        db.execSQL("drop Table if exists ComicHistory");
    }
    public Boolean insertUser(String name, String phone){
        SQLiteDatabase BD = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("phone", name);
        contentValues.put("imageSrc", phone);
        long rs = BD.insert("User", null, contentValues);

        if (rs == -1){
            return false;
        }
        return true;
    }
    public Boolean insertFavouriteComic(String slugg){
        SQLiteDatabase BD = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("slugg", slugg);
        long rs = BD.insert("FavouriteComic", null, contentValues);

        if (rs == -1){
            return false;
        }
        return true;
    }
    public Boolean insertComicHistory(String slugg){
        SQLiteDatabase BD = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("slugg", slugg);
        long rs = BD.insert("ComicHistory", null, contentValues);

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
    public List<String> getComicHistory(){
        SQLiteDatabase BD = this.getWritableDatabase();
        List<String> list = new ArrayList<>();
        Cursor cursor = BD.rawQuery("Select * from ComicHistory", null);

        while (cursor.moveToNext()){
            list.add(cursor.getString(0));
        }

        return list;
    }
    public List<String> getFavouriteComic(){
        SQLiteDatabase BD = this.getWritableDatabase();
        List<String> list = new ArrayList<>();
        Cursor cursor = BD.rawQuery("Select * from FavouriteComic", null);

        while (cursor.moveToNext()){
            list.add(cursor.getString(0));
        }

        return list;
    }
}
