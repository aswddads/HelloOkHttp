package tj.com.http;

import java.io.IOException;
import java.net.URI;

import tj.com.http.http.HttpMethod;
import tj.com.http.http.HttpRequest;
import tj.com.http.http.HttpRequestFactory;
import tj.com.http.utils.Utils;

/**
 * Created by Jun on 17/8/6.
 */

public class HttpRequestProvider {

    private HttpRequestFactory mHttpRequestFactory;

    private static boolean OKHTTP_REQUEST = Utils.isExist("okhttp3.OkHttpClient", HttpRequestProvider.class.getClassLoader());

    public HttpRequestProvider() {

        if (OKHTTP_REQUEST) {
            mHttpRequestFactory = new OkHttpRequestFactory();
        }

    }



    public HttpRequest getHttpRequest(URI uri, HttpMethod httpMethod) throws IOException {
        return mHttpRequestFactory.createHttpRequest(uri, httpMethod);
    }

    public HttpRequestFactory getHttpRequestFactory() {
        return mHttpRequestFactory;
    }

    public void setHttpRequestFactory(HttpRequestFactory httpRequestFactory) {
        mHttpRequestFactory = httpRequestFactory;
    }

}
