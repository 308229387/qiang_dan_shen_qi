package com.huangyezhaobiao.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.huangyezhaobiao.utils.UserUtils;

/**
 * Created by SongYongmeng on 2016/7/21.
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    private static DBOpenHelper instance;

    private static final String TABLE_CREATE = "CREATE TABLE "
            + UserRequestDao.TABLE_NAME + " ("
            + UserRequestDao.INTERFACE_GETBINDS + " TEXT, "
            + UserRequestDao.INTERFACE_ORDERLIST + " TEXT); ";

    private static final int DATABASE_VERSION = 1;

    private DBOpenHelper(Context context) {
        super(context, getUserDatabaseName(), null, DATABASE_VERSION);
    }

    public static DBOpenHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBOpenHelper(context.getApplicationContext());
        }
        return instance;
    }

    private static String getUserDatabaseName() {
        return UserUtils.getUserId() + ".db";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void closeDB() {
        if (instance != null) {
            try {
                SQLiteDatabase db = instance.getWritableDatabase();
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            instance = null;
        }
    }
}
