package com.shoneworn.libcore.net.okhttputils.db;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */


import android.content.ContentValues;
import android.database.Cursor;

import com.shoneworn.libcore.net.okhttputils.cache.CacheEntity;

import java.util.List;

public class CacheManager extends BaseDao<CacheEntity<?>> {
    public static CacheManager getInstance() {
        return CacheManager.CacheManagerHolder.instance;
    }

    private CacheManager() {
        super(new DBHelper());
    }

    public CacheEntity<?> parseCursorToBean(Cursor cursor) {
        return CacheEntity.parseCursorToBean(cursor);
    }

    public ContentValues getContentValues(CacheEntity<?> cacheEntity) {
        return CacheEntity.getContentValues(cacheEntity);
    }

    public String getTableName() {
        return "cache";
    }

    public void unInit() {
    }

    public CacheEntity<?> get(String key) {
        if (key == null) {
            return null;
        } else {
            List<CacheEntity<?>> cacheEntities = this.query("key=?", new String[]{key});
            return cacheEntities.size() > 0 ? (CacheEntity) cacheEntities.get(0) : null;
        }
    }

    public boolean remove(String key) {
        return key == null ? false : this.delete("key=?", new String[]{key});
    }

    public <T> CacheEntity<T> get(String key, Class<T> clazz) {
        return (CacheEntity<T>) this.get(key);
    }

    public List<CacheEntity<?>> getAll() {
        return this.queryAll();
    }

    public <T> CacheEntity<T> replace(String key, CacheEntity<T> entity) {
        entity.setKey(key);
        this.replace(entity);
        return entity;
    }

    public boolean clear() {
        return this.deleteAll();
    }

    private static class CacheManagerHolder {
        private static final CacheManager instance = new CacheManager();

        private CacheManagerHolder() {
        }
    }
}
