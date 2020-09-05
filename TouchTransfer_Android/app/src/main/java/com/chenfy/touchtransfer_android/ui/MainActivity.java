package com.chenfy.touchtransfer_android.ui;

import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chenfy.touchtransfer_android.ui.adapter.AlbumAdapter;
import com.chenfy.touchtransfer_android.dao.FileEntity;
import com.chenfy.touchtransfer_android.R;
import com.chenfy.touchtransfer_android.base.BaseActivity;
import com.chenfy.touchtransfer_android.base.bindview.BindLayout;
import com.chenfy.touchtransfer_android.base.bindview.BindView;
import com.chenfy.touchtransfer_android.filehandle.FileManager;
import com.chenfy.touchtransfer_android.network.NetWorkManager;
import com.chenfy.touchtransfer_android.util.ActivityUtil;
import com.chenfy.touchtransfer_android.util.DialogUtil;
import com.chenfy.touchtransfer_android.util.VibratorUtil;
import com.chenfy.touchtransfer_android.view.EmptyRecyclerView;
import com.chenfy.touchtransfer_android.view.MarginDecoration;
import com.chenfy.touchtransfer_android.view.OnRecyclerItemClickListener;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.List;

import static com.chenfy.touchtransfer_android.dao.Contact.DB.PROVIDER_DEL_URI_STR;
import static com.chenfy.touchtransfer_android.dao.Contact.DB.PROVIDER_INSERT_URI_STR;
import static com.chenfy.touchtransfer_android.dao.Contact.DIALOG_TYPE.TYPE_EXIT;

@BindLayout(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @BindView(R.id.album_rv)
    private EmptyRecyclerView mAlbumRv;
    @BindView(value = R.id.main_tool_bar)
    private Toolbar mToolbar;
    @BindView(value = R.id.add_iv, click = true)
    private View mAddView;
    @BindView(R.id.main_empty_view)
    private View mEmptyView;

    private AlbumAdapter mAlbumAdapter;
    private ContentObserver mInsertObservable;
    private ContentObserver mDelObservable;
    private NetWorkManager mManager;

    @Override
    public void initViews() {
        super.initViews();

        ActivityUtil.enableAty(NFCActivity.class);

        setSupportActionBar(mToolbar);
        mAlbumRv.setEmptyView(mEmptyView);
        mAlbumAdapter = new AlbumAdapter();
        mAlbumRv.setLayoutManager(new GridLayoutManager(this, 3));
        mAlbumRv.setAdapter(mAlbumAdapter);

        mAlbumRv.addItemDecoration(new MarginDecoration(10));//10像素的间隔
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState, Intent preIntent) {
        super.initData(savedInstanceState, preIntent);
        List<FileEntity> all = FileManager.getInstance().getAll();
        mManager = new NetWorkManager();
        mAlbumAdapter.addFileEntities(all);
        Handler handler = new Handler();
        mInsertObservable = new ContentObserver(handler) {
            @Override
            public void onChange(boolean selfChange, Uri uri) {
                String fileID = uri.getLastPathSegment();
                FileEntity fileEntity = FileManager.getInstance().getByFileID(fileID);
                mAlbumAdapter.addFileEntity(fileEntity);
            }
        };
        getContentResolver().registerContentObserver(Uri.parse(PROVIDER_INSERT_URI_STR),
                true, mInsertObservable);

        mDelObservable = new ContentObserver(handler) {
            @Override
            public void onChange(boolean selfChange, Uri uri) {
                String delID = uri.getLastPathSegment();
                if ("all".equals(delID)){
                    mAlbumAdapter.clear();
                }
            }
        };
        getContentResolver().registerContentObserver(Uri.parse(PROVIDER_DEL_URI_STR),
                true, mDelObservable);

    }

    @Override
    public void initListeners() {
        super.initListeners();
        mAlbumRv.addOnItemTouchListener(new OnRecyclerItemClickListener(mAlbumRv) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh, int childId) {
                int pos = vh.getAdapterPosition();
                showGallery(pos);
            }
        });


    }

    private void showGallery(int pos) {
        Bundle translateBundle = ActivityOptionsCompat.makeCustomAnimation(this, R.anim.galery_in,
                0).toBundle();
        Intent intent = new Intent(this, GalleryActivity.class);
        intent.putExtra(KEY_POS, pos);
        startActivity(intent, translateBundle);
    }

    @Override
    protected void onDestroy() {
        ActivityUtil.disableAty(NFCActivity.class);
        getContentResolver().unregisterContentObserver(mInsertObservable);
        getContentResolver().unregisterContentObserver(mDelObservable);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_bind_nfc:
                ActivityUtil.enableAty(NFCWriteActivity.class);
                new IntentIntegrator(this)
                        // 自定义Activity，重点是这行----------------------------
                        .setCaptureActivity(ScanQRActivity.class)
                        .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)// 扫码的类型,可选：一维码，二维码，一/二维码
                        .setPrompt("请扫描PC端二维码")// 设置提示语
                        .setCameraId(0)// 选择摄像头,可使用前置或者后置
                        .setBeepEnabled(false)// 是否开启声音,扫完码之后会"哔"的一声
                        .setBarcodeImageEnabled(false)// 扫完码之后生成二维码的图片
                        .initiateScan();// 初始化扫码
                break;
            case R.id.action_del_all:
                // 清除所有
                FileManager.getInstance().deleteAll();
                break;
            case R.id.action_about:
                // 关于页面
                startActivity(AboutActivity.class);
                break;
            case R.id.action_help:
                startActivity(HelpActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String nfcId = intent.getStringExtra(KEY_NFC_ID);
        if (!TextUtils.isEmpty(nfcId)
                && mAlbumAdapter.getItemCount() > 0
                && !mManager.isUploading()) {
            VibratorUtil.vibrator();
            mManager.transAllFile(nfcId, new NetWorkManager.TransListener() {

                @Override
                public void onInit() {
                    ActivityUtil.disableAty(NFCActivity.class);
                    mAlbumAdapter.startUpload();
                    Toast.makeText(MainActivity.this, "准备传输", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onTransferFinish(String fileID) {
                    Log.d("MainActivity", "传输完成:" + fileID);
                    mAlbumAdapter.uploadFinish(fileID);
                }

                @Override
                public void onTransferFinishAll() {
                    ActivityUtil.enableAty(NFCActivity.class);
                    VibratorUtil.vibrator();
                    Toast.makeText(MainActivity.this, "传输完成", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onConnFailed() {
                    ActivityUtil.enableAty(NFCActivity.class);
                    Toast.makeText(MainActivity.this, "没有找到PC", Toast.LENGTH_SHORT).show();
                    mAlbumAdapter.resetAll();
                }
            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mAlbumAdapter.getItemCount() > 0) {
            showExitDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showExitDialog() {
        DialogUtil.getDialog(this, TYPE_EXIT, () -> {
            FileManager.getInstance().deleteAll();
            finish();
        },null).show();
    }

    @Override
    public void doClick(View v) {
        if (v.getId() == R.id.add_iv){
            FileManager.getInstance().openFileChooser(this);
        }
    }
}