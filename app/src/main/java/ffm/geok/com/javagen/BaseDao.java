package ffm.geok.com.javagen;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import ffm.geok.com.global.XApplication;

/**
 * Created by zhanghs on 2017/8/18.
 */

public class BaseDao {
    private static DaoMaster daoMaster;

    private static DaoSession daoSession;

    private static SQLiteDatabase database;

    /*
      db文件名
    */
    public static final String DB_NAME = "ffm_db.sqlite";

    private static DaoMaster obtainMaster(Context context, String dbName) {

        return new DaoMaster(getDatabase());
    }

    private static DaoMaster getDaoMaster(Context context, String dbName) {
        if (dbName == null)
            return null;
        if (daoMaster == null) {
            daoMaster = obtainMaster(context, dbName);
        }
        return daoMaster;
    }

    /**
     * 默认操作localdata数据库
     */
    public static DaoSession getDaoSession() {
        if (daoSession == null) {
            daoSession = getDaoMaster(XApplication.getInstance(), DB_NAME).newSession();
        }
        return daoSession;
    }

    public static SQLiteDatabase getDatabase() {
        if (database == null) {
            database = new DaoMaster.DevOpenHelper(XApplication.getInstance(), DB_NAME, null).getWritableDatabase();
        }
        return database;
    }
}
