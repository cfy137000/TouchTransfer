package com.chenfy.touchtransfer_android.view;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class MarginDecoration extends RecyclerView.ItemDecoration {

    private int margin;

    public MarginDecoration(int margin) {
        this.margin = margin;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int itemPosition = parent.getChildLayoutPosition(view);
        int l = 0, t = 0, r = 0, b = 0;
        if (itemPosition >= 3) {
            t = margin;
        }
        if (itemPosition % 3 == 0) {
            r = margin * 2 / 3;
        }
        if (itemPosition % 3 == 1) {
            l = margin / 3;
            r = margin / 3;
        }
        if (itemPosition % 3 == 2) {
            l = margin * 2 / 3;
        }
        outRect.set(l, t, r, b);

    }

}
