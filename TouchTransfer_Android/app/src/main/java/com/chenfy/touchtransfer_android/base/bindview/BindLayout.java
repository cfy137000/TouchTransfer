package com.chenfy.touchtransfer_android.base.bindview;


import androidx.annotation.LayoutRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindLayout {
    // the layout res you want to bind to activity
    @LayoutRes int value() default 0;
}
