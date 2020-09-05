package com.chenfy.touchtransfer_android.base;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.chenfy.touchtransfer_android.R;


/**
 * Common Dialog
 */
public class BaseDialog extends DialogFragment implements View.OnClickListener {
    private Builder.OnConfirmListener mOnConfirmListener;
    private Builder.OnCancelListener mOnCancelListener;
    private int mTitle;
    private int mMessage;

    private TextView mTitleTv;
    private TextView mContentTv;
    private TextView mConfirmTv;
    private TextView mCancelTv;

    private int confirmBtnText;
    private int cancelBtnText;
    private boolean onlyConfirm;

    private Context mContext;

    private boolean mLayoutSwap;
    private LinearLayout mClickBar;
    private static final String TAG = "BaseDialog";
    private int mDialogID;
    private static final String KEY_SHOULD_DISMISS = "shouldDismiss";
    private static final String KEY_DIALOG_ID = "dialogId";
    private static final String KEY_NONE_LISTENER = "noneListener";

    private static final String KEY_TITLE = "title";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_CONFIRM_TEXT = "confirmText";
    private static final String KEY_CANCLE_TEXT = "cancelText";
    private static final String KEY_LAYOUT_SWAP = "layoutSwap";
    private static final String KEY_ONLY_CONFIRM = "onlySwap";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_base, container, false);
    }

    public BaseDialog setContext(Context context) {
        mContext = context;
        return this;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTitleTv = view.findViewById(R.id.dialog_title_tv);
        mContentTv = view.findViewById(R.id.dialog_content_tv);
        mConfirmTv = view.findViewById(R.id.dialog_confirm_tv);
        mCancelTv = view.findViewById(R.id.dialog_cancel_tv);
        mClickBar = view.findViewById(R.id.dialog_click_bar);

        initView();

        Dialog dialog = getDialog();
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);


    }

    private void initView() {
        if (mLayoutSwap) {
            mClickBar.removeView(mConfirmTv);
            mClickBar.addView(mConfirmTv);
        }

        mConfirmTv.setOnClickListener(this);
        mCancelTv.setOnClickListener(this);


        if (mTitle == 0) {
            mTitleTv.setVisibility(View.GONE);
        } else {
            mTitleTv.setVisibility(View.VISIBLE);
            mTitleTv.setText(getString(mTitle));
        }
        if (mMessage != 0) {
            mContentTv.setText(getString(mMessage));
        }

        if (confirmBtnText != 0) {
            mConfirmTv.setText(getString(confirmBtnText));
        }

        if (cancelBtnText != 0) {
            mCancelTv.setText(getString(cancelBtnText));
        }

        if (onlyConfirm) {
            mCancelTv.setVisibility(View.GONE);
        }
    }

    public void show() {
        BaseActivity activity = (BaseActivity) mContext;
        String string = activity.getString(mMessage);
        Log.d(TAG, string);
        show(activity.getSupportFragmentManager(), string);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }


    public Builder.OnConfirmListener getOnConfirmListener() {
        return mOnConfirmListener;
    }

    public BaseDialog setOnConfirmListener(Builder.OnConfirmListener onConfirmListener) {
        mOnConfirmListener = onConfirmListener;
        return this;
    }

    public Builder.OnCancelListener getOnCancelListener() {
        return mOnCancelListener;
    }

    public BaseDialog setOnCancelListener(Builder.OnCancelListener onCancelListener) {
        mOnCancelListener = onCancelListener;
        return this;
    }

    public int getTitle() {
        return mTitle;
    }

    public BaseDialog setTitle(int title) {
        mTitle = title;
        return this;
    }

    public int getMessage() {
        return mMessage;
    }

    public BaseDialog setMessage(int message) {
        mMessage = message;
        return this;
    }

    public int getConfirmBtnText() {
        return confirmBtnText;
    }

    public BaseDialog setConfirmBtnText(int confirmBtnText) {
        this.confirmBtnText = confirmBtnText;
        return this;
    }

    public int getCancelBtnText() {
        return cancelBtnText;
    }

    public BaseDialog setCancelBtnText(int cancelBtnText) {
        this.cancelBtnText = cancelBtnText;
        return this;
    }

    public BaseDialog setOnlyConfirm(boolean onlyConfirm) {
        this.onlyConfirm = onlyConfirm;
        return this;
    }

    public BaseDialog setLayoutSwap(boolean layoutSwap) {
        this.mLayoutSwap = layoutSwap;
        return this;
    }


    public int getDialogID() {
        return mDialogID;
    }

    public BaseDialog setDialogID(int dialogID) {
        mDialogID = dialogID;
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_confirm_tv:
                if (mOnConfirmListener != null) {
                    mOnConfirmListener.onConfirm();
                }
                break;
            case R.id.dialog_cancel_tv:
                if (mOnCancelListener != null) {
                    mOnCancelListener.onCancel();
                }
                break;
        }
        dismiss();
    }

    public static class Builder {
        private Context mContext;
        private int mTitle;
        private int mMessage;
        private OnConfirmListener mOnConfirmListener;
        private OnCancelListener mOnCancelListener;
        private int confirmBtnText;
        private int cancelBtnText;
        private boolean onlyConfirm;
        private boolean mLayoutSwap;
        private int mID;

        private Builder(Context context) {
            mContext = context;
        }

        public static Builder with(Context context) {
            return new Builder(context);
        }

        public Builder setTitle(int title) {
            mTitle = title;
            return this;
        }

        public Builder setMessage(int message) {
            mMessage = message;
            return this;
        }

        public Builder setOnConfirmListener(OnConfirmListener onConfirmListener) {
            mOnConfirmListener = onConfirmListener;
            return this;
        }

        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            mOnCancelListener = onCancelListener;
            return this;
        }


        public Builder setConfirmBtnText(int confirmBtnText) {
            this.confirmBtnText = confirmBtnText;
            return this;
        }

        public Builder setCancelBtnText(int cancelBtnText) {
            this.cancelBtnText = cancelBtnText;
            return this;
        }

        public Builder setOnlyConfirm(boolean onlyConfirm) {
            this.onlyConfirm = onlyConfirm;
            return this;
        }

        public Builder setLayoutSwap(boolean layoutSwap) {
            this.mLayoutSwap = layoutSwap;
            return this;
        }

        public Builder setID(int id) {
            this.mID = id;
            return this;
        }

        public BaseDialog build() {
            BaseDialog baseDialog = new BaseDialog();
            baseDialog.setTitle(mTitle);
            baseDialog.setMessage(mMessage)
                    .setConfirmBtnText(confirmBtnText)
                    .setCancelBtnText(cancelBtnText)
                    .setOnlyConfirm(onlyConfirm)
                    .setLayoutSwap(mLayoutSwap)
                    .setContext(mContext)
                    .setDialogID(mID);
            if (mOnCancelListener != null) {
                baseDialog.setOnCancelListener(mOnCancelListener);
            }
            if (mOnConfirmListener != null) {
                baseDialog.setOnConfirmListener(mOnConfirmListener);
            }


            return baseDialog;
        }

        public interface OnConfirmListener {
            void onConfirm();
        }

        public interface OnCancelListener {
            void onCancel();
        }
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean(KEY_NONE_LISTENER)){
                mTitle = savedInstanceState.getInt(KEY_TITLE,0);
                mMessage = savedInstanceState.getInt(KEY_MESSAGE, 0);
                confirmBtnText = savedInstanceState.getInt(KEY_CONFIRM_TEXT,0);
                cancelBtnText = savedInstanceState.getInt(KEY_CANCLE_TEXT, 0);
                mLayoutSwap = savedInstanceState.getBoolean(KEY_LAYOUT_SWAP, false);
                onlyConfirm = savedInstanceState.getBoolean(KEY_ONLY_CONFIRM, false);
                initView();
                return;
            }

            if (savedInstanceState.getBoolean(KEY_SHOULD_DISMISS, false)) {
                dismiss();
                mDialogID = savedInstanceState.getInt(KEY_DIALOG_ID, -1);
                if (getContext() instanceof BaseActivity) {
                    ((BaseActivity) getContext()).reShowDialog(mDialogID);
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_DIALOG_ID, mDialogID);
        if (mOnCancelListener == null && mOnConfirmListener == null) {
            outState.putBoolean(KEY_SHOULD_DISMISS, false);
            outState.putBoolean(KEY_NONE_LISTENER, true);

            outState.putInt(KEY_TITLE, mTitle);
            outState.putInt(KEY_MESSAGE, mMessage);
            outState.putInt(KEY_CONFIRM_TEXT, confirmBtnText);
            outState.putInt(KEY_CANCLE_TEXT, cancelBtnText);
            outState.putBoolean(KEY_LAYOUT_SWAP, mLayoutSwap);
            outState.putBoolean(KEY_ONLY_CONFIRM, onlyConfirm);

        } else {
            outState.putBoolean(KEY_NONE_LISTENER, false);
            outState.putBoolean(KEY_SHOULD_DISMISS, true);
        }
    }
}
