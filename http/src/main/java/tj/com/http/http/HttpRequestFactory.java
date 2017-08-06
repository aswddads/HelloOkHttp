package tj.com.http.http;

import java.net.URI;

/**
 * Created by Jun on 17/8/6.
 */

public interface HttpRequestFactory {
    HttpRequest createHttpRequest(URI uri, HttpMethod method );
}
