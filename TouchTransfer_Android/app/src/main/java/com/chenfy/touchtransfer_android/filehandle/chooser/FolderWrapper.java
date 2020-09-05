package com.chenfy.touchtransfer_android.filehandle.chooser;

import android.content.Intent;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

public class FolderWrapper {

    private static final String FILE_CHOOSER_FRAGMENT_TAG = "tag";

    private ShareFolderFragment mFragment;

    public FolderWrapper(FragmentActivity activity) {
        mFragment = getFolderFragment(activity);
    }

    public static FolderWrapper with(FragmentActivity activity) {
        return new FolderWrapper(activity);
    }

    private ShareFolderFragment getFolderFragment(FragmentActivity activity) {
        FragmentManager manager = activity.getSupportFragmentManager();
        ShareFolderFragment fragment = (ShareFolderFragment) manager.findFragmentByTag(FILE_CHOOSER_FRAGMENT_TAG);
        if (fragment == null) {

            fragment = new ShareFolderFragment();
        }
        manager.beginTransaction().add(fragment, FILE_CHOOSER_FRAGMENT_TAG).commitNow();
        return fragment;
    }

    public void startResult(Intent intent, int requestCode, CallbackResult callback) {
        mFragment.startResult(intent, requestCode, callback);
    }


}
