package tj.com.http.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

/**
 * @author Jun
 */

public interface HttpRequest extends Header {

    HttpMethod getMethod();

    URI getUri();

    OutputStream getBody();

    HttpResponse execute() throws IOException;

}
