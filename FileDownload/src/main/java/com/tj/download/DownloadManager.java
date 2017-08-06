package com.tj.download;

import android.support.annotation.NonNull;

import com.tj.download.http.DownloadCallback;
import com.tj.download.http.HttpManager;

import java.io.IOException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Jun on 17/8/6.
 */

public class DownloadManager {

    private static final int MAX_THREAD = 2;

    private static final DownloadManager sManager = new DownloadManager();

    private static final ThreadPoolExecutor sThreadPool = new ThreadPoolExecutor(
            MAX_THREAD, MAX_THREAD, 60, TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>(), new ThreadFactory() {

        private AtomicInteger mInteger = new AtomicInteger(1);

        @Override
        public Thread newThread(@NonNull Runnable r) {

            Thread thread = new Thread(r,"download thread #"+mInteger.getAndIncrement());
            return thread;
        }
    });

    public static DownloadManager getInstance(){
        return sManager;
    }
    private DownloadManager(){

    }

    public void download(String url, final DownloadCallback callback){
        HttpManager.getInstance().asyncRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful() && callback!=null){
                    callback.fail(HttpManager.NETWORK_CODE,"Network error!");
                    return;
                }
                long length = response.body().contentLength();
                if (length == -1){
                    callback.fail(HttpManager.CONTENT_LENGTH_CODE_ERROR,"content length -1!");
                    return;
                }
            }
        });
    }
}
