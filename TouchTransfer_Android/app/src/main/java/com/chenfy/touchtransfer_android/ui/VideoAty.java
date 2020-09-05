package com.chenfy.touchtransfer_android.ui;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chenfy.touchtransfer_android.R;
import com.chenfy.touchtransfer_android.base.BaseActivity;
import com.chenfy.touchtransfer_android.base.bindview.BindLayout;
import com.chenfy.touchtransfer_android.base.bindview.BindView;
import com.chenfy.touchtransfer_android.dao.Contact;
import com.chenfy.touchtransfer_android.util.BlurTransformation;

/**
 * Created by ChenFengyao
 * Date: 20-9-4
 */
@BindLayout(R.layout.activity_video)
public class VideoAty extends BaseActivity implements Contact.ATY {
    @BindView(R.id.video_view)
    private VideoView mVideoView;
    @BindView(R.id.video_left_iv)
    private ImageView mLeftIv;
    @BindView(R.id.video_right_iv)
    private ImageView mRightIv;
    private MediaController mController;

    @Override
    public void initWindow(Window window) {
        super.initWindow(window);
        // 设置全屏显示
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void initViews() {
        super.initViews();
        mController = new MediaController(this);
        mVideoView.setMediaController(mController);
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState, Intent preIntent) {
        super.initData(savedInstanceState, preIntent);
        String videoPath = preIntent.getStringExtra(KEY_VIDEO_PATH);
        if (TextUtils.isEmpty(videoPath)) {
            Toast.makeText(this, "没找到视频", Toast.LENGTH_SHORT).show();
            finish();
        }
        Glide.with(this)
                .load(videoPath)
                .transform(new BlurTransformation())
                .into(mLeftIv);
        Glide.with(this)
                .load(videoPath)
                .transform(new BlurTransformation())
                .into(mRightIv);
//        Glide.with(this)
//                .asBitmap()
//                .load(videoPath)
//                .transform(new BlurTransformation());



        mVideoView.setVideoPath(videoPath);
    }

    @Override
    public void initListeners() {
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mVideoView.start();
                mController.show(5 * 1000);
            }
        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // 播放完成后 显示控制条
                mController.show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoView.isPlaying()) {
            mVideoView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        mVideoView.stopPlayback();
        super.onDestroy();
    }

    @Override
    public void setRequestedOrientation(int requestedOrientation) {
    }
}
