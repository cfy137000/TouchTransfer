package com.chenfy.touchtransfer_android.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chenfy.touchtransfer_android.dao.FileEntity;
import com.chenfy.touchtransfer_android.R;

import java.util.ArrayList;
import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumVH> {
    private List<FileEntity> mFileEntities;

    public AlbumAdapter() {
        mFileEntities = new ArrayList<>();
    }

    public void startUpload(){
        for (FileEntity fileEntity : mFileEntities) {
            fileEntity.setUploading(true);
        }
        notifyDataSetChanged();
    }

    public void uploadFinish(String fileID){
        for (int i = 0; i < mFileEntities.size(); i++) {
            FileEntity fileEntity = mFileEntities.get(i);
            if (fileEntity.getFileID().equals(fileID)){
                fileEntity.setUploading(false);
                notifyItemChanged(i);
                return;
            }
        }
    }

    public AlbumAdapter addFileEntities(List<FileEntity> fileEntities) {
        mFileEntities.addAll(fileEntities);
        notifyDataSetChanged();
        return this;
    }

    @NonNull
    @Override
    public AlbumVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_album, parent, false);

        return new AlbumVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumVH holder, int position) {
        FileEntity fileEntity = mFileEntities.get(position);
        String thumbPath = fileEntity.getThumbPath();
        Glide.with(holder.getContext())
                .load(thumbPath)
                .into(holder.mAlbumIv);
        if (fileEntity.isImg()){
            holder.mVideoTypeIv.setVisibility(View.GONE);
        }else {
            holder.mVideoTypeIv.setVisibility(View.VISIBLE);
        }
        if (fileEntity.isUploading()){
            holder.mProgressBar.setVisibility(View.VISIBLE);
            holder.mBg.setVisibility(View.VISIBLE);
        } else {
            holder.mProgressBar.setVisibility(View.GONE);
            holder.mBg.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mFileEntities == null ? 0 : mFileEntities.size();
    }

    public void addFileEntity(FileEntity fileEntity) {
        mFileEntities.add(fileEntity);
        notifyItemInserted(mFileEntities.size() - 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // 清除所有
    public void clear() {
        int size = mFileEntities.size();
        mFileEntities.clear();
        notifyItemRangeRemoved(0,size);
    }

    public void resetAll() {
        for (FileEntity fileEntity : mFileEntities) {
            fileEntity.setUploading(false);
        }
        notifyDataSetChanged();
    }

    public static class AlbumVH extends RecyclerView.ViewHolder {
        private ImageView mAlbumIv;
        private ImageView mVideoTypeIv;
        private ProgressBar mProgressBar;
        private View mBg;

        public AlbumVH(@NonNull View itemView) {
            super(itemView);
            mAlbumIv = itemView.findViewById(R.id.item_album_iv);
            mVideoTypeIv = itemView.findViewById(R.id.item_video_type_iv);
            mProgressBar = itemView.findViewById(R.id.item_progress);
            mBg = itemView.findViewById(R.id.item_progress_bg);
        }

        public Context getContext() {
            return itemView.getContext();
        }
    }

}
