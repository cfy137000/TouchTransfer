package com.chenfy.touchtransfer_android.util;

import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import com.chenfy.touchtransfer_android.base.App;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.chenfy.touchtransfer_android.dao.Contact.CACHE_PATH;
import static com.chenfy.touchtransfer_android.dao.Contact.DB.FILE_TYPE_IMG;
import static com.chenfy.touchtransfer_android.dao.Contact.DB.FILE_TYPE_VIDEO;
import static com.chenfy.touchtransfer_android.dao.Contact.RAW_FILE_PATH;
import static com.chenfy.touchtransfer_android.dao.Contact.THUMBNAIL_PATH;

public class FileUtils {


    public static List<Uri> getUrisFromIntent(Intent intent){
        List<Uri> uris = new ArrayList<>();
        Uri uri = intent.getData();
        if (uri != null) {
            uris.add(uri);
        } else {
            ClipData clipData = intent.getClipData();
            if (clipData != null) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    ClipData.Item item = clipData.getItemAt(i);
                    Uri itemUri = item.getUri();
                    uris.add(itemUri);
                }
            }
        }
        return uris;
    }

    public static int getTypeFromUri(Uri uri){
        ContentResolver resolver = App.getInstance().getContentResolver();
        String type = String.valueOf(resolver.getType(uri));
        if (type.contains("image")){
            return FILE_TYPE_IMG;
        }
        return FILE_TYPE_VIDEO;
    }

    public static String getExtensionNameFromUri(Uri uri) {
        ContentResolver resolver = App.getInstance().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(resolver.getType(uri));
    }

    public static String getFileId(){
        return UUID.randomUUID()
                .toString()
                .replace("-","")
                .toLowerCase();
    }

    public static String saveThumb(Bitmap bitmap,String id){
        File thumbDir = new File(THUMBNAIL_PATH);
        if (!thumbDir.exists()){
            thumbDir.mkdirs();
        }
        File bitmapFile = new File(thumbDir,id + ".jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(bitmapFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG,80,fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FileUtils.closeAll(fos);
        }
        return bitmapFile.getAbsolutePath();
    }

    public static String saveRawFile(InputStream is, String fileName) throws IOException {
        if (is == null) {
            throw new IOException("is 为null");
        }
        BufferedInputStream bis = new BufferedInputStream(is);
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File root = new File(RAW_FILE_PATH);
        if (!root.exists()) {
            root.mkdirs();
        }
        File rawFile = new File(root, fileName);
        try {
            rawFile.createNewFile();
            fos = new FileOutputStream(rawFile);
            bos = new BufferedOutputStream(fos);
            byte[] tmp = new byte[2048];
            int len = 0;
            while (true) {
                if ((len = bis.read(tmp)) == -1) break;
                bos.write(tmp, 0, len);
            }
            bos.flush();
        } catch (IOException e) {
            rawFile.delete();
            throw new IOException(e);
        } finally {
            closeAll(fos, bos);
        }

        return rawFile.getAbsolutePath();
    }

    public static void closeAll(Closeable ... closeables){
        if (closeables == null || closeables.length == 0) {
            return;
        }

        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (Exception e) {
//                    e.printStackTrace();
                }
            }
        }
    }

    public static void deleteAll() {
        // 删除thumb
        File thumbFile = new File(THUMBNAIL_PATH);
        delete(thumbFile);
        // 删除原图
        File file = new File(RAW_FILE_PATH);
        delete(file);
        // 删除缓存
        File cacheDir = new File(CACHE_PATH);
        delete(cacheDir);
    }

    private static void delete(File dir){
        if (dir == null){
            return;
        }
        if (dir.isFile()){
            dir.delete();
        }else {
            File[] subFiles = dir.listFiles();
            if (subFiles != null){
                for (File subFile : subFiles) {
                    delete(subFile);
                }
            }
            dir.delete();
        }
    }
}
