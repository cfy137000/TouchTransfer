package com.chenfy.touchtransfer_android.ui;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;

import com.chenfy.touchtransfer_android.R;
import com.chenfy.touchtransfer_android.base.BaseActivity;
import com.chenfy.touchtransfer_android.util.NFCUtil;


public class NFCActivity extends BaseActivity {

    @Override
    protected void initData(@Nullable Bundle savedInstanceState, Intent preIntent) {
        super.initData(savedInstanceState, preIntent);
        disposeIntent(preIntent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        disposeIntent(intent);
    }

    private void disposeIntent(Intent intent){
        String cardId = getCardId(intent);
        if (cardId != null) {
            Log.d("NFCActivity", String.format("NFC ID:%s", cardId));
            Intent startIntent = new Intent();
            startIntent.setClass(this,MainActivity.class);
            startIntent.putExtra(KEY_NFC_ID,cardId);
            Bundle translateBundle = ActivityOptionsCompat.makeCustomAnimation(this, R.anim.galery_in,
                    0).toBundle();
            startActivity(startIntent,translateBundle);
        }
        finish();
    }

    private String getCardId(Intent intent) {
        NdefMessage[] msgs = NFCUtil.getNdefMsg(intent); //重点功能，解析nfc标签中的数据

        if (msgs == null) {
            Toast.makeText(this, "非NFC启动", Toast.LENGTH_SHORT).show();
        } else {
            NdefMessage msg = msgs[0];
            for (NdefRecord record : msg.getRecords()) {
                byte[] type = record.getType();
                String typeStr = new String(type);
                if ("com.chenfy.touchtransfer_android:nfc".equals(typeStr)){
                    byte[] payload = record.getPayload();
                    String cardID = new String(payload);
                    return cardID;
                }
            }
        }
        return null;

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.galery_out);
    }

}
