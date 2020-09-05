package com.chenfy.touchtransfer_android.base;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import com.chenfy.touchtransfer_android.R;
import com.chenfy.touchtransfer_android.base.bindview.BindLayout;
import com.chenfy.touchtransfer_android.base.bindview.BindView;
import com.chenfy.touchtransfer_android.dao.Contact;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener, Contact.ATY {
    private static final String TAG = "BaseActivity";
    private List<OnDestroyListener> mDestroyListener;


    public void initViews() {
    }

    public void initListeners() {
    }

    public void doClick(View v) {
    }

    public void initWindow(Window window) {}


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        mDestroyListener = new ArrayList<>();
        initWindow(getWindow());

        super.onCreate(savedInstanceState);


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        _setContentView();

        viewInject();
        initViews();

        initData(savedInstanceState, getIntent());
        initListeners();

    }


    @LayoutRes
    protected int getLayout() {
        return 0;
    }


    private void viewInject() {
        Class clazz = this.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {

            if (field.isAnnotationPresent(BindView.class)) {
                BindView bindView = field.getAnnotation(BindView.class);
                int id = bindView.value();
                if (id > 0) {
                    field.setAccessible(true);
                    try {
                        View view = this.findViewById(id);
                        field.set(this, view);
                        if (bindView.click()) {
                            view.setOnClickListener(this);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    protected void initData(@Nullable Bundle savedInstanceState, Intent preIntent) {
    }


    private void _setContentView() {
        if (getLayout() != 0) {
            setContentView(getLayout());
        } else {
            Class clazz = this.getClass();
            if (clazz.isAnnotationPresent(BindLayout.class)) {
                BindLayout bindContent = (BindLayout) clazz.getAnnotation(BindLayout.class);
                int id = bindContent.value();
                if (id > 0) {
                    this.setContentView(id);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (mDestroyListener.size() > 0) {
            for (int i = mDestroyListener.size() - 1; i >= 0; i--) {
                OnDestroyListener listener = mDestroyListener.get(i);
                listener.onDestroy();
            }
        }
        mDestroyListener.clear();
        mDestroyListener = null;
        super.onDestroy();

    }


    protected void startActivity(Class<? extends Activity> atyClass) {
        Intent intent = new Intent(this, atyClass);
        startActivity(intent);
    }

    protected void startActivity(Class<? extends Activity> atyClass, int anim) {
        Intent intent = new Intent(this, atyClass);
        Bundle translateBundle = ActivityOptionsCompat.makeCustomAnimation(this, anim,
                0).toBundle();
        startActivity(intent, translateBundle);
    }

    // 需要重新显示Dialog时调用
    public void reShowDialog(int dialogID){

    }


    public interface OnDestroyListener {
        void onDestroy();
    }

    @Override
    public void onClick(View v) {
        doClick(v);
    }

}
