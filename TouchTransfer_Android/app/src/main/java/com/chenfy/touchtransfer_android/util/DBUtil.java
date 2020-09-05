package com.chenfy.touchtransfer_android.util;

import android.database.sqlite.SQLiteDatabase;

import com.chenfy.touchtransfer_android.filehandle.dao.FileDaoOpenHelper;

import java.util.concurrent.atomic.AtomicReference;

public class DBUtil {
    private static final AtomicReference<DBUtil> INSTANCE = new AtomicReference<>();
    private SQLiteDatabase mWritableDatabase;
    private SQLiteDatabase mReadableDatabase;
    public static DBUtil getInstance() {
        for (; ; ) {
            DBUtil current = INSTANCE.get();
            if (current != null) {
                return current;
            }
            current = new DBUtil();
            if (INSTANCE.compareAndSet(null, current)) {
                return current;
            }
        }
    }

    private DBUtil(){
        mWritableDatabase =  new FileDaoOpenHelper().getWritableDatabase();
        mReadableDatabase = new FileDaoOpenHelper().getReadableDatabase();
    }

    public SQLiteDatabase getWritableDatabase(){
        return mWritableDatabase;
    }

    public SQLiteDatabase getReadableDatabase(){
        return mReadableDatabase;
    }
}
