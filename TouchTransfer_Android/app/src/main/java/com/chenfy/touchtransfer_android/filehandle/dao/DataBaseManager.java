package com.chenfy.touchtransfer_android.filehandle.dao;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import androidx.annotation.Nullable;

import com.chenfy.touchtransfer_android.dao.FileEntity;
import com.chenfy.touchtransfer_android.base.App;

import java.util.ArrayList;
import java.util.List;

import static com.chenfy.touchtransfer_android.dao.Contact.DB.COLUMN_FILE_ID;
import static com.chenfy.touchtransfer_android.dao.Contact.DB.COLUMN_FILE_TYPE;
import static com.chenfy.touchtransfer_android.dao.Contact.DB.COLUMN_ID;
import static com.chenfy.touchtransfer_android.dao.Contact.DB.COLUMN_RAW_PATH;
import static com.chenfy.touchtransfer_android.dao.Contact.DB.COLUMN_THUMB_PATH;
import static com.chenfy.touchtransfer_android.dao.Contact.DB.PROVIDER_URI;

/**
 * Created by ChenFengyao
 * Date: 20-9-2
 */
public class DataBaseManager {


    public static void insert(String fileId, String rawPath, String thumbPath, int type) {
        System.out.println("插入数据库");
        Context context = App.getInstance();
        ContentResolver resolver = context.getContentResolver();
        ContentValues contentValues = new ContentValues(3);
        contentValues.put(COLUMN_FILE_ID, fileId);
        contentValues.put(COLUMN_RAW_PATH, rawPath);
        contentValues.put(COLUMN_THUMB_PATH, thumbPath);
        contentValues.put(COLUMN_FILE_TYPE, type);
        resolver.insert(PROVIDER_URI, contentValues);
    }

    @Nullable
    public static FileEntity getByFileID(String fileID) {
        FileEntity fileEntity = null;
        Context context = App.getInstance();
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = null;
        try {
            cursor = resolver.query(PROVIDER_URI, null, COLUMN_FILE_ID + "=?", new String[]{fileID}, null);
            if (cursor.moveToFirst()) {
                String rawPath = cursor.getString(cursor.getColumnIndex(COLUMN_RAW_PATH));
                String thumbPath = cursor.getString(cursor.getColumnIndex(COLUMN_THUMB_PATH));
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                int type = cursor.getInt(cursor.getColumnIndex(COLUMN_FILE_TYPE));
                fileEntity = new FileEntity();
                fileEntity
                        .setId(id)
                        .setFileID(fileID)
                        .setRawPath(rawPath)
                        .setThumbPath(thumbPath)
                        .setFileType(type);
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return fileEntity;
    }

    public static List<FileEntity> getAllFileInfo() {
        List<FileEntity> fileEntities = new ArrayList<>();
        Context context = App.getInstance();
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = null;
        try {
            cursor = resolver.query(PROVIDER_URI, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String rawPath = cursor.getString(cursor.getColumnIndex(COLUMN_RAW_PATH));
                    String thumbPath = cursor.getString(cursor.getColumnIndex(COLUMN_THUMB_PATH));
                    String fileID = cursor.getString(cursor.getColumnIndex(COLUMN_FILE_ID));
                    int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                    int type = cursor.getInt(cursor.getColumnIndex(COLUMN_FILE_TYPE));
                    FileEntity fileEntity = new FileEntity();
                    fileEntity.setId(id)
                            .setFileID(fileID)
                            .setRawPath(rawPath)
                            .setThumbPath(thumbPath)
                            .setFileType(type);
                    fileEntities.add(fileEntity);
                }
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return fileEntities;
    }

    /**
     * 删除所有
     */
    public static void deleteAll() {
        Context context = App.getInstance();
        ContentResolver resolver = context.getContentResolver();
        resolver.delete(PROVIDER_URI, null, null);
    }
}
