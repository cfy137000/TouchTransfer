package com.chenfy.touchtransfer_android.base;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by ChenFengyao
 * Date: 20-9-4
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {
    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public Context getContext(){
        return itemView.getContext();
    }
}
