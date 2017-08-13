package tj.com.http.service.convert;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;

import tj.com.http.http.HttpResponse;

/**
 * Created by Jun on 17/8/8.
 */

public class JsonConvert implements Convert {

    private Gson gson = new Gson();
    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";

    @Override
    public Object parse(HttpResponse response, Type type) {
        try {
            Reader reader = new InputStreamReader(response.getBody());
            return gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isCanParse(String contentType) {
        return CONTENT_TYPE.equals(contentType);
    }

    @Override
    public Object parse(String content, Type type) throws IOException {

        return gson.fromJson(content, type);

    }
}
