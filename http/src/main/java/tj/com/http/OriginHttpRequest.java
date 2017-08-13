package tj.com.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Map;

import tj.com.http.http.HttpHeader;
import tj.com.http.http.HttpMethod;
import tj.com.http.http.HttpResponse;

/**
 * Created by Jun on 17/8/7.
 */

public class OriginHttpRequest extends BufferHttpRequest{

    private HttpURLConnection mConnection;
    private String mUrl;
    private HttpMethod mMethod;

    public OriginHttpRequest(HttpURLConnection connection,HttpMethod method,String url){
        this.mConnection=connection;
        this.mUrl=url;
        this.mMethod=method;

    }

    @Override
    public HttpMethod getMethod() {
        return mMethod;
    }

    @Override
    public URI getUri() {
        return URI.create(mUrl);
    }

    @Override
    protected HttpResponse executeInternal(HttpHeader header, byte[] data) throws IOException {
        for (Map.Entry<String, String> entry : header.entrySet()) {
            mConnection.addRequestProperty(entry.getKey(),entry.getValue());
        }
        mConnection.setDoOutput(true);
        mConnection.setDoInput(true);
        mConnection.setRequestMethod(mMethod.name());
        mConnection.connect();

        if (data!=null&&data.length>0){
            OutputStream out=mConnection.getOutputStream();
            out.write(data,0,data.length);
            out.close();
        }

        OriginHttpResponse response = new OriginHttpResponse(mConnection);
        return response;
    }
}
