package tj.com.hellookhttp;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.tj.download.DownloadConfig;
import com.tj.download.DownloadManager;
import com.tj.download.db.DownloadHelper;
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
        Stetho.initializeWithDefaults(this);
        DownloadHelper.getInstance().init(this);

        DownloadConfig config = new DownloadConfig.Builder()
                .setCoreThreadSize(2)
                .setMaxThreadSize(4)
                .setLocalProgressThreadSize(1)
                .builder();
        DownloadManager.getInstance().init(config);
    }
}
