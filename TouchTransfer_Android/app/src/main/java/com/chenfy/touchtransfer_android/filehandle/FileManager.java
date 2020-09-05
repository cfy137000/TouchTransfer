package com.chenfy.touchtransfer_android.filehandle;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import com.chenfy.touchtransfer_android.base.App;
import com.chenfy.touchtransfer_android.base.BaseActivity;
import com.chenfy.touchtransfer_android.dao.FileEntity;
import com.chenfy.touchtransfer_android.filehandle.chooser.CallbackResult;
import com.chenfy.touchtransfer_android.filehandle.chooser.FolderWrapper;
import com.chenfy.touchtransfer_android.filehandle.dao.DataBaseManager;
import com.chenfy.touchtransfer_android.ui.MainActivity;
import com.chenfy.touchtransfer_android.util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static com.chenfy.touchtransfer_android.dao.Contact.NET_WORK.DATA_THRESHOLD;

public class FileManager {
    private static final AtomicReference<FileManager> INSTANCE = new AtomicReference<>();
    private final LinkedBlockingQueue<Runnable> mWorkQueue;
    private ExecutorService mExecutorService;
    public static final int ACTIVITY_CHOOSE_FILE = 1;


    private FileManager() {
        // 初始化线程池
        // MIUI的限制, 如果相册分享过来的URI不立即处理,那么就会报出权限问题
        // 所以, 权衡考虑, 允许最多选择50张图片
        int nThreads = 50;
        Log.d("FileManager", "nThreads:" + nThreads);
//        int nThreads = 1;
        mWorkQueue = new LinkedBlockingQueue<>();
//        mExecutorService = new ThreadPoolExecutor(nThreads, nThreads,
//                0L, TimeUnit.MILLISECONDS,
//                mWorkQueue);
        // TODO 先不限制了, 先用用看
        mExecutorService = Executors.newCachedThreadPool();

    }

    public static FileManager getInstance() {
        for (; ; ) {
            FileManager current = INSTANCE.get();
            if (current != null) {
                return current;
            }
            current = new FileManager();
            if (INSTANCE.compareAndSet(null, current)) {
                return current;
            }
        }
        File.separator
    }

    public void addUris(List<Uri> uris) {
        Log.d("FileManager", "插入:uris.size():" + uris.size());
        int i = 0;
        for (Uri uri : uris) {
            // 加入到线程池中
            Log.d("FileManager", "添加任务:" + i++);
            mExecutorService.execute(new FileInsertRunnable(uri));
        }
    }


    public List<FileEntity> getAll() {
        return DataBaseManager.getAllFileInfo();
    }

    public List<List<FileEntity>> getAllForTransfer(){
        List<FileEntity> all = getAll();
        List<List<FileEntity>> result = new ArrayList<>();
        List<FileEntity> current = new ArrayList<>();
        result.add(current);
        long sumSize = 0;
        for (int i = 0; i < all.size(); i++) {
            FileEntity fileEntity = all.get(i);
            current.add(fileEntity);
            File file = new File(fileEntity.getRawPath());
            sumSize += file.length();
            if (sumSize >= DATA_THRESHOLD){
                current = new ArrayList<>();
                result.add(current);
                sumSize = 0;
            }
        }
        return result;
    }

    public FileEntity getByFileID(String fileID) {
        return DataBaseManager.getByFileID(fileID);
    }

    /**
     * 删除所有
     */
    public void deleteAll() {
        mWorkQueue.clear();// 清除其他任务
        // 添加删除任务
        mExecutorService.submit(DataBaseManager::deleteAll);
    }

    /**
     * 打开文件选择器
     *
     * @param context context
     */
    public void openFileChooser(BaseActivity context) {
        final Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        String[] mimeTypes = {"image/*","video/*"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        FolderWrapper wrapper = new FolderWrapper(context);
        wrapper.startResult(intent, ACTIVITY_CHOOSE_FILE, new CallbackResult() {
            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                if (requestCode == ACTIVITY_CHOOSE_FILE && resultCode == Activity.RESULT_OK) {
                    List<Uri> uris = FileUtils.getUrisFromIntent(data);
                    addUris(uris);
                }
            }
        });
    }

}
