package com.chenfy.touchtransfer_android.base;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;

/**
 * Created by ChenFengyao
 * Date: 20-9-1
 */
public class App extends Application {
    private static Context sContext;
    private ContentResolver mContentResolver;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        mContentResolver = super.getContentResolver();
    }

    public static Context getInstance(){
        return sContext;
    }

    public ContentResolver getContentResolver(){
        return mContentResolver;
    }
}
