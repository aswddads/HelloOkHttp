package com.tj.download;

import com.tj.download.db.DownloadEntity;
import com.tj.download.db.DownloadHelper;
import com.tj.download.file.FileStorageManager;
import com.tj.download.http.DownloadCallback;
import com.tj.download.http.HttpManager;
import com.tj.download.utills.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.Response;

/**
 * Created by Jun on 17/8/6.
 */

public class DownloadRunnable implements Runnable {

    private long mStart;

    private long mEend;

    private String mUrl;

    private DownloadCallback mCallback;

    private DownloadEntity mEntity;

    public DownloadRunnable(long mStart, long mEend, String mUrl, DownloadCallback mCallback, DownloadEntity mEntity) {
        this.mStart = mStart;
        this.mEend = mEend;
        this.mUrl = mUrl;
        this.mCallback = mCallback;
        this.mEntity = mEntity;
    }

    public DownloadRunnable(long mStart, long mEend, String mUrl, DownloadCallback mCallback) {
        this.mStart = mStart;
        this.mEend = mEend;
        this.mUrl = mUrl;
        this.mCallback = mCallback;
    }

    @Override
    public void run() {
        Response response=HttpManager.getInstance().syncRequestByRange(mUrl,mStart,mEend);
        if (response==null&&mCallback!=null){
            mCallback.fail(HttpManager.NETWORK_CODE,"NetWork error!");
            return;
        }

        File file = FileStorageManager.getInstance().getFileByName(mUrl);

        long progress=0;
        try {
            RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rwd");

            randomAccessFile.seek(mStart);

            byte[] buffer = new byte[1024 * 500];
            int len;
            InputStream inStream = response.body().byteStream();
            while((len=inStream.read(buffer,0,buffer.length))!=-1){
                try {
                    randomAccessFile.write(buffer,0,len);
                    progress+=len;
                    mEntity.setProgress_position(progress);
                    Logger.debug("tanjun","progress---->"+progress);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            randomAccessFile.close();
            mCallback.success(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            DownloadHelper.getInstance().insert(mEntity);
        }


    }
}
