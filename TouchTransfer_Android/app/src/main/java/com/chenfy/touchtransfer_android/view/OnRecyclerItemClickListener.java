package com.chenfy.touchtransfer_android.view;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by ChenFengyao
 * Date: 20-9-3
 */
public class OnRecyclerItemClickListener extends RecyclerView.SimpleOnItemTouchListener {
    private static final String TAG = "OnRecyclerItemClickListener";
    private GestureDetectorCompat mGestureDetector;

    private RecyclerView recyclerView;


    public OnRecyclerItemClickListener(RecyclerView recyclerView) {

        this.recyclerView = recyclerView;
        mGestureDetector = new GestureDetectorCompat(recyclerView.getContext(), new ItemTouchHelperGestureListener());

    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return mGestureDetector.onTouchEvent(e);
    }


    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
    }


    private class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {


            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            RecyclerView.ViewHolder vh = null;
            if (child != null) {
                vh = recyclerView.getChildViewHolder(child);
            } else {
                return false;
            }
            if (child instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) child;

                int x = (int) (e.getX() - child.getLeft());
                int y = (int) (e.getY() - child.getTop());
                int childCount = group.getChildCount();

                for (int i = childCount - 1; i >= 0; i--) {
                    View v = group.getChildAt(i);
                    if (v == null) {
                        continue;
                    }
                    if (x >= v.getLeft() && x <= v.getRight()
                            && y >= v.getTop() && y <= v.getBottom()) {
                        if (v.isFocusable()) {
                            onItemClick(vh, v.getId());
                            return true;
                        }
                    }
                }

            }
            onItemClick(vh, 0);

            return true;

        }


        @Override
        public void onLongPress(MotionEvent e) {
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null) {
                RecyclerView.ViewHolder vh = recyclerView.getChildViewHolder(child);
                onLongClick(vh);
            }

        }

    }


    public void onLongClick(RecyclerView.ViewHolder vh) {
    }

    public void onItemClick(RecyclerView.ViewHolder vh, int childId) {
    }

}
