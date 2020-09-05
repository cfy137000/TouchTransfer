package com.chenfy.touchtransfer_android.view;

/**
 * Created by ChenFengyao
 * Date: 20-9-3
 * 为了处理手势冲突(多指缩放)
 */
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class TouchRecyclerView extends RecyclerView {
    public TouchRecyclerView(@NonNull Context context) {
        super(context);
    }

    public TouchRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (e.getPointerCount() > 1){
            return false;
        }
        return super.onTouchEvent(e);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (e.getPointerCount() > 1){
            return false;
        }
        return super.onInterceptTouchEvent(e);
    }
}
