package com.tj.download.http;

import android.content.Context;

import com.tj.download.file.FileStorageManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Jun on 17/8/6.
 */

public class HttpManager {

    public static HttpManager sManager = new HttpManager();
    public static final int NETWORK_CODE = 1;
    public static final int CONTENT_LENGTH_CODE_ERROR = 2;
    public static final int TASK_RUNNING_ERROR_CODE = 3;


    private Context mContext;

    private OkHttpClient mClient;


    public static HttpManager getInstance() {
        return sManager;
    }

    private HttpManager() {
        mClient = new OkHttpClient();
    }

    public void init(Context context) {
        this.mContext = context;
    }

    /**
     * sync request
     *
     * @param url
     * @return
     */
    public Response syncRequestByRange(String url, long start, long end) {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Range", "bytes=" + start + "-" + end)
                .build();
        try {
            return mClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * async
     *
     * @param url
     * @param callback
     */


    public void asyncRequest(final String url,final Callback callback){
        final Request request = new Request.Builder().url(url).build();
        mClient.newCall(request).enqueue(callback);
    }
    public void asyncRequest(final String url, final DownloadCallback callback) {
        final Request request = new Request.Builder().url(url).build();
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful() && callback != null) {
                    callback.fail(NETWORK_CODE, "Network request failed");
                }

                File file = FileStorageManager.getInstance().getFileByName(url);

                byte[] buffer = new byte[1024 * 500];
                int len;
                FileOutputStream fileout = new FileOutputStream(file);
                InputStream inStream = response.body().byteStream();
                while ((len = inStream.read(buffer, 0, buffer.length)) != -1) {
                    fileout.write(buffer, 0, len);
                    fileout.flush();
                }
                callback.success(file);
            }
        });
    }
}
