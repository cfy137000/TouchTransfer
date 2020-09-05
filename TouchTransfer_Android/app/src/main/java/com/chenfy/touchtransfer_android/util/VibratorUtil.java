package com.chenfy.touchtransfer_android.util;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;

import com.chenfy.touchtransfer_android.base.App;
import com.chenfy.touchtransfer_android.ui.MainActivity;

import static android.content.Context.VIBRATOR_SERVICE;

public class VibratorUtil {
    /**
     * 震动
     */
    public static void vibrator(){
        Vibrator vibrator = (Vibrator) App.getInstance().getSystemService(VIBRATOR_SERVICE);
        VibrationEffect vibrationEffect =
                VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE);
        vibrator.vibrate(vibrationEffect);
    }
}
