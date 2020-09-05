package com.chenfy.touchtransfer_android.util;

import android.content.Context;
import android.util.DisplayMetrics;

import com.chenfy.touchtransfer_android.base.App;

/**
 * Created by ChenFengyao
 * Date: 20-9-2
 */
public class ScreenUtil {
    private static ScreenInfo sScreenInf;

    /**
     * Change from DP unit to PX (pixel) according to the resolution of mobile phone
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * Change from PX (pixel) to DP according to the resolution of mobile phone
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    public static ScreenInfo getScreenSize() {
        if (sScreenInf != null){
            return sScreenInf;
        }
        Context context = App.getInstance();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int height1 = dm.heightPixels;
        int width1 = dm.widthPixels;

        ScreenInfo screenInfo = new ScreenInfo();
        screenInfo.setHeight(height1);
        screenInfo.setWidth(width1);
        sScreenInf = screenInfo;
        return screenInfo;
    }

    public static class ScreenInfo{
        private int mHeight;
        private int mWidth;

        public int getHeight() {
            return mHeight;
        }

        public ScreenInfo setHeight(int height) {
            mHeight = height;
            return this;
        }

        public int getWidth() {
            return mWidth;
        }

        public ScreenInfo setWidth(int width) {
            mWidth = width;
            return this;
        }
    }

}
