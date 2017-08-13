package tj.com.http.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tj.com.http.http.HttpMethod;
import tj.com.http.service.convert.Convert;

/**
 * Created by Jun on 17/8/8.
 */

public class TJAPIProvider {

    private static final String ENCODING="utf-8";
    private static WorkStation sWorkStation = new WorkStation();
    private static final List<Convert> sConvert  =  new ArrayList<>();

    public static byte[] encodeParam(Map<String,String> value){
        if (value==null||value.size()==0){
            return null;
        }
        StringBuffer buffer = new StringBuffer();
        int count = 0;
        for (Map.Entry<String, String> entry : value.entrySet()) {
            try {
                buffer.append(URLEncoder.encode(entry.getKey(),ENCODING))
                .append("=")
                .append(URLEncoder.encode(entry.getValue(),ENCODING));
                if (count!=value.size()-1){
                    buffer.append("&");
                }
                count++;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return buffer.toString().getBytes();
    }

    public static void helloworld(String url, Map<String,String> value,TJResponse<String> response){
        TJRequest request = new TJRequest();
        request.setmUrl(url);
        request.setmMethod(HttpMethod.POST);
        request.setmData(encodeParam(value));
        request.setmResponse(response);
        sWorkStation.add(request);
    }
}
