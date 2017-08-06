package tj.com.hellookhttp;

import android.app.Application;

import com.tj.download.file.FileStorageManager;
import com.tj.download.http.HttpManager;

/**
 * Created by Jun on 17/8/6.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FileStorageManager.getInstance().init(this);
        HttpManager.getInstance().init(this);
    }
}
