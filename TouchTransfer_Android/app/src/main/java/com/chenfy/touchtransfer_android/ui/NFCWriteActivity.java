package com.chenfy.touchtransfer_android.ui;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.chenfy.touchtransfer_android.R;
import com.chenfy.touchtransfer_android.base.BaseActivity;
import com.chenfy.touchtransfer_android.base.bindview.BindLayout;
import com.chenfy.touchtransfer_android.util.ActivityUtil;
import com.chenfy.touchtransfer_android.util.NFCUtil;
import com.chenfy.touchtransfer_android.util.VibratorUtil;

import static com.chenfy.touchtransfer_android.dao.Contact.ATY.KEY_NFC_ID;

@BindLayout(R.layout.activity_nfc_write)
public class NFCWriteActivity extends BaseActivity {
    private static final String TAG = "NfcWriteActivity";
    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    private String mNfcid;

    @Override
    protected void initData(@Nullable Bundle savedInstanceState, Intent preIntent) {
        super.initData(savedInstanceState, preIntent);
        mNfcid = preIntent.getStringExtra(KEY_NFC_ID);
        Log.d(TAG, String.valueOf(mNfcid));
        if (mNfcAdapter == null) {
            mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        }
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, NFCWriteActivity.class), 0);
        doSomethingWithIntent(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        doSomethingWithIntent(intent);
        finish();
    }

    private void doSomethingWithIntent(Intent intent) {
        final Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag == null) {
            return;
        }
        NFCUtil.writeNFC(mNfcid,tag, new NFCUtil.NFCWriteCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(NFCWriteActivity.this, "写入成功", Toast.LENGTH_SHORT).show();
                VibratorUtil.vibrator();
            }

            @Override
            public void onFailed() {
                VibratorUtil.vibrator();
                Toast.makeText(NFCWriteActivity.this, "写入失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        ActivityUtil.enableAty(NFCWriteActivity.class);
        super.onDestroy();
    }
}
