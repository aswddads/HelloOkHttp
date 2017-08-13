package tj.com.http.service;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import tj.com.http.HttpRequestProvider;
import tj.com.http.http.HttpRequest;

/**
 * Created by Jun on 17/8/7.
 */

public class WorkStation {
    private static final int MAX_REQUEST_SIZE = 60;
    private static final ThreadPoolExecutor sThreadPool = new ThreadPoolExecutor(0, Integer.MAX_VALUE
            , 60, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), new ThreadFactory() {
        private AtomicInteger index = new AtomicInteger();

        @Override
        public Thread newThread(@NonNull Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("http thread name is " + index.getAndIncrement());
            return thread;
        }
    });

    private Deque<TJRequest> mRunning = new ArrayDeque<>();
    private Deque<TJRequest> mCache = new ArrayDeque<>();
    private HttpRequestProvider mRequestProvider;

    public WorkStation() {
        mRequestProvider = new HttpRequestProvider();
    }

    public void add(TJRequest request) {
        if (mRunning.size() > MAX_REQUEST_SIZE) {
            mCache.add(request);
        } else {
            doHttpRequest(request);
        }
    }

    public void finish(TJRequest request) {
        mRunning.remove(request);
        if (mRunning.size() > 60) {
            return;
        }
        if (mCache.size() == 0) {
            return;
        }

        Iterator<TJRequest> iterator = mCache.iterator();

        while (iterator.hasNext()) {
            TJRequest next = iterator.next();
            mRunning.add(next);
            iterator.remove();
            doHttpRequest(next);
        }
    }

    public void doHttpRequest(TJRequest request) {
        HttpRequest httpRequest = null;
        try {
            httpRequest = mRequestProvider.getHttpRequest(URI.create(request.getmUrl()), request.getmMethod());
        } catch (IOException e) {
            e.printStackTrace();
        }
        sThreadPool.execute(new HttpRunnable(httpRequest, request,this));
    }
}
