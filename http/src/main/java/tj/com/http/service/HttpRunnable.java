package tj.com.http.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import tj.com.http.http.HttpRequest;
import tj.com.http.http.HttpResponse;

/**
 * Created by Jun on 17/8/8.
 */

public class HttpRunnable implements Runnable {
    private HttpRequest mHttpRequest;
    private TJRequest mRequest;
    private WorkStation mWorkStation;

    public HttpRunnable(HttpRequest mHttpRequest, TJRequest mRequest, WorkStation workStation) {
        this.mHttpRequest = mHttpRequest;
        this.mRequest = mRequest;
        this.mWorkStation = workStation;
    }

    @Override
    public void run() {
        try {
            mHttpRequest.getBody().write(mRequest.getmData());
            HttpResponse response = mHttpRequest.execute();

            String contentType=response.getHeaders().getContentType();
            mRequest.setmContentType(contentType);

            if (response.getStatus().isSuccess()) {
                if (mRequest.getmResponse() != null) {
                    mRequest.getmResponse().success(mRequest, new String(getData(response)));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            mWorkStation.finish(mRequest);
        }
    }

    public byte[] getData(HttpResponse response) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream((int) response.getContentLength());
        int len;
        byte[] data = new byte[512];
        try {
            while ((len = response.getBody().read(data)) != -1) {
                outputStream.write(data, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }
}
