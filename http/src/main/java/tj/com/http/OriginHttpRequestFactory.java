package tj.com.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;

import tj.com.http.http.HttpMethod;
import tj.com.http.http.HttpRequest;
import tj.com.http.http.HttpRequestFactory;

/**
 * Created by Jun on 17/8/7.
 */

public class OriginHttpRequestFactory implements HttpRequestFactory {

    private HttpURLConnection mConnection;


    public OriginHttpRequestFactory(){

    }

    public void setReadTimeOut(int readTimeOut){
        mConnection.setReadTimeout(readTimeOut);
    }

    public void setConnectionTimeOut(int connecTimeOut){
        mConnection.setConnectTimeout(connecTimeOut);
    }

    @Override
    public HttpRequest createHttpRequest(URI uri, HttpMethod method) throws IOException{

            mConnection= (HttpURLConnection) uri.toURL().openConnection();

        return new OriginHttpRequest(mConnection,method,uri.toString());
    }
}
