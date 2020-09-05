package com.chenfy.touchtransfer_android.network;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.chenfy.touchtransfer_android.dao.Contact;
import com.chenfy.touchtransfer_android.dao.FileEntity;
import com.chenfy.touchtransfer_android.filehandle.FileManager;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetWorkManager implements Contact.NET_WORK {
    private OkHttpClient client = new OkHttpClient();
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private volatile boolean mUploading;
    private ReentrantReadWriteLock mReadWriteLock = new ReentrantReadWriteLock();
    private Lock mReadLock = mReadWriteLock.readLock();
    private Lock mWriteLock = mReadWriteLock.writeLock();

    public boolean isUploading() {
        try {
            mReadLock.lock();
            return mUploading;
        } finally {
            mReadLock.unlock();
        }
    }

    public void setUploading(boolean uploading) {
        try {
            mWriteLock.lock();
            this.mUploading = uploading;
        } finally {
            mWriteLock.unlock();
        }
    }

    public void transAllFile(final String nfcID, final TransListener listener) {
        if (listener != null) {
            listener.onInit();
        }
        new Thread(() -> {
            setUploading(true);
            // 获取IP
            String remoteIP = getRemoteIP(nfcID);
            if (TextUtils.isEmpty(remoteIP)) {
                if (listener != null) {
                    mHandler.post(listener::onConnFailed);
                    return;
                }
            }

            String url = HTTP + remoteIP + ":" + PORT_HTTP + UPLOAD_PATH;
            Log.d("NetWorkManager", url);
            // 上传图片
            List<List<FileEntity>> allForTransfer = FileManager.getInstance().getAllForTransfer();

            for (int i = 0; i < allForTransfer.size(); i++) {
                List<FileEntity> fileEntities = allForTransfer.get(i);

                Request.Builder builder = new Request.Builder();
                Request req = builder.url(url)
                        .post(getRequestBody(fileEntities, i == allForTransfer.size() - 1))
                        .build();
                Call call = client.newCall(req);
                try {
                    Response execute = call.execute();
                    //TODO 检查返回值
                    mHandler.post(new TransferFinishRunnable(fileEntities, listener));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (listener != null) {
                mHandler.post(listener::onTransferFinishAll);
            }
            setUploading(false);
        }).start();
    }

    public static class TransferFinishRunnable implements Runnable {
        private List<FileEntity> mFileEntities;
        private TransListener mTransListener;

        public TransferFinishRunnable(List<FileEntity> fileEntities, TransListener transListener) {
            mFileEntities = fileEntities;
            mTransListener = transListener;
        }

        @Override
        public void run() {
            if (mTransListener != null && mFileEntities != null) {
                for (FileEntity fileEntity : mFileEntities) {
                    mTransListener.onTransferFinish(fileEntity.getFileID());
                }
            }
        }
    }

    private static RequestBody getRequestBody(List<FileEntity> files, boolean isLast) {
        //创建MultipartBody.Builder，用于添加请求的数据
        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (int i = 0; i < files.size(); i++) { //对文件进行遍历
            File file = new File(files.get(i).getRawPath()); //生成文件
            builder.addFormDataPart( //给Builder添加上传的文件
                    KEY_FILE,  //请求的名字
                    file.getName(), //文件的文字，服务器端用来解析的
                    //创建RequestBody，把上传的文件放入
                    RequestBody.create(file, MediaType.parse("multipart/form-data"))
//                    RequestBody.create(MediaType.parse("multipart/form-data"), file)
            );
        }

        if (isLast) {
            builder.addFormDataPart("ifOpen", "open");
        }
        return builder.build(); //根据Builder创建请求
    }

    private String getRemoteIP(String nfcID) {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(10 * 1000);
            byte[] bytes = nfcID.getBytes();
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length,
                    InetAddress.getByName(BROADCAST_ADDRESS), PORT_UDP);

            socket.send(packet);
            byte[] buffer = new byte[1024];
            DatagramPacket receive = new DatagramPacket(buffer, buffer.length);
            socket.receive(receive);
            String ip = new String(receive.getData()).trim();
            Log.d("NetWorkManager", ip);
            return ip;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
        return null;
    }

    public interface TransListener {
        void onInit();

        void onTransferFinish(String fileID);

        void onTransferFinishAll();

        void onConnFailed();

    }
}
