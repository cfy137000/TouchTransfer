package com.chenfy.touchtransfer_android.util;

import android.content.ComponentName;
import android.content.pm.PackageManager;

import com.chenfy.touchtransfer_android.base.App;
import com.chenfy.touchtransfer_android.base.BaseActivity;

public class ActivityUtil {
    public static void enableAty(Class<? extends BaseActivity> atyClazz){
        ComponentName componentName = new ComponentName(App.getInstance(),atyClazz);
        PackageManager packageManager = App.getInstance().getPackageManager();
        packageManager.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public static void disableAty(Class<? extends BaseActivity> atyClazz){
        ComponentName componentName = new ComponentName(App.getInstance(),atyClazz);
        PackageManager packageManager = App.getInstance().getPackageManager();
        packageManager.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}
