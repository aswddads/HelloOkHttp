package com.tj.download.http;

import java.io.File;

/**
 * Created by Jun on 17/8/6.
 */

public interface DownloadCallback {

    void success(File file);

    void fail(int errorCode,String errorMessage);

    void progress(int progress);
}
