package com.tj.download;

import android.support.annotation.NonNull;

import com.tj.download.db.DownloadEntity;
import com.tj.download.db.DownloadHelper;
import com.tj.download.file.FileStorageManager;
import com.tj.download.http.DownloadCallback;
import com.tj.download.http.HttpManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

    public static final int MAX_THREAD = 2;

    public static final int LOCAL_PROGRESS_SIZE = 1;

    private static final DownloadManager sManager = new DownloadManager();


    private static  ExecutorService SLOCAL_PROGRESS_POOL= Executors.newFixedThreadPool(1);

    private HashSet<DownloadTask> mHashSet = new HashSet<>();

    private static  ThreadPoolExecutor sThreadPool = new ThreadPoolExecutor(
            MAX_THREAD, MAX_THREAD, 60, TimeUnit.MILLISECONDS, new SynchronousQueue(), new ThreadFactory() {

        private AtomicInteger mInteger = new AtomicInteger(1);

        @Override
        public Thread newThread(@NonNull Runnable r) {

            Thread thread = new Thread(r, "download thread #" + mInteger.getAndIncrement());
            return thread;
        }
    });
    private List<DownloadEntity> mCache;

    private long mLength;

    public static DownloadManager getInstance() {
        return sManager;
    }

    private DownloadManager() {

    }

    public void init(DownloadConfig config){
        sThreadPool = new ThreadPoolExecutor(
                config.getCoreThreadSize(),config.getMaxThreadSize(), 60, TimeUnit.MILLISECONDS, new SynchronousQueue(), new ThreadFactory() {

            private AtomicInteger mInteger = new AtomicInteger(1);

            @Override
            public Thread newThread(@NonNull Runnable r) {

                Thread thread = new Thread(r, "download thread #" + mInteger.getAndIncrement());
                return thread;
            }
        });
        SLOCAL_PROGRESS_POOL= Executors.newFixedThreadPool(config.getLocalProgressThreadSize());
    }

    private void finish(DownloadTask task) {
        mHashSet.remove(task);
    }

    public void download(final String url, final DownloadCallback callback) {

        final DownloadTask task = new DownloadTask(url, callback);

        if (mHashSet.contains(task)) {
            callback.fail(HttpManager.TASK_RUNNING_ERROR_CODE, "Task had run!");
            return;
        }

        mHashSet.add(task);

        mCache = DownloadHelper.getInstance().getAll(url);

        if (mCache == null || mCache.size() == 0) {


            HttpManager.getInstance().asyncRequest(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    finish(task);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful() && callback != null) {
                        callback.fail(HttpManager.NETWORK_CODE, "Network error!");
                        return;
                    }
                    mLength = response.body().contentLength();
                    if (mLength == -1) {
                        callback.fail(HttpManager.CONTENT_LENGTH_CODE_ERROR, "content length -1!");
                        return;
                    }
                    processDownload(url, mLength, callback, mCache);
                    finish(task);
                }

            });
        }else{
            // TODO: 17/8/6     Deal with had download data
            for (int i= 0 ;i<mCache.size();i++) {
                DownloadEntity entity = mCache.get(i);

                if (i==mCache.size()-1){
                    mLength = entity.getEnd_position()+1;
                }

                long startSize = entity.getStart_position()+entity.getProgress_position();
                long endSize = entity.getEnd_position();

                sThreadPool.execute(new DownloadRunnable(startSize, endSize, url, callback,entity));
            }

        }
        SLOCAL_PROGRESS_POOL.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(500);

                        File file = FileStorageManager.getInstance().getFileByName(url);
                        long fileSize = file.length();

                        int progress = (int) (fileSize* 100.0/mLength);
                        if (progress>=100){
                            callback.progress(progress);
                            return;
                        }
                        callback.progress(progress);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void processDownload(String url, long length, DownloadCallback callback,List<DownloadEntity> cache) {
        long threadDownloadSize = length / MAX_THREAD;

        if (cache==null&&cache.size()==0){
            mCache= new ArrayList<>();
        }

        for (int i = 0; i < MAX_THREAD; i++) {

            DownloadEntity entity =new DownloadEntity();

            long startSize = i * threadDownloadSize;
            long endSize = 0;
            if (endSize == MAX_THREAD - 1) {
                endSize = length - 1;
            } else {
                endSize = (i + 1) * threadDownloadSize - 1;
            }

            entity.setDownload_url(url);
            entity.setStart_position(startSize);
            entity.setEnd_position(endSize);
            entity.setThread_id(i+1);//start with 1

            sThreadPool.execute(new DownloadRunnable(startSize, endSize, url, callback,entity));
        }
    }
}
