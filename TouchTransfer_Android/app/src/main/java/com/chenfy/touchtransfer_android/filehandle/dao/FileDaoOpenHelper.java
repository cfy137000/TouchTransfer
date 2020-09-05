package com.chenfy.touchtransfer_android.filehandle.dao;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.chenfy.touchtransfer_android.dao.Contact;
import com.chenfy.touchtransfer_android.base.App;

/**
 * Created by ChenFengyao
 * Date: 20-9-2
 */
public class FileDaoOpenHelper extends SQLiteOpenHelper implements Contact.DB {
    public FileDaoOpenHelper() {
        super(App.getInstance(), "file.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (" +
                COLUMN_ID + " integer PRIMARY KEY autoincrement, " +
                COLUMN_FILE_ID + " varchar(50)," +
                COLUMN_FILE_TYPE + " integer," +
                COLUMN_RAW_PATH + " varchar(500)," +
                COLUMN_THUMB_PATH + " varchar(500))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
