package com.tj.download.file;

import android.content.Context;
import android.os.Environment;

import com.tj.download.utills.Md5Utils;

import java.io.File;
import java.io.IOException;

/**
 * Created by Jun on 17/8/6.
 */

public class FileStorageManager {

    private static final FileStorageManager sManager = new FileStorageManager();
    private Context mContext;

    public static FileStorageManager getInstance() {
        return sManager;
    }

    private FileStorageManager() {

    }

    public void init(Context context) {
        this.mContext = context;
    }

    public File getFileByName(String url) {
        File parent;
        //have sd-card
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            parent = mContext.getExternalCacheDir();
        } else {  //have no sd-card, use the default
            parent = mContext.getCacheDir();
        }
        String fileName = Md5Utils.generateCode(url);
        File file = new File(parent, fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
