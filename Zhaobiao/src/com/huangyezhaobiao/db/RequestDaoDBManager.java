package com.huangyezhaobiao.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.huangyezhaobiao.application.BiddingApplication;

/**
 * Created by SongYongmeng on 2016/7/21.
 */
public class RequestDaoDBManager {
    static private RequestDaoDBManager dbMgr = new RequestDaoDBManager();
    private DBOpenHelper dbHelper;

    private RequestDaoDBManager() {
        dbHelper = DBOpenHelper.getInstance(BiddingApplication.getBiddingApplication().getApplicationContext());
    }

    public static synchronized RequestDaoDBManager getInstance() {
        if (dbMgr == null) {
            dbMgr = new RequestDaoDBManager();
        }
        return dbMgr;
    }

    synchronized public String getDate(String port) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + port + " from " + UserRequestDao.TABLE_NAME, null);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }

        String strVal = cursor.getString(0);
        if (strVal == null || strVal.equals("")) {
            return null;
        }

        cursor.close();
        closeDB();
        return strVal;
    }

    synchronized public void insert(String port, String data) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(port, data);
            db.insert(UserRequestDao.TABLE_NAME, null, values);
        }
        closeDB();
    }

    synchronized public void delete() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(UserRequestDao.TABLE_NAME, null, null);
        }
        closeDB();
    }

    synchronized public void delete(String port) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(UserRequestDao.TABLE_NAME, port, null);
        }
        closeDB();
    }

    synchronized public void delete(String port, String data) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(UserRequestDao.TABLE_NAME, port + "=?", new String[]{data});
        }
        closeDB();
    }

    synchronized public void replace(String port, String data) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(port, data);
            db.replace(UserRequestDao.TABLE_NAME, port, values);
        }
        closeDB();
    }

    synchronized public void update(String port, String data) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(port, data);
            db.update(UserRequestDao.TABLE_NAME, values, null, null);
        }
        closeDB();
    }

    synchronized public void update(String port, String newData, String oldData) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(port, newData);
            db.update(UserRequestDao.TABLE_NAME, values, port + "=?", new String[]{oldData});
        }
        closeDB();
    }

    synchronized public boolean hasData(String port) {
        Boolean hasData = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + port + " from " + UserRequestDao.TABLE_NAME, null);
        if (cursor.moveToNext())
            hasData = true;
        else
            hasData = false;
        cursor.close();
        closeDB();
        return hasData;
    }

    synchronized public void closeDB() {
        if (dbHelper != null) {
            dbHelper.closeDB();
        }
        dbMgr = null;
    }
}