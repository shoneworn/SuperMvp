package com.shoneworn.libcore.net.okhttputils.db;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import com.shoneworn.libcore.net.okhttputils.OkHttpUtils;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class DBHelper extends SQLiteOpenHelper {
    private static final String DB_CACHE_NAME = "_okhttputils.db";
    private static final int DB_CACHE_VERSION = 1;
    static final String TABLE_CACHE = "cache";
    static final String TABLE_COOKIE = "cookie";
    static final String TABLE_DOWNLOAD = "download";
    static final String TABLE_UPLOAD = "upload";
    static final Lock lock = new ReentrantLock();
    private TableEntity cacheTableEntity;
    private TableEntity cookieTableEntity;
    private TableEntity downloadTableEntity;
    private TableEntity uploadTableEntity;

    DBHelper() {
        this(OkHttpUtils.getInstance().getContext());
    }

    DBHelper(Context context) {
        super(context, "_okhttputils.db", (CursorFactory)null, 1);
        this.cacheTableEntity = new TableEntity("cache");
        this.cookieTableEntity = new TableEntity("cookie");
        this.downloadTableEntity = new TableEntity("download");
        this.uploadTableEntity = new TableEntity("upload");
        this.cacheTableEntity.addColumn(new ColumnEntity("key", "VARCHAR", true, true)).addColumn(new ColumnEntity("localExpire", "INTEGER")).addColumn(new ColumnEntity("head", "BLOB")).addColumn(new ColumnEntity("data", "BLOB"));
        this.cookieTableEntity.addColumn(new ColumnEntity("host", "VARCHAR")).addColumn(new ColumnEntity("name", "VARCHAR")).addColumn(new ColumnEntity("domain", "VARCHAR")).addColumn(new ColumnEntity("cookie", "BLOB")).addColumn(new ColumnEntity(new String[]{"host", "name", "domain"}));
        this.downloadTableEntity.addColumn(new ColumnEntity("tag", "VARCHAR", true, true)).addColumn(new ColumnEntity("url", "VARCHAR")).addColumn(new ColumnEntity("folder", "VARCHAR")).addColumn(new ColumnEntity("filePath", "VARCHAR")).addColumn(new ColumnEntity("fileName", "VARCHAR")).addColumn(new ColumnEntity("fraction", "VARCHAR")).addColumn(new ColumnEntity("totalSize", "INTEGER")).addColumn(new ColumnEntity("currentSize", "INTEGER")).addColumn(new ColumnEntity("status", "INTEGER")).addColumn(new ColumnEntity("priority", "INTEGER")).addColumn(new ColumnEntity("date", "INTEGER")).addColumn(new ColumnEntity("request", "BLOB")).addColumn(new ColumnEntity("extra1", "BLOB")).addColumn(new ColumnEntity("extra2", "BLOB")).addColumn(new ColumnEntity("extra3", "BLOB"));
        this.uploadTableEntity.addColumn(new ColumnEntity("tag", "VARCHAR", true, true)).addColumn(new ColumnEntity("url", "VARCHAR")).addColumn(new ColumnEntity("folder", "VARCHAR")).addColumn(new ColumnEntity("filePath", "VARCHAR")).addColumn(new ColumnEntity("fileName", "VARCHAR")).addColumn(new ColumnEntity("fraction", "VARCHAR")).addColumn(new ColumnEntity("totalSize", "INTEGER")).addColumn(new ColumnEntity("currentSize", "INTEGER")).addColumn(new ColumnEntity("status", "INTEGER")).addColumn(new ColumnEntity("priority", "INTEGER")).addColumn(new ColumnEntity("date", "INTEGER")).addColumn(new ColumnEntity("request", "BLOB")).addColumn(new ColumnEntity("extra1", "BLOB")).addColumn(new ColumnEntity("extra2", "BLOB")).addColumn(new ColumnEntity("extra3", "BLOB"));
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(this.cacheTableEntity.buildTableString());
        db.execSQL(this.cookieTableEntity.buildTableString());
        db.execSQL(this.downloadTableEntity.buildTableString());
        db.execSQL(this.uploadTableEntity.buildTableString());
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(DBUtils.isNeedUpgradeTable(db, this.cacheTableEntity)) {
            db.execSQL("DROP TABLE IF EXISTS cache");
        }

        if(DBUtils.isNeedUpgradeTable(db, this.cookieTableEntity)) {
            db.execSQL("DROP TABLE IF EXISTS cookie");
        }

        if(DBUtils.isNeedUpgradeTable(db, this.downloadTableEntity)) {
            db.execSQL("DROP TABLE IF EXISTS download");
        }

        if(DBUtils.isNeedUpgradeTable(db, this.uploadTableEntity)) {
            db.execSQL("DROP TABLE IF EXISTS upload");
        }

        this.onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.onUpgrade(db, oldVersion, newVersion);
    }
}
