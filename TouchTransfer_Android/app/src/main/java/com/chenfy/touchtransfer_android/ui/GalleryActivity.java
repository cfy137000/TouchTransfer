package com.chenfy.touchtransfer_android.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.chenfy.touchtransfer_android.R;
import com.chenfy.touchtransfer_android.base.BaseActivity;
import com.chenfy.touchtransfer_android.base.bindview.BindLayout;
import com.chenfy.touchtransfer_android.base.bindview.BindView;
import com.chenfy.touchtransfer_android.dao.FileEntity;
import com.chenfy.touchtransfer_android.filehandle.FileManager;
import com.chenfy.touchtransfer_android.ui.adapter.GalleryAdapter;
import com.chenfy.touchtransfer_android.view.OnRecyclerItemClickListener;

import java.util.List;

import static com.chenfy.touchtransfer_android.dao.Contact.ATY.KEY_POS;
import static com.chenfy.touchtransfer_android.dao.Contact.ATY.KEY_VIDEO_PATH;

/**
 * Created by ChenFengyao
 * Date: 20-9-3
 */
@BindLayout(R.layout.activity_gallery)
public class GalleryActivity extends BaseActivity {

    @BindView(R.id.gallery_rv)
    private RecyclerView mGalleryRv;


    private GalleryAdapter mGalleryAdapter;

    @Override
    public void initWindow(Window window) {
        super.initWindow(window);
        // 改变状态栏颜色
        window.setStatusBarColor(getColor(R.color.status_color));
        window.setNavigationBarColor(getColor(R.color.native_bar_color));
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                        | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
    }

    @Override
    public void initViews() {
        super.initViews();

        mGalleryAdapter = new GalleryAdapter();
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        mGalleryRv.setLayoutManager(mLinearLayoutManager);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mGalleryRv);
        mGalleryRv.setAdapter(mGalleryAdapter);
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState, Intent preIntent) {
        super.initData(savedInstanceState, preIntent);
        int pos = preIntent.getIntExtra(KEY_POS, 0);

        List<FileEntity> all = FileManager.getInstance().getAll();
        mGalleryAdapter.setFileEntityList(all);
        mGalleryRv.scrollToPosition(pos);
    }

    @Override
    public void initListeners() {
        mGalleryRv.addOnItemTouchListener(new OnRecyclerItemClickListener(mGalleryRv) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh, int childId) {
                if (childId == R.id.item_play) {
                    int pos = vh.getAdapterPosition();
                    FileEntity data = mGalleryAdapter.getData(pos);
                    String rawPath = data.getRawPath();
                    Intent intent = new Intent(GalleryActivity.this, VideoAty.class);
                    intent.putExtra(KEY_VIDEO_PATH, rawPath);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.galery_out);
    }
}
