package com.chenfy.touchtransfer_android.util;

import android.content.Context;

import com.chenfy.touchtransfer_android.R;
import com.chenfy.touchtransfer_android.base.BaseDialog;
import com.chenfy.touchtransfer_android.dao.Contact;

/**
 * Created by ChenFengyao
 * Date: 20-9-3
 */
public class DialogUtil implements Contact.DIALOG_TYPE {
    public static BaseDialog getDialog(Context context,
                                       int type,
                                       BaseDialog.Builder.OnConfirmListener confirmListener,
                                       BaseDialog.Builder.OnCancelListener cancelListener) {
        BaseDialog.Builder builder = BaseDialog.Builder
                .with(context)
                .setOnConfirmListener(confirmListener)
                .setOnCancelListener(cancelListener)
                .setID(type);
        switch (type) {
            case TYPE_EXIT:
                builder.setTitle(R.string.dialog_exit_title)
                        .setMessage(R.string.dialog_exit_msg)
                        .setLayoutSwap(true);
                break;
        }
        return builder.build();
    }
}
