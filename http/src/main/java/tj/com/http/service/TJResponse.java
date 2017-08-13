package tj.com.http.service;

/**
 * Created by Jun on 17/8/7.
 */

public abstract class TJResponse<T> {
    abstract void success(TJRequest request,T data);

    abstract void fail(int errorCode,String errorMessage);
}
