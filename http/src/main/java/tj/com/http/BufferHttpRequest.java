package tj.com.http;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import tj.com.http.http.HttpHeader;
import tj.com.http.http.HttpResponse;

/**
 * @author Jun
 */

public abstract class BufferHttpRequest extends AbstractHttpRequest {

    private ByteArrayOutputStream mByteArray = new ByteArrayOutputStream();

    protected OutputStream getBodyOutputStream() {
        return mByteArray;
    }

    protected HttpResponse executeInternal(HttpHeader header) throws IOException {
        byte[] data = mByteArray.toByteArray();
        return executeInternal(header, data);
    }

    protected abstract HttpResponse executeInternal(HttpHeader header, byte[] data) throws IOException;
}
