package tj.com.http.service;

import tj.com.http.http.HttpMethod;

/**
 * Created by Jun on 17/8/7.
 */

public class TJRequest {
    private String mUrl;
    private HttpMethod mMethod;
    private byte[] mData;
    private TJResponse mResponse;
    private String mContentType;

    public String getmContentType() {
        return mContentType;
    }

    public void setmContentType(String mContentType) {
        this.mContentType = mContentType;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public HttpMethod getmMethod() {
        return mMethod;
    }

    public void setmMethod(HttpMethod mMethod) {
        this.mMethod = mMethod;
    }

    public byte[] getmData() {
        return mData;
    }

    public void setmData(byte[] mData) {
        this.mData = mData;
    }

    public TJResponse getmResponse() {
        return mResponse;
    }

    public void setmResponse(TJResponse mResponse) {
        this.mResponse = mResponse;
    }
}
