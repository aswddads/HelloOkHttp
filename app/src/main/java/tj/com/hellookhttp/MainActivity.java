package tj.com.hellookhttp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.tj.download.file.FileStorageManager;
import com.tj.download.http.DownloadCallback;
import com.tj.download.http.HttpManager;
import com.tj.download.utills.Logger;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.image);

        File file=FileStorageManager.getInstance().getFileByName("http://www.qq.com");
        Logger.debug("tanjun","file path = "+file.getAbsoluteFile());

        HttpManager.getInstance().asyncRequest("https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png"
                , new DownloadCallback() {
                    @Override
                    public void success(File file) {
                        final Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageBitmap(bitmap);
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

                    }
                });
    }
}
