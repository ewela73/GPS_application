package com.example.domi.gps_application;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Domiś on 2016-03-19.
 */
public class DBHelper extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME="gps.db";
    public static final String TABLE_NAME = "gps_data";
    public static final String COL_ID_POINT="id_point";
    public static final String COL_LATITUDE="latitude";
    public static final String COL_LONGITUDE="longitude";
    public static final String COL_ISSYNCH="isSynch";
    public static final String COL_ID_ROUTE="id_route";



    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table " + TABLE_NAME + " " +
                "( ID_POINT REAL PRIMARY KEY ," +
                " LATITUDE REAL ," +
                " LONGITUDE REAL ," +
                " ISSYNCH INTEGER ," +
                " ID_ROUTE REAL )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(long id_p,double lat, double longi, int isSyn, long id_rou)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID_POINT, id_p);
        contentValues.put(COL_LATITUDE, lat);
        contentValues.put(COL_LONGITUDE, longi);
        contentValues.put(COL_ISSYNCH, isSyn);
        contentValues.put(COL_ID_ROUTE, id_rou);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }

    }

    public Cursor getAllData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }
    public Cursor select(String qwerty)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(qwerty,null);
        return res;
    }

    public void deleteAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,null,null);
    }


}
