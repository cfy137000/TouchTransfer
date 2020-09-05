package com.chenfy.touchtransfer_android.util;

import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

public class NFCUtil {
    public static final String TAG = "NFCUtil";
    public static NdefMessage[] getNdefMsg(Intent intent) {
        if (intent == null)
            return null;

        //nfc卡支持的格式
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        String[] temp = tag.getTechList();
        for (String s : temp) {
            Log.i(TAG, "resolveIntent tag: " + s);
        }


        String action = intent.getAction();

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action) ||
                NfcAdapter.ACTION_TECH_DISCOVERED.equals(action) ||
                NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
            Parcelable[] rawMessage = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] ndefMessages = null;

            // 判断是哪种类型的数据 默认为NDEF格式
            if (rawMessage != null) {
                Log.i(TAG, "getNdefMsg: ndef格式 ");
                ndefMessages = new NdefMessage[rawMessage.length];
                for (int i = 0; i < rawMessage.length; i++) {
                    ndefMessages[i] = (NdefMessage) rawMessage[i];
                }
            } else {
                //未知类型 (公交卡类型)
                Log.i(TAG, "getNdefMsg: 未知类型");
                //对应的解析操作，在Github上有
            }


            return ndefMessages;
        }

        return null;
    }

    public interface NFCWriteCallback{
        void onSuccess();
        void onFailed();
    }

    public static void writeNFC(String nfcID,Tag tag,NFCWriteCallback callback) {
        // 这里是将数据写入NFC卡中
        NdefMessage ndefMessage = new NdefMessage(
                new NdefRecord[]{NdefRecord.createExternal("com.chenfy.touchtransfer_android", "nfc", nfcID.getBytes())
                        //,NdefRecord.createApplicationRecord("com.chenfy.touchtransfer_android")
                });
        int size = ndefMessage.toByteArray().length;
        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();
                if (!ndef.isWritable()) {
                    return;
                }
                if (ndef.getMaxSize() < size) {
                    return;
                }
                try {
                    ndef.writeNdefMessage(ndefMessage);
                    if (callback != null){
                        callback.onSuccess();
                    }
                } catch (FormatException e) {
                    e.printStackTrace();
                }
            } else {
                NdefFormatable format = NdefFormatable.get(tag);
                format.connect();
                format.format(ndefMessage);
                if (format.isConnected()) {
                    format.close();
                }
                if (callback != null){
                    callback.onSuccess();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            callback.onFailed();
        }
    }
}
