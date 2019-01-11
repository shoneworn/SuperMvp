package com.shoneworn.libcore.net.okhttputils.db;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.shoneworn.libcore.net.okhttputils.cookie.SerializableCookie;

public class CookieManager extends BaseDao<SerializableCookie> {
    private static Context context;
    private static volatile CookieManager instance;

    public static CookieManager getInstance() {
        if(instance == null) {
            Class var0 = CookieManager.class;
            synchronized(CookieManager.class) {
                if(instance == null) {
                    instance = new CookieManager();
                }
            }
        }

        return instance;
    }

    private CookieManager() {
        super(new DBHelper(context));
    }

    public static void init(Context ctx) {
        context = ctx;
    }

    public SerializableCookie parseCursorToBean(Cursor cursor) {
        return SerializableCookie.parseCursorToBean(cursor);
    }

    public ContentValues getContentValues(SerializableCookie serializableCookie) {
        return SerializableCookie.getContentValues(serializableCookie);
    }

    public String getTableName() {
        return "cookie";
    }

    public void unInit() {
    }
}
