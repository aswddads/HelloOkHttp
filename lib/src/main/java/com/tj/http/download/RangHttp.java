package com.tj.http.download;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Jun on 17/8/6.
 */

public class RangHttp {
    public static void main(String args[]) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://img.mukewang.com/5510c7a600016ea802200220-100-100.jpg")
                .addHeader("Accept-Encoding","identity")
                .addHeader("Range","bytes=50-")
                .build();
        try {
            Response response = client.newCall(request).execute();
            System.out.println("Content-Length : " + response.body().contentLength());
            if (response.isSuccessful()) {
                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    System.out.println(headers.name(i) + " : " + headers.value(i));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
