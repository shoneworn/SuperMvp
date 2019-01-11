package com.shoneworn.libcore.net.okhttputils.model;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */


import android.content.ContentValues;
import android.database.Cursor;
import android.os.SystemClock;
import com.shoneworn.libcore.net.okhttputils.OkHttpUtils;
import com.shoneworn.libcore.net.okhttputils.request.base.Request;
import com.shoneworn.libcore.net.okhttputils.utils.IOUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Progress implements Serializable {
    private static final long serialVersionUID = 6353658567594109891L;
    public static final int NONE = 0;
    public static final int WAITING = 1;
    public static final int LOADING = 2;
    public static final int PAUSE = 3;
    public static final int ERROR = 4;
    public static final int FINISH = 5;
    public static final String TAG = "tag";
    public static final String URL = "url";
    public static final String FOLDER = "folder";
    public static final String FILE_PATH = "filePath";
    public static final String FILE_NAME = "fileName";
    public static final String FRACTION = "fraction";
    public static final String TOTAL_SIZE = "totalSize";
    public static final String CURRENT_SIZE = "currentSize";
    public static final String STATUS = "status";
    public static final String PRIORITY = "priority";
    public static final String DATE = "date";
    public static final String REQUEST = "request";
    public static final String EXTRA1 = "extra1";
    public static final String EXTRA2 = "extra2";
    public static final String EXTRA3 = "extra3";
    public String tag;
    public String url;
    public String folder;
    public String filePath;
    public String fileName;
    public float fraction;
    public long totalSize = -1L;
    public long currentSize;
    public transient long speed;
    public int status;
    public int priority = 0;
    public long date = System.currentTimeMillis();
    public Request<? extends Request> request;
    public Serializable extra1;
    public Serializable extra2;
    public Serializable extra3;
    public Throwable exception;
    private transient long tempSize;
    private transient long lastRefreshTime = SystemClock.elapsedRealtime();
    private transient List<Long> speedBuffer = new ArrayList();

    public Progress() {
    }

    public static Progress changeProgress(Progress progress, long writeSize, Progress.Action action) {
        return changeProgress(progress, writeSize, progress.totalSize, action);
    }

    public static Progress changeProgress(Progress progress, long writeSize, long totalSize, Progress.Action action) {
        progress.totalSize = totalSize;
        progress.currentSize += writeSize;
        progress.tempSize += writeSize;
        long currentTime = SystemClock.elapsedRealtime();
        boolean isNotify = currentTime - progress.lastRefreshTime >= OkHttpUtils.REFRESH_TIME;
        if(isNotify || progress.currentSize == totalSize) {
            long diffTime = currentTime - progress.lastRefreshTime;
            if(diffTime == 0L) {
                diffTime = 1L;
            }

            progress.fraction = (float)progress.currentSize * 1.0F / (float)totalSize;
            progress.speed = progress.bufferSpeed(progress.tempSize * 1000L / diffTime);
            progress.lastRefreshTime = currentTime;
            progress.tempSize = 0L;
            if(action != null) {
                action.call(progress);
            }
        }

        return progress;
    }

    private long bufferSpeed(long speed) {
        this.speedBuffer.add(Long.valueOf(speed));
        if(this.speedBuffer.size() > 10) {
            this.speedBuffer.remove(0);
        }

        long sum = 0L;

        float speedTemp;
        for(Iterator var5 = this.speedBuffer.iterator(); var5.hasNext(); sum = (long)((float)sum + speedTemp)) {
            speedTemp = (float)((Long)var5.next()).longValue();
        }

        return sum / (long)this.speedBuffer.size();
    }

    public void from(Progress progress) {
        this.totalSize = progress.totalSize;
        this.currentSize = progress.currentSize;
        this.fraction = progress.fraction;
        this.speed = progress.speed;
        this.lastRefreshTime = progress.lastRefreshTime;
        this.tempSize = progress.tempSize;
    }

    public static ContentValues buildContentValues(Progress progress) {
        ContentValues values = new ContentValues();
        values.put("tag", progress.tag);
        values.put("url", progress.url);
        values.put("folder", progress.folder);
        values.put("filePath", progress.filePath);
        values.put("fileName", progress.fileName);
        values.put("fraction", Float.valueOf(progress.fraction));
        values.put("totalSize", Long.valueOf(progress.totalSize));
        values.put("currentSize", Long.valueOf(progress.currentSize));
        values.put("status", Integer.valueOf(progress.status));
        values.put("priority", Integer.valueOf(progress.priority));
        values.put("date", Long.valueOf(progress.date));
        values.put("request", IOUtils.toByteArray(progress.request));
        values.put("extra1", IOUtils.toByteArray(progress.extra1));
        values.put("extra2", IOUtils.toByteArray(progress.extra2));
        values.put("extra3", IOUtils.toByteArray(progress.extra3));
        return values;
    }

    public static ContentValues buildUpdateContentValues(Progress progress) {
        ContentValues values = new ContentValues();
        values.put("fraction", Float.valueOf(progress.fraction));
        values.put("totalSize", Long.valueOf(progress.totalSize));
        values.put("currentSize", Long.valueOf(progress.currentSize));
        values.put("status", Integer.valueOf(progress.status));
        values.put("priority", Integer.valueOf(progress.priority));
        values.put("date", Long.valueOf(progress.date));
        return values;
    }

    public static Progress parseCursorToBean(Cursor cursor) {
        Progress progress = new Progress();
        progress.tag = cursor.getString(cursor.getColumnIndex("tag"));
        progress.url = cursor.getString(cursor.getColumnIndex("url"));
        progress.folder = cursor.getString(cursor.getColumnIndex("folder"));
        progress.filePath = cursor.getString(cursor.getColumnIndex("filePath"));
        progress.fileName = cursor.getString(cursor.getColumnIndex("fileName"));
        progress.fraction = cursor.getFloat(cursor.getColumnIndex("fraction"));
        progress.totalSize = cursor.getLong(cursor.getColumnIndex("totalSize"));
        progress.currentSize = cursor.getLong(cursor.getColumnIndex("currentSize"));
        progress.status = cursor.getInt(cursor.getColumnIndex("status"));
        progress.priority = cursor.getInt(cursor.getColumnIndex("priority"));
        progress.date = cursor.getLong(cursor.getColumnIndex("date"));
        progress.request = (Request)IOUtils.toObject(cursor.getBlob(cursor.getColumnIndex("request")));
        progress.extra1 = (Serializable)IOUtils.toObject(cursor.getBlob(cursor.getColumnIndex("extra1")));
        progress.extra2 = (Serializable)IOUtils.toObject(cursor.getBlob(cursor.getColumnIndex("extra2")));
        progress.extra3 = (Serializable)IOUtils.toObject(cursor.getBlob(cursor.getColumnIndex("extra3")));
        return progress;
    }

    public boolean equals(Object o) {
        if(this == o) {
            return true;
        } else if(o != null && this.getClass() == o.getClass()) {
            Progress progress = (Progress)o;
            return this.tag != null?this.tag.equals(progress.tag):progress.tag == null;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.tag != null?this.tag.hashCode():0;
    }

    public String toString() {
        return "Progress{fraction=" + this.fraction + ", totalSize=" + this.totalSize + ", currentSize=" + this.currentSize + ", speed=" + this.speed + ", status=" + this.status + ", priority=" + this.priority + ", folder=" + this.folder + ", filePath=" + this.filePath + ", fileName=" + this.fileName + ", tag=" + this.tag + ", url=" + this.url + '}';
    }

    public interface Action {
        void call(Progress var1);
    }
}
