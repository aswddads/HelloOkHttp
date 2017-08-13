package tj.com.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

import tj.com.http.http.HttpHeader;
import tj.com.http.http.HttpStatus;

/**
 * Created by Jun on 17/8/7.
 */

public class OriginHttpResponse extends AbstractHttpResponse {

    private HttpURLConnection mConnection;

    public OriginHttpResponse(HttpURLConnection connection) {
        this.mConnection = connection;
    }


    @Override
    public HttpHeader getHeaders() {
        HttpHeader header = new HttpHeader();
        for (Map.Entry<String, List<String>> entry : mConnection.getHeaderFields().entrySet()) {
            header.set(entry.getKey(),entry.getValue().get(0));
        }
        return header;
    }

    @Override
    public HttpStatus getStatus() {
        try {
            return HttpStatus.getValue(mConnection.getResponseCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getStatusMsg() {
        try {
            return mConnection.getResponseMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected InputStream getBodyInternal() throws IOException {
        return mConnection.getInputStream();
    }

    @Override
    protected void closeInternal() {
        mConnection.disconnect();
    }

    @Override
    public long getContentLength() {
        return mConnection.getContentLength();
    }
}
