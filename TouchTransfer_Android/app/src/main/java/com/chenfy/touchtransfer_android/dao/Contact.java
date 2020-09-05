package com.chenfy.touchtransfer_android.dao;

import android.net.Uri;

import com.chenfy.touchtransfer_android.base.App;
import com.chenfy.touchtransfer_android.util.ScreenUtil;

import java.io.File;

/**
 * Created by ChenFengyao
 * Date: 20-9-2
 */
public final class Contact {
    public static final String RAW_FILE_PATH = App.getInstance().getFilesDir()+ File.separator + "raw";
    // 缩略图保存路径
    public static final String THUMBNAIL_PATH = App.getInstance().getFilesDir() + File.separator + "thumbnails";
    // 缩略图边长
    public static final int THUMB_EDGE = ScreenUtil.getScreenSize().getWidth() / 3;
    // 缓存路径
    public static final String CACHE_PATH = App.getInstance().getCacheDir().toString();

    public interface DB {
        String PROVIDER_URI_STR = "com.chenfy.touchtransfer_android.imgprovider";
        String PROVIDER_DEL_URI_STR = "content://com.chenfy.touchtransfer_android.imgprovider/del";
        String PROVIDER_INSERT_URI_STR = "content://com.chenfy.touchtransfer_android.imgprovider/insert";
        Uri PROVIDER_URI = Uri.parse("content://" + PROVIDER_URI_STR);
        String TABLE_NAME = "fileInfo";
        String COLUMN_ID = "id";
        String COLUMN_FILE_ID = "fileID";
        String COLUMN_RAW_PATH = "rawPath";
        String COLUMN_THUMB_PATH = "thumbPath";
        String COLUMN_FILE_TYPE = "type";
        int FILE_TYPE_IMG = 0;
        int FILE_TYPE_VIDEO = 1;
    }

    public interface ATY {
        String KEY_POS = "pos";
        String KEY_NFC_ID = "NFCID";
        String KEY_VIDEO_PATH = "VideoPath";
    }

    public interface DIALOG_TYPE {
        int TYPE_EXIT = 0;
    }

    public interface NET_WORK{
        String HTTP = "http://";
        int PORT_HTTP = 8888;
        String UPLOAD_PATH = "/image/upload";
        String KEY_FILE = "files";

        int PORT_UDP = 19999;
        String BROADCAST_ADDRESS = "255.255.255.255";

        long DATA_THRESHOLD = 100L * 1024 * 1024; // 每个数据包在100M左右
    }
}
