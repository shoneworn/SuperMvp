package com.shoneworn.libcore.net.okhttputils.db;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */


import android.content.ContentValues;
import android.database.Cursor;

import com.shoneworn.libcore.net.okhttputils.model.Progress;

import java.util.List;

public class DownloadManager extends BaseDao<Progress> {
    private DownloadManager() {
        super(new DBHelper());
    }

    public static DownloadManager getInstance() {
        return DownloadManager.DownloadManagerHolder.instance;
    }

    public Progress parseCursorToBean(Cursor cursor) {
        return Progress.parseCursorToBean(cursor);
    }

    public ContentValues getContentValues(Progress progress) {
        return Progress.buildContentValues(progress);
    }

    public String getTableName() {
        return "download";
    }

    public void unInit() {
    }

    public Progress get(String tag) {
        return (Progress) this.queryOne("tag=?", new String[]{tag});
    }

    public void delete(String taskKey) {
        this.delete("tag=?", new String[]{taskKey});
    }

    public boolean update(Progress progress) {
        return this.update(progress, "tag=?", new String[]{progress.tag});
    }

    public boolean update(ContentValues contentValues, String tag) {
        return this.update(contentValues, "tag=?", new String[]{tag});
    }

    public List<Progress> getAll() {
        return this.query((String[]) null, (String) null, (String[]) null, (String) null, (String) null, "date ASC", (String) null);
    }

    public List<Progress> getFinished() {
        return this.query((String[]) null, "status=?", new String[]{"5"}, (String) null, (String) null, "date ASC", (String) null);
    }

    public List<Progress> getDownloading() {
        return this.query((String[]) null, "status not in(?)", new String[]{"5"}, (String) null, (String) null, "date ASC", (String) null);
    }

    public boolean clear() {
        return this.deleteAll();
    }

    private static class DownloadManagerHolder {
        private static final DownloadManager instance = new DownloadManager();

        private DownloadManagerHolder() {
        }
    }
}
