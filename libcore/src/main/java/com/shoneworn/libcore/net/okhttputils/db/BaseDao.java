package com.shoneworn.libcore.net.okhttputils.db;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;

import com.shoneworn.libcore.net.okhttputils.utils.OkLogger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;

public abstract class BaseDao<T> {
    protected static String TAG;
    protected Lock lock;
    protected SQLiteOpenHelper helper;
    protected SQLiteDatabase database;

    public BaseDao(SQLiteOpenHelper helper) {
        TAG = this.getClass().getSimpleName();
        this.lock = DBHelper.lock;
        this.helper = helper;
        this.database = this.openWriter();
    }

    public SQLiteDatabase openReader() {
        return this.helper.getReadableDatabase();
    }

    public SQLiteDatabase openWriter() {
        return this.helper.getWritableDatabase();
    }

    protected final void closeDatabase(SQLiteDatabase database, Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        if (database != null && database.isOpen()) {
            database.close();
        }

    }

    public boolean insert(T t) {
        if (t == null) {
            return false;
        } else {
            long start = System.currentTimeMillis();
            this.lock.lock();

            try {
                this.database.beginTransaction();
                this.database.insert(this.getTableName(), (String) null, this.getContentValues(t));
                this.database.setTransactionSuccessful();
                boolean var4 = true;
                return var4;
            } catch (Exception var8) {
                OkLogger.printStackTrace(var8);
            } finally {
                this.database.endTransaction();
                this.lock.unlock();
                OkLogger.v(TAG, System.currentTimeMillis() - start + " insertT");
            }

            return false;
        }
    }

    public long insert(SQLiteDatabase database, T t) {
        return database.insert(this.getTableName(), (String) null, this.getContentValues(t));
    }

    public boolean insert(List<T> ts) {
        if (ts == null) {
            return false;
        } else {
            long start = System.currentTimeMillis();
            this.lock.lock();

            try {
                this.database.beginTransaction();
                Iterator var4 = ts.iterator();

                while (var4.hasNext()) {
                    T t = (T) var4.next();
                    this.database.insert(this.getTableName(), (String) null, this.getContentValues(t));
                }

                this.database.setTransactionSuccessful();
                boolean var11 = true;
                return var11;
            } catch (Exception var9) {
                OkLogger.printStackTrace(var9);
            } finally {
                this.database.endTransaction();
                this.lock.unlock();
                OkLogger.v(TAG, System.currentTimeMillis() - start + " insertList");
            }

            return false;
        }
    }

    public boolean insert(SQLiteDatabase database, List<T> ts) {
        try {
            Iterator var3 = ts.iterator();

            while (var3.hasNext()) {
                T t = (T) var3.next();
                database.insert(this.getTableName(), (String) null, this.getContentValues(t));
            }

            return true;
        } catch (Exception var5) {
            OkLogger.printStackTrace(var5);
            return false;
        }
    }

    public boolean deleteAll() {
        return this.delete((String) null, (String[]) null);
    }

    public long deleteAll(SQLiteDatabase database) {
        return this.delete(database, (String) null, (String[]) null);
    }

    public boolean delete(String whereClause, String[] whereArgs) {
        long start = System.currentTimeMillis();
        this.lock.lock();

        try {
            this.database.beginTransaction();
            this.database.delete(this.getTableName(), whereClause, whereArgs);
            this.database.setTransactionSuccessful();
            boolean var5 = true;
            return var5;
        } catch (Exception var9) {
            OkLogger.printStackTrace(var9);
        } finally {
            this.database.endTransaction();
            this.lock.unlock();
            OkLogger.v(TAG, System.currentTimeMillis() - start + " delete");
        }

        return false;
    }

    public long delete(SQLiteDatabase database, String whereClause, String[] whereArgs) {
        return (long) database.delete(this.getTableName(), whereClause, whereArgs);
    }

    public boolean deleteList(List<Pair<String, String[]>> where) {
        long start = System.currentTimeMillis();
        this.lock.lock();

        try {
            this.database.beginTransaction();
            Iterator var4 = where.iterator();

            while (var4.hasNext()) {
                Pair<String, String[]> pair = (Pair) var4.next();
                this.database.delete(this.getTableName(), (String) pair.first, (String[]) pair.second);
            }

            this.database.setTransactionSuccessful();
            boolean var11 = true;
            return var11;
        } catch (Exception var9) {
            OkLogger.printStackTrace(var9);
        } finally {
            this.database.endTransaction();
            this.lock.unlock();
            OkLogger.v(TAG, System.currentTimeMillis() - start + " deleteList");
        }

        return false;
    }

    public boolean replace(T t) {
        if (t == null) {
            return false;
        } else {
            long start = System.currentTimeMillis();
            this.lock.lock();

            try {
                this.database.beginTransaction();
                this.database.replace(this.getTableName(), (String) null, this.getContentValues(t));
                this.database.setTransactionSuccessful();
                boolean var4 = true;
                return var4;
            } catch (Exception var8) {
                OkLogger.printStackTrace(var8);
            } finally {
                this.database.endTransaction();
                this.lock.unlock();
                OkLogger.v(TAG, System.currentTimeMillis() - start + " replaceT");
            }

            return false;
        }
    }

    public long replace(SQLiteDatabase database, T t) {
        return database.replace(this.getTableName(), (String) null, this.getContentValues(t));
    }

    public boolean replace(ContentValues contentValues) {
        long start = System.currentTimeMillis();
        this.lock.lock();

        try {
            this.database.beginTransaction();
            this.database.replace(this.getTableName(), (String) null, contentValues);
            this.database.setTransactionSuccessful();
            boolean var4 = true;
            return var4;
        } catch (Exception var8) {
            OkLogger.printStackTrace(var8);
        } finally {
            this.database.endTransaction();
            this.lock.unlock();
            OkLogger.v(TAG, System.currentTimeMillis() - start + " replaceContentValues");
        }

        return false;
    }

    public long replace(SQLiteDatabase database, ContentValues contentValues) {
        return database.replace(this.getTableName(), (String) null, contentValues);
    }

    public boolean replace(List<T> ts) {
        if (ts == null) {
            return false;
        } else {
            long start = System.currentTimeMillis();
            this.lock.lock();

            try {
                this.database.beginTransaction();
                Iterator var4 = ts.iterator();

                while (var4.hasNext()) {
                    T t = (T) var4.next();
                    this.database.replace(this.getTableName(), (String) null, this.getContentValues(t));
                }

                this.database.setTransactionSuccessful();
                boolean var11 = true;
                return var11;
            } catch (Exception var9) {
                OkLogger.printStackTrace(var9);
            } finally {
                this.database.endTransaction();
                this.lock.unlock();
                OkLogger.v(TAG, System.currentTimeMillis() - start + " replaceList");
            }

            return false;
        }
    }

    public boolean replace(SQLiteDatabase database, List<T> ts) {
        try {
            Iterator var3 = ts.iterator();

            while (var3.hasNext()) {
                T t = (T)var3.next();
                database.replace(this.getTableName(), (String) null, this.getContentValues(t));
            }

            return true;
        } catch (Exception var5) {
            OkLogger.printStackTrace(var5);
            return false;
        }
    }

    public boolean update(T t, String whereClause, String[] whereArgs) {
        if (t == null) {
            return false;
        } else {
            long start = System.currentTimeMillis();
            this.lock.lock();

            try {
                this.database.beginTransaction();
                this.database.update(this.getTableName(), this.getContentValues(t), whereClause, whereArgs);
                this.database.setTransactionSuccessful();
                boolean var6 = true;
                return var6;
            } catch (Exception var10) {
                OkLogger.printStackTrace(var10);
            } finally {
                this.database.endTransaction();
                this.lock.unlock();
                OkLogger.v(TAG, System.currentTimeMillis() - start + " updateT");
            }

            return false;
        }
    }

    public long update(SQLiteDatabase database, T t, String whereClause, String[] whereArgs) {
        return (long) database.update(this.getTableName(), this.getContentValues(t), whereClause, whereArgs);
    }

    public boolean update(ContentValues contentValues, String whereClause, String[] whereArgs) {
        long start = System.currentTimeMillis();
        this.lock.lock();

        try {
            this.database.beginTransaction();
            this.database.update(this.getTableName(), contentValues, whereClause, whereArgs);
            this.database.setTransactionSuccessful();
            boolean var6 = true;
            return var6;
        } catch (Exception var10) {
            OkLogger.printStackTrace(var10);
        } finally {
            this.database.endTransaction();
            this.lock.unlock();
            OkLogger.v(TAG, System.currentTimeMillis() - start + " updateContentValues");
        }

        return false;
    }

    public long update(SQLiteDatabase database, ContentValues contentValues, String whereClause, String[] whereArgs) {
        return (long) database.update(this.getTableName(), contentValues, whereClause, whereArgs);
    }

    public List<T> queryAll(SQLiteDatabase database) {
        return this.query(database, (String) null, (String[]) null);
    }

    public List<T> query(SQLiteDatabase database, String selection, String[] selectionArgs) {
        return this.query(database, (String[]) null, selection, selectionArgs, (String) null, (String) null, (String) null, (String) null);
    }

    public T queryOne(SQLiteDatabase database, String selection, String[] selectionArgs) {
        List<T> query = this.query(database, (String[]) null, selection, selectionArgs, (String) null, (String) null, (String) null, "1");
        return query.size() > 0 ? query.get(0) : null;
    }

    public List<T> query(SQLiteDatabase database, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        List<T> list = new ArrayList();
        Cursor cursor = null;

        try {
            cursor = database.query(this.getTableName(), columns, selection, selectionArgs, groupBy, having, orderBy, limit);

            while (!cursor.isClosed() && cursor.moveToNext()) {
                list.add(this.parseCursorToBean(cursor));
            }
        } catch (Exception var15) {
            OkLogger.printStackTrace(var15);
        } finally {
            this.closeDatabase((SQLiteDatabase) null, cursor);
        }

        return list;
    }

    public List<T> queryAll() {
        return this.query((String) null, (String[]) null);
    }

    public List<T> query(String selection, String[] selectionArgs) {
        return this.query((String[]) null, selection, selectionArgs, (String) null, (String) null, (String) null, (String) null);
    }

    public T queryOne(String selection, String[] selectionArgs) {
        long start = System.currentTimeMillis();
        List<T> query = this.query((String[]) null, selection, selectionArgs, (String) null, (String) null, (String) null, "1");
        OkLogger.v(TAG, System.currentTimeMillis() - start + " queryOne");
        return query.size() > 0 ? query.get(0) : null;
    }

    public List<T> query(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        long start = System.currentTimeMillis();
        this.lock.lock();
        List<T> list = new ArrayList();
        Cursor cursor = null;

        try {
            this.database.beginTransaction();
            cursor = this.database.query(this.getTableName(), columns, selection, selectionArgs, groupBy, having, orderBy, limit);

            while (!cursor.isClosed() && cursor.moveToNext()) {
                list.add(this.parseCursorToBean(cursor));
            }

            this.database.setTransactionSuccessful();
        } catch (Exception var16) {
            OkLogger.printStackTrace(var16);
        } finally {
            this.closeDatabase((SQLiteDatabase) null, cursor);
            this.database.endTransaction();
            this.lock.unlock();
            OkLogger.v(TAG, System.currentTimeMillis() - start + " query");
        }

        return list;
    }

    public void startTransaction(BaseDao.Action action) {
        this.lock.lock();

        try {
            this.database.beginTransaction();
            action.call(this.database);
            this.database.setTransactionSuccessful();
        } catch (Exception var6) {
            OkLogger.printStackTrace(var6);
        } finally {
            this.database.endTransaction();
            this.lock.unlock();
        }

    }

    public abstract String getTableName();

    public abstract void unInit();

    public abstract T parseCursorToBean(Cursor var1);

    public abstract ContentValues getContentValues(T var1);

    public interface Action {
        void call(SQLiteDatabase var1);
    }
}
