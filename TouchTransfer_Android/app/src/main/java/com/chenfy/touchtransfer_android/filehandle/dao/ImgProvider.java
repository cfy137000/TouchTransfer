package com.chenfy.touchtransfer_android.filehandle.dao;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.chenfy.touchtransfer_android.dao.Contact;
import com.chenfy.touchtransfer_android.base.App;
import com.chenfy.touchtransfer_android.util.DBUtil;
import com.chenfy.touchtransfer_android.util.FileUtils;

// 使用ContentProvider操作数据库
// 这样是有callback的
public class ImgProvider extends ContentProvider implements Contact.DB {

    public ImgProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int delete = DBUtil.getInstance().getWritableDatabase().delete(TABLE_NAME, selection, selectionArgs);
        Context context = getContext();
        if (context == null) {
            context = App.getInstance();
        }
        if (selection == null) {
            // 删除所有
            Log.d("ImgProvider", Thread.currentThread().getName());
            FileUtils.deleteAll();
            Uri updateUri = Uri.parse(PROVIDER_DEL_URI_STR + "/all");
            context.getContentResolver().notifyChange(updateUri, null);
        }
        return delete;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        System.out.println("Provider: 插入数据");
        SQLiteDatabase writableDatabase = DBUtil.getInstance().getWritableDatabase();
        writableDatabase.insert(TABLE_NAME, null, values);
        Context context = getContext();
        if (context == null) {
            context = App.getInstance();
        }
        Uri updateUri = Uri.parse(PROVIDER_INSERT_URI_STR + "/" + values.getAsString(COLUMN_FILE_ID));
        context.getContentResolver().notifyChange(updateUri, null);
        return uri;
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase readableDatabase = DBUtil.getInstance().getReadableDatabase();

        Cursor cursor = null;
        cursor = readableDatabase
                .query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
        return cursor;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
