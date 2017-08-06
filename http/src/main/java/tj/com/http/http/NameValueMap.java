package tj.com.http.http;

import java.util.Map;

/**
 * @author Jun
 */
public interface NameValueMap<K, V> extends Map<K, V> {

    String get(String name);

    void set(String name, String value);

    void setAll(Map<String, String> map);
}
