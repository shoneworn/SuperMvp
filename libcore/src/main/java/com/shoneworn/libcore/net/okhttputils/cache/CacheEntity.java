package com.shoneworn.libcore.net.okhttputils.cache;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */


import android.content.ContentValues;
import android.database.Cursor;

import com.shoneworn.libcore.net.okhttputils.model.HttpHeaders;
import com.shoneworn.libcore.net.okhttputils.utils.IOUtils;

import java.io.Serializable;

public class CacheEntity<T> implements Serializable {
    private static final long serialVersionUID = -4337711009801627866L;
    public static final long CACHE_NEVER_EXPIRE = -1L;
    public static final String KEY = "key";
    public static final String LOCAL_EXPIRE = "localExpire";
    public static final String HEAD = "head";
    public static final String DATA = "data";
    private String key;
    private long localExpire;
    private HttpHeaders responseHeaders;
    private T data;
    private boolean isExpire;

    public CacheEntity() {
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public HttpHeaders getResponseHeaders() {
        return this.responseHeaders;
    }

    public void setResponseHeaders(HttpHeaders responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public long getLocalExpire() {
        return this.localExpire;
    }

    public void setLocalExpire(long localExpire) {
        this.localExpire = localExpire;
    }

    public boolean isExpire() {
        return this.isExpire;
    }

    public void setExpire(boolean expire) {
        this.isExpire = expire;
    }

    public boolean checkExpire(CacheMode cacheMode, long cacheTime, long baseTime) {
        return cacheMode == CacheMode.DEFAULT ? this.getLocalExpire() < baseTime : (cacheTime == -1L ? false : this.getLocalExpire() + cacheTime < baseTime);
    }

    public static <T> ContentValues getContentValues(CacheEntity<T> cacheEntity) {
        ContentValues values = new ContentValues();
        values.put("key", cacheEntity.getKey());
        values.put("localExpire", Long.valueOf(cacheEntity.getLocalExpire()));
        values.put("head", IOUtils.toByteArray(cacheEntity.getResponseHeaders()));
        values.put("data", IOUtils.toByteArray(cacheEntity.getData()));
        return values;
    }

    public static <T> CacheEntity<T> parseCursorToBean(Cursor cursor) {
        CacheEntity<T> cacheEntity = new CacheEntity();
        cacheEntity.setKey(cursor.getString(cursor.getColumnIndex("key")));
        cacheEntity.setLocalExpire(cursor.getLong(cursor.getColumnIndex("localExpire")));
        cacheEntity.setResponseHeaders((HttpHeaders) IOUtils.toObject(cursor.getBlob(cursor.getColumnIndex("head"))));
        cacheEntity.setData((T)IOUtils.toObject(cursor.getBlob(cursor.getColumnIndex("data"))));
        return cacheEntity;
    }

    public String toString() {
        return "CacheEntity{key='" + this.key + '\'' + ", responseHeaders=" + this.responseHeaders + ", data=" + this.data + ", localExpire=" + this.localExpire + '}';
    }
}
