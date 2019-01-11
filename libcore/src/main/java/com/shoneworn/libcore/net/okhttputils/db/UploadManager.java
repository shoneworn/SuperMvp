package com.shoneworn.libcore.net.okhttputils.db;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */


import android.content.ContentValues;
import android.database.Cursor;
import com.shoneworn.libcore.net.okhttputils.model.Progress;
import java.util.List;

public class UploadManager extends BaseDao<Progress> {
    private UploadManager() {
        super(new DBHelper());
    }

    public static UploadManager getInstance() {
        return UploadManager.UploadManagerHolder.instance;
    }

    public Progress parseCursorToBean(Cursor cursor) {
        return Progress.parseCursorToBean(cursor);
    }

    public ContentValues getContentValues(Progress progress) {
        return Progress.buildContentValues(progress);
    }

    public String getTableName() {
        return "upload";
    }

    public void unInit() {
    }

    public Progress get(String tag) {
        return (Progress)this.queryOne("tag=?", new String[]{tag});
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
        return this.query((String[])null, (String)null, (String[])null, (String)null, (String)null, "date ASC", (String)null);
    }

    public List<Progress> getFinished() {
        return this.query((String[])null, "status=?", new String[]{"5"}, (String)null, (String)null, "date ASC", (String)null);
    }

    public List<Progress> getUploading() {
        return this.query((String[])null, "status not in(?)", new String[]{"5"}, (String)null, (String)null, "date ASC", (String)null);
    }

    public boolean clear() {
        return this.deleteAll();
    }

    private static class UploadManagerHolder {
        private static final UploadManager instance = new UploadManager();

        private UploadManagerHolder() {
        }
    }
}
