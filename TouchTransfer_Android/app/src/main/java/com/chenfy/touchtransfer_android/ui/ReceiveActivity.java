package com.chenfy.touchtransfer_android.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.chenfy.touchtransfer_android.R;
import com.chenfy.touchtransfer_android.base.BaseActivity;
import com.chenfy.touchtransfer_android.filehandle.FileManager;
import com.chenfy.touchtransfer_android.util.FileUtils;

import java.util.List;

public class ReceiveActivity extends BaseActivity {
    @Override
    protected void initData(@Nullable Bundle savedInstanceState, Intent preIntent) {
        super.initData(savedInstanceState, preIntent);
        String action = preIntent.getAction();
        String type = preIntent.getType();
        if (Intent.ACTION_SEND.equals(action)
                || Intent.ACTION_SEND_MULTIPLE.equals(action)
                && type != null){
            List<Uri> urisFromIntent = FileUtils.getUrisFromIntent(preIntent);
            if (urisFromIntent.size() > 0){
                FileManager.getInstance().addUris(urisFromIntent);
                startActivity(MainActivity.class, R.anim.galery_in);
                if (urisFromIntent.size() > 50){
                    System.out.println("最多只能选择50张图片");
                }
            }
        }
        finish();

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.galery_out);
    }
}
