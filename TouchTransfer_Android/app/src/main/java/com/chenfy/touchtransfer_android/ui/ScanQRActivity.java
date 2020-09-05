package com.chenfy.touchtransfer_android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chenfy.touchtransfer_android.R;
import com.chenfy.touchtransfer_android.base.BaseActivity;
import com.chenfy.touchtransfer_android.base.bindview.BindLayout;
import com.chenfy.touchtransfer_android.base.bindview.BindView;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

import static com.chenfy.touchtransfer_android.dao.Contact.ATY.KEY_NFC_ID;

/**
 * Created by ChenFengyao
 * Date: 20-9-3
 */
@BindLayout(R.layout.scan_qrcode)
public class ScanQRActivity extends BaseActivity {
    private CaptureManager capture;
    @BindView(R.id.dbv_scan)
    private DecoratedBarcodeView barcodeScannerView;

    @Override
    protected void initData(@Nullable Bundle savedInstanceState, Intent preIntent) {
        super.initData(savedInstanceState, preIntent);

        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        barcodeScannerView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                capture.onPause();
                String resultText = result.getText();
                Intent intent = new Intent(ScanQRActivity.this,NFCWriteActivity.class);
                intent.putExtra(KEY_NFC_ID,resultText);
                startActivity(intent);
                finish();
            }
            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

}
