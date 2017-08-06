package tj.com.http;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import tj.com.http.http.HttpMethod;
import tj.com.http.http.HttpRequest;
import tj.com.http.http.HttpRequestFactory;

/**
 * Created by Jun on 17/8/6.
 */

public class OkHttpRequestFactory implements HttpRequestFactory {

    private OkHttpClient mClient;

    public OkHttpRequestFactory(){
        this.mClient=new OkHttpClient();
    }

    public OkHttpRequestFactory(OkHttpClient client){
        this.mClient=client;
    }

    @Override
    public HttpRequest createHttpRequest(URI uri, HttpMethod method) {
        return new OkHttpRequest(mClient,method,uri.toString());
    }

    public void setReadTimeOut(int readTimeOut){
        this.mClient=mClient.newBuilder()
                .readTimeout(readTimeOut, TimeUnit.MILLISECONDS)
                .build();
    }

    public void setWriteTimeOut(int writeTimeOut){
        this.mClient=mClient.newBuilder()
                .writeTimeout(writeTimeOut,TimeUnit.MILLISECONDS)
                .build();
    }

    public void setConnectionTime(int connectionTimeOut){
        this.mClient=mClient.newBuilder()
                .connectTimeout(connectionTimeOut,TimeUnit.MILLISECONDS)
                .build();
    }
}
