package tj.com.http.service.convert;

import java.io.IOException;
import java.lang.reflect.Type;

import tj.com.http.http.HttpResponse;

/**
 * Created by Jun on 17/8/8.
 */

public interface Convert {
    Object parse(HttpResponse response, Type type);
    boolean isCanParse(String contentType);
    Object parse(String content,Type type) throws IOException;

}
