package tj.com.http.service;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import tj.com.http.service.convert.Convert;

/**
 * Created by Jun on 17/8/8.
 */

public class WrapperResponse extends TJResponse<String> {

    private TJResponse mTJResponse;
    List<Convert> mConvert;


    public WrapperResponse(TJResponse mTJResponse,List<Convert> converts) {
        this.mTJResponse = mTJResponse;
        this.mConvert=converts;
    }

    public
    @Override
    void success(TJRequest request, String data) {
        for (Convert convert : mConvert) {
            if (convert.isCanParse(request.getmContentType())){

                try {
                    Object object=convert.parse(data,getType());
                    mTJResponse.success(request,object);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return;
            }
        } 
    }

    public Type getType(){
        Type type=mTJResponse.getClass().getGenericSuperclass();
        Type[] paramType=((ParameterizedType)type).getActualTypeArguments();
        return paramType[0];
    }

    @Override
    void fail(int errorCode, String errorMessage) {

    }
}
