package com.chenfy.touchtransfer_android.filehandle;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.chenfy.touchtransfer_android.base.App;
import com.chenfy.touchtransfer_android.filehandle.dao.DataBaseManager;
import com.chenfy.touchtransfer_android.util.BitmapUtil;
import com.chenfy.touchtransfer_android.util.FileUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class FileInsertRunnable implements Runnable {
    private Uri mUri;

    public FileInsertRunnable(Uri uri) {
        mUri = uri;
    }

    @Override
    public void run() {

        if (!isContentUriExists(App.getInstance(), mUri)) {
            try {
                Thread.sleep(1000);
                if (!isContentUriExists(App.getInstance(), mUri)){
                    Thread.sleep(1000);
                    if (!isContentUriExists(App.getInstance(), mUri)){
                        return;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }


        Log.d("FileInsertRunnable", "开始执行");
        ContentResolver resolver = App.getInstance().getContentResolver();
        InputStream is = null;
        try {
            is = resolver.openInputStream(mUri);
            String fileId = FileUtils.getFileId();
            String extensionName = FileUtils.getExtensionNameFromUri(mUri);
            int fileType = FileUtils.getTypeFromUri(mUri);
            String fileName = fileId + "." + extensionName;
            Log.d("FileInsertRunnable", fileName);
            // 保存源文件
            String rawFilePath = FileUtils.saveRawFile(is, fileName);
            // 生成缩略图
            Bitmap thumb = BitmapUtil.getThumbBitmap(rawFilePath);
            // 保存缩略图
            String thumbFilePath = FileUtils.saveThumb(thumb, fileId);
            // 存入数据到数据库
            DataBaseManager.insert(fileId, rawFilePath, thumbFilePath, fileType);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            FileUtils.closeAll(is);
        }

    }

    // 判断Uri是否存在
    public static boolean isContentUriExists(Context context, Uri uri) {
        if (null == context) {
            return false;
        }
        ContentResolver cr = context.getContentResolver();
        try {
            AssetFileDescriptor descriptor = cr.openAssetFileDescriptor(uri, "r");
            if (null == descriptor) {
                System.out.println("null");
                return false;
            } else {
                try {
                    descriptor.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


}
