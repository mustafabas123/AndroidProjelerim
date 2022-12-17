package com.mustafa.bitirmeprojem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.sql.Blob;

public class DB extends SQLiteOpenHelper {

    public static final String DBNAME="Login.db";

    public DB(Context context) {
        super(context,"Login.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table users(username TEXT primary key,password TEXT)");
        db.execSQL("create table foods(id integer primary key,foodName TEXT,foodRecipe TEXT, foodingredients TEXT,foodImage blob)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop Table if exists users");
        db.execSQL("drop Table if exists foods");

    }
    public boolean insertData(String username,String password){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("username",username);
        contentValues.put("password",password);
        long result=db.insert("users",null,contentValues);
        if(result==1){
            return false;
        }else{
            return true;
        }
    }
    public boolean insertData2(String yemekAd, String YemekTarif, String YemekIcindekiler, byte[] YemeginResmi){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("foodName",yemekAd);
        contentValues.put("foodRecipe",YemekTarif);
        contentValues.put("foodingredients",YemekIcindekiler);
        contentValues.put("foodImage",YemeginResmi);
        long result=db.insert("foods",null,contentValues);
        if(result==1){
            return false;
        }
        else{
            return true;
        }
    }
    public boolean checkusername(String username){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from users where username=?",new String[] {username});
        if(cursor.getCount()>0){
            return true;
        }else {
            return false;
        }
    }
    public boolean checkUsernameOrPassword(String username,String password){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from users where username=? and password=? ",new String[] {username,password});
        if(cursor.getCount()>0){
            return true;
        }
        else{
            return false;
        }
    }
}
