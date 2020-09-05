package com.chenfy.touchtransfer_android.filehandle.chooser;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class ShareFolderFragment extends Fragment {
    private SparseArray<CallbackResult> mCallBackMap = new SparseArray<>();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void startResult(Intent intent, int requestCode, CallbackResult callback) {
        mCallBackMap.put(requestCode, callback);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CallbackResult callback = mCallBackMap.get(requestCode);
        if (callback != null) {
            callback.onActivityResult(requestCode, resultCode, data);
        }
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager!= null){

            fragmentManager.beginTransaction()
                    .remove(this)
                    .commitNow();
        }

    }

}
