package com.chenfy.touchtransfer_android.dao;

import static com.chenfy.touchtransfer_android.dao.Contact.DB.FILE_TYPE_IMG;

/**
 * Created by ChenFengyao
 * Date: 20-9-2
 */
public class FileEntity {
    private int mId;
    private String mFileID;
    private String mRawPath;
    private String mThumbPath;
    private int mFileType;
    private boolean mUploading = false;

    public boolean isUploading() {
        return mUploading;
    }

    public void setUploading(boolean uploading) {
        mUploading = uploading;
    }

    public int getFileType() {
        return mFileType;
    }

    public FileEntity setFileType(int fileType) {
        mFileType = fileType;
        return this;
    }

    public int getId() {
        return mId;
    }

    public FileEntity setId(int id) {
        mId = id;
        return this;
    }

    public String getFileID() {
        return mFileID;
    }

    public FileEntity setFileID(String fileID) {
        mFileID = fileID;
        return this;
    }

    public String getRawPath() {
        return mRawPath;
    }

    public FileEntity setRawPath(String rawPath) {
        mRawPath = rawPath;
        return this;
    }

    public String getThumbPath() {
        return mThumbPath;
    }

    public FileEntity setThumbPath(String thumbPath) {
        mThumbPath = thumbPath;
        return this;
    }

    /**
     * 是否是图片
     * @return true: 图片 false: 视频
     */
    public boolean isImg() {
        return getFileType() == FILE_TYPE_IMG;
    }
}
