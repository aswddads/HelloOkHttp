package tj.com.hellookhttp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.tj.download.DownloadManager;
import com.tj.download.http.DownloadCallback;
import com.tj.download.utills.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import tj.com.http.service.TJAPIProvider;
import tj.com.http.service.TJRequest;
import tj.com.http.service.TJResponse;

public class MainActivity extends AppCompatActivity {
    private ImageView mImageView;

    private ImageView mImageView1;

    private ProgressBar mProgress;

    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = (ImageView) findViewById(R.id.image);
        mProgress = (ProgressBar) findViewById(R.id.progress);

        Map<String,String> map = new HashMap<>();
        map.put("username","tanjun");
        map.put("userage","20");

        TJAPIProvider.helloworld("", map, new TJResponse<String>() {
            @Override
            public void success(TJRequest request, String data) {
                Logger.debug("tanjun","success="+data);
            }

            @Override
            public void fail(int errorCode, String errorMessage) {

            }
        });

        //File file=FileStorageManager.getInstance().getFileByName("http://www.qq.com");
       // Logger.debug("tanjun","file
        // path = "+file.getAbsoluteFile());

//        HttpManager.getInstance().asyncRequest("https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png"
//                , new DownloadCallback() {
//                    @Override
//                    public void success(File file) {
//                        final Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                imageView.setImageBitmap(bitmap);
//                            }
//                        });
//                        Logger.debug("tanjun","success :"+file.getAbsoluteFile());
//                    }
//
//                    @Override
//                    public void fail(int errorCode, String errorMessage) {
//                        Logger.debug("tanjun","fail :"+errorCode+" "+errorMessage);
//                    }
//
//                    @Override
//                    public void progress(int progress) {
//
//                    }
//                });
        DownloadManager.getInstance().download("http://szimg.mukewang.com/59118b92000147bf05400300-360-202.jpg", new DownloadCallback() {
            @Override
            public void success(File file) {

                if (count<1){
                    count++;
                    return;
                }

                final Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mImageView.setImageBitmap(bitmap);
                            }
                        });

                Logger.debug("tanjun","success :"+file.getAbsoluteFile());
            }

            @Override
            public void fail(int errorCode, String errorMessage) {
                Logger.debug("tanjun","fail :"+errorCode+" "+errorMessage);
            }

            @Override
            public void progress(int progress) {
                Logger.debug("tanjun","progress :"+progress);
                mProgress.setProgress(progress);

            }
        });
    }
}
