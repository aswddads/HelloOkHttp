package com.tj.download.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by Jun on 17/8/6.
 */

public class DownloadHelper {

    private static DownloadHelper sHelper = new DownloadHelper();

    public static DownloadHelper getInstance() {
        return sHelper;
    }

    private DownloadHelper() {

    }

    public void init(Context context) {
        SQLiteDatabase db = new DaoMaster.DevOpenHelper(context, "download.db", null).getWritableDatabase();
        DaoMaster master = new DaoMaster(db);
        mSession = master.newSession();
        mDao = mSession.getDownloadEntityDao();
    }

    private DaoMaster mMaster;
    private DaoSession mSession;
    private DownloadEntityDao mDao;


    public void insert(DownloadEntity entry) {
        mDao.insertOrReplace(entry);
    }

    public List<DownloadEntity> getAll(String url) {
        return mDao.queryBuilder().where(DownloadEntityDao.Properties.Download_url.eq(url))
                .orderAsc(DownloadEntityDao.Properties.Thread_id).list();
    }

}
