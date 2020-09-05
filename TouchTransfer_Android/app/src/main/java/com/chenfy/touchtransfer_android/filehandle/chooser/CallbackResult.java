package com.chenfy.touchtransfer_android.filehandle.chooser;

import android.content.Intent;

public interface CallbackResult {

    void onActivityResult(int requestCode, int resultCode, Intent data);
}
