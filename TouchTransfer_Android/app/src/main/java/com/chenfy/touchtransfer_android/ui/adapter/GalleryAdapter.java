package com.chenfy.touchtransfer_android.ui.adapter;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chenfy.touchtransfer_android.R;
import com.chenfy.touchtransfer_android.base.BaseViewHolder;
import com.chenfy.touchtransfer_android.dao.FileEntity;

import java.util.List;

/**
 * Created by ChenFengyao
 * Date: 20-9-3
 */
public class GalleryAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private List<FileEntity> mFileEntityList;
    private static final String TAG = "GalleryAdapter";
    private static final int TYPE_IMG = 0;
    private static final int TYPE_VIDEO = 1;

    public GalleryAdapter setFileEntityList(List<FileEntity> fileInfoList) {
        mFileEntityList = fileInfoList;
        notifyDataSetChanged();
        return this;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        if (viewType == TYPE_IMG) {
            View itemView = LayoutInflater.from(context)
                    .inflate(R.layout.item_gallery, parent, false);
            return new ImgVH(itemView);
        } else {
            View itemView = LayoutInflater.from(context)
                    .inflate(R.layout.item_gallery_video, parent, false);
            return new VideoVH(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_IMG) {
            ImageView imageView = ((ImgVH)holder).mImageView;
            String srcPath = mFileEntityList.get(position).getRawPath();
            Glide.with(holder.getContext())
                    .load(srcPath)
                    .into(imageView);
        } else {
            ImageView preIv = ((VideoVH)holder).mPreImg;
            String rawPath = mFileEntityList.get(position).getRawPath();
            Glide.with(holder.getContext())
                    .load(rawPath)
                    .into(preIv);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mFileEntityList == null ? 0 : mFileEntityList.size();
    }

    public FileEntity getData(int pos){
        return mFileEntityList == null ? null : mFileEntityList.get(pos);
    }


    @Override
    public int getItemViewType(int position) {
        FileEntity fileEntity = mFileEntityList.get(position);
        if (fileEntity.isImg()){
            return TYPE_IMG;
        }
        return TYPE_VIDEO;
    }

    static class ImgVH extends BaseViewHolder {
        private ImageView mImageView;

        public ImgVH(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.item_gallery_iv);
        }

        public Context getContext(){
            return itemView.getContext();
        }
    }


    static class VideoVH extends BaseViewHolder {
        private ImageView mPreImg;

        public VideoVH(@NonNull View itemView) {
            super(itemView);
            mPreImg = itemView.findViewById(R.id.item_gallery_video_pre_iv);
        }

        public Context getContext(){
            return itemView.getContext();
        }
    }
}

